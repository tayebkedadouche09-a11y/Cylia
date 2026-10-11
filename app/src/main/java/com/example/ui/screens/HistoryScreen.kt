package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CoupleQuestion
import com.example.data.model.QuestionAnswer
import com.example.data.repository.QuestionsRepository
import com.example.ui.CouplesUiState
import com.example.ui.CouplesViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkLight
import com.example.ui.theme.RomanticPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HistoryScreen(
    state: CouplesUiState,
    viewModel: CouplesViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0 = Answer History, 1 = Favorites

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = DarkSurface,
            contentColor = NeonPink,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = NeonPink
                )
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "سجل الإجابات" else "Answered History",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )

            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.language == AppLanguage.ARABIC) "المفضلة (${state.favorites.size})" else "Favorites (${state.favorites.size})",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedSubTab == 0) {
            // Answered questions history
            val answeredQuestions = state.answersMap.keys.map { qId ->
                QuestionsRepository.getQuestionById(qId)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (answeredQuestions.isEmpty()) {
                    item {
                        EmptyHistoryView(message = if (state.language == AppLanguage.ARABIC) "لم تجيبا على أي سؤال بعد. ابدآ اللعب الآن! 💖" else "No answered questions yet. Start playing together! 💖")
                    }
                } else {
                    items(answeredQuestions) { question ->
                        val answers = state.answersMap[question.id] ?: emptyList()
                        AnsweredQuestionHistoryCard(
                            question = question,
                            answers = answers,
                            language = state.language
                        )
                    }
                }
            }
        } else {
            // Favorites list
            val favoriteQuestions = state.favorites.map { qId ->
                QuestionsRepository.getQuestionById(qId)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (favoriteQuestions.isEmpty()) {
                    item {
                        EmptyHistoryView(message = if (state.language == AppLanguage.ARABIC) "لا توجد أسئلة مفضلة بعد. اضغط على أيقونة القلب لحفظها! ❤️" else "No favorites saved yet. Tap the heart on questions to bookmark! ❤️")
                    }
                } else {
                    items(favoriteQuestions) { question ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${question.category.emoji} ${question.category.titleAr}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NeonPinkLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = question.getText(state.language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnsweredQuestionHistoryCard(
    question: CoupleQuestion,
    answers: List<QuestionAnswer>,
    language: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "${question.category.emoji} ${question.getText(language)}",
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            answers.forEach { ans ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${ans.authorName}:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = NeonPinkLight
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (ans.isRevealed) ans.text else "•••••••• (Hidden)",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (ans.isRevealed) TextSecondary else TextMuted,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
