# ============================================================
#  Imagem Docker do site (Angular)
#  Etapa 1: instala as dependências e gera os arquivos do site
#  Etapa 2: o nginx (servidor web) entrega esses arquivos
# ============================================================

# --- Etapa 1: compilar ---
FROM node:24-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npx ng build

# --- Etapa 2: servir ---
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/web/browser /usr/share/nginx/html
EXPOSE 80
