import { ExpoConfig, ConfigContext } from 'expo/config';

export default ({ config }: ConfigContext): ExpoConfig => ({
  ...config,
  name: 'Tayeb & Cylia ❤️',
  slug: 'tayeb-cylia-love-game',
  version: '1.0.0',
  orientation: 'portrait',
  icon: './assets/icon.png',
  scheme: 'tayebcylia',
  userInterfaceStyle: 'dark',
  splash: {
    image: './assets/splash.png',
    resizeMode: 'contain',
    backgroundColor: '#09030E',
  },
  ios: {
    supportsTablet: true,
    bundleIdentifier: 'com.aistudio.tayebcylia.lovegame',
    infoPlist: {
      NSPhotoLibraryUsageDescription:
        'Tayeb & Cylia uses access to your photo library solely when you choose to share consensual memories with your partner.',
    },
  },
  android: {
    adaptiveIcon: {
      foregroundImage: './assets/adaptive-icon.png',
      backgroundColor: '#09030E',
    },
    package: 'com.aistudio.tayebcylia.lovegame',
  },
  extra: {
    supabaseUrl: process.env.EXPO_PUBLIC_SUPABASE_URL,
    supabaseAnonKey: process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY,
  },
});
