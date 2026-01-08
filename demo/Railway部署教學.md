# 🚄 Railway 雲端部署教學 - 超簡單版

## 🎯 為什麼選擇 Railway？

### 優勢
- ✅ **超級簡單** - 3 步驟完成部署
- ✅ **現代化介面** - 美觀易用
- ✅ **GitHub 整合** - 自動部署
- ✅ **免費額度** - 每月 $5 免費額度
- ✅ **快速啟動** - 無睡眠機制

### 適合場景
- 想要更現代化的部署體驗
- 需要更穩定的服務
- 不介意少量費用

---

## 🚀 3 步驟部署

### 步驟 1: 上傳到 GitHub
1. 註冊 GitHub 帳號：https://github.com
2. 建立新的 repository
3. 上傳你的專案檔案

### 步驟 2: 連接 Railway
1. 前往：https://railway.app
2. 點擊 "Start a New Project"
3. 選擇 "Deploy from GitHub repo"
4. 授權 GitHub 存取
5. 選擇你的倉儲管理專案

### 步驟 3: 自動部署
1. Railway 會自動偵測 Java 專案
2. 自動開始建置和部署
3. 等待 2-3 分鐘完成
4. 取得你的網址

---

## 🔧 Railway 配置

### 建立 railway.json
```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "NIXPACKS"
  },
  "deploy": {
    "startCommand": "java -Dserver.port=$PORT -jar target/inventory-web-1.0-SNAPSHOT.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### 環境變數設定
在 Railway Dashboard 中設定：
```
SPRING_PROFILES_ACTIVE=production
PORT=8080
```

---

## 📱 使用你的 Railway 系統

### 存取網址
```
https://your-project-name.up.railway.app
```

### 特色
- 🚀 **快速載入** - 無睡眠機制
- 🔒 **自動 HTTPS** - 安全連線
- 📊 **即時監控** - 美觀的儀表板
- 🔄 **自動部署** - 推送程式碼即部署

---

## 💰 費用說明

### 免費額度
- 每月 $5 免費額度
- 約可運行 500 小時
- 適合中小型使用

### 付費方案
- 超出免費額度後按使用量計費
- 通常每月 $5-10 即可滿足需求

---

## 🎉 Railway vs Heroku

| 特色 | Railway | Heroku |
|------|---------|--------|
| 免費額度 | $5/月 | 550小時/月 |
| 睡眠機制 | 無 | 有 |
| 介面設計 | 現代化 | 傳統 |
| 部署速度 | 快 | 中等 |
| 學習曲線 | 簡單 | 簡單 |

---

## 🚀 開始部署

1. **準備 GitHub**: 上傳專案到 GitHub
2. **註冊 Railway**: https://railway.app
3. **連接專案**: 選擇 GitHub repository
4. **等待部署**: 2-3 分鐘完成
5. **取得網址**: 開始使用！

Railway 提供了更現代化和穩定的雲端部署體驗！