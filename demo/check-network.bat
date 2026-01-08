@echo off
echo ========================================
echo    網路設定檢查工具
echo ========================================
echo.

echo 正在檢查網路設定...
echo.

REM 顯示本機 IP 位址
echo 【本機 IP 位址】
ipconfig | findstr /C:"IPv4"
echo.

REM 檢查 8080 埠口是否被占用
echo 【埠口 8080 使用狀況】
netstat -ano | findstr :8080
if %errorlevel% equ 0 (
    echo 警告: 埠口 8080 已被占用！
) else (
    echo 埠口 8080 可用
)
echo.

REM 檢查 Java 版本
echo 【Java 版本檢查】
java -version 2>&1 | findstr /C:"version"
if %errorlevel% neq 0 (
    echo 錯誤: 未找到 Java！
) else (
    echo Java 已安裝
)
echo.

REM 檢查防火牆狀態
echo 【防火牆狀態】
netsh advfirewall show allprofiles state | findstr "State"
echo.

echo 【網路連線測試】
ping -n 1 8.8.8.8 >nul
if %errorlevel% equ 0 (
    echo 網路連線正常
) else (
    echo 網路連線異常
)
echo.

echo ========================================
echo 檢查完成！
echo ========================================
echo.
echo 如果要讓其他人存取系統：
echo 1. 確保 Java 已安裝
echo 2. 確保埠口 8080 可用
echo 3. 設定防火牆允許 8080 埠口
echo 4. 告訴其他人你的 IP 位址
echo.
pause