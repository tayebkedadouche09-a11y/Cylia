package com.example.data.model

enum class PartnerRole(val defaultName: String, val emoji: String) {
    TAYEB("Tayeb", "👦🏻"),
    CYLIA("Cylia", "👧🏻")
}

enum class AppLanguage(val code: String, val label: String, val isRtl: Boolean) {
    ARABIC("ar", "العربية", true),
    FRENCH("fr", "Français", false),
    ENGLISH("en", "English", false)
}

enum class QuestionCategory(
    val id: String,
    val titleAr: String,
    val titleFr: String,
    val titleEn: String,
    val emoji: String,
    val isAdultOnly: Boolean = false
) {
    LOVE_FEELINGS(
        "love_feelings",
        "مشاعر وحب",
        "Amour & Sentiments",
        "Love & Feelings",
        "💖"
    ),
    ATTRACTION_CRUSHES(
        "attraction_crushes",
        "إعجاب وجاذبية",
        "Attraction & Coups de cœur",
        "Attraction & Crushes",
        "✨"
    ),
    CARE_ATTENTION(
        "care_attention",
        "اهتمام وتفاصيل",
        "Attention & Tendresse",
        "Care & Attention",
        "🫂"
    ),
    BOLD_QUESTIONS(
        "bold_questions",
        "أسئلة جريئة",
        "Questions Audacieuses",
        "Bold Questions",
        "🔥"
    ),
    INTIMACY_BOUNDARIES(
        "intimacy_boundaries",
        "حميمية وحدود (18+)",
        "Intimité & Limites (18+)",
        "Intimacy & Boundaries (18+)",
        "🔞",
        isAdultOnly = true
    )
}

data class CoupleQuestion(
    val id: String,
    val category: QuestionCategory,
    val textAr: String,
    val textFr: String,
    val textEn: String,
    val hintAr: String? = null,
    val hintFr: String? = null,
    val hintEn: String? = null
) {
    fun getText(language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> textAr
        AppLanguage.FRENCH -> textFr
        AppLanguage.ENGLISH -> textEn
    }

    fun getHint(language: AppLanguage): String? = when (language) {
        AppLanguage.ARABIC -> hintAr
        AppLanguage.FRENCH -> hintFr
        AppLanguage.ENGLISH -> hintEn
    }
}

data class RoomState(
    val id: String = "",
    val code: String = "",
    val hostRole: PartnerRole = PartnerRole.TAYEB,
    val hostName: String = "Tayeb",
    val guestRole: PartnerRole = PartnerRole.CYLIA,
    val guestName: String = "Cylia",
    val isGuestJoined: Boolean = false,
    val isHostOnline: Boolean = true,
    val isGuestOnline: Boolean = false,
    val currentQuestionId: String = "q1",
    val selectedCategory: QuestionCategory = QuestionCategory.LOVE_FEELINGS,
    val createdAtEpoch: Long = System.currentTimeMillis(),
    val expiresAtEpoch: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000L) // 24 hours
)

data class QuestionAnswer(
    val questionId: String,
    val role: PartnerRole,
    val authorName: String,
    val text: String,
    val isRevealed: Boolean = false,
    val answeredAtEpoch: Long = System.currentTimeMillis()
)

data class LoveNote(
    val id: String,
    val senderRole: PartnerRole,
    val senderName: String,
    val message: String,
    val emoji: String = "❤️",
    val timestampEpoch: Long = System.currentTimeMillis()
)

data class SharedPhoto(
    val id: String,
    val senderRole: PartnerRole,
    val senderName: String,
    val photoUri: String,
    val caption: String = "",
    val timestampEpoch: Long = System.currentTimeMillis()
)
