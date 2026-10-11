package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.PartnerRole
import com.example.ui.CouplesTab
import com.example.ui.CouplesViewModel
import com.example.ui.components.AdultConsentDialog
import com.example.ui.components.FloatingHeartsEffect
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.PhotosScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkLight
import com.example.ui.theme.RomanticPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    private val viewModel: CouplesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.uiState.collectAsState()
            val layoutDir = if (state.language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
                MyApplicationTheme {
                    MainAppContent(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    viewModel: CouplesViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    if (state.showAdultConsentDialog) {
        AdultConsentDialog(
            language = state.language,
            onConfirm = { viewModel.acceptAdultConsent() },
            onDismiss = { viewModel.dismissAdultConsent() }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = DarkBg,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Tayeb & Cylia ❤️",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    },
                    actions = {
                        // Language indicator / toggle
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceVariant,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (state.myRole == PartnerRole.TAYEB) "👦🏻 Tayeb" else "👧🏻 Cylia",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NeonPinkLight
                                )
                            }
                        }

                        // Quick language switch button
                        IconButton(
                            onClick = {
                                val nextLang = when (state.language) {
                                    AppLanguage.ARABIC -> AppLanguage.FRENCH
                                    AppLanguage.FRENCH -> AppLanguage.ENGLISH
                                    AppLanguage.ENGLISH -> AppLanguage.ARABIC
                                }
                                viewModel.setLanguage(nextLang)
                            },
                            modifier = Modifier.testTag("lang_toggle_button")
                        ) {
                            Text(
                                text = when (state.language) {
                                    AppLanguage.ARABIC -> "عربي"
                                    AppLanguage.FRENCH -> "FR"
                                    AppLanguage.ENGLISH -> "EN"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = RomanticPurple
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = DarkSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    val tabs = listOf(
                        Triple(CouplesTab.GAME, Icons.Default.Quiz, if (state.language == AppLanguage.ARABIC) "الأسئلة" else "Questions"),
                        Triple(CouplesTab.NOTES, Icons.Default.ChatBubble, if (state.language == AppLanguage.ARABIC) "رسائل" else "Notes"),
                        Triple(CouplesTab.PHOTOS, Icons.Default.PhotoLibrary, if (state.language == AppLanguage.ARABIC) "الصور" else "Photos"),
                        Triple(CouplesTab.HISTORY, Icons.Default.History, if (state.language == AppLanguage.ARABIC) "السجل" else "History"),
                        Triple(CouplesTab.SETTINGS, Icons.Default.Settings, if (state.language == AppLanguage.ARABIC) "إعدادات" else "Settings")
                    )

                    tabs.forEach { (tab, icon, label) ->
                        val isSelected = state.currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) NeonPink else TextMuted
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) NeonPink else TextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = DarkSurfaceVariant
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
                when (state.currentTab) {
                    CouplesTab.GAME -> GameScreen(state = state, viewModel = viewModel)
                    CouplesTab.NOTES -> NotesScreen(state = state, viewModel = viewModel)
                    CouplesTab.PHOTOS -> PhotosScreen(state = state, viewModel = viewModel)
                    CouplesTab.HISTORY -> HistoryScreen(state = state, viewModel = viewModel)
                    CouplesTab.SETTINGS -> SettingsScreen(state = state, viewModel = viewModel)
                }
            }
        }

        // Floating Hearts Layer across the whole app
        FloatingHeartsEffect(triggerCount = state.floatingHeartsCount)
    }
}
