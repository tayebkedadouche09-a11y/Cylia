-- ==============================================================================
-- Tayeb & Cylia ❤️ Couples Game - Supabase PostgreSQL Schema & Security Policies
-- ==============================================================================

-- 1. EXTENSIONS & UTILITIES
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. ROOMS TABLE
CREATE TABLE IF NOT EXISTS public.rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(8) NOT NULL UNIQUE,
    host_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    guest_id UUID REFERENCES auth.users(id) ON DELETE SET NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'waiting', -- 'waiting', 'active', 'closed'
    active_category VARCHAR(50) NOT NULL DEFAULT 'love_feelings',
    current_question_id VARCHAR(50) NOT NULL DEFAULT 'love_1',
    adult_content_unlocked BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMPTZ NOT NULL DEFAULT (NOW() + INTERVAL '24 hours'),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_rooms_code ON public.rooms(code);
CREATE INDEX IF NOT EXISTS idx_rooms_expires ON public.rooms(expires_at);

-- 3. ROOM MEMBERS TABLE
CREATE TABLE IF NOT EXISTS public.room_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL REFERENCES public.rooms(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL, -- 'TAYEB', 'CYLIA'
    display_name VARCHAR(50) NOT NULL,
    is_online BOOLEAN NOT NULL DEFAULT TRUE,
    last_seen TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    joined_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(room_id, user_id),
    UNIQUE(room_id, role)
);

CREATE INDEX IF NOT EXISTS idx_room_members_user ON public.room_members(user_id);

