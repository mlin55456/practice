@echo off
echo ========================================
echo    建立倉儲管理系統分發包
echo ========================================
echo.

REM 檢查 Maven 是否安裝
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 錯誤: 未找到 Maven，請先安裝 Apache Maven
    pause
    exit /b 1
)

echo 正在清理並編譯專案...
mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo 編譯失敗，請檢查錯誤訊息
    pause
    exit /b 1
)

echo.
echo 正在建立分發目錄...

REM 建立分發目錄
if exist "distribution" rmdir /s /q distribution
mkdir distribution
mkdir distribution\data
mkdir distribution\data\backup

REM 複製 JAR 檔案
copy target\inventory-web-1.0-SNAPSHOT.jar distribution\inventory-management.jar

REM 建立啟動腳本
echo @echo off > distribution\start.bat
echo echo 正在啟動倉儲管理系統... >> distribution\start.bat
echo echo. >> distribution\start.bat
echo java -version ^>nul 2^>^&1 >> distribution\start.bat
echo if %%errorlevel%% neq 0 ^( >> distribution\start.bat
echo     echo 錯誤: 未找到 Java，請先安裝 Java 8 或更高版本 >> distribution\start.bat
echo     pause >> distribution\start.bat
echo     exit /b 1 >> distribution\start.bat
echo ^) >> distribution\start.bat
echo echo 系統將在 http://localhost:8080 啟動 >> distribution\start.bat
echo echo 按 Ctrl+C 停止服務 >> distribution\start.bat
echo echo. >> distribution\start.bat
echo java -jar inventory-management.jar >> distribution\start.bat

REM 建立 Linux/Mac 啟動腳本
echo #!/bin/bash > distribution\start.sh
echo echo "正在啟動倉儲管理系統..." >> distribution\start.sh
echo echo "" >> distribution\start.sh
echo if ! command -v java ^&^> /dev/null; then >> distribution\start.sh
echo     echo "錯誤: 未找到 Java，請先安裝 Java 8 或更高版本" >> distribution\start.sh
echo     exit 1 >> distribution\start.sh
echo fi >> distribution\start.sh
echo echo "系統將在 http://localhost:8080 啟動" >> distribution\start.sh
echo echo "按 Ctrl+C 停止服務" >> distribution\start.sh
echo echo "" >> distribution\start.sh
echo java -jar inventory-management.jar >> distribution\start.sh

REM 建立說明文件
echo 倉儲管理系統 - 使用說明 > distribution\README.txt
echo ================================ >> distribution\README.txt
echo. >> distribution\README.txt
echo 系統需求： >> distribution\README.txt
echo - Java 8 或更高版本 >> distribution\README.txt
echo - 至少 512MB 可用記憶體 >> distribution\README.txt
echo - 100MB 磁碟空間 >> distribution\README.txt
echo. >> distribution\README.txt
echo 啟動方式： >> distribution\README.txt
echo Windows: 雙擊 start.bat >> distribution\README.txt
echo Linux/Mac: ./start.sh >> distribution\README.txt
echo. >> distribution\README.txt
echo 存取網址： >> distribution\README.txt
echo http://localhost:8080 >> distribution\README.txt
echo. >> distribution\README.txt
echo 資料庫控制台： >> distribution\README.txt
echo http://localhost:8080/h2-console >> distribution\README.txt
echo 連線設定： >> distribution\README.txt
echo JDBC URL: jdbc:h2:file:./data/inventory >> distribution\README.txt
echo 用戶名: sa >> distribution\README.txt
echo 密碼: (空白) >> distribution\README.txt
echo. >> distribution\README.txt
echo 資料位置： >> distribution\README.txt
echo - 資料庫檔案: data/inventory.mv.db >> distribution\README.txt
echo - 備份檔案: data/backup/ >> distribution\README.txt

echo.
echo ========================================
echo 分發包建立完成！
echo ========================================
echo.
echo 分發包位置: distribution\
echo 包含檔案:
echo - inventory-management.jar (主程式)
echo - start.bat (Windows 啟動腳本)
echo - start.sh (Linux/Mac 啟動腳本)
echo - README.txt (使用說明)
echo - data\ (資料目錄)
echo.
echo 將整個 distribution 目錄複製給其他人即可使用！
echo.
pause