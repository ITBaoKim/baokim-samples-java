# Baokim B2B API - Java 8+ Example 

Bộ source code mẫu tích hợp Baokim B2B API, viết bằng Java 8+ với Maven.

## 🔧 Yêu cầu
- Java 8+
- Maven 3.x

## 📦 Cài đặt

```bash
git clone https://github.com/Mulligan1499/baokim-b2b-java-example.git
cd baokim-b2b-java-example
cp src/main/resources/config.properties src/main/resources/config.local.properties
mvn clean compile
```

Chỉnh sửa `src/main/resources/config.local.properties` với thông tin Baokim cung cấp:
- `client_id`, `client_secret` - Thông tin OAuth2
- `merchant_code`, `master_merchant_code`, `sub_merchant_code`
- `direct_client_id`, `direct_client_secret`, `direct_merchant_code` - Cho Direct connection
- Đặt file `merchant_private.pem` vào thư mục `keys/`

## 🚀 Quick Start

```bash
# Build project
mvn clean compile

# Test tất cả APIs
mvn exec:java -Dexec.mainClass="vn.baokim.b2b.TestFullFlow"

# Test từng loại connection
mvn exec:java -Dexec.mainClass="vn.baokim.b2b.TestFullFlow" -Dexec.args="basic_pro"
mvn exec:java -Dexec.mainClass="vn.baokim.b2b.TestFullFlow" -Dexec.args="host_to_host"
mvn exec:java -Dexec.mainClass="vn.baokim.b2b.TestFullFlow" -Dexec.args="direct"
```

---

## 📖 Hướng dẫn sử dụng

### Bước 1: Import và load config
```java
import vn.baokim.b2b.*;
import vn.baokim.b2b.mastersub.BaokimOrder;
import vn.baokim.b2b.hosttohost.BaokimVA;
import vn.baokim.b2b.direct.BaokimDirect;

// Load config
Config.load();
```

### Bước 2: Khởi tạo Authentication
```java
// Lấy token (tự động cache, không cần gọi lại)
BaokimAuth auth = new BaokimAuth();
String token = auth.getToken();
```

---

## 🔷 Basic/Pro - Thanh toán qua Master/Sub Merchant

**Class:** `BaokimOrder` (trong `vn.baokim.b2b.mastersub`)

### Tạo đơn hàng
```java
BaokimOrder orderService = new BaokimOrder(auth);

Map<String, Object> orderData = new HashMap<>();
orderData.put("mrcOrderId", "ORDER_" + System.currentTimeMillis());   // Mã đơn hàng (bắt buộc)
orderData.put("totalAmount", 100000);                                  // Số tiền (bắt buộc)
orderData.put("description", "Thanh toán đơn hàng");                   // Mô tả (bắt buộc)
orderData.put("paymentMethod", 1);                                     // 1=VA, 6=VNPay QR
orderData.put("customerInfo", BaokimOrder.buildCustomerInfo(
    "NGUYEN VAN A", "email@example.com", "0901234567", "123 Street"
));

BaokimOrder.ApiResponse result = orderService.createOrder(orderData);

if (result.success) {
    String paymentUrl = result.data.get("redirect_url").getAsString();
    System.out.println("Chuyển khách hàng đến: " + paymentUrl);
}
```

### Tra cứu đơn hàng
```java
BaokimOrder.ApiResponse result = orderService.queryOrder("ORDER_123456");
```

### Hoàn tiền
```java
BaokimOrder.ApiResponse result = orderService.refundOrder("ORDER_123456", 50000, "Hoàn tiền cho khách");
```

### Thu hộ tự động (Auto Debit)
```java
Map<String, Object> autoDebitData = new HashMap<>();
autoDebitData.put("mrcOrderId", "AD_" + System.currentTimeMillis());
autoDebitData.put("totalAmount", 0);
autoDebitData.put("description", "Thu hộ tự động");
autoDebitData.put("paymentMethod", BaokimOrder.PAYMENT_METHOD_AUTO_DEBIT);
autoDebitData.put("serviceCode", "QL_THU_HO_1");
autoDebitData.put("customerInfo", BaokimOrder.buildCustomerInfo(...));

BaokimOrder.ApiResponse result = orderService.createOrder(autoDebitData);
```

---

## 🔷 Host-to-Host - Virtual Account (VA)

**Class:** `BaokimVA` (trong `vn.baokim.b2b.hosttohost`)

### Tạo VA động (mỗi đơn hàng 1 VA riêng)
```java
BaokimVA vaService = new BaokimVA(auth);

BaokimOrder.ApiResponse result = vaService.createDynamicVA(
    "NGUYEN VAN A",           // Tên khách hàng
    "ORDER_123",              // Mã đơn hàng
    100000,                   // Số tiền cần thu
    ""                        // Mô tả (để rỗng nếu không có)
);

if (result.success) {
    System.out.println("Số VA: " + result.data.get("acc_no").getAsString());
    System.out.println("Ngân hàng: " + result.data.get("bank_name").getAsString());
}
```

