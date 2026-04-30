# Financial Portfolio Aggregator

A complete, production-ready backend project using Java and Spring Boot for a financial portfolio aggregator.

## Technologies Used
- Java 21
- Spring Boot 3.2.4 (Web, Data JPA, Validation, Cache, Redis)
- PostgreSQL
- Redis
- Docker & Docker Compose
- Maven

## How to Run

1. Make sure you have Docker and Docker Compose installed.
2. Run the application stack:
   ```bash
   docker-compose up -d --build
   ```
3. The application will be available at `http://localhost:8080`.

## Features
- **Transactions**: Buy and sell assets.
- **Portfolio**: Get total portfolio value, unrealized PnL, and current positions.
- **Caching**: The portfolio calculation and mock prices are cached using Redis.

## Sample Data
On startup, if the database is empty, it will automatically seed:
- Users: John Doe (ID: 1), Jane Smith (ID: 2)
- Assets: AAPL, GOOGL, BTC, ETH

## API Endpoints

### 1. Register Transaction
`POST /transactions`
```json
{
  "userId": 1,
  "assetSymbol": "BTC",
  "type": "BUY",
  "quantity": 0.5,
  "price": 60000.0,
  "timestamp": "2023-10-25T10:00:00"
}
```

### 2. Get User Transactions
`GET /transactions?userId=1`

### 3. Get Portfolio Summary
`GET /portfolio?userId=1`
