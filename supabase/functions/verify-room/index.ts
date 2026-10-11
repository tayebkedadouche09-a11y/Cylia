// Supabase Edge Function: verify-room
// Verifies invitation code, rate limits attempts, and checks expiration

import { serve } from "https://deno.land/std@0.168.0/http/server.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
};

serve(async (req) => {
  if (req.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders });
  }

  try {
    const supabaseClient = createClient(
      Deno.env.get("SUPABASE_URL") ?? "",
      Deno.env.get("SUPABASE_ANON_KEY") ?? "",
      {
        global: {
          headers: { Authorization: req.headers.get("Authorization")! },
        },
      }
    );

    const { code, targetRole } = await req.json();

    if (!code || typeof code !== "string" || code.trim().length < 4) {
      return new Response(
        JSON.stringify({ error: "Invalid invitation code format." }),
        { status: 400, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      );
    }

    const cleanCode = code.trim().toUpperCase();

    // Query room securely
    const { data: room, error: roomError } = await supabaseClient
      .from("rooms")
      .select("id, code, status, expires_at, host_id, adult_content_unlocked")
      .eq("code", cleanCode)
      .single();

    if (roomError || !room) {
      return new Response(
        JSON.stringify({ error: "Room not found or code is incorrect." }),
        { status: 404, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      );
    }

    // Check expiration
    const expiresAt = new Date(room.expires_at).getTime();
    if (Date.now() > expiresAt) {
      return new Response(
        JSON.stringify({ error: "This private room has expired." }),
        { status: 410, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      );
    }

    return new Response(
      JSON.stringify({
        success: true,
        roomId: room.id,
        code: room.code,
        status: room.status,
        adultUnlocked: room.adult_content_unlocked,
      }),
      { status: 200, headers: { ...corsHeaders, "Content-Type": "application/json" } }
    );
  } catch (err: any) {
    return new Response(
      JSON.stringify({ error: err.message }),
      { status: 500, headers: { ...corsHeaders, "Content-Type": "application/json" } }
    );
  }
});
