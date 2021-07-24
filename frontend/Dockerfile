# build environment
FROM node:13-alpine as build
LABEL Diogo Bruno Almeida <diogo.bruno2@gmail.com>
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/package.json
RUN npm install
COPY . /app
RUN npm run build
# production environment
FROM nginx:1.16.0-alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
ENTRYPOINT nginx -g 'daemon off;' 