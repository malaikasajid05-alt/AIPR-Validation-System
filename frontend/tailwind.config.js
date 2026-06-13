/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        bg: '#FFFFFF',
        primary: '#14B8A6',
        accent: '#005345',
        'text-primary': '#00221A',
        border: '#E2E8F0',
      },
      fontFamily: {
        headline: ['Epilogue', 'sans-serif'],
        body: ['Newsreader', 'serif'],
      },
      boxShadow: {
        card: '0 12px 40px rgba(0, 34, 26, 0.06)',
      },
    },
  },
  plugins: [],
};
