package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.model.SharedPhoto
import com.example.ui.CouplesUiState
import com.example.ui.CouplesViewModel
import com.example.ui.components.PhotoConsentDialog
import com.example.ui.theme.DangerRose
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHigh
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkLight
import com.example.ui.theme.RomanticPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun PhotosScreen(
    state: CouplesUiState,
    viewModel: CouplesViewModel,
    modifier: Modifier = Modifier
) {
    var pendingPickedUri by remember { mutableStateOf<Uri?>(null) }

    // Android Photo Picker contract (Zero permissions required, Privacy-friendly)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            if (state.isPhotoConsentAgreed) {
                viewModel.agreePhotoConsentAndUpload(uri.toString(), "Our Special Memory ❤️")
            } else {
                pendingPickedUri = uri
                viewModel.openPhotoConsent()
            }
        }
    }

    if (state.showPhotoConsentDialog) {
        PhotoConsentDialog(
            language = state.language,
            onConfirm = {
                val uri = pendingPickedUri
                if (uri != null) {
                    viewModel.agreePhotoConsentAndUpload(uri.toString(), "Our Special Memory ❤️")
                    pendingPickedUri = null
                }
            },
            onDismiss = {
                pendingPickedUri = null
                viewModel.dismissPhotoConsent()
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Privacy Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = NeonPink,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "مساحة الصور الخاصة والمحمية 🔒" else "Private & Consensual Photos 🔒",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (state.language == AppLanguage.ARABIC)
                            "تنبيه: مشاركة الصور اختيارية تماماً. حذف الصورة لا يمسح لقطات الشاشة المأخوذة من الطرف الآخر."
                        else "Privacy Note: Sharing is strictly optional. Deleting photos from app cannot erase screenshots saved by recipient.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Action: Pick / Share Photo Button
        item {
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("pick_photo_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink)
            ) {
                Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.language == AppLanguage.ARABIC) "اختيار صورة ومشاركتها بالرضا ❤️" else "Share a Consensual Photo ❤️",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Upload Progress Indicator
        if (state.isPhotoUploading) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Encrypting & uploading photo... 🔒",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonPinkLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { state.photoUploadProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = NeonPink,
                            trackColor = DarkSurfaceHigh,
                        )
                    }
                }
            }
        }

        // Empty state or Photo Cards
        if (state.sharedPhotos.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = DarkSurfaceHigh,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC)
                                "لا توجد صور مشتركة بعد.\nشارك لحظة مميزة أو ابتسامة دافئة برضا تام."
                            else "No shared photos yet.\nShare a sweet moment when you're both ready.",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(state.sharedPhotos) { photo ->
                SharedPhotoItem(
                    photo = photo,
                    isMine = photo.senderRole == state.myRole,
                    onDelete = { viewModel.deletePhoto(photo.id) }
                )
            }
        }
    }
}

@Composable
fun SharedPhotoItem(
    photo: SharedPhoto,
    isMine: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = photo.photoUri,
                    contentDescription = photo.caption,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Delete button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color(0x88000000), RoundedCornerShape(20.dp))
                        .testTag("delete_photo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Photo",
                        tint = DangerRose,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = photo.caption.ifBlank { "Shared with love" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "From ${photo.senderName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isMine) NeonPinkLight else RomanticPurple
                    )
                }

                Text(
                    text = "Encrypted 🔒",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}
