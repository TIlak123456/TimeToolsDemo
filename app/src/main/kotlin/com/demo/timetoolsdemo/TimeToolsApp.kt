package com.demo.timetoolsdemo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.timetoolsdemo.ui.theme.AlarmColor
import com.demo.timetoolsdemo.ui.theme.StopwatchColor
import com.demo.timetoolsdemo.ui.theme.TimerColor

enum class Tab(val title: String, val icon: ImageVector, val selectedIcon: ImageVector, val color: Color) {
    ALARM("Alarm", Icons.Outlined.Alarm, Icons.Filled.Alarm, AlarmColor),
    STOPWATCH("Stopwatch", Icons.Outlined.Timer, Icons.Filled.Timer, StopwatchColor),
    TIMER("Timer", Icons.Outlined.HourglassEmpty, Icons.Filled.HourglassFull, TimerColor)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeToolsApp() {
    var selectedTab by remember { mutableStateOf(Tab.ALARM) }
    
    // Feature flags state
    var alarmEnabled by remember { mutableStateOf(true) }
    var alarmSnoozeEnabled by remember { mutableStateOf(true) }
    var alarmSoundOptionsEnabled by remember { mutableStateOf(true) }
    
    var stopwatchEnabled by remember { mutableStateOf(true) }
    var stopwatchLapEnabled by remember { mutableStateOf(true) }
    var stopwatchHistoryEnabled by remember { mutableStateOf(true) }
    
    var timerEnabled by remember { mutableStateOf(true) }
    var timerPresetsEnabled by remember { mutableStateOf(true) }
    var timerSoundEnabled by remember { mutableStateOf(true) }
    
    // Load feature flags
    LaunchedEffect(Unit) {
        alarmEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_ALARM_ENABLED, true)
        alarmSnoozeEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_ALARM_SNOOZE, true)
        alarmSoundOptionsEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_ALARM_SOUND_OPTIONS, true)
        
        stopwatchEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_STOPWATCH_ENABLED, true)
        stopwatchLapEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_STOPWATCH_LAP, true)
        stopwatchHistoryEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_STOPWATCH_HISTORY, true)
        
        timerEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_TIMER_ENABLED, true)
        timerPresetsEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_TIMER_PRESETS, true)
        timerSoundEnabled = LaunchDarklyManager.getBooleanFlag(LaunchDarklyManager.FLAG_TIMER_SOUND, true)
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Time Tools",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedTab == tab) tab.selectedIcon else tab.icon,
                                contentDescription = tab.title,
                                tint = if (selectedTab == tab) tab.color else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                tab.title,
                                color = if (selectedTab == tab) tab.color else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = tab.color.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                Tab.ALARM -> AlarmScreen(
                    isEnabled = alarmEnabled,
                    snoozeEnabled = alarmSnoozeEnabled,
                    soundOptionsEnabled = alarmSoundOptionsEnabled
                )
                Tab.STOPWATCH -> StopwatchScreen(
                    isEnabled = stopwatchEnabled,
                    lapEnabled = stopwatchLapEnabled,
                    historyEnabled = stopwatchHistoryEnabled
                )
                Tab.TIMER -> TimerScreen(
                    isEnabled = timerEnabled,
                    presetsEnabled = timerPresetsEnabled,
                    soundEnabled = timerSoundEnabled
                )
            }
        }
    }
}

