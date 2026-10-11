package com.example.data.repository

import com.example.data.model.CoupleQuestion
import com.example.data.model.QuestionCategory

object QuestionsRepository {

    val allQuestions: List<CoupleQuestion> = listOf(
        // === 1. Love & Feelings (مشاعر وحب / Amour & Sentiments) ===
        CoupleQuestion(
            id = "love_1",
            category = QuestionCategory.LOVE_FEELINGS,
            textAr = "ما هي اللحظة المحددة التي أدركت فيها أنك تحبني بصدق؟",
            textFr = "Quel est le moment exact où tu as réalisé que tu m'aimais vraiment ?",
            textEn = "What was the exact moment you realized you truly loved me?",
            hintAr = "تذكر التفاصيل والمشاعر في ذلك اليوم",
            hintFr = "Rappelle-toi les détails et ce que tu ressentais ce jour-là",
            hintEn = "Recall the details and feelings of that day"
        ),
        CoupleQuestion(
            id = "love_2",
            category = QuestionCategory.LOVE_FEELINGS,
            textAr = "كيف يغير وجودي في حياتك نظرتك للمستقبل رغم المسافات بيننا؟",
            textFr = "Comment ma présence dans ta vie change-t-elle ta vision de l'avenir malgré la distance ?",
            textEn = "How does having me in your life change your perspective on the future despite the distance?",
            hintAr = "عن الأمان والدعم اليومي",
            hintFr = "Parle de sécurité et de soutien au quotidien",
            hintEn = "Think about emotional safety and daily support"
        ),
        CoupleQuestion(
            id = "love_3",
            category = QuestionCategory.LOVE_FEELINGS,
            textAr = "ما هو الشيء الذي أفعله ويجعلك تشعر أنك أغلى إنسان في قلبي؟",
            textFr = "Quelle est la chose que je fais qui te fait sentir le plus précieux à mes yeux ?",
            textEn = "What is something I do that makes you feel most cherished in my heart?",
            hintAr = "حركة بسيطة أو كلمة معينة",
            hintFr = "Un petit geste ou un mot particulier",
            hintEn = "A subtle gesture or a special phrase"
        ),
        CoupleQuestion(
            id = "love_4",
            category = QuestionCategory.LOVE_FEELINGS,
            textAr = "ما هي أكثر رسالة أو كلمة بيننا لا زلت تتذكرها وتبتسم كلما خطرت ببالك؟",
            textFr = "Quel message ou mot échangé entre nous te fait encore sourire à chaque fois ?",
            textEn = "What text message or phrase from us still makes you smile every time you remember it?",
            hintAr = "من بدايات حبنا أو في ليلة خاصة",
            hintFr = "Au tout début ou lors d'une soirée marquante",
            hintEn = "From early in our story or on a memorable night"
        ),
        CoupleQuestion(
            id = "love_5",
            category = QuestionCategory.LOVE_FEELINGS,
            textAr = "عندما تشتاق إليّ في أوقات الوحدة، ما هو أول تصرف تلجأ إليه؟",
            textFr = "Quand je te manque dans les moments de solitude, quel est le premier réflexe que tu as ?",
            textEn = "When you miss me in lonely moments, what is the first thing you tend to do?",
            hintAr = "صورة، صوت، أو ذكريات",
            hintFr = "Une photo, un enregistrement audio, ou des souvenirs",
            hintEn = "A photo, voice note, or quiet memories"
        ),
        CoupleQuestion(
            id = "love_6",
            category = QuestionCategory.LOVE_FEELINGS,
            textAr = "ما هو الوعد الذي تقطعه على نفسك لحبنا ولعلاقتنا مهما كانت الظروف؟",
            textFr = "Quelle est la promesse que tu te fais pour notre amour quoi qu'il arrive ?",
            textEn = "What promise do you make to yourself for our love, no matter what challenges come?",
            hintAr = "عهد بالوفاء والصبر",
            hintFr = "Un serment de loyauté et de patience",
            hintEn = "A vow of patience and unwavering devotion"
        ),

        // === 2. Attraction & Crushes (إعجاب وجاذبية / Attraction & Coups de cœur) ===
        CoupleQuestion(
            id = "attr_1",
            category = QuestionCategory.ATTRACTION_CRUSHES,
            textAr = "ما هي أول ميزة لفتت انتباهك وجذبتك نحوي في البداية؟",
            textFr = "Quelle est la toute première chose qui t'a attiré(e) chez moi au début ?",
            textEn = "What was the very first trait or feature that caught your attention about me?",
            hintAr = "العيون، الابتسامة، الصوت، أو الأسلوب",
            hintFr = "Le regard, le sourire, la voix ou la façon d'être",
            hintEn = "Eyes, smile, voice, or vibe"
        ),
        CoupleQuestion(
            id = "attr_2",
            category = QuestionCategory.ATTRACTION_CRUSHES,
            textAr = "ما هي نظرة العين أو الابتسامة مني التي تجعل قلبك يخفق بشدة؟",
            textFr = "Quel regard ou sourire de ma part fait battre ton cœur le plus fort ?",
            textEn = "Which look or smile of mine makes your heart beat the fastest?",
            hintAr = "في مكالمات الفيديو أو الصور",
            hintFr = "En appel vidéo ou sur une photo",
            hintEn = "On video calls or in photos"
        ),
        CoupleQuestion(
            id = "attr_3",
            category = QuestionCategory.ATTRACTION_CRUSHES,
            textAr = "لو وصفت جاذبيتي وعطري ووجودي بثلاث كلمات، ماذا تختار؟",
            textFr = "Si tu devais décrire mon charme et mon aura en trois mots, lesquels choisirais-tu ?",
            textEn = "If you had to describe my charm and aura in three words, what would they be?",
            hintAr = "كلمات صادقة من أعماق القلب",
            hintFr = "Des mots sincères du fond du cœur",
            hintEn = "Three heartfelt adjectives"
        ),
        CoupleQuestion(
            id = "attr_4",
            category = QuestionCategory.ATTRACTION_CRUSHES,
            textAr = "ما هي العادة أو الحركة اللطيفة التي أفعلها بعفوية وتجعلك تقع في غرامي أكثر؟",
            textFr = "Quelle petite habitude spontanée chez moi te fait craquer encore plus ?",
            textEn = "What subtle, spontaneous habit of mine makes you fall for me all over again?",
            hintAr = "طريقة الضحك، الخجل، أو نبرة الصوت",
            hintFr = "La manière de rire, la timidité ou une intonation",
            hintEn = "A laugh, blushing, or tone of voice"
        ),
        CoupleQuestion(
            id = "attr_5",
            category = QuestionCategory.ATTRACTION_CRUSHES,
            textAr = "ما هو أكثر زي أو إطلالة رأيتني بها وسحرت عقلك تماماً؟",
            textFr = "Quelle tenue ou quel style dans lequel tu m'as vu(e) t'a totalement envoûté(e) ?",
            textEn = "Which outfit or look you've seen me in completely enchanted you?",
            hintAr = "لون أو مظهر معين تفضله",
            hintFr = "Une couleur ou un style précis que tu adores",
            hintEn = "A favorite color or style"
        ),

        // === 3. Care & Attention (اهتمام وتفاصيل / Attention & Tendresse) ===
        CoupleQuestion(
            id = "care_1",
            category = QuestionCategory.CARE_ATTENTION,
            textAr = "عندما أكون حزيناً أو متعباً، كيف تحب أن تحتويني وتهدئ روعي عن بعد؟",
            textFr = "Quand je suis triste ou épuisé(e), comment aimes-tu me réconforter à distance ?",
            textEn = "When I am sad or drained, how do you prefer to comfort and soothe me from afar?",
            hintAr = "صوت دافئ، الاستماع، أو كلمات تشجيع",
            hintFr = "Une voix douce, l'écoute, ou des mots d'encouragement",
            hintEn = "Gentle listening, sweet voice notes, or reassurance"
        ),
        CoupleQuestion(
            id = "care_2",
            category = QuestionCategory.CARE_ATTENTION,
            textAr = "ما هي أدق تفصيلة تخصني وتعرفها أنت ولا ينتبه إليها أحد غيرك؟",
            textFr = "Quel détail subtil me concernant connais-tu et que personne d'autre ne remarque ?",
            textEn = "What subtle detail about me do you notice that nobody else seems to?",
            hintAr = "مزاجي من نبرة صوتي، أو عاداتي الصغيرة",
            hintFr = "Mon humeur au son de ma voix, ou de petites manies",
            hintEn = "My mood changes by voice tone, or little habits"
        ),
        CoupleQuestion(
            id = "care_3",
            category = QuestionCategory.CARE_ATTENTION,
            textAr = "ما هو الشيء الصغير الذي يمكنني فعله اليوم ليجعل يومك أجمل وأخف وزناً؟",
            textFr = "Quelle petite attention de ma part aujourd'hui rendrait ta journée plus belle et légère ?",
            textEn = "What small gesture from me today could make your day noticeably brighter and lighter?",
            hintAr = "مشاركة دعاء، محادثة هادئة، أو صورة مفاجئة",
            hintFr = "Un mot tendre, un appel calme ou une photo surprise",
            hintEn = "A loving note, calm call, or surprise smile"
        ),
        CoupleQuestion(
            id = "care_4",
            category = QuestionCategory.CARE_ATTENTION,
            textAr = "كيف تشعر حين نتشارك نفس الموسيقى أو نشاهد شيئاً معاً في نفس الدقيقة عبر الشاشة؟",
            textFr = "Que ressens-tu quand on écoute la même musique ou regarde quelque chose en même temps à distance ?",
            textEn = "How does it feel when we listen to music or watch something synchronized across screen distance?",
            hintAr = "تقريب المسافات ودفء الروح",
            hintFr = "Effacer les kilomètres et sentir la proximité",
            hintEn = "Bridging the miles and feeling connected"
        ),
        CoupleQuestion(
            id = "care_5",
            category = QuestionCategory.CARE_ATTENTION,
            textAr = "ما هو الطقس أو النشاط الروتيني الذي تتمنى أن نفعله كل صباح معاً عندما نكون في نفس البيت؟",
            textFr = "Quelle routine matinale as-tu hâte de partager avec moi quand nous serons enfin réunis ?",
            textEn = "What morning routine are you most excited to share together once distance is behind us?",
            hintAr = "فنجان قهوة، صباح الخير، أو المشي معاً",
            hintFr = "Café ensemble, réveil en douceur, ou petit-déjeuner",
            hintEn = "Morning coffee, gentle awakening, cooking together"
        ),

        // === 4. Bold Questions (أسئلة جريئة / Questions Audacieuses) ===
        CoupleQuestion(
            id = "bold_1",
            category = QuestionCategory.BOLD_QUESTIONS,
            textAr = "ما هو الاعتراف الجريء الذي تتردد في قوله بصوت عالٍ لكنك تفكر فيه دائماً؟",
            textFr = "Quel aveu osé hésites-tu à formuler à voix haute alors qu'il t'occupe souvent l'esprit ?",
            textEn = "What bold confession do you hesitate to say out loud, yet think about often?",
            hintAr = "لا تخجل، هذا مكان آمن وخاص بنا وحدنا",
            hintFr = "N'aie aucune gêne, nous sommes dans notre bulle intime",
            hintEn = "Be fearless, this is our sacred and private space"
        ),
        CoupleQuestion(
            id = "bold_2",
            category = QuestionCategory.BOLD_QUESTIONS,
            textAr = "ما هي الفكرة الأكثر جنوناً ورومانسية التي تخيلت أن نعيشها سوياً في أول لقاء قادم؟",
            textFr = "Quelle est l'idée la plus folle et passionnée que tu as imaginée pour nos retrouvailles ?",
            textEn = "What is the wildest and most passionate scenario you've imagined for our next reunion?",
            hintAr = "العناق الأول، الهروب لمكان سري، أو البقاء معاً لأيام",
            hintFr = "La première étreinte, une escapade secrète, ou un long tête-à-tête",
            hintEn = "The first long embrace, a secret getaway, or staying lost in each other"
        ),
        CoupleQuestion(
            id = "bold_3",
            category = QuestionCategory.BOLD_QUESTIONS,
            textAr = "هل سبق وشعرت بالغيرة الشديدة بسبب شيء ما ولم تخبرني به في حينه؟",
            textFr = "As-tu déjà ressenti une vive jalousie sans me le dire sur le moment ?",
            textEn = "Have you ever felt secretly very jealous over something and held it in at the time?",
            hintAr = "شاركني بصراحة وبلا تردد",
            hintFr = "Partage-le avec franchise et tendresse",
            hintEn = "Share honestly and tenderly"
        ),
        CoupleQuestion(
            id = "bold_4",
            category = QuestionCategory.BOLD_QUESTIONS,
            textAr = "ما هو الشيء الذي يمكنني فعله ليفقدك صوابك تماماً من فرط الحب والافتتان؟",
            textFr = "Que pourrais-je faire pour te faire totalement perdre la tête d'amour et de fascination ?",
            textEn = "What could I do or whisper that would make you completely lose your composure?",
            hintAr = "همسة معينة، نظرة صامتة، أو جرأة غير متوقعة",
            hintFr = "Un murmure particulier, un regard insistant, ou une audace",
            hintEn = "A whisper, an intense glance, or surprising boldness"
        ),

        // === 5. Adult Intimacy & Boundaries (حميمية وحدود 18+ / Intimité & Limites 18+) ===
        CoupleQuestion(
            id = "intimacy_1",
            category = QuestionCategory.INTIMACY_BOUNDARIES,
            textAr = "ما هي اللمسة أو القبلة التي تتخيلها بيننا عندما تنتهي المسافات ونكون وحدنا تماماً؟",
            textFr = "Quelle caresse ou quel baiser imagines-tu entre nous quand la distance aura disparu et que nous serons seuls ?",
            textEn = "What touch or kiss do you envision between us when the distance is gone and we are completely alone?",
            hintAr = "صف رغبتك وشوقك بكل صدق ودفء",
            hintFr = "Décris ton désir et ton attente avec passion et douceur",
            hintEn = "Describe your longing and tenderness with honesty"
        ),
        CoupleQuestion(
            id = "intimacy_2",
            category = QuestionCategory.INTIMACY_BOUNDARIES,
            textAr = "ما هي الحدود أو المساحات الشخصية التي ترغب في أن نحترمها ونعززها دائماً في علاقتنا؟",
            textFr = "Quelles limites et espaces personnels souhaites-tu que nous respections toujours dans notre intimité ?",
            textEn = "What boundaries and personal comfort zones do you want us to always honor and safeguard in our intimacy?",
            hintAr = "التواصل الصريح والاحترام المتبادل أهم ركائز الحب",
            hintFr = "La communication et le respect mutuel sont nos piliers",
            hintEn = "Open communication and enthusiastic mutual consent"
        ),
        CoupleQuestion(
            id = "intimacy_3",
            category = QuestionCategory.INTIMACY_BOUNDARIES,
            textAr = "ما هو الخيال الحميمي الأكثر دفئاً الذي يراودك عن ليلتنا الأولى معاً في عش الزوجية؟",
            textFr = "Quel est ton plus doux fantasme intime concernant notre toute première nuit ensemble ?",
            textEn = "What is your warmest, most cherished intimate fantasy about our first shared night together?",
            hintAr = "أمان، هدوء، شغف، وانسجام تام",
            hintFr = "Sécurité, douceur, passion et harmonie absolue",
            hintEn = "Safety, tenderness, passion, and deep connection"
        ),
        CoupleQuestion(
            id = "intimacy_4",
            category = QuestionCategory.INTIMACY_BOUNDARIES,
            textAr = "كيف تحب أن نعبر عن رغبتنا وشوقنا الجسدي عندما نكون بعيدين عن بعضنا البعض؟",
            textFr = "Comment aimes-tu exprimer notre désir et notre manque physique lorsque nous sommes éloignés ?",
            textEn = "How do you like us to express our physical desire and longing while miles apart?",
            hintAr = "بالكلمات، الهمسات، أو الرسائل الخاصة المشفرة",
            hintFr = "Par les mots, les chuchotements ou des messages complices",
            hintEn = "Through romantic words, late-night whispers, and subtle codes"
        )
    )

    fun getQuestionsByCategory(category: QuestionCategory): List<CoupleQuestion> {
        return allQuestions.filter { it.category == category }
    }

    fun getQuestionById(id: String): CoupleQuestion {
        return allQuestions.firstOrNull { it.id == id } ?: allQuestions.first()
    }
}
