package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CoupleQuestion
import com.example.data.model.PartnerRole
import com.example.data.model.QuestionAnswer
import com.example.data.model.QuestionCategory
import com.example.ui.CouplesUiState
import com.example.ui.CouplesViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHigh
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GradientEnd
import com.example.ui.theme.GradientStart
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkLight
import com.example.ui.theme.RomanticPurple
import com.example.ui.theme.RomanticPurpleDark
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun GameScreen(
    state: CouplesUiState,
    viewModel: CouplesViewModel,
    modifier: Modifier = Modifier
) {
    val question = viewModel.getCurrentQuestion()
    val answersForQuestion = if (question != null) {
        state.answersMap[question.id] ?: emptyList()
    } else emptyList()

    val myAnswer = answersForQuestion.firstOrNull { it.role == state.myRole }
    val partnerRole = if (state.myRole == PartnerRole.TAYEB) PartnerRole.CYLIA else PartnerRole.TAYEB
    val partnerAnswer = answersForQuestion.firstOrNull { it.role == partnerRole }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Partner Status & Heartbeat bar
        item {
            PartnerPresenceHeader(
                state = state,
                onSendHeartbeat = { viewModel.triggerHeartBurst() }
            )
        }

        // 2. Category Filter Chips
        item {
            CategoryChipsRow(
                selectedCategory = state.selectedCategory,
                onSelectCategory = { viewModel.setCategory(it) },
                isAdultUnlocked = state.isAdultConsentAgreed
            )
        }

        // 3. Question Card
        item {
            if (question != null) {
                QuestionCard(
                    question = question,
                    language = state.language,
                    index = state.questionIndex + 1,
                    total = state.filteredQuestions.size,
                    isFavorite = state.favorites.contains(question.id),
                    onToggleFavorite = { viewModel.toggleFavorite(question.id) },
                    onShuffle = { viewModel.shuffleQuestion() }
                )
            } else {
                EmptyQuestionsPlaceholder()
            }
        }

        // 4. Private Answer Input Box
        item {
            if (question != null) {
                PrivateAnswerSection(
                    state = state,
                    myAnswer = myAnswer,
                    draftAnswer = state.draftAnswer,
                    onDraftChange = { viewModel.updateDraftAnswer(it) },
                    onSubmit = { viewModel.submitMyAnswer() }
                )
            }
        }

        // 5. Revealed / Masked Answers Comparison Card
        item {
            if (question != null && (myAnswer != null || partnerAnswer != null)) {
                AnswersRevealSection(
                    question = question,
                    myAnswer = myAnswer,
                    partnerAnswer = partnerAnswer,
                    partnerRole = partnerRole,
                    partnerName = state.partnerCustomName,
                    onRevealAll = { viewModel.revealAllAnswers(question.id) },
                    onSimulatePartner = { viewModel.simulatePartnerQuickAnswer() }
                )
            }
        }

        // 6. Navigation Controls (Prev, Skip, Next)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.previousQuestion() },
                    modifier = Modifier.testTag("prev_question_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = TextSecondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.language == AppLanguage.ARABIC) "السابق" else "Previous",
                        color = TextSecondary
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.skipQuestion() },
                    modifier = Modifier.testTag("skip_question_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = null, tint = RomanticPurple)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.language == AppLanguage.ARABIC) "تخطي" else "Skip",
                        color = RomanticPurple
                    )
                }

                Button(
                    onClick = { viewModel.nextQuestion() },
                    modifier = Modifier.testTag("next_question_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPink)
                ) {
                    Text(
                        text = if (state.language == AppLanguage.ARABIC) "التالي" else "Next",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun PartnerPresenceHeader(
    state: CouplesUiState,
    onSendHeartbeat: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(SuccessEmerald)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tayeb & Cylia ❤️",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = if (state.language == AppLanguage.ARABIC)
                        "المسافة مؤقتة.. وحبنا أبدي ✈️"
                    else "Distance is temporary, love is forever ✨",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            // Quick Heartbeat Button
            Button(
                onClick = onSendHeartbeat,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                modifier = Modifier.testTag("heartbeat_pulse_button")
            ) {
                Text(text = "💓 Pulse", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun CategoryChipsRow(
    selectedCategory: QuestionCategory,
    onSelectCategory: (QuestionCategory) -> Unit,
    isAdultUnlocked: Boolean
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuestionCategory.values().forEach { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onSelectCategory(category) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(category.emoji)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(category.titleAr)
                        if (category.isAdultOnly && !isAdultUnlocked) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonPink,
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: CoupleQuestion,
    language: AppLanguage,
    index: Int,
    total: Int,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShuffle: () -> Unit
) {
    var showHint by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${question.category.emoji} #${index}/$total",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = NeonPinkLight
                    )
                }

                Row {
                    IconButton(
                        onClick = onShuffle,
                        modifier = Modifier.size(36.dp).testTag("shuffle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            tint = RomanticPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(36.dp).testTag("fav_button")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) NeonPink else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Question Text
            Text(
                text = question.getText(language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 32.sp
            )

            // Alternate languages toggle preview
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (language == AppLanguage.ARABIC) question.textFr else question.textAr,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    textAlign = TextAlign.End
                )
            }

            // Hint / Inspiration section
            val hint = question.getHint(language)
            if (!hint.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x22A855F7))
                        .clickable { showHint = !showHint }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = RomanticPurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showHint) hint else (if (language == AppLanguage.ARABIC) "تلميح وإلهام للإجابة..." else "Tap for inspiration..."),
                        style = MaterialTheme.typography.labelSmall,
                        color = RomanticPurple
                    )
                }
            }
        }
    }
}