### Tạo VA tĩnh (1 VA dùng nhiều lần)
```java
BaokimOrder.ApiResponse result = vaService.createStaticVA(
    "TRAN VAN B",                    // Tên khách hàng
    "CUSTOMER_001",                  // Mã định danh khách
    "2026-12-31 23:59:59",           // Ngày hết hạn
    10000,                           // Số tiền tối thiểu
    10000000                         // Số tiền tối đa
);
```

### Tra cứu giao dịch VA
```java
BaokimOrder.ApiResponse result = vaService.queryTransaction("00812345678901");
```

---

## 🔷 Direct Connection - Không qua Master Merchant

**Class:** `BaokimDirect` (trong `vn.baokim.b2b.direct`)

> ⚠️ Direct connection cần credentials riêng, cấu hình trong `direct_client_id`, `direct_client_secret`

### Khởi tạo với Direct credentials
```java
BaokimAuth directAuth = BaokimAuth.forDirectConnection();
BaokimDirect directService = new BaokimDirect(directAuth);
```

### Tạo đơn hàng Direct
```java
Map<String, Object> orderData = new HashMap<>();
orderData.put("mrc_order_id", "DRT_" + System.currentTimeMillis());
orderData.put("total_amount", 150000);
orderData.put("description", "Thanh toán Direct");

Map<String, Object> customerInfo = new HashMap<>();
customerInfo.put("name", "NGUYEN VAN A");
customerInfo.put("email", "customer@email.com");
customerInfo.put("phone", "0901234567");
customerInfo.put("address", "123 Nguyen Hue, HCM");
customerInfo.put("gender", 1);
orderData.put("customer_info", customerInfo);

BaokimOrder.ApiResponse result = directService.createOrder(orderData);

if (result.success) {
    System.out.println("Payment URL: " + result.data.get("redirect_url").getAsString());
}
```

### Tra cứu đơn hàng
```java
BaokimOrder.ApiResponse result = directService.queryOrder("DRT_123456");
```

### Hủy đơn hàng
```java
BaokimOrder.ApiResponse result = directService.cancelOrder("DRT_123456", "Lý do hủy");
```

---

## 📁 Cấu trúc thư mục

```
├── pom.xml                           # Maven config
├── src/main/
│   ├── java/vn/baokim/b2b/
│   │   ├── mastersub/                # Basic/Pro APIs
│   │   │   └── BaokimOrder.java
│   │   ├── hosttohost/               # VA Host-to-Host APIs
│   │   │   └── BaokimVA.java
│   │   ├── direct/                   # Direct Connection APIs
│   │   │   └── BaokimDirect.java
│   │   ├── BaokimAuth.java           # Authentication
│   │   ├── Config.java               # Configuration
│   │   ├── HttpClient.java           # HTTP client
│   │   ├── SignatureHelper.java      # RSA signing
│   │   └── TestFullFlow.java         # Test tất cả APIs
│   └── resources/
│       ├── config.properties         # Template
│       └── config.local.properties   # Config thực (không commit)
├── examples/                         # Ví dụ từng API
│   ├── basic_pro/
│   ├── va_host_to_host/
│   └── direct/
├── keys/                             # RSA Keys
│   └── merchant_private.pem          # Private key của bạn
└── logs/                             # Log files
```

## 📚 API Endpoints

### Basic Pro (Master/Sub)
| API | Endpoint |
|-----|----------|
| Tạo đơn | `/b2b/core/api/ext/mm/order/send` |
| Tra cứu | `/b2b/core/api/ext/mm/order/get-order` |
| Hoàn tiền | `/b2b/core/api/ext/mm/refund/send` |

### VA Host to Host
| API | Endpoint |
|-----|----------|
| Tạo VA | `/b2b/core/api/ext/mm/bank-transfer/create` |
| Cập nhật VA | `/b2b/core/api/ext/mm/bank-transfer/update` |
| Tra cứu VA | `/b2b/core/api/ext/mm/bank-transfer/detail` |

### Direct Connection
| API | Endpoint |
|-----|----------|
| Tạo đơn | `/b2b/core/api/ext/order/send` |
| Tra cứu | `/b2b/core/api/ext/order/get-order` |
| Hủy đơn | `/b2b/core/api/ext/order/cancel` |

---

## ❓ Troubleshooting

| Lỗi | Nguyên nhân | Cách sửa |
|-----|-------------|----------|
| `Chữ ký số không hợp lệ` | Private key không đúng | Kiểm tra file `keys/merchant_private.pem` |
| `Token expired` | Token hết hạn | SDK tự động refresh, không cần xử lý |
| `Invalid merchant_code` | Sai mã merchant | Kiểm tra config |

---
© 2026 Baokim
