# Truck 車行費用管理系統

車行（貨車車隊）用來管理**車主/司機、車輛**，以及每月各項**費用、借還款、發票、罰單**等帳務的後端系統，並可產出月帳單 PDF。

## 技術棧

- **語言/框架**：Java 17、Spring Boot 3.2.4（Web / Data JPA / Security）
- **資料庫**：MySQL（透過 Spring Data JPA / Hibernate）
- **認證**：JWT（`com.auth0:java-jwt`），無狀態（STATELESS）
- **報表**：JasperReports 6.12.2（產生帳單 PDF，內嵌中文字型 STSong）
- **其他**：Lombok、Guava（本機快取）、FastJSON、jakarta validation、RSA 參數加解密
- **建置**：Maven（內含 `mvnw` wrapper）

## 系統架構

採典型分層架構，套件根為 `com.luzhu.truck`：

```
controller/   REST API 進入點（@RestController）
service/      商業邏輯
dao/          Spring Data JPA Repository
entity/       JPA Entity（對應資料表）
dto/          請求/回應物件
schedule/     每月自動產生費用的排程任務
  ├── task/      @Scheduled 觸發器
  └── service/   各費用的產生邏輯
aop/          切面（參數解密、民國日期轉換）
filter/       JwtFilter（JWT 驗證）
config/       Spring Security 設定
util/         JWT、RSA、日期、加密等工具
cache/        車輛清單 Guava 快取
```

## 核心領域模型

### 主檔
| Entity | 說明 |
|--------|------|
| `Car` | 車輛（車牌、車主、所屬車行、入隊/退隊日與金額、牌照/驗車日、噸數、報停/報廢日、狀態 enable/disable） |
| `Owner` | 車主 / 司機基本資料 |
| `CarAgency` | 車行 |
| `InsuranceCompany` / `LoanCompany` | 保險公司 / 貸款公司（下拉主檔） |
| `UserOperator` | 系統操作者（登入帳號） |

### 費用 / 帳務（每車每月）
**週期性費用**（多由排程自動產生）：

- 管理費 `ManageFee`、公會費 `UnionFee`、車貸 `LoanFee`、勞保 `LaborInsurance`、健保 `HealthFee`、保險費 `InsuranceFee`、牌照稅 `LicenseTax`、燃料稅 `FuelTax`

**交易性紀錄**：

- 發票 `Invoice`（分 GAS / SALE / OFFSET 三類）、借款 `LendMoney`、入款 `GiveBackMoney`、其他應收 `OtherLendMoney`、其他抵收 `OtherGiveBackMoney`、罰單 `TrafficTicket`、代支利息 `PayInterest`、收據抵收 `ReceiveOffset`、入款退回 `ReturnMoney`、上月欠款 `LastMonthOwe`

**費用設定**：`LoanFeeSetting`（車貸條件）、`InsuranceFeeSetting`（保費設定）

> 注意：多數日期欄位以 `String`（含民國年格式）儲存，而非 `Date` 型別。

## 月帳單計算

核心為 `service/bill/BillService`：

- `getMonthBill(車牌, 月份)`：**先讀快照** `MonthBillSnapshot`，無快照才即時計算。
- `calculateMonthBill`：依「車牌 + 月份」逐項加總所有費用與交易紀錄，取整後計算**本月欠款 totalSum**。
- `getMonthBillForceRecalculate`：忽略快照強制重算（供排程 / 手動重建快照用）。
- 帳單明細可透過 `ReportService.billDetailPDF` 套用 Jasper 模板（`static/t3.jrxml`）輸出 PDF。

## 排程任務（每月 1 號凌晨自動產生費用）

各費用有獨立 `@Scheduled` 任務（`schedule/task/`，時區 `Asia/Taipei`），錯開分鐘避免衝突：

