# Bank Account REST API Documentation

## Base URL
```
/api/v1/bank-accounts
```

## Endpoints Overview

### 1. **Create Bank Account**
```http
POST /api/v1/bank-accounts
```

**What it does:**
- Creates a new bank account in the system
- Validates the account data before creation
- Returns the created account with a location header

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe", 
  "birthDate": "1990-05-15",
  "accountBalance": 1000
}
```

**Response:**
- **Status:** `201 CREATED`
- **Headers:** `Location: /api/v1/bank-accounts/{id}`
- **Body:** Created bank account details

**Use Case:** Opening a new bank account for a customer

---

### 2. **Get All Bank Accounts**
```http
GET /api/v1/bank-accounts
```

**What it does:**
- Retrieves a list of all bank accounts in the system
- Each account includes HATEOAS navigation links
- Provides collection-level navigation options

**Response:**
```json
{
  "_embedded": {
    "bankAccountEntityList": [
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "birthDate": "1990-05-15",
        "accountBalance": 1000,
        "_links": {
          "self": {"href": "/api/v1/bank-accounts/1"},
          "delete": {"href": "/api/v1/bank-accounts/1"},
          "update": {"href": "/api/v1/bank-accounts/1"}
        }
      }
    ]
  },
  "_links": {
    "self": {"href": "/api/v1/bank-accounts"},
    "create": {"href": "/api/v1/bank-accounts"}
  }
}
```

**Use Case:** Listing all accounts for administrative purposes or account selection

---

### 3. **Get Specific Bank Account**
```http
GET /api/v1/bank-accounts/{id}
```

**What it does:**
- Retrieves detailed information for a specific bank account
- Includes comprehensive HATEOAS links for related operations
- Provides navigation to account operations (withdraw, credit, update, delete)

**Path Parameters:**
- `id` (integer): The unique bank account ID

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1990-05-15",
  "accountBalance": 1000,
  "_links": {
    "self": {"href": "/api/v1/bank-accounts/1"},
    "all-bank-accounts": {"href": "/api/v1/bank-accounts"},
    "update": {"href": "/api/v1/bank-accounts/1"},
    "delete": {"href": "/api/v1/bank-accounts/1"},
    "withdraw": {"href": "/api/v1/bank-accounts/1/transactions/withdraw"},
    "credit": {"href": "/api/v1/bank-accounts/1/transactions/credit"}
  }
}
```

**Use Case:** Viewing account details and discovering available operations

---

### 4. **Update Bank Account**
```http
PUT /api/v1/bank-accounts/{id}
```

**What it does:**
- Updates existing bank account information
- Supports partial updates (only send fields you want to change)
- Validates the updated data before saving

**Path Parameters:**
- `id` (integer): The unique bank account ID

**Request Body (partial update example):**
```json
{
  "firstName": "Jane",
  "accountBalance": 1500
}
```

**Response:**
- **Status:** `200 OK`
- **Body:** Updated bank account details

**Use Case:** Updating customer information or correcting account details

---

### 5. **Delete Bank Account**
```http
DELETE /api/v1/bank-accounts/{id}
```

**What it does:**
- Permanently removes a bank account from the system
- Returns no content upon successful deletion

**Path Parameters:**
- `id` (integer): The unique bank account ID

**Response:**
- **Status:** `204 NO CONTENT`
- **Body:** Empty

**Use Case:** Closing an account permanently

---

### 6. **Withdraw Money**
```http
POST /api/v1/bank-accounts/{id}/transactions/withdraw
```

**What it does:**
- Withdraws money from the specified bank account
- Decreases the account balance by the transaction amount
- Creates a transaction record for audit purposes
- Validates the transaction before processing

**Path Parameters:**
- `id` (integer): The unique bank account ID

**Request Body:**
```json
{
  "description": "ATM Withdrawal at Main Street",
  "amount": 200
}
```

