package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.PartnerRole
import com.example.ui.CouplesUiState
import com.example.ui.CouplesViewModel
import com.example.ui.theme.DangerRose
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHigh
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkLight
import com.example.ui.theme.RomanticPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun SettingsScreen(
    state: CouplesUiState,
    viewModel: CouplesViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputRoomCode by remember { mutableStateOf("") }
    var inputSupabaseUrl by remember { mutableStateOf(state.supabaseUrl) }
    var inputSupabaseKey by remember { mutableStateOf(state.supabaseAnonKey) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Language selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = RomanticPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "لغة التطبيق" else "Language / Langue",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.values().forEach { lang ->
                            FilterChip(
                                selected = state.language == lang,
                                onClick = { viewModel.setLanguage(lang) },
                                label = { Text(lang.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonPink,
                                    selectedLabelColor = Color.White,
                                    containerColor = DarkSurfaceVariant,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }

        // 2. Partner Identity switch
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NeonPink)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "أنت تلعب الآن بدور:" else "You are playing as:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.switchRole(PartnerRole.TAYEB) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.myRole == PartnerRole.TAYEB) NeonPink else DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("👦🏻 Tayeb", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.switchRole(PartnerRole.CYLIA) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.myRole == PartnerRole.CYLIA) NeonPink else DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("👧🏻 Cylia", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Room Management
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MeetingRoom, contentDescription = null, tint = RomanticPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "الغرفة الخاصة والرمز" else "Private Room & Code",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Room Code:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = state.roomState.code,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = NeonPinkLight
                            )
                        }

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val inviteMsg = "Rejoins notre jeu d'amour Tayeb & Cylia ❤️ Code: ${state.roomState.code}"
                                val clip = ClipData.newPlainText("Room Code", inviteMsg)
                                clipboard.setPrimaryClip(clip)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RomanticPurple),
                            modifier = Modifier.testTag("copy_room_code_button")
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Invite", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputRoomCode,
                            onValueChange = { inputRoomCode = it.uppercase() },
                            placeholder = { Text("Enter 6-char code...", color = TextMuted, fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonPink,
                                unfocusedBorderColor = DarkSurfaceHigh,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Button(
                            onClick = {
                                viewModel.joinRoom(inputRoomCode)
                                inputRoomCode = ""
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                            modifier = Modifier.testTag("join_room_button")
                        ) {
                            Text("Join")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.createNewRoom() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("new_room_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = RomanticPurple, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate New Private Room Code", color = RomanticPurple)
                    }
                }
            }
        }

        // 4. Supabase Backend Integration
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Cloud, contentDescription = null, tint = if (state.isSupabaseConnected) SuccessEmerald else RomanticPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Supabase Cloud Sync / التزامن السحابي",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = state.supabaseSyncStatus,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (state.isSupabaseConnected) SuccessEmerald else TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputSupabaseUrl,
                        onValueChange = { inputSupabaseUrl = it },
                        label = { Text("Supabase URL (https://xyz.supabase.co)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPink,
                            unfocusedBorderColor = DarkSurfaceHigh,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = NeonPink,
                            unfocusedLabelColor = TextMuted
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputSupabaseKey,
                        onValueChange = { inputSupabaseKey = it },
                        label = { Text("Supabase Anon Key (public)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPink,
                            unfocusedBorderColor = DarkSurfaceHigh,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = NeonPink,
                            unfocusedLabelColor = TextMuted
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.updateSupabaseConfig(inputSupabaseUrl, inputSupabaseKey)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RomanticPurple),
                        modifier = Modifier.fillMaxWidth().testTag("save_supabase_config_button")
                    ) {
                        Icon(imageVector = Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Connect / Save Credentials", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 5. Privacy, Consent & Danger Zone
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = WarningAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "الخصوصية وإدارة البيانات" else "Privacy & Data Management",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (state.language == AppLanguage.ARABIC)
                            "جميع الإجابات والرسائل مشفرة ومحمية. يمكنك حذف بيانات الجلسة فوراً بضغطة واحدة."
                        else "All answers and notes are private. You can wipe session data anytime.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.clearAllData() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRose),
                        modifier = Modifier.fillMaxWidth().testTag("wipe_session_button")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = DangerRose, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "مسح جميع بيانات الجلسة ⚠️" else "Erase Session Data ⚠️",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
