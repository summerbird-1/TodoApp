# 构建阶段
FROM node:16-alpine as build-stage

# 设置时区（可选）
ENV TZ=Asia/Shanghai

# 安装npm（已包含在Node.js中）
WORKDIR /admim

COPY .npmrc package.json package-lock.json ./

RUN npm install

COPY . .

ARG NODE_ENV=""
RUN NODE_ENV=${NODE_ENV} npm run build

# Nginx阶段
FROM nginx:alpine

# 设置时区（可选）
ENV TZ=Asia/Shanghai

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build-stage /admim/dist /usr/share/nginx/html

EXPOSE 80
