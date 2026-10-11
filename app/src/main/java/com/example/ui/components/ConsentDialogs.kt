package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun AdultConsentDialog(
    language: AppLanguage,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = when (language) {
        AppLanguage.ARABIC -> "تأكيد موافقة البالغين (18+)"
        AppLanguage.FRENCH -> "Consentement d'adultes (18+)"
        AppLanguage.ENGLISH -> "Consenting Adults Confirmation (18+)"
    }

    val message = when (language) {
        AppLanguage.ARABIC -> "قسم الحميمية والحدود مخصص فقط للبالغين بالتراضي التام. تهدف هذه الأسئلة لتعزيز التقارب الصادق والاحترام المتبادل بين الشريكين. هل توافق على فتح هذا القسم؟"
        AppLanguage.FRENCH -> "La section 'Intimité & Limites' est strictement réservée aux adultes consentants. Ces questions visent à approfondir votre complicité dans le respect absolu de chacun. Confirmez-vous votre accord mutuel ?"
        AppLanguage.ENGLISH -> "The 'Intimacy & Boundaries' section is strictly intended for consenting adults. These questions foster deep, honest connection and mutual respect. Do you both agree to unlock this category?"
    }

    val confirmText = when (language) {
        AppLanguage.ARABIC -> "أوافق بالتراضي"
        AppLanguage.FRENCH -> "J'accepte avec consentement"
        AppLanguage.ENGLISH -> "I Agree & Consent"
    }

    val cancelText = when (language) {
        AppLanguage.ARABIC -> "إلغاء"
        AppLanguage.FRENCH -> "Annuler"
        AppLanguage.ENGLISH -> "Cancel"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = DarkSurfaceVariant,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningAmber,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x33F59E0B), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Private & Encrypted Space",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarningAmber
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                modifier = Modifier.testTag("confirm_adult_consent_button")
            ) {
                Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_adult_consent_button")
            ) {
                Text(cancelText, color = TextMuted)
            }
        }
    )
}

@Composable
fun PhotoConsentDialog(
    language: AppLanguage,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = when (language) {
        AppLanguage.ARABIC -> "مشاركة الصور بالتراضي والخصوصية"
        AppLanguage.FRENCH -> "Partage de photos & Confidentialité"
        AppLanguage.ENGLISH -> "Consensual Photo Sharing & Privacy"
    }

    val warning1 = when (language) {
        AppLanguage.ARABIC -> "• مشاركة الصور اختيارية تماماً وقائمة على الرضا المشترك."
        AppLanguage.FRENCH -> "• Le partage de photos est strictement facultatif et consenti."
        AppLanguage.ENGLISH -> "• Photo sharing is 100% optional and strictly consensual."
    }

    val warning2 = when (language) {
        AppLanguage.ARABIC -> "• تنبيه هام: حذف الصورة من التطبيق لا يمسح أي لقطة شاشة (Screenshot) قد يأخذها الطرف الآخر."
        AppLanguage.FRENCH -> "• Attention : Supprimer une photo n'efface pas les captures d'écran déjà enregistrées."
        AppLanguage.ENGLISH -> "• Privacy note: Deleting a photo cannot erase screenshots saved by your partner."
    }

    val warning3 = when (language) {
        AppLanguage.ARABIC -> "• لا تشارك أي صورة إلا برغبتك واطمئنانك التام."
        AppLanguage.FRENCH -> "• Ne partagez que ce que vous êtes pleinement à l'aise de transmettre."
        AppLanguage.ENGLISH -> "• Never upload or share photos under any pressure."
    }

    val confirmText = when (language) {
        AppLanguage.ARABIC -> "فهمت وأوافق"
        AppLanguage.FRENCH -> "J'ai compris et j'accepte"
        AppLanguage.ENGLISH -> "I Understand & Consent"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = DarkSurfaceVariant,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = NeonPink,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = warning1, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text(text = warning2, style = MaterialTheme.typography.bodySmall, color = WarningAmber, fontWeight = FontWeight.SemiBold)
                Text(text = warning3, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                modifier = Modifier.testTag("confirm_photo_consent_button")
            ) {
                Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_photo_consent_button")
            ) {
                Text(if (language == AppLanguage.ARABIC) "إلغاء" else "Cancel", color = TextMuted)
            }
        }
    )
}
