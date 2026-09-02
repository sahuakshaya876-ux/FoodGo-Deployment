# ---------- Build stage ----------
FROM node:20-alpine AS build
WORKDIR /build

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
# The backend URL can be overridden at build time, e.g.:
#   docker build --build-arg VITE_API_BASE_URL=https://api.foodgo.example.com/api .
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

# ---------- Runtime stage ----------
FROM nginx:1.27-alpine AS runtime

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /build/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://localhost:80/ >/dev/null 2>&1 || exit 1

CMD ["nginx", "-g", "daemon off;"]
