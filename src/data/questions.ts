import { CoupleQuestion } from '../types';

export const BUILT_IN_QUESTIONS: CoupleQuestion[] = [
  // 1. Love & Feelings
  {
    id: 'love_1',
    category: 'love_feelings',
    textAr: 'ما هي اللحظة المحددة التي أدركت فيها أنك تحبني بصدق؟',
    textFr: 'Quel est le moment exact où tu as réalisé que tu m\'aimais vraiment ?',
    textEn: 'What was the exact moment you realized you truly loved me?',
    hintAr: 'تذكر التفاصيل والمشاعر في ذلك اليوم',
    hintFr: 'Rappelle-toi les détails et ce que tu ressentais ce jour-là',
    hintEn: 'Recall the details and feelings of that day'
  },
  {
    id: 'love_2',
    category: 'love_feelings',
    textAr: 'كيف يغير وجودي في حياتك نظرتك للمستقبل رغم المسافات بيننا؟',
    textFr: 'Comment ma présence dans ta vie change-t-elle ta vision de l\'avenir malgré la distance ?',
    textEn: 'How does having me in your life change your perspective on the future despite the distance?',
  },
  {
    id: 'love_3',
    category: 'love_feelings',
    textAr: 'ما هو الشيء الذي أفعله ويجعلك تشعر أنك أغلى إنسان في قلبي؟',
    textFr: 'Quelle est la chose que je fais qui te fait sentir le plus précieux à mes yeux ?',
    textEn: 'What is something I do that makes you feel most cherished in my heart?',
  },

  // 2. Attraction & Crushes
  {
    id: 'attr_1',
    category: 'attraction_crushes',
    textAr: 'ما هي أول ميزة لفتت انتباهك وجذبتك نحوي في البداية؟',
    textFr: 'Quelle est la toute première chose qui t\'a attiré(e) chez moi au début ?',
    textEn: 'What was the very first trait or feature that caught your attention about me?',
  },
  {
    id: 'attr_2',
    category: 'attraction_crushes',
    textAr: 'ما هي نظرة العين أو الابتسامة مني التي تجعل قلبك يخفق بشدة؟',
    textFr: 'Quel regard ou sourire de ma part fait battre ton cœur le plus fort ?',
    textEn: 'Which look or smile of mine makes your heart beat the fastest?',
  },

  // 3. Care & Attention
  {
    id: 'care_1',
    category: 'care_attention',
    textAr: 'عندما أكون حزيناً أو متعباً، كيف تحب أن تحتويني وتهدئ روعي عن بعد؟',
    textFr: 'Quand je suis triste ou épuisé(e), comment aimes-tu me réconforter à distance ?',
    textEn: 'When I am sad or drained, how do you prefer to comfort and soothe me from afar?',
  },
  {
    id: 'care_2',
    category: 'care_attention',
    textAr: 'ما هي أدق تفصيلة تخصني وتعرفها أنت ولا ينتبه إليها أحد غيرك؟',
    textFr: 'Quel détail subtil me concernant connais-tu et que personne d\'autre ne remarque ?',
    textEn: 'What subtle detail about me do you notice that nobody else seems to?',
  },

  // 4. Bold Questions
  {
    id: 'bold_1',
    category: 'bold_questions',
    textAr: 'ما هو الاعتراف الجريء الذي تتردد في قوله بصوت عالٍ لكنك تفكر فيه دائماً؟',
    textFr: 'Quel aveu osé hésites-tu à formuler à voix haute alors qu\'il t\'occupe souvent l\'esprit ?',
    textEn: 'What bold confession do you hesitate to say out loud, yet think about often?',
  },
  {
    id: 'bold_2',
    category: 'bold_questions',
    textAr: 'ما هي الفكرة الأكثر جنوناً ورومانسية التي تخيلت أن نعيشها سوياً في أول لقاء قادم؟',
    textFr: 'Quelle est l\'idée la plus folle et passionnée que tu as imaginée pour nos retrouvailles ?',
    textEn: 'What is the wildest and most passionate scenario you\'ve imagined for our next reunion?',
  },

  // 5. Adult Intimacy & Boundaries (18+)
  {
    id: 'intimacy_1',
    category: 'intimacy_boundaries',
    textAr: 'ما هي اللمسة أو القبلة التي تتخيلها بيننا عندما تنتهي المسافات ونكون وحدنا تماماً؟',
    textFr: 'Quelle caresse ou quel baiser imagines-tu entre nous quand la distance aura disparu et que nous serons seuls ?',
    textEn: 'What touch or kiss do you envision between us when the distance is gone and we are completely alone?',
    isAdult: true,
  },
  {
    id: 'intimacy_2',
    category: 'intimacy_boundaries',
    textAr: 'ما هي الحدود أو المساحات الشخصية التي ترغب في أن نحترمها ونعززها دائماً في علاقتنا؟',
    textFr: 'Quelles limites et espaces personnels souhaites-tu que nous respections toujours dans notre intimité ?',
    textEn: 'What boundaries and personal comfort zones do you want us to always honor and safeguard in our intimacy?',
    isAdult: true,
  }
];
