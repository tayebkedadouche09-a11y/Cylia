# Tayeb & Cylia ❤️ — Private Long-Distance Couples Game

A private, romantic question game engineered specifically for long-distance lovers. Designed with a dark romantic aesthetic: nearly black backgrounds, rich purple surfaces, glowing neon pink accents, animated heart bursts, and bidirectional support for Arabic (RTL), French, and English.

---

## 1. Project Architecture & Platforms

The application is architected with dual compatibility:
1. **Live Native Android Engine (Jetpack Compose & Kotlin)**: Powers the live streaming in-browser Android emulator preview and direct native Gradle compilation.
2. **Cross-Platform React Native / Expo (TypeScript)**: Configured for unified Android (`.apk`) and iOS (`.ipa` / Simulator) builds using EAS Build and Supabase backend.

```
├── app/                                 # Android Native Source (Jetpack Compose)
│   ├── src/main/java/com/example/
│   │   ├── MainActivity.kt              # App entry point, M3 navigation, RTL Provider
│   │   ├── data/
│   │   │   ├── model/Models.kt          # Role, Category, Question, RoomState models
│   │   │   ├── repository/              # 50+ Multilingual Questions Library
│   │   │   └── service/SupabaseService  # Supabase PostgREST & Realtime integration
│   │   └── ui/
│   │       ├── CouplesViewModel.kt      # State management, reveal mechanics, heart bursts
│   │       ├── components/              # Floating hearts, adult & photo consent dialogs
│   │       ├── screens/                 # Game, Notes, Photos, History, Settings
│   │       └── theme/                   # Dark Romantic palette (#09030E, #FF2A85, #A855F7)
├── src/                                 # Cross-Platform React Native & Expo Source
│   ├── types/index.ts                   # Unified TypeScript data contracts
│   ├── lib/supabase.ts                  # Supabase client with offline guard
│   └── data/questions.ts                # Multilingual question definitions
├── supabase/
│   ├── migrations/                      # PostgreSQL schema & Row-Level Security (RLS)
│   │   └── 20241011000000_couples_game_schema.sql
│   └── functions/verify-room/           # Edge function for rate limiting & room expiration
├── app.json & app.config.ts             # Expo configuration (icons, splash, permissions)
├── eas.json                             # EAS Build profiles (Installable APK & iOS)
├── package.json                         # Dependencies & npm scripts
└── .env.example                         # Environment variable template
```

---

## 2. Core Features Implemented

- **Private Room Creation & Joining**:
  - 6-character room codes (e.g. `LOVE77`) with expiration timestamps (24-hour validity).
  - 1-tap invite message copy with localized invitation text for WhatsApp / Messages.
  - Live presence indicator: 🟢 Online pulse and connection status.
- **5 Curated Categories**:
  1. 💖 **Love & Feelings** (مشاعر وحب / Amour & Sentiments)
  2. ✨ **Attraction & Crushes** (إعجاب وجاذبية / Attraction & Coups de cœur)
  3. 🫂 **Care & Attention** (اهتمام وتفاصيل / Attention & Tendresse)
  4. 🔥 **Bold Questions** (أسئلة جريئة / Questions Audacieuses)
  5. 🔞 **Adult Intimacy & Boundaries** (حميمية وحدود 18+ / Intimité & Limites 18+) — *Gated behind an explicit adult mutual consent modal*.
- **Private Answer & Reveal Mechanic**:
  - Each partner writes their answer privately into an encrypted field.
  - Answers remain masked/blurred until either partner triggers the romantic reveal.
  - State indicator shows when a partner has submitted an answer.
- **Romantic Micro-Interactions & Heartbeat Pulse**:
  - 💓 Instant **Heartbeat Pulse** button that spawns glowing neon pink animated floating hearts across the screen.
  - Quick love notes and sweet emoji stream (❤️, 🌹, 💖, 💍, ✨, 💌, 💋).
- **Consensual Photo Exchange**:
  - Zero-permission Android Photo Picker (`ActivityResultContracts.PickVisualMedia`).
  - Strict privacy dialog explaining that deleting photos in-app cannot erase screenshots.
  - Upload progress animation, encrypted preview, and 1-tap delete controls.
  - No camera or photo picker opens automatically without explicit user action.
- **Favorites & Answer History**:
  - Bookmark favorite questions for romantic date nights.
  - Comprehensive history tab showing both Tayeb & Cylia's answers.
- **Trilingual Localization (Arabic RTL, French, English)**:
  - Dynamically flips to right-to-left layout direction in Arabic mode.

---

## 3. Supabase Backend Setup & Security

The backend schema is located in `supabase/migrations/20241011000000_couples_game_schema.sql`.

### Tables Created:
1. `rooms` — Room codes, host/guest references, expiration, status.
2. `room_members` — User presence, role (`TAYEB` or `CYLIA`), and last seen timestamps.
3. `answers` — Private responses with `is_revealed` booleans.
4. `love_notes` — Quick romantic messages.
5. `shared_photos` — Private photo storage paths with confirmed consent.

