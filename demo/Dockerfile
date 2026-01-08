FROM openjdk:8-jre-alpine

# 設定工作目錄
WORKDIR /app

# 安裝 curl (用於健康檢查)
RUN apk add --no-cache curl

# 複製 JAR 檔案
COPY target/inventory-web-1.0-SNAPSHOT.jar app.jar

# 建立資料目錄
RUN mkdir -p /app/data /app/data/backup

# 設定時區
RUN apk add --no-cache tzdata
ENV TZ=Asia/Taipei

# 暴露埠口
EXPOSE 8080

# 健康檢查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/products || exit 1

# 啟動應用程式
ENTRYPOINT ["java", "-jar", "/app/app.jar"]