export type PartnerRole = 'TAYEB' | 'CYLIA';

export type AppLanguage = 'ar' | 'fr' | 'en';

export type QuestionCategory =
  | 'love_feelings'
  | 'attraction_crushes'
  | 'care_attention'
  | 'bold_questions'
  | 'intimacy_boundaries';

export interface CoupleQuestion {
  id: string;
  category: QuestionCategory;
  textAr: string;
  textFr: string;
  textEn: string;
  hintAr?: string;
  hintFr?: string;
  hintEn?: string;
  isAdult?: boolean;
}

export interface QuestionAnswer {
  id?: string;
  questionId: string;
  role: PartnerRole;
  authorName: string;
  answerText: string;
  isRevealed: boolean;
  createdAt?: string;
}

export interface LoveNote {
  id: string;
  senderRole: PartnerRole;
  senderName: string;
  message: string;
  emoji: string;
  createdAt: string;
}

export interface SharedPhoto {
  id: string;
  senderRole: PartnerRole;
  senderName: string;
  photoUrl: string;
  caption?: string;
  createdAt: string;
}

export interface RoomState {
  id: string;
  code: string;
  status: 'waiting' | 'active' | 'closed';
  hostRole: PartnerRole;
  guestRole: PartnerRole;
  isGuestJoined: boolean;
  isPartnerOnline: boolean;
  adultUnlocked: boolean;
  expiresAt: string;
}
