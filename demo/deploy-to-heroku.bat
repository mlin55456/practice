@echo off
echo ========================================
echo    倉儲管理系統 - Heroku 雲端部署
echo ========================================
echo.

echo 這個腳本將幫助你部署到 Heroku 雲端平台
echo 部署完成後，你可以在任何地方存取系統！
echo.

REM 檢查是否安裝 Heroku CLI
heroku --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 錯誤: 未找到 Heroku CLI
    echo.
    echo 請先安裝 Heroku CLI：
    echo https://devcenter.heroku.com/articles/heroku-cli
    echo.
    pause
    exit /b 1
)

echo ✅ Heroku CLI 已安裝
echo.

REM 檢查是否已登入 Heroku
echo 正在檢查 Heroku 登入狀態...
heroku auth:whoami >nul 2>&1
if %errorlevel% neq 0 (
    echo 需要登入 Heroku 帳號
    echo 即將開啟瀏覽器進行登入...
    heroku login
    if %errorlevel% neq 0 (
        echo 登入失敗，請重試
        pause
        exit /b 1
    )
)

echo ✅ Heroku 登入成功
echo.

REM 輸入應用程式名稱
echo 請輸入你的應用程式名稱 (只能包含小寫字母、數字和連字號):
echo 例如: my-inventory-system
set /p APP_NAME="應用程式名稱: "

if "%APP_NAME%"=="" (
    echo 錯誤: 應用程式名稱不能為空
    pause
    exit /b 1
)

echo.
echo 正在建立 Heroku 應用程式: %APP_NAME%
heroku create %APP_NAME%

if %errorlevel% neq 0 (
    echo.
    echo 應用程式名稱可能已被使用，請嘗試其他名稱
    echo 或者使用隨機名稱 (按 Y 繼續，其他鍵退出):
    set /p CHOICE="選擇: "
    if /i "%CHOICE%"=="Y" (
        echo 正在建立隨機名稱的應用程式...
        heroku create
    ) else (
        exit /b 1
    )
)

echo ✅ Heroku 應用程式建立成功
echo.

REM 設定環境變數
echo 正在設定環境變數...
heroku config:set SPRING_PROFILES_ACTIVE=heroku

echo ✅ 環境變數設定完成
echo.

REM 編譯專案
echo 正在編譯專案...
mvn clean package -DskipTests -q

if %errorlevel% neq 0 (
    echo 編譯失敗，請檢查錯誤訊息
    pause
    exit /b 1
)

echo ✅ 專案編譯完成
echo.

REM 初始化 Git (如果需要)
if not exist .git (
    echo 正在初始化 Git...
    git init
    git add .
    git commit -m "Initial commit for Heroku deployment"
)

REM 部署到 Heroku
echo 正在部署到 Heroku...
echo 這可能需要幾分鐘時間，請耐心等待...
echo.

git push heroku main

if %errorlevel% neq 0 (
    echo.
    echo 部署失敗，請檢查錯誤訊息
    echo 常見問題：
    echo 1. 確認所有檔案都已提交到 Git
    echo 2. 檢查 Procfile 檔案是否存在
    echo 3. 確認 Java 版本設定正確
    pause
    exit /b 1
)

echo.
echo ========================================
echo 🎉 部署成功！
echo ========================================
echo.

REM 取得應用程式網址
for /f "tokens=*" %%i in ('heroku info -s ^| findstr web_url') do set WEB_URL=%%i
set WEB_URL=%WEB_URL:web_url=%

echo 你的倉儲管理系統已成功部署到雲端！
echo.
echo 🌐 網址: %WEB_URL%
echo.
echo 現在你可以：
echo ✅ 在 2樓電腦存取系統
echo ✅ 在 3樓用手機管理庫存
echo ✅ 在任何地方查看報表
echo ✅ 與團隊成員即時協作
echo.
echo 建議將網址加入書籤，方便使用！
echo.

REM 詢問是否開啟網站
set /p OPEN_SITE="是否現在開啟網站？ (Y/N): "
if /i "%OPEN_SITE%"=="Y" (
    heroku open
)

echo.
echo 🔧 實用指令：
echo heroku logs --tail    (查看即時日誌)
echo heroku restart        (重啟應用程式)
echo heroku open          (開啟網站)
echo.
echo 🎉 享受雲端倉儲管理的便利！
echo.
pause