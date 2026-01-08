@echo off
echo ========================================
echo    倉儲管理系統 - 伺服器模式啟動
echo ========================================
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

REM 取得本機 IP 位址
echo 正在取得網路資訊...
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /c:"IPv4"') do (
    set "ip=%%a"
    goto :found
)
:found
set ip=%ip: =%

echo.
echo ========================================
echo 編譯並啟動系統 (伺服器模式)
echo ========================================
echo.

REM 使用生產環境配置啟動
echo 正在編譯專案...
mvn clean compile -q

if %errorlevel% neq 0 (
    echo 編譯失敗，請檢查錯誤訊息
    pause
    exit /b 1
)

echo.
echo ========================================
echo 系統啟動成功！
echo ========================================
echo.
echo 本機存取: http://localhost:8080
echo 區域網路存取: http://%ip%:8080
echo 資料庫控制台: http://%ip%:8080/h2-console
echo.
echo 其他人可以透過以下網址存取系統：
echo http://%ip%:8080
echo.
echo 按 Ctrl+C 停止服務
echo ========================================
echo.

REM 使用生產環境配置啟動
mvn spring-boot:run -Dspring-boot.run.profiles=production