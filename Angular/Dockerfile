# Build Stage
FROM node:18.20-alpine AS build

WORKDIR /app
COPY package*.json /app/

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Install dependencies
RUN npm install --legacy-peer-deps && npm cache clean --force

COPY ./ /app/

# Run the build using the Angular CLI
RUN ng build --configuration=production --output-path=dist

# Run Stage
FROM nginx:1.21-alpine as production-stage

# Copy the Nginx configuration
COPY my-nginx.conf /etc/nginx/nginx.conf

# Copy the built files from the previous stage into the NGINX server
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
