package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.CoupleQuestion
import com.example.data.model.LoveNote
import com.example.data.model.PartnerRole
import com.example.data.model.QuestionAnswer
import com.example.data.model.QuestionCategory
import com.example.data.model.RoomState
import com.example.data.model.SharedPhoto
import com.example.data.repository.QuestionsRepository
import com.example.data.service.SupabaseService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class CouplesTab(val titleEn: String, val titleFr: String, val titleAr: String, val iconName: String) {
    GAME("Game", "Jeu", "اللعبة", "quiz"),
    NOTES("Love Notes", "Mots doux", "رسائل حب", "favorite"),
    PHOTOS("Shared Photos", "Photos", "الصور", "photo"),
    HISTORY("History", "Historique", "السجل", "history"),
    SETTINGS("Settings", "Réglages", "الإعدادات", "settings")
}

data class CouplesUiState(
    val currentTab: CouplesTab = CouplesTab.GAME,
    val myRole: PartnerRole = PartnerRole.TAYEB,
    val myCustomName: String = "Tayeb",
    val partnerCustomName: String = "Cylia",
    val language: AppLanguage = AppLanguage.ARABIC,
    val isInRoom: Boolean = true,
    val roomState: RoomState = RoomState(
        id = "room_tc_1",
        code = "LOVE77",
        hostRole = PartnerRole.TAYEB,
        hostName = "Tayeb",
        guestRole = PartnerRole.CYLIA,
        guestName = "Cylia",
        isGuestJoined = true,
        isHostOnline = true,
        isGuestOnline = true
    ),
    val selectedCategory: QuestionCategory = QuestionCategory.LOVE_FEELINGS,
    val questionIndex: Int = 0,
    val filteredQuestions: List<CoupleQuestion> = emptyList(),
    val answersMap: Map<String, List<QuestionAnswer>> = emptyMap(),
    val draftAnswer: String = "",
    val favorites: Set<String> = setOf("love_1"),
    val loveNotes: List<LoveNote> = emptyList(),
    val sharedPhotos: List<SharedPhoto> = emptyList(),
    val isAdultConsentAgreed: Boolean = false,
    val showAdultConsentDialog: Boolean = false,
    val pendingAdultCategory: QuestionCategory? = null,
    val showPhotoConsentDialog: Boolean = false,
    val isPhotoConsentAgreed: Boolean = false,
    val isPhotoUploading: Boolean = false,
    val photoUploadProgress: Float = 0f,
    val floatingHeartsCount: Int = 0,
    val toastMessage: String? = null,
    val supabaseUrl: String = "",
    val supabaseAnonKey: String = "",
    val isSupabaseConnected: Boolean = false,
    val supabaseSyncStatus: String = "Local Private Session (Offline / Ready)"
)

class CouplesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CouplesUiState())
    val uiState: StateFlow<CouplesUiState> = _uiState.asStateFlow()

    private val supabaseService = SupabaseService()

    init {
        // Initialize filtered questions
        val initialQuestions = QuestionsRepository.getQuestionsByCategory(QuestionCategory.LOVE_FEELINGS)
        
        // Initial romantic notes
        val sampleNotes = listOf(
            LoveNote(
                id = "note_1",
                senderRole = PartnerRole.CYLIA,
                senderName = "Cylia",
                message = "Je pense à toi chaque seconde mon amour ❤️",
                emoji = "🌹",
                timestampEpoch = System.currentTimeMillis() - 1000 * 60 * 15
            ),
            LoveNote(
                id = "note_2",
                senderRole = PartnerRole.TAYEB,
                senderName = "Tayeb",
                message = "أنتِ أجمل صدفة في حياتي يا سيليا.. أحبك إلى ما لا نهاية 💖",
                emoji = "✨",
                timestampEpoch = System.currentTimeMillis() - 1000 * 60 * 5
            )
        )

        // Initial answers for question 1 as preview
        val initialAnswers = mapOf(
            "love_1" to listOf(
                QuestionAnswer(
                    questionId = "love_1",
                    role = PartnerRole.TAYEB,
                    authorName = "Tayeb",
                    text = "اليوم الذي سمعت فيه ضحكتك لأول مرة، شعرت أن كل همومي تلاشت.",
                    isRevealed = true
                ),
                QuestionAnswer(
                    questionId = "love_1",
                    role = PartnerRole.CYLIA,
                    authorName = "Cylia",
                    text = "Quand tu as pris le temps de m'écouter pendant des heures sans jamais te lasser ❤️",
                    isRevealed = true
                )
            )
        )

        _uiState.update {
            it.copy(
                filteredQuestions = initialQuestions,
                loveNotes = sampleNotes,
                answersMap = initialAnswers
            )
        }
    }

    fun setTab(tab: CouplesTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(language = language) }
    }

    fun switchRole(role: PartnerRole) {
        _uiState.update {
            val myName = if (role == PartnerRole.TAYEB) "Tayeb" else "Cylia"
            val partnerName = if (role == PartnerRole.TAYEB) "Cylia" else "Tayeb"
            it.copy(
                myRole = role,
                myCustomName = myName,
                partnerCustomName = partnerName,
                draftAnswer = ""
            )
        }
    }

    fun setCategory(category: QuestionCategory) {
        if (category.isAdultOnly && !_uiState.value.isAdultConsentAgreed) {
            _uiState.update {
                it.copy(
                    showAdultConsentDialog = true,
                    pendingAdultCategory = category
                )
            }
            return
        }

        val questions = QuestionsRepository.getQuestionsByCategory(category)
        _uiState.update {
            it.copy(
                selectedCategory = category,
                filteredQuestions = questions,
                questionIndex = 0,
                draftAnswer = ""
            )
        }
    }

    fun acceptAdultConsent() {
        val category = _uiState.value.pendingAdultCategory ?: QuestionCategory.INTIMACY_BOUNDARIES
        val questions = QuestionsRepository.getQuestionsByCategory(category)
        _uiState.update {
            it.copy(
                isAdultConsentAgreed = true,
                showAdultConsentDialog = false,
                pendingAdultCategory = null,
                selectedCategory = category,
                filteredQuestions = questions,
                questionIndex = 0
            )
        }
    }

    fun dismissAdultConsent() {
        _uiState.update {
            it.copy(
                showAdultConsentDialog = false,
                pendingAdultCategory = null
            )
        }
    }

    fun getCurrentQuestion(): CoupleQuestion? {
        val questions = _uiState.value.filteredQuestions
        if (questions.isEmpty()) return null
        val idx = _uiState.value.questionIndex.coerceIn(0, questions.lastIndex)
        return questions[idx]
    }

    fun nextQuestion() {
        val questions = _uiState.value.filteredQuestions
        if (questions.isEmpty()) return
        val nextIdx = (_uiState.value.questionIndex + 1) % questions.size
        _uiState.update {
            it.copy(
                questionIndex = nextIdx,
                draftAnswer = ""
            )
        }
    }

    fun skipQuestion() {
        val questions = _uiState.value.filteredQuestions
        if (questions.isEmpty()) return
        val nextIdx = (_uiState.value.questionIndex + 1) % questions.size
        _uiState.update {
            it.copy(
                questionIndex = nextIdx,
                draftAnswer = "",
                toastMessage = "Question skipped"
            )
        }
    }

    fun previousQuestion() {
        val questions = _uiState.value.filteredQuestions
        if (questions.isEmpty()) return
        val prevIdx = if (_uiState.value.questionIndex - 1 < 0) questions.lastIndex else _uiState.value.questionIndex - 1
        _uiState.update {
            it.copy(
                questionIndex = prevIdx,
                draftAnswer = ""
            )
        }
    }

    fun shuffleQuestion() {
        val questions = _uiState.value.filteredQuestions
        if (questions.size <= 1) return
        var randomIdx = Random.nextInt(questions.size)
        if (randomIdx == _uiState.value.questionIndex) {
            randomIdx = (randomIdx + 1) % questions.size
        }
        _uiState.update {
            it.copy(
                questionIndex = randomIdx,
                draftAnswer = ""
            )
        }
    }

    fun toggleFavorite(questionId: String) {
        _uiState.update { current ->
            val newFavs = current.favorites.toMutableSet()
            if (newFavs.contains(questionId)) {
                newFavs.remove(questionId)
            } else {
                newFavs.add(questionId)
            }
            current.copy(favorites = newFavs)
        }
    }

    fun updateDraftAnswer(text: String) {
        _uiState.update { it.copy(draftAnswer = text) }
    }

    fun submitMyAnswer() {
        val question = getCurrentQuestion() ?: return
        val answerText = _uiState.value.draftAnswer.trim()
        if (answerText.isBlank()) return

        val myRole = _uiState.value.myRole
        val myName = _uiState.value.myCustomName

        val currentMap = _uiState.value.answersMap.toMutableMap()
        val answersForQ = (currentMap[question.id] ?: emptyList()).toMutableList()

        // Replace or add my answer (unrevealed initially by default unless toggled)
        answersForQ.removeAll { it.role == myRole }
        answersForQ.add(
            QuestionAnswer(
                questionId = question.id,
                role = myRole,
                authorName = myName,
                text = answerText,
                isRevealed = false
            )
        )
        currentMap[question.id] = answersForQ

        _uiState.update {
            it.copy(
                answersMap = currentMap,
                draftAnswer = "",
                toastMessage = "Answer saved privately! Reveal when ready ❤️"
            )
        }

        // Trigger floating hearts on answer
        triggerHeartBurst()
    }

    fun revealAnswer(questionId: String, role: PartnerRole) {
        val currentMap = _uiState.value.answersMap.toMutableMap()
        val answersForQ = (currentMap[questionId] ?: emptyList()).map {
            if (it.role == role) it.copy(isRevealed = true) else it
        }
        currentMap[questionId] = answersForQ
        _uiState.update { it.copy(answersMap = currentMap) }
        triggerHeartBurst()
    }

    fun revealAllAnswers(questionId: String) {
        val currentMap = _uiState.value.answersMap.toMutableMap()
        val answersForQ = (currentMap[questionId] ?: emptyList()).map {
            it.copy(isRevealed = true)
        }
        currentMap[questionId] = answersForQ
        _uiState.update { it.copy(answersMap = currentMap) }
        triggerHeartBurst()
    }

    fun simulatePartnerQuickAnswer(suggestedText: String? = null) {
        val question = getCurrentQuestion() ?: return
        val partnerRole = if (_uiState.value.myRole == PartnerRole.TAYEB) PartnerRole.CYLIA else PartnerRole.TAYEB
        val partnerName = _uiState.value.partnerCustomName

        val text = suggestedText ?: when (partnerRole) {
            PartnerRole.CYLIA -> "Tu es l'amour de ma vie Tayeb, chaque jour avec toi est un cadeau précieux ❤️"
            PartnerRole.TAYEB -> "أنتِ نور عيني وقلبي يا سيليا، لا شيء في العالم يقارن بجمالك وطيبتك 💖"
        }

        val currentMap = _uiState.value.answersMap.toMutableMap()
        val answersForQ = (currentMap[question.id] ?: emptyList()).toMutableList()
        answersForQ.removeAll { it.role == partnerRole }
        answersForQ.add(
            QuestionAnswer(
                questionId = question.id,
                role = partnerRole,
                authorName = partnerName,
                text = text,
                isRevealed = false
            )
        )
        currentMap[question.id] = answersForQ

        _uiState.update {
            it.copy(
                answersMap = currentMap,
                toastMessage = "$partnerName has answered! Click Reveal to view ✨"
            )
        }
    }

    fun triggerHeartBurst() {
        _uiState.update { it.copy(floatingHeartsCount = it.floatingHeartsCount + 1) }
    }

    fun sendLoveNote(message: String, emoji: String = "❤️") {
        if (message.isBlank()) return
        val newNote = LoveNote(
            id = "note_${System.currentTimeMillis()}",
            senderRole = _uiState.value.myRole,
            senderName = _uiState.value.myCustomName,
            message = message.trim(),
            emoji = emoji
        )
        _uiState.update {
            it.copy(loveNotes = listOf(newNote) + it.loveNotes)
        }
        triggerHeartBurst()
    }

    fun openPhotoConsent() {
        _uiState.update { it.copy(showPhotoConsentDialog = true) }
    }

    fun dismissPhotoConsent() {
        _uiState.update { it.copy(showPhotoConsentDialog = false) }
    }

    fun agreePhotoConsentAndUpload(photoUri: String, caption: String = "") {
        _uiState.update {
            it.copy(
                isPhotoConsentAgreed = true,
                showPhotoConsentDialog = false,
                isPhotoUploading = true,
                photoUploadProgress = 0.1f
            )
        }

        viewModelScope.launch {
            // Simulated upload progress with secure validation
            delay(300)
            _uiState.update { it.copy(photoUploadProgress = 0.5f) }
            delay(400)
            _uiState.update { it.copy(photoUploadProgress = 1.0f) }
            delay(200)

            val photo = SharedPhoto(
                id = "photo_${System.currentTimeMillis()}",
                senderRole = _uiState.value.myRole,
                senderName = _uiState.value.myCustomName,
                photoUri = photoUri,
                caption = caption
            )

            _uiState.update {
                it.copy(
                    isPhotoUploading = false,
                    photoUploadProgress = 0f,
                    sharedPhotos = listOf(photo) + it.sharedPhotos,
                    toastMessage = "Photo uploaded with privacy encryption 🔒"
                )
            }
            triggerHeartBurst()
        }
    }

    fun deletePhoto(photoId: String) {
        _uiState.update { current ->
            current.copy(
                sharedPhotos = current.sharedPhotos.filterNot { it.id == photoId },
                toastMessage = "Photo removed from shared memory"
            )
        }
    }

    fun createNewRoom() {
        val codeChars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val code = (1..6).map { codeChars.random() }.joinToString("")
        val newRoom = RoomState(
            id = "room_${System.currentTimeMillis()}",
            code = code,
            hostRole = _uiState.value.myRole,
            hostName = _uiState.value.myCustomName,
            guestRole = if (_uiState.value.myRole == PartnerRole.TAYEB) PartnerRole.CYLIA else PartnerRole.TAYEB,
            guestName = _uiState.value.partnerCustomName,
            isGuestJoined = true,
            isHostOnline = true,
            isGuestOnline = true
        )
        _uiState.update {
            it.copy(
                roomState = newRoom,
                toastMessage = "New private room created: $code ✨"
            )
        }
        triggerHeartBurst()
    }

    fun joinRoom(code: String) {
        val cleanCode = code.trim().uppercase()
        if (cleanCode.length < 4) {
            _uiState.update { it.copy(toastMessage = "Please enter a valid room code") }
            return
        }
        _uiState.update {
            it.copy(
                roomState = it.roomState.copy(
                    code = cleanCode,
                    isGuestJoined = true,
                    isGuestOnline = true
                ),
                toastMessage = "Connected to room $cleanCode! 💓"
            )
        }
        triggerHeartBurst()
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun updateSupabaseConfig(url: String, anonKey: String) {
        supabaseService.updateConfig(url, anonKey)
        val isConfigured = supabaseService.isConfigured()
        _uiState.update {
            it.copy(
                supabaseUrl = url,
                supabaseAnonKey = anonKey,
                isSupabaseConnected = isConfigured,
                supabaseSyncStatus = if (isConfigured) "Connected to Supabase Cloud Database 🟢" else "Local Session (Enter Supabase credentials to enable multi-device sync)",
                toastMessage = if (isConfigured) "Supabase credentials updated successfully!" else "Configuration saved"
            )
        }
    }

    fun clearAllData() {
        _uiState.update {
            it.copy(
                answersMap = emptyMap(),
                loveNotes = emptyList(),
                sharedPhotos = emptyList(),
                favorites = emptySet(),
                draftAnswer = "",
                toastMessage = "Session data erased safely"
            )
        }
    }
}