-- 4. ANSWERS TABLE (Private Until Revealed)
CREATE TABLE IF NOT EXISTS public.answers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL REFERENCES public.rooms(id) ON DELETE CASCADE,
    question_id VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    author_role VARCHAR(20) NOT NULL,
    author_name VARCHAR(50) NOT NULL,
    answer_text TEXT NOT NULL,
    is_revealed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(room_id, question_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_answers_room_q ON public.answers(room_id, question_id);

-- 5. LOVE NOTES TABLE
CREATE TABLE IF NOT EXISTS public.love_notes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL REFERENCES public.rooms(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    sender_role VARCHAR(20) NOT NULL,
    sender_name VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    emoji VARCHAR(10) NOT NULL DEFAULT '❤️',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_love_notes_room ON public.love_notes(room_id, created_at DESC);

-- 6. SHARED PHOTOS TABLE (Consensual & Private Storage)
CREATE TABLE IF NOT EXISTS public.shared_photos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL REFERENCES public.rooms(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    sender_role VARCHAR(20) NOT NULL,
    sender_name VARCHAR(50) NOT NULL,
    storage_path TEXT NOT NULL,
    caption TEXT DEFAULT '',
    consent_confirmed BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ==============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- ==============================================================================

ALTER TABLE public.rooms ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.room_members ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.answers ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.love_notes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.shared_photos ENABLE ROW LEVEL SECURITY;

-- Helper security definer function to check room membership without infinite recursion
CREATE OR REPLACE FUNCTION public.is_member_of_room(target_room_id UUID, check_user_id UUID)
RETURNS BOOLEAN
LANGUAGE sql
SECURITY DEFINER
SET search_path = public
STABLE
AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.room_members
        WHERE room_id = target_room_id AND user_id = check_user_id
    );
$$;

-- Helper to check if room is expired
CREATE OR REPLACE FUNCTION public.is_room_valid(target_room_id UUID)
RETURNS BOOLEAN
LANGUAGE sql
SECURITY DEFINER
SET search_path = public
STABLE
AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.rooms
        WHERE id = target_room_id AND expires_at > NOW() AND status != 'closed'
    );
$$;

-- RLS: ROOMS
-- Anyone authenticated can view a room if they are a member or look up by code to join
CREATE POLICY "Users can view rooms they belong to"
ON public.rooms
FOR SELECT
USING (
    auth.uid() = host_id OR 
    auth.uid() = guest_id OR
    public.is_member_of_room(id, auth.uid()) OR
    (status = 'waiting' AND expires_at > NOW())
);

CREATE POLICY "Authenticated users can create rooms"
ON public.rooms
FOR INSERT
WITH CHECK (auth.uid() = host_id);

CREATE POLICY "Host or Guest can update their active room"
ON public.rooms
FOR UPDATE
USING (
    public.is_member_of_room(id, auth.uid()) AND public.is_room_valid(id)
);

CREATE POLICY "Host can delete or close room"
ON public.rooms
FOR DELETE
USING (auth.uid() = host_id);

-- RLS: ROOM MEMBERS
CREATE POLICY "Members can view other members in their room"
ON public.room_members
FOR SELECT
USING (public.is_member_of_room(room_id, auth.uid()));

CREATE POLICY "Users can join rooms as members"
ON public.room_members
FOR INSERT
WITH CHECK (auth.uid() = user_id AND public.is_room_valid(room_id));

CREATE POLICY "Users can update their own presence status"
ON public.room_members
FOR UPDATE
USING (auth.uid() = user_id);

CREATE POLICY "Users can leave room"
ON public.room_members
FOR DELETE
USING (auth.uid() = user_id);

-- RLS: ANSWERS
-- Members can view answers only if:
-- 1. It is their own answer, OR
-- 2. The answer has been revealed by the author/room rules
CREATE POLICY "View answers in active room"
ON public.answers
FOR SELECT
USING (
    public.is_member_of_room(room_id, auth.uid()) AND
    (auth.uid() = user_id OR is_revealed = TRUE)
);

CREATE POLICY "Insert own answer"
ON public.answers
FOR INSERT
WITH CHECK (
    auth.uid() = user_id AND 
    public.is_member_of_room(room_id, auth.uid()) AND
    public.is_room_valid(room_id)
);

CREATE POLICY "Update own answer reveal status"
ON public.answers
FOR UPDATE
USING (
    auth.uid() = user_id AND 
    public.is_member_of_room(room_id, auth.uid())
);

-- RLS: LOVE NOTES
CREATE POLICY "Room members can read love notes"
ON public.love_notes
FOR SELECT
USING (public.is_member_of_room(room_id, auth.uid()));

CREATE POLICY "Room members can send love notes"
ON public.love_notes
FOR INSERT
WITH CHECK (
    auth.uid() = user_id AND 
    public.is_member_of_room(room_id, auth.uid()) AND
    public.is_room_valid(room_id)
);

-- RLS: SHARED PHOTOS
CREATE POLICY "Room members can view shared photos"
ON public.shared_photos
FOR SELECT
USING (public.is_member_of_room(room_id, auth.uid()));

CREATE POLICY "Room members can upload photos with confirmed consent"
ON public.shared_photos
FOR INSERT
WITH CHECK (
    auth.uid() = user_id AND 
    consent_confirmed = TRUE AND
    public.is_member_of_room(room_id, auth.uid()) AND
    public.is_room_valid(room_id)
);

CREATE POLICY "Uploader can delete their photo"
ON public.shared_photos
FOR DELETE
USING (auth.uid() = user_id);

-- ==============================================================================
-- STORAGE CONFIGURATION (Private bucket: couples-private-photos)
-- ==============================================================================
INSERT INTO storage.buckets (id, name, public) 
VALUES ('couples-private-photos', 'couples-private-photos', false)
ON CONFLICT (id) DO NOTHING;

CREATE POLICY "Couples photo access"
ON storage.objects
FOR SELECT
USING (
    bucket_id = 'couples-private-photos' AND
    auth.role() = 'authenticated'
);

CREATE POLICY "Couples photo upload"
ON storage.objects
FOR INSERT
WITH CHECK (
    bucket_id = 'couples-private-photos' AND
    auth.role() = 'authenticated'
);

CREATE POLICY "Couples photo delete"
ON storage.objects
FOR DELETE
USING (
    bucket_id = 'couples-private-photos' AND
    auth.uid() = owner
);

-- ==============================================================================
-- REALTIME SUBSCRIPTIONS
-- ==============================================================================
BEGIN;
  DROP PUBLICATION IF EXISTS supabase_realtime;
  CREATE PUBLICATION supabase_realtime FOR TABLE 
    public.rooms, 
    public.room_members, 
    public.answers, 
    public.love_notes, 
    public.shared_photos;
COMMIT;
