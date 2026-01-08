@echo off
echo 正在啟動倉儲管理系統...
echo.

REM 檢查 Java 是否安裝
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 錯誤: 未找到 Java，請先安裝 Java 8 或更高版本
    pause
    exit /b 1
)

REM 檢查 Maven 是否安裝
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 錯誤: 未找到 Maven，請先安裝 Apache Maven
    pause
    exit /b 1
)

REM 建立資料目錄
if not exist "data" mkdir data
if not exist "data\backup" mkdir data\backup

echo 正在編譯專案...
mvn clean compile

if %errorlevel% neq 0 (
    echo 編譯失敗，請檢查錯誤訊息
    pause
    exit /b 1
)

echo 正在啟動應用程式...
echo.
echo 系統將在 http://localhost:8080 啟動
echo 資料庫控制台: http://localhost:8080/h2-console
echo.
echo 按 Ctrl+C 停止服務
echo.

mvn spring-boot:run