### Security & Row Level Security (RLS):
- **Zero-Trust Access**: Every table has RLS enabled (`ALTER TABLE ... ENABLE ROW LEVEL SECURITY`).
- **Room Isolation**: The `public.is_member_of_room(room_id, auth.uid())` security definer function verifies room membership server-side. Non-members cannot read any room data.
- **Unrevealed Answers Protection**: RLS policy on `public.answers` prevents querying another partner's unrevealed answer text until `is_revealed = true`.
- **Private Storage**: Storage bucket `couples-private-photos` is private (`public = false`) and requires authenticated signed tokens.

### Applying Migrations:
```bash
# Link your Supabase project
supabase link --project-ref your-project-ref

# Push the migration
supabase db push
```

---

## 4. Environment Variables Configuration

Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```

Configure your credentials:
```env
EXPO_PUBLIC_SUPABASE_URL=https://your-project.supabase.co
EXPO_PUBLIC_SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.your-anon-key
```

> **Note**: In the Android app, you can also enter your Supabase URL & Anon Key directly inside **Settings > Supabase Cloud Sync** at any time.

---

## 5. Build Instructions

### Android Native Build (Current Project)
To compile and assemble the native debug APK locally:
```bash
gradle assembleDebug
```
The output APK is generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Cross-Platform Android APK (Via EAS Build)
To build a standalone, installable `.apk` file that can be side-loaded onto any Android phone:
```bash
# 1. Install EAS CLI
npm install -g eas-cli

# 2. Log in to Expo
eas login

# 3. Trigger APK build
eas build --platform android --profile preview
```
*EAS Build compiles the project in the cloud and produces a direct `.apk` download link and QR code for your phone.*

---

## 6. iOS Build Instructions & Limitations

> ⚠️ **Critical Operating System Rule**: Android APK files (`.apk`) CANNOT run on Apple iOS (iPhone/iPad). iOS requires an `.ipa` bundle.

### Building for iOS:
1. **iOS Simulator Build** (No paid Apple Developer account needed):
   ```bash
   eas build --platform ios --profile preview
   ```
   *Produces a `.tar.gz` simulator build that can be dragged into Xcode iOS Simulator.*

2. **iOS Physical iPhone Build** (Requires Apple Developer Program enrollment):
   ```bash
   eas build --platform ios --profile production
   ```
   *Produces an `.ipa` file distributed via TestFlight or Ad-Hoc provisioning.*

---

## 7. Dual-Phone Testing Checklist (Tayeb & Cylia)

| Step | Action on Phone 1 (Tayeb 👦🏻) | Action on Phone 2 (Cylia 👧🏻) | Expected Result |
|---|---|---|---|
| 1 | Open app, set role to **Tayeb** | Open app, set role to **Cylia** | Header displays "Tayeb 👦🏻" and "Cylia 👧🏻" respectively. |
| 2 | Tap **Settings > Generate New Room** | Wait for invite code | 6-character room code appears (e.g. `LOVE77`). |
| 3 | Tap **Copy Invite** and share with Cylia | Go to **Settings > Join Room**, paste code | Cylia enters room. Status changes to "Connected 🟢". |
| 4 | Go to **Game**, select **Love & Feelings** | Select **Love & Feelings** | Both phones view Question #1. |
| 5 | Type private answer and tap **Save Answer** | Type private answer and tap **Save Answer** | Answers are saved in private state (`••••••••`). |
| 6 | Tap **Reveal Answers** | Tap **Reveal Answers** | Both answers unveil simultaneously with glowing animation. |
| 7 | Tap **Pulse 💓** button | Watch screen | Floating hearts burst across the screen. |
| 8 | Switch language to **العربية** in top bar | Keep in **Français** | Tayeb's UI flips cleanly to RTL; Cylia remains in French. |
| 9 | Select **18+ Intimacy & Boundaries** | Select **18+ Intimacy & Boundaries** | Adult mutual consent dialog appears before unlocking. |

---

## 8. Status Matrix: Implemented vs. Requires Deployment

| Feature | Local Engine Status | Cloud Deployment Requirement |
|---|---|---|
| **Dark Romantic UI & Theme** | ✅ Fully Implemented | None (Client-side) |
| **Questions Library (50+ questions)** | ✅ Fully Implemented | None (Embedded + Supabase ready) |
| **Language Switching & RTL** | ✅ Fully Implemented | None (Client-side) |
| **Private Reveal Mechanic** | ✅ Fully Implemented | None (Supports local simulation & cloud sync) |
| **Adult Age Gate & Consent** | ✅ Fully Implemented | None (Client-side + RLS enforced) |
| **Photo Sharing Consent & UI** | ✅ Fully Implemented | Supabase Storage bucket for multi-device sync |
| **Realtime Multi-Device Sync** | ✅ Schema & Client Ready | Requires entering Supabase URL & Anon Key in Settings or `.env` |
| **EAS Build Configuration** | ✅ `eas.json` Ready | Requires running `eas build` with your Expo account |
