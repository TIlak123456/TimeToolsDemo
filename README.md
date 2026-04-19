# Time Tools Demo - LaunchDarkly Feature Flags Demo App

A polished Android demo app showcasing LaunchDarkly feature flags with three time-based tools: Alarm Clock, Stopwatch, and Countdown Timer.

## Features

Each feature has its own set of feature flags that can be toggled via LaunchDarkly:

### Alarm Clock
- `alarm-clock-enabled` - Enable/disable the entire alarm feature
- `alarm-snooze-enabled` - Show/hide snooze option
- `alarm-sound-options-enabled` - Show/hide sound selection

### Stopwatch
- `stopwatch-enabled` - Enable/disable the entire stopwatch feature
- `stopwatch-lap-enabled` - Show/hide lap button
- `stopwatch-history-enabled` - Show/hide lap history section

### Countdown Timer
- `countdown-timer-enabled` - Enable/disable the entire timer feature
- `timer-presets-enabled` - Show/hide quick preset buttons
- `timer-sound-enabled` - Show/hide timer sound option

## Setup

### 1. Add Your LaunchDarkly SDK Key

Edit `app/src/main/kotlin/com/demo/timetoolsdemo/LaunchDarklyManager.kt`:

```kotlin
private const val MOBILE_KEY = "YOUR_LAUNCHDARKLY_MOBILE_KEY"
```

Replace `YOUR_LAUNCHDARKLY_MOBILE_KEY` with your **Mobile SDK Key** from LaunchDarkly.

### 2. Build the APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

The APK will be generated at:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release-unsigned.apk`

## LaunchDarkly Dashboard Setup

### Step 1: Create a Project
1. Log in to [LaunchDarkly](https://app.launchdarkly.com)
2. Go to **Account Settings** → **Projects**
3. Click **Create project** and name it "Time Tools Demo"

### Step 2: Get Your Mobile SDK Key
1. Go to **Account Settings** → **Projects** → Your Project
2. Copy the **Mobile key** (NOT the SDK key or Client-side ID)

### Step 3: Create Feature Flags

Create the following **boolean** feature flags:

| Flag Key | Name | Default Value |
|----------|------|---------------|
| `alarm-clock-enabled` | Alarm Clock Enabled | `true` |
| `alarm-snooze-enabled` | Alarm Snooze Enabled | `true` |
| `alarm-sound-options-enabled` | Alarm Sound Options | `true` |
| `stopwatch-enabled` | Stopwatch Enabled | `true` |
| `stopwatch-lap-enabled` | Stopwatch Lap Enabled | `true` |
| `stopwatch-history-enabled` | Stopwatch History | `true` |
| `countdown-timer-enabled` | Countdown Timer Enabled | `true` |
| `timer-presets-enabled` | Timer Presets Enabled | `true` |
| `timer-sound-enabled` | Timer Sound Enabled | `true` |

### Step 4: Create Each Flag

For each flag:
1. Go to **Feature flags** → **Create flag**
2. Enter the **Key** exactly as shown above
3. Set **Flag type** to **Boolean**
4. Set **Default variations**:
   - When ON: `true`
   - When OFF: `false`
5. Click **Save flag**
6. **Turn ON** the flag in your environment

### Step 5: Demo the App

1. Install the APK on your device
2. Open the app - all features should be visible
3. In LaunchDarkly dashboard, toggle any flag OFF
4. Restart the app (or wait for real-time updates)
5. The corresponding feature will show a "Feature Disabled" screen

## Demo Script for VP Presentation

1. **Show all features enabled** - Navigate through Alarm, Stopwatch, Timer tabs
2. **Disable a main feature** - Turn off `alarm-clock-enabled` → Show locked screen
3. **Disable sub-features** - Turn off `stopwatch-lap-enabled` → Lap button disappears
4. **Explain the value** - Feature flags allow:
   - Gradual rollouts
   - A/B testing
   - Kill switches for problematic features
   - User segmentation

## Tech Stack

- **Kotlin** with Jetpack Compose
- **Material Design 3** for modern UI
- **LaunchDarkly Android SDK** for feature flags
- **Minimal dependencies** for fast builds

## Requirements

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Min SDK: Android 7.0 (API 24)
