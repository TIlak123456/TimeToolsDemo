package com.demo.timetoolsdemo

import android.app.Application
import android.content.Context
import com.launchdarkly.sdk.LDUser
import com.launchdarkly.sdk.android.LDClient
import com.launchdarkly.sdk.android.LDConfig

object LaunchDarklyManager {
    
    // Replace with your LaunchDarkly Mobile SDK Key
    private const val MOBILE_KEY = "mob-118be441-72a8-4d9c-a59e-570908c5a2b1"
    
    private var ldClient: LDClient? = null
    
    // Feature flag keys
    const val FLAG_ALARM_ENABLED = "alarm-clock-enabled"
    const val FLAG_ALARM_SNOOZE = "alarm-snooze-enabled"
    const val FLAG_ALARM_SOUND_OPTIONS = "alarm-sound-options-enabled"
    
    const val FLAG_STOPWATCH_ENABLED = "stopwatch-enabled"
    const val FLAG_STOPWATCH_LAP = "stopwatch-lap-enabled"
    const val FLAG_STOPWATCH_HISTORY = "stopwatch-history-enabled"
    
    const val FLAG_TIMER_ENABLED = "countdown-timer-enabled"
    const val FLAG_TIMER_PRESETS = "timer-presets-enabled"
    const val FLAG_TIMER_SOUND = "timer-sound-enabled"
    
    fun initialize(context: Context) {
        val ldConfig = LDConfig.Builder()
            .mobileKey(MOBILE_KEY)
            .build()
        
        val ldUser = LDUser.Builder("demo-user-123")
            .name("Demo User")
            .email("demo@example.com")
            .custom("plan", "premium")
            .build()
        
        ldClient = LDClient.init(context.applicationContext as Application, ldConfig, ldUser, 5)
    }
    
    fun getBooleanFlag(flagKey: String, defaultValue: Boolean = false): Boolean {
        return ldClient?.boolVariation(flagKey, defaultValue) ?: defaultValue
    }
    
    fun close() {
        ldClient?.close()
    }
}
