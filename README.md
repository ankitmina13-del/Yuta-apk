# Yuta AI

A premium-feeling, completely free Android AI companion app.

- 💬 **Chat** — natural conversation in Hindi, Hinglish, or English
- 🎨 **Create** — unlimited AI image generation (no account, no payment)
- 🎙️ **Voice** — talk to Yuta and hear her talk back

No API keys required. No subscriptions. No ads. Runs entirely on free, keyless
endpoints from [Pollinations.ai](https://pollinations.ai), and the APK itself
is built for free by GitHub Actions.

---

## How to get the APK on your phone

You don't need Android Studio or a computer that can build Android apps.
GitHub will build it for you, for free, in the cloud.

1. **Create a GitHub account** if you don't have one (free): https://github.com/join
2. **Create a new repository** on GitHub (any name, e.g. `yuta-ai`), then upload
   every file from this folder into it (drag-and-drop on the GitHub website
   works fine, or use `git push` if you're comfortable with git).
3. Once the files are uploaded, open the **Actions** tab on your repository.
4. You'll see a workflow called **"Build Yuta AI APK"** — click it, then click
   **"Run workflow"** (or it will run automatically after you push).
5. Wait 3–5 minutes. When it finishes with a green checkmark, click into that
   run and scroll down to **Artifacts** — download **`yuta-ai-debug-apk`**.
6. Unzip it — inside is `app-debug.apk`.
7. Transfer that `.apk` to your phone (email it to yourself, upload to Google
   Drive, or use a USB cable) and tap it to install.
8. Your phone will warn you about installing from an unknown source — that's
   normal for any app not from the Play Store. Go to **Settings → allow this
   source**, then install.

That's it — Yuta AI is on your phone, and you paid nothing at any step.

---

## Project structure

```
app/
 └─ src/main/
     ├─ java/com/yuta/ai/
     │   ├─ MainActivity.kt          — bottom navigation (Chat / Create / Voice)
     │   ├─ YutaApp.kt
     │   ├─ data/
     │   │   ├─ PollinationsApi.kt   — free chat + image API client
     │   │   └─ ChatViewModel.kt     — conversation state
     │   └─ ui/
     │       ├─ theme/               — premium dark + violet/cyan gradient theme
     │       ├─ components/          — shared gradient UI pieces
     │       └─ screens/             — ChatScreen, ImageScreen, VoiceScreen
     └─ res/
         ├─ drawable/                — Yuta's logo (adaptive app icon)
         └─ values/                  — colors, strings, theme
.github/workflows/build-apk.yml      — builds the APK on GitHub's free servers
```

---

## What you'll need to add yourself

The app works out of the box with **zero setup** — but here's what's optional
and worth knowing about:

1. **Nothing is required to get it running.** Chat, image generation, and
   voice all use Pollinations.ai's free, keyless endpoints — no sign-up.
2. **Rate limits**: since there's no API key, anonymous requests are limited
   to roughly one request every 15 seconds and chat has a shared daily quota.
   If you hit a limit, wait a bit and try again.
3. **Optional — remove the watermark / raise limits**: get a free key at
   https://enter.pollinations.ai (still $0, no card) and pass it as a header
   in `PollinationsApi.kt` if you want higher limits down the line. Not
   required to use the app.
4. **App icon**: I designed a simple "Y" spark-mark logo
   (`app/src/main/res/drawable/ic_launcher_foreground.xml`). Swap that file
   for your own artwork any time if you want a different look.
5. **Releasing on Play Store (optional, not needed to just use the app)**:
   you'd need a signing keystore and a $25 one-time Google Play developer
   fee — skip this entirely if you just want the APK on your own phone.

Everything else — the chat engine, image generation, voice input/output, and
the whole interface — is already wired up and ready to use.
