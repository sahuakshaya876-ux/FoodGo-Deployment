/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        brand: {
          50: "#fff3ed",
          100: "#ffe4d5",
          200: "#ffc4a8",
          300: "#ff9c70",
          400: "#ff6f38",
          500: "#fc4a12",
          600: "#ed3208",
          700: "#c4230a",
          800: "#9c1e10",
          900: "#7d1c10",
        },
      },
      fontFamily: {
        sans: ["Inter", "ui-sans-serif", "system-ui", "sans-serif"],
      },
      boxShadow: {
        card: "0 2px 10px rgba(15, 23, 42, 0.08)",
        "card-hover": "0 8px 24px rgba(15, 23, 42, 0.14)",
      },
    },
  },
  plugins: [],
};
