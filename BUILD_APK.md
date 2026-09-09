# Building and Downloading Zxh4Stream APK via GitHub Actions

This repository includes an automated CI/CD pipeline (`.github/workflows/build-apk.yml`) to compile, sign, and export downloadable Android APKs directly from GitHub Actions.

---

## 1. How to Run the Build on GitHub

### Automatic Builds
- **Every Git Push**: Every push to the `main` or `master` branch automatically triggers the APK build workflow.
- **Git Tags**: Pushing a tag (e.g. `git tag v1.0.0 && git push origin v1.0.0`) automatically builds the APK and creates a **GitHub Release** with the APK binaries attached.

### Manual One-Click Build
1. Open your repository on GitHub.
2. Click on the **Actions** tab at the top.
3. In the left sidebar, click **Build & Export Android APK**.
4. Click the **Run workflow** dropdown on the right.
5. Choose:
   - **Build Variant**: `debug` (default, immediately installable on any phone), `release`, or `both`.
   - **Create Release**: check the box if you want a GitHub Release created.
6. Click **Run workflow**.

---

## 2. Where to Download the APK

1. Once the workflow run completes (takes ~2–3 minutes), click on the completed run.
2. Scroll down to the **Artifacts** section at the bottom of the summary page.
3. Click on **Zxh4Stream-APK** to download the ZIP file containing:
   - `Zxh4Stream-latest-debug.apk`: Ready to install directly on any Android device.
   - `CHECKSUMS.txt`: SHA-256 verification hashes for security.

---

## 3. How to Install on Android

1. Transfer the `.apk` file to your Android device (or download it directly from GitHub on your phone browser).
2. Tap the `.apk` file to start installation.
3. If prompted with *"For your security, your phone is not allowed to install unknown apps from this source"*:
   - Tap **Settings**.
   - Toggle **Allow from this source** to **ON**.
   - Return and tap **Install**.
4. Open **Zxh4Stream** and enjoy!

---

## 4. Optional: Custom Release Signing Key

If you want release builds signed with your own private keystore instead of the default signing key:
1. Go to your GitHub repository **Settings** -> **Secrets and variables** -> **Actions**.
2. Add the following repository secrets:
   - `RELEASE_KEYSTORE_BASE64`: Base64 encoded string of your `.jks` keystore file (`base64 -w 0 my-upload-key.jks`).
   - `STORE_PASSWORD`: Keystore password.
   - `KEY_PASSWORD`: Key alias password.
