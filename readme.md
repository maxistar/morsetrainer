# Morse Trainer

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
alt="Get it on Google Play"
height="80">](https://play.google.com/store/apps/details?id=com.maxistar.morsetrainer)

Morse Trainer is an Android application for learning Morse code. It presents characters, plays audio feedback, accepts dot/dash input, and tracks learning progress over time.

More information is available on the project website: <https://morse.maxistar.me/>.

## Project Scope

The Android app supports:

- Latin letters, numbers, punctuation, and Cyrillic characters.
- Audio feedback for characters, Morse sounds, correct answers, and errors.
- Two-button input, one-button input, and optional volume-button input.
- Progress tracking and configurable character groups.
- Localized interface strings.

## Development Requirements

Use Android Studio or the Gradle wrapper from this repository.

Common local commands should be run from the `morsetrainer/` directory:

```sh
./gradlew test
./gradlew lint
```

## Build And Verification

Run unit tests:

```sh
./gradlew test
```

Run Android lint:

```sh
./gradlew lint
```

For a maintenance release, also smoke test the main user flows:

- App launch.
- Training screen.
- One-button mode.
- Settings screen.
- Language selection.
- Progress screen.
- Audio feedback.

## Screenshots

Fastlane is used for screenshot automation.

```sh
bundle exec fastlane screengrab
```

The debug manifest intentionally avoids deprecated shared-storage permissions. If screenshot tooling needs storage behavior changes in the future, handle that as a dedicated screenshot-tooling change.

## Release Metadata

Android release metadata is maintained in:

```text
app/build.gradle.kts
```

For each release, update:

- `versionCode`
- `versionName`

The manifest should not own release version metadata.

## Fastlane Release Notes

Google Play / Fastlane changelogs are stored by version code and locale:

```text
fastlane/metadata/android/en-US/changelogs/
fastlane/metadata/android/ru-RU/changelogs/
```

For version code `21`, the release notes are:

```text
fastlane/metadata/android/en-US/changelogs/21.txt
fastlane/metadata/android/ru-RU/changelogs/21.txt
```

## CI/CD Release Ownership

Signed APK creation and publishing are handled by the existing Fastlane CI/CD pipeline. Local maintenance work should not require signing secrets for normal verification.

Local verification should focus on:

- `./gradlew test`
- `./gradlew lint`
- manual smoke testing
- release notes and version metadata review

## Issues

The project was made for fun and is supported from time to time. Ideas and issues can be filed at:

<https://github.com/maxistar/morsetrainer/issues>
