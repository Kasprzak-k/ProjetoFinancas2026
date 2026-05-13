# Agregador de Portfólio Financeiro

Um projeto de backend completo e pronto para produção usando Java e Spring Boot para um agregador de portfólio financeiro.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.2.4 (Web, Data JPA, Validation, Cache, Redis)
- PostgreSQL
- Redis
- Docker e Docker Compose
- Maven

## Como Executar

1. Certifique-se de ter o Docker e o Docker Compose instalados.
2. Execute a stack da aplicação:
   ```bash
   docker-compose up -d --build
   ```
3. A aplicação estará disponível em `http://localhost:8080`.

## Funcionalidades
- **Transações**: Compra e venda de ativos.
- **Portfólio**: Obtenha o valor total do portfólio, PnL não realizado e as posições atuais.
- **Cache**: O cálculo do portfólio e os preços simulados (mock) são armazenados em cache usando Redis.

## Dados de Exemplo
Na inicialização, se o banco de dados estiver vazio, ele será preenchido automaticamente com:
- Usuários: John Doe (ID: 1), Jane Smith (ID: 2)
- Ativos: AAPL, GOOGL, BTC, ETH

## Endpoints da API

### 1. Registrar Transação
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

### 2. Obter Transações do Usuário
`GET /transactions?userId=1`

### 3. Obter Resumo do Portfólio
`GET /portfolio?userId=1`
