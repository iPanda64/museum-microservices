# Stage 1: Build the React application
FROM node:22-alpine AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy source code and build
COPY . .
RUN npm run build

# Stage 2: Serve the application with Nginx
FROM nginx:alpine

# Completely wipe the default nginx html directory and config
RUN rm -rf /usr/share/nginx/html/* && rm -rf /etc/nginx/conf.d/*

# Copy our custom configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built React files from the build stage
COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