**Response:**
```json
{
  "id": 101,
  "description": "ATM Withdrawal at Main Street",
  "amount": 200,
  "_links": {
    "account": {"href": "/api/v1/bank-accounts/1"},
    "credit": {"href": "/api/v1/bank-accounts/1/transactions/credit"}
  }
}
```

**Business Logic:**
- Account balance is reduced: `newBalance = currentBalance - amount`
- Transaction is saved with reference to the bank account

**Use Case:** ATM withdrawals, check payments, transfers out

---

### 7. **Credit Money**
```http
POST /api/v1/bank-accounts/{id}/transactions/credit
```

**What it does:**
- Credits (deposits) money to the specified bank account
- Increases the account balance by the transaction amount
- Creates a transaction record for audit purposes
- Validates the transaction before processing

**Path Parameters:**
- `id` (integer): The unique bank account ID

**Request Body:**
```json
{
  "description": "Salary deposit from XYZ Corp",
  "amount": 3000
}
```

**Response:**
```json
{
  "id": 102,
  "description": "Salary deposit from XYZ Corp",
  "amount": 3000,
  "_links": {
    "account": {"href": "/api/v1/bank-accounts/1"},
    "withdraw": {"href": "/api/v1/bank-accounts/1/transactions/withdraw"}
  }
}
```

**Business Logic:**
- Account balance is increased: `newBalance = currentBalance + amount`
- Transaction is saved with reference to the bank account

**Use Case:** Salary deposits, check deposits, transfers in

---

## Common Response Patterns

### Success Responses
- **200 OK**: Successful GET operations
- **201 CREATED**: Successful POST operations (create, transactions)
- **204 NO CONTENT**: Successful DELETE operations

### Error Responses
- **400 BAD REQUEST**: Invalid input data or validation errors
- **404 NOT FOUND**: Account not found with specified ID
- **500 INTERNAL SERVER ERROR**: Server-side processing errors

## HATEOAS Navigation

Each response includes `_links` that allow clients to:
- Navigate to related resources
- Discover available operations
- Build dynamic user interfaces
- Implement proper REST client behavior

## Data Validation Rules

### Bank Account Creation/Update:
- `firstName`: Minimum 2 characters
- `lastName`: Minimum 2 characters  
- `birthDate`: Must be in the past
- `accountBalance`: Must be non-negative (≥ 0)

### Transaction Operations:
- `description`: Minimum 10 characters
- `amount`: Must be positive (> 0)

## Usage Examples

### Complete Account Lifecycle:

1. **Create Account:**
   ```bash
   curl -X POST /api/v1/bank-accounts \
   -H "Content-Type: application/json" \
   -d '{"firstName":"John","lastName":"Doe","birthDate":"1990-05-15","accountBalance":1000}'
   ```

2. **Check Balance:**
   ```bash
   curl -X GET /api/v1/bank-accounts/1
   ```

3. **Make Deposit:**
   ```bash
   curl -X POST /api/v1/bank-accounts/1/transactions/credit \
   -H "Content-Type: application/json" \
   -d '{"description":"Salary deposit","amount":2000}'
   ```

4. **Make Withdrawal:**
   ```bash
   curl -X POST /api/v1/bank-accounts/1/transactions/withdraw \
   -H "Content-Type: application/json" \
   -d '{"description":"ATM withdrawal","amount":500}'
   ```

5. **Update Account:**
   ```bash
   curl -X PUT /api/v1/bank-accounts/1 \
   -H "Content-Type: application/json" \
   -d '{"firstName":"Jane"}'
   ```

## API Features

✅ **RESTful Design**: Follows REST principles with proper HTTP methods  
✅ **HATEOAS Support**: Hypermedia links for API discoverability  
✅ **Input Validation**: Comprehensive validation on all inputs  
✅ **Proper Status Codes**: Meaningful HTTP status codes  
✅ **Consistent Responses**: Uniform response structure across all endpoints  
✅ **Transaction Safety**: Atomic operations for financial transactions  
✅ **Audit Trail**: All transactions are recorded for compliance