@Composable
fun AlarmScreen(
    isEnabled: Boolean,
    snoozeEnabled: Boolean,
    soundOptionsEnabled: Boolean
) {
    var alarmTime by remember { mutableStateOf("07:30") }
    var isAlarmOn by remember { mutableStateOf(false) }
    
    if (!isEnabled) {
        FeatureDisabledScreen(
            title = "Alarm Clock",
            message = "This feature is currently disabled",
            color = AlarmColor
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Large clock display
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AlarmColor.copy(alpha = 0.1f),
                            AlarmColor.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                AlarmColor.copy(alpha = 0.15f),
                                AlarmColor.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Alarm,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = AlarmColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = alarmTime,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Light,
                        color = AlarmColor
                    )
                    Text(
                        text = "AM",
                        fontSize = 20.sp,
                        color = AlarmColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Alarm toggle
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isAlarmOn) AlarmColor.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Wake Up Alarm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        if (isAlarmOn) "Alarm is set" else "Tap to enable",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isAlarmOn,
                    onCheckedChange = { isAlarmOn = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AlarmColor
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Feature options
        AnimatedVisibility(visible = snoozeEnabled) {
            FeatureOptionCard(
                icon = Icons.Filled.Snooze,
                title = "Snooze",
                subtitle = "5 minutes",
                color = AlarmColor
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        AnimatedVisibility(visible = soundOptionsEnabled) {
            FeatureOptionCard(
                icon = Icons.Filled.MusicNote,
                title = "Alarm Sound",
                subtitle = "Classic Bell",
                color = AlarmColor
            )
        }
    }
}

@Composable
fun StopwatchScreen(
    isEnabled: Boolean,
    lapEnabled: Boolean,
    historyEnabled: Boolean
) {
    var isRunning by remember { mutableStateOf(false) }
    var displayTime by remember { mutableStateOf("00:00.00") }
    
    if (!isEnabled) {
        FeatureDisabledScreen(
            title = "Stopwatch",
            message = "This feature is currently disabled",
            color = StopwatchColor
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Stopwatch display
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            StopwatchColor.copy(alpha = 0.1f),
                            StopwatchColor.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                StopwatchColor.copy(alpha = 0.15f),
                                StopwatchColor.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = StopwatchColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = displayTime,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Light,
                        color = StopwatchColor
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Reset button
            FilledTonalButton(
                onClick = { displayTime = "00:00.00" },
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Start/Stop button
            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier.size(88.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color(0xFFE53935) else StopwatchColor
                )
            ) {
                Icon(
                    if (isRunning) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Stop" else "Start",
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }
            
            // Lap button
            AnimatedVisibility(visible = lapEnabled) {
                FilledTonalButton(
                    onClick = { },
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = StopwatchColor.copy(alpha = 0.15f)
                    )
                ) {
                    Icon(
                        Icons.Filled.Flag,
                        contentDescription = "Lap",
                        modifier = Modifier.size(28.dp),
                        tint = StopwatchColor
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // History section
        AnimatedVisibility(
            visible = historyEnabled,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Lap History",
                            style = MaterialTheme.typography.titleSmall,
                            color = StopwatchColor
                        )
                        Icon(
                            Icons.Filled.History,
                            contentDescription = null,
                            tint = StopwatchColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No laps recorded yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TimerScreen(
    isEnabled: Boolean,
    presetsEnabled: Boolean,
    soundEnabled: Boolean
) {
    var timerValue by remember { mutableStateOf("05:00") }
    var isRunning by remember { mutableStateOf(false) }
    
    if (!isEnabled) {
        FeatureDisabledScreen(
            title = "Countdown Timer",
            message = "This feature is currently disabled",
            color = TimerColor
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Timer display
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            TimerColor.copy(alpha = 0.1f),
                            TimerColor.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                TimerColor.copy(alpha = 0.15f),
                                TimerColor.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.HourglassFull,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = TimerColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = timerValue,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Light,
                        color = TimerColor
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Preset buttons
        AnimatedVisibility(visible = presetsEnabled) {
            Column {
                Text(
                    "Quick Presets",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("1:00", "3:00", "5:00", "10:00").forEach { preset ->
                        AssistChip(
                            onClick = { timerValue = preset },
                            label = { Text(preset) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (timerValue == preset) TimerColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (timerValue == preset) TimerColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Reset button
            FilledTonalButton(
                onClick = { timerValue = "05:00"; isRunning = false },
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Start/Stop button
            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier.size(88.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color(0xFFE53935) else TimerColor
                )
            ) {
                Icon(
                    if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start",
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sound option
        AnimatedVisibility(visible = soundEnabled) {
            FeatureOptionCard(
                icon = Icons.Filled.NotificationsActive,
                title = "Timer Sound",
                subtitle = "Gentle Chime",
                color = TimerColor
            )
        }
    }
}

@Composable
fun FeatureDisabledScreen(
    title: String,
    message: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = color.copy(alpha = 0.5f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color.copy(alpha = 0.08f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Controlled by LaunchDarkly",
                    style = MaterialTheme.typography.bodyMedium,
                    color = color
                )
            }
        }
    }
}

@Composable
fun FeatureOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