| 任務 | 排程 | 說明 |
|------|------|------|
| `HealthTask` / `LicenseTaxTask` | 每月 1 號 00:01 | 健保費；牌照稅僅 3、9 月 |
| `ManageFeeTask` | 每月 1 號 00:04 | 管理費 |
| `UnionFeeTask` | 每月 1 號 00:05 | 公會費 |
| `LoanFeeTask` | 每月 1 號 00:06 | 車貸 |
| `InsuranceFeeTask` | 每月 1 號 00:07 | 保險費 |
| `FuelTaxTask` | 每月 1 號 00:01 | 燃料稅，僅 3、6、9、12 月 |
| `LastMonthOweTask` | 每月 1 號 00:30 | 結算上月欠款 |
| `MonthBillSnapshotTask` | 每日 01:00 | 產生 / 更新月帳單快照 |

## 安全機制

- **JWT 驗證**：`filter/JwtFilter` 攔截所有請求並驗證 `Authorization: Bearer <token>`。公開路徑僅 `/user/register`、`/user/login`、`/authentication/login`、`/key/getKey`。
- **無狀態 Session**：`config/SecurityConfig` 設定 `SessionCreationPolicy.STATELESS`，停用 CSRF / formLogin。
- **參數 RSA 解密**：標註 `@ApiDecryptData` 的 API + 欄位上的 `@DecryptParam`，由 `aop/ApiDecryptDataAop` 在進入 controller 前自動解密前端送來的加密欄位。
- **民國日期切面**：`aop/MinguoDateTimeAspect` 處理民國 / 西元日期轉換。

## 快速開始

### 前置需求
- JDK 17
- MySQL（預設連線 `localhost:3306`，資料庫 `truck`，帳密 `root/root`）

### 設定
編輯 `src/main/resources/application.properties` 調整資料庫連線與參數：

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/truck?useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
spring.datasource.username=root
spring.datasource.password=root
env.time.offset=8                 # 時區偏移（台北 +8）
env.token.secret=...              # JWT 簽章密鑰
jasper.path.billdetail=classpath:static/t3.jrxml
```

可用設定檔：`application.properties`（預設）、`application-beta.properties`、`application-prod.properties`（port 8082）。以 `--spring.profiles.active=prod` 切換。

### 執行

```bash
# 開發模式啟動
./mvnw spring-boot:run

# 打包
./mvnw clean package
java -jar target/truck-0.0.1-SNAPSHOT.jar

# 指定設定檔
java -jar target/truck-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

服務預設啟動於 `http://localhost:8080`。

## API 模組概覽

所有 API 皆為 `POST`、`@CrossOrigin("*")`，回應統一包成 `ResponseModel<T>`：

| 前綴 | 模組 | 主要功能 |
|------|------|----------|
| `/user` | 使用者 | 註冊、登入（回傳 JWT） |
| `/key` | 金鑰 | 取得 RSA 公鑰 |
| `/car` | 車輛 | 車輛/車主 CRUD、下拉清單、查詢、停用、車輛費用設定 |
| `/caragency` | 車行 | 車行管理 |
| `/bill` | 帳單 | 月帳單查詢、明細、產生當月帳單 |
| `/balance` | 結餘 | 結餘相關查詢 |
| `/invoice` | 發票 | 發票 CRUD、作廢重開 |
| `/lendmoney`、`/givebackmoney` | 借款 / 入款 | 含利息計算 |
| `/otherlendmoney`、`/othergivebackmoney` | 其他應收 / 抵收 | |
| `/returnmoney`、`/receiveoffset`、`/payinterest` | 入款退回 / 收據抵收 / 代支利息 | |
| `/trafficticket` | 罰單 | |
| `/insurancecompany`、`/loancompany` | 保險 / 貸款公司 | 主檔管理 |
| `/insurancefeesetting`、`/loanFeeSetting` | 保費 / 車貸設定 | |

## 專案結構備註

- `src/main/resources/static/t3.jrxml`：Jasper 帳單模板。
- `src/main/resources/fonts/STSong.TTF`：PDF 中文字型。
- `src/lib/npf-db-*.jar`：本機 lib 相依。
- 開發流程文件（PRD / TECH_DESIGN / QA_CHECKLIST）採三步驟工作流，分支命名與 commit 規範請參考團隊內部規範。
