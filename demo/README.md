# 倉儲管理系統

一個功能完整的倉儲管理系統，支援網頁操作和離線功能，具備自動資料同步機制。

## 功能特色

### 🌐 網頁版功能
- **產品管理**: 新增、編輯、刪除產品資訊
- **庫存管理**: 入庫、出庫、庫存調整
- **即時監控**: 儀表板顯示庫存狀態和統計資料
- **搜尋篩選**: 支援產品名稱、SKU、類別篩選
- **低庫存警告**: 自動提醒庫存不足的產品

### 💾 離線功能
- **離線操作**: 網路中斷時仍可進行基本操作
- **資料快取**: 自動快取常用資料，離線時可查看
- **自動同步**: 網路恢復時自動同步離線期間的操作
- **狀態指示**: 即時顯示線上/離線狀態

### 📊 資料同步與備份
- **每日備份**: 每天凌晨 2:00 自動備份所有資料
- **增量備份**: 每小時備份當日異動記錄
- **手動備份**: 支援隨時手動觸發備份
- **CSV 格式**: 備份檔案為 CSV 格式，便於匯入其他系統

## 系統需求

- Java 8 或更高版本
- Apache Maven 3.6 或更高版本
- 至少 512MB 可用記憶體
- 100MB 可用磁碟空間

## 快速開始

### 1. 啟動系統

#### Windows 用戶
```bash
# 雙擊執行
start.bat

# 或在命令提示字元中執行
.\start.bat
```

#### 手動啟動
```bash
# 建立資料目錄
mkdir data
mkdir data\backup

# 編譯專案
mvn clean compile

# 啟動應用程式
mvn spring-boot:run
```

### 2. 存取系統

- **主要網頁**: http://localhost:8080
- **資料庫控制台**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/inventory`
  - 用戶名: `sa`
  - 密碼: (空白)

## 使用說明

### 儀表板
- 查看總產品數、庫存價值、低庫存警告等統計資訊
- 監控今日異動記錄
- 查看庫存分類分析圖表

### 產品管理
- **新增產品**: 點擊「新增產品」按鈕
- **編輯產品**: 點擊產品列表中的編輯圖示
- **刪除產品**: 點擊刪除圖示（需確認）
- **搜尋產品**: 使用搜尋框輸入產品名稱或 SKU
- **篩選產品**: 按類別或庫存狀態篩選

### 庫存管理
- **入庫**: 增加產品庫存數量
- **出庫**: 減少產品庫存數量
- **調整**: 直接設定新的庫存數量
- 所有異動都會記錄原因和時間

### 離線使用
1. 系統會自動偵測網路狀態
2. 離線時可查看已快取的資料
3. 離線操作會暫存在本地
4. 網路恢復時自動同步所有操作

### 資料備份
- **自動備份**: 系統會自動執行每日和增量備份
- **手動備份**: 點擊右上角的「手動備份」按鈕
- **備份位置**: `data/backup/` 目錄
- **檔案格式**: 
  - `products_YYYY-MM-DD.csv`: 每日產品備份
  - `transactions_YYYY-MM-DD.csv`: 每日異動備份
  - `incremental_YYYY-MM-DD_HH.csv`: 增量備份

## API 文件

### 產品 API
- `GET /api/products` - 取得所有產品
- `GET /api/products/{id}` - 取得特定產品
- `POST /api/products` - 新增產品
- `PUT /api/products/{id}` - 更新產品
- `DELETE /api/products/{id}` - 刪除產品
- `GET /api/products/search?keyword={keyword}` - 搜尋產品
- `GET /api/products/low-stock` - 取得低庫存產品

### 庫存 API
- `POST /api/inventory/add-stock` - 入庫
- `POST /api/inventory/remove-stock` - 出庫
- `POST /api/inventory/adjust-stock` - 調整庫存
- `GET /api/inventory/transactions/today` - 今日異動
- `GET /api/inventory/transactions/product/{id}` - 產品異動歷史

### 同步 API
- `POST /api/sync/backup` - 手動備份
- `GET /api/sync/status` - 備份狀態

## 資料庫結構

### products 表
- `id`: 產品 ID (主鍵)
- `sku`: 產品編號 (唯一)
- `name`: 產品名稱
- `description`: 產品描述
- `quantity`: 庫存數量
- `price`: 單價
- `category`: 產品類別
- `created_at`: 建立時間
- `updated_at`: 更新時間

### inventory_transactions 表
- `id`: 異動 ID (主鍵)
- `product_id`: 產品 ID (外鍵)
- `type`: 異動類型 (IN/OUT/ADJUST)
- `quantity`: 異動數量
- `reason`: 異動原因
- `transaction_date`: 異動時間

## 故障排除

### 常見問題

1. **無法啟動**
   - 檢查 Java 和 Maven 是否正確安裝
   - 確認 8080 埠口未被占用

2. **資料遺失**
   - 檢查 `data/` 目錄是否存在
   - 查看 `data/backup/` 目錄中的備份檔案

3. **離線功能異常**
   - 清除瀏覽器快取
   - 檢查瀏覽器是否支援 localStorage

4. **備份失敗**
   - 確認 `data/backup/` 目錄有寫入權限
   - 檢查磁碟空間是否足夠

### 日誌查看
系統日誌會顯示在控制台中，包含：
- 資料庫操作記錄
- 備份執行狀態
- 錯誤訊息

## 技術架構

- **後端**: Spring Boot 2.6.15
- **資料庫**: H2 Database (檔案模式)
- **前端**: Bootstrap 5 + Thymeleaf
- **圖表**: Chart.js
- **離線支援**: Service Worker + localStorage

## 授權

本專案採用 MIT 授權條款。