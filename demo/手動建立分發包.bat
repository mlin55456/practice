@echo off
echo ========================================
echo    手動建立倉儲管理系統分發包
echo ========================================
echo.

echo 請按照以下步驟操作：
echo.

echo 步驟 1: 停止目前運行的系統
echo ----------------------------------------
echo 如果系統正在運行，請按 Ctrl+C 停止
echo 然後按任意鍵繼續...
pause >nul
echo.

echo 步驟 2: 編譯專案
echo ----------------------------------------
echo 正在編譯專案，請稍候...
mvn clean package -DskipTests -q

if %errorlevel% neq 0 (
    echo 編譯失敗！請檢查錯誤訊息
    pause
    exit /b 1
)
echo 編譯完成！
echo.

echo 步驟 3: 建立分發目錄
echo ----------------------------------------
if exist distribution rmdir /s /q distribution
mkdir distribution
mkdir distribution\data
mkdir distribution\data\backup
echo 目錄建立完成！
echo.

echo 步驟 4: 複製 JAR 檔案
echo ----------------------------------------
if exist target\inventory-web-1.0-SNAPSHOT.jar (
    copy target\inventory-web-1.0-SNAPSHOT.jar distribution\inventory-management.jar >nul
    echo JAR 檔案複製完成！
) else (
    echo 錯誤: 找不到 JAR 檔案！
    pause
    exit /b 1
)
echo.

echo 步驟 5: 建立 Windows 啟動腳本
echo ----------------------------------------
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
echo pause >> distribution\start.bat
echo Windows 啟動腳本建立完成！
echo.

echo 步驟 6: 建立 Linux/Mac 啟動腳本
echo ----------------------------------------
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
echo Linux/Mac 啟動腳本建立完成！
echo.

echo 步驟 7: 建立使用說明
echo ----------------------------------------
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
echo 功能說明： >> distribution\README.txt
echo - 儀表板: 查看庫存統計和圖表 >> distribution\README.txt
echo - 產品管理: 新增、編輯、刪除產品 >> distribution\README.txt
echo - 庫存異動: 入庫、出庫、調整操作 >> distribution\README.txt
echo - 報表分析: 詳細報表和資料匯出 >> distribution\README.txt
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
echo. >> distribution\README.txt
echo 注意事項： >> distribution\README.txt
echo - 首次啟動會自動建立範例資料 >> distribution\README.txt
echo - 系統會自動備份資料到 data/backup/ 目錄 >> distribution\README.txt
echo - 支援離線操作，網路恢復時自動同步 >> distribution\README.txt
echo 使用說明建立完成！
echo.

echo ========================================
echo 分發包建立完成！
echo ========================================
echo.
echo 分發包位置: distribution\
echo.
echo 包含檔案:
dir /b distribution
echo.
echo 檔案大小:
for %%f in (distribution\*) do echo %%~nxf: %%~zf bytes
echo.
echo ========================================
echo 使用說明
echo ========================================
echo.
echo 1. 將整個 distribution 目錄壓縮成 ZIP 檔案
echo 2. 傳送給需要使用的人
echo 3. 對方解壓後雙擊 start.bat 即可使用
echo.
echo 建議 ZIP 檔名: 倉儲管理系統_v1.0.zip
echo.
pause