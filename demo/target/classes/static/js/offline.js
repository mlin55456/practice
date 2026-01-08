// 簡化版離線功能支援
class SimpleOfflineManager {
    constructor() {
        this.isOnline = navigator.onLine;
        this.pendingOperations = JSON.parse(localStorage.getItem('pendingOperations') || '[]');
        this.cachedData = JSON.parse(localStorage.getItem('cachedData') || '{}');
        
        this.initEventListeners();
        this.updateOnlineStatus();
    }
    
    initEventListeners() {
        window.addEventListener('online', () => {
            this.isOnline = true;
            this.updateOnlineStatus();
            this.syncPendingOperations();
        });
        
        window.addEventListener('offline', () => {
            this.isOnline = false;
            this.updateOnlineStatus();
        });
    }
    
    updateOnlineStatus() {
        const statusElement = document.getElementById('onlineStatus');
        if (statusElement) {
            if (this.isOnline) {
                statusElement.innerHTML = '<i class="fas fa-wifi text-success"></i> 線上';
                statusElement.className = 'badge bg-success';
            } else {
                statusElement.innerHTML = '<i class="fas fa-wifi-slash text-danger"></i> 離線';
                statusElement.className = 'badge bg-danger';
            }
        }
        
        // 顯示待同步操作數量
        const pendingElement = document.getElementById('pendingCount');
        if (pendingElement && this.pendingOperations.length > 0) {
            pendingElement.textContent = this.pendingOperations.length;
            pendingElement.style.display = 'inline';
        } else if (pendingElement) {
            pendingElement.style.display = 'none';
        }
    }
    
    // 快取資料
    cacheData(key, data) {
        this.cachedData[key] = {
            data: data,
            timestamp: Date.now()
        };
        localStorage.setItem('cachedData', JSON.stringify(this.cachedData));
    }
    
    // 取得快取資料
    getCachedData(key, maxAge = 300000) { // 預設5分鐘過期
        const cached = this.cachedData[key];
        if (cached && (Date.now() - cached.timestamp) < maxAge) {
            return cached.data;
        }
        return null;
    }
    
    // 新增待同步操作
    addPendingOperation(operation) {
        operation.id = Date.now() + Math.random();
        operation.timestamp = Date.now();
        this.pendingOperations.push(operation);
        localStorage.setItem('pendingOperations', JSON.stringify(this.pendingOperations));
        this.updateOnlineStatus();
    }
    
    // 同步待處理操作
    async syncPendingOperations() {
        if (!this.isOnline || this.pendingOperations.length === 0) {
            return;
        }
        
        const operations = this.pendingOperations.slice(); // 複製陣列
        this.pendingOperations = [];
        localStorage.setItem('pendingOperations', JSON.stringify(this.pendingOperations));
        
        for (const operation of operations) {
            try {
                await this.executeOperation(operation);
                console.log('同步操作成功:', operation);
            } catch (error) {
                console.error('同步操作失敗:', operation, error);
                // 如果失敗，重新加入待同步列表
                this.pendingOperations.push(operation);
            }
        }
        
        localStorage.setItem('pendingOperations', JSON.stringify(this.pendingOperations));
        this.updateOnlineStatus();
        
        // 重新載入頁面資料
        if (typeof loadProducts === 'function') {
            loadProducts();
        }
        if (typeof loadDashboardData === 'function') {
            loadDashboardData();
        }
    }
    
    // 執行操作
    async executeOperation(operation) {
        const originalAxios = window.axios.create();
        
        switch (operation.type) {
            case 'CREATE_PRODUCT':
                return await originalAxios.post('/api/products', operation.data);
            case 'UPDATE_PRODUCT':
                return await originalAxios.put('/api/products/' + operation.productId, operation.data);
            case 'DELETE_PRODUCT':
                return await originalAxios.delete('/api/products/' + operation.productId);
            case 'ADD_STOCK':
                return await originalAxios.post('/api/inventory/add-stock', operation.data);
            case 'REMOVE_STOCK':
                return await originalAxios.post('/api/inventory/remove-stock', operation.data);
            case 'ADJUST_STOCK':
                return await originalAxios.post('/api/inventory/adjust-stock', operation.data);
            default:
                throw new Error('未知的操作類型: ' + operation.type);
        }
    }
}

// 全域離線管理器
const offlineManager = new SimpleOfflineManager();

// 頁面載入時檢查待同步操作
document.addEventListener('DOMContentLoaded', function() {
    // 嘗試同步待處理操作
    setTimeout(function() {
        offlineManager.syncPendingOperations();
    }, 1000);
});