@Composable
fun PrivateAnswerSection(
    state: CouplesUiState,
    myAnswer: QuestionAnswer?,
    draftAnswer: String,
    onDraftChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkSurfaceHigh, DarkSurfaceVariant)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = NeonPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (state.language == AppLanguage.ARABIC)
                            "إجابتك الخاصة (${state.myCustomName})"
                        else "Your Private Answer (${state.myCustomName})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                if (myAnswer != null) {
                    Text(
                        text = if (state.language == AppLanguage.ARABIC) "تم الحفظ 🔒" else "Saved 🔒",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessEmerald
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = draftAnswer,
                onValueChange = onDraftChange,
                placeholder = {
                    Text(
                        text = if (state.language == AppLanguage.ARABIC)
                            "اكتب إجابتك هنا بصدق.. لن يراها شريكك إلا بعد الكشف ❤️"
                        else "Write privately here... Your partner only sees it when revealed ❤️",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("answer_input_field"),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPink,
                    unfocusedBorderColor = DarkSurfaceHigh,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onSubmit,
                    enabled = draftAnswer.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                    modifier = Modifier.testTag("submit_answer_button")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (state.language == AppLanguage.ARABIC) "حفظ الإجابة الخاصة" else "Save Answer",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AnswersRevealSection(
    question: CoupleQuestion,
    myAnswer: QuestionAnswer?,
    partnerAnswer: QuestionAnswer?,
    partnerRole: PartnerRole,
    partnerName: String,
    onRevealAll: () -> Unit,
    onSimulatePartner: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Couples Answers / إجابات الحبيبين 💑",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = RomanticPurple
            )

            Spacer(modifier = Modifier.height(12.dp))

            // My Answer Card
            SingleAnswerItem(
                roleLabel = myAnswer?.authorName ?: "You",
                answer = myAnswer,
                isMine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Partner Answer Card
            SingleAnswerItem(
                roleLabel = partnerName,
                answer = partnerAnswer,
                isMine = false
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Reveal action button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If partner hasn't answered, provide quick simulate button for testing on single device
                if (partnerAnswer == null) {
                    OutlinedButton(
                        onClick = onSimulatePartner,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("simulate_partner_answer_button")
                    ) {
                        Text("Simulate $partnerName's Answer", fontSize = 11.sp, color = RomanticPurple)
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = onRevealAll,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RomanticPurple),
                    modifier = Modifier.testTag("reveal_all_button")
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reveal Answers / كشف الإجابات ✨", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SingleAnswerItem(
    roleLabel: String,
    answer: QuestionAnswer?,
    isMine: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, if (isMine) NeonPink.copy(alpha = 0.4f) else RomanticPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = roleLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isMine) NeonPinkLight else RomanticPurple
                )

                Text(
                    text = if (answer == null) "Waiting... ⏳"
                    else if (answer.isRevealed) "Revealed 🔓"
                    else "Private 🔒",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (answer?.isRevealed == true) SuccessEmerald else WarningAmber
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (answer == null) {
                Text(
                    text = "No answer submitted yet.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            } else if (answer.isRevealed) {
                Text(
                    text = answer.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            } else {
                Text(
                    text = "•••••••••••••••••••••••••••••••• (Hidden until revealed)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.blur(2.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyQuestionsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No questions in this category.", color = TextMuted)
    }
}
