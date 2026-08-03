# Agregador de Portfólio Financeiro

Um projeto de backend em Java e Spring Boot para um agregador de portfólio financeiro.

---

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.2.4** (Web, Data JPA, Validation, Cache, Redis)
- **PostgreSQL 15**
- **Redis 7**
- **Docker & Docker Compose**
- **Maven**

---

## Como Executar

Você pode executar o projeto de duas formas: usando **Docker Compose** (recomendado por já configurar todo o ambiente automaticamente) ou **manualmente** (configurando os serviços de banco de dados e Redis de forma local).

### Método 1: Usando Docker Compose (Recomendado)

#### No Windows:
1. Certifique-se de que o **Docker Desktop** está instalado e em execução.
2. Abra o terminal (PowerShell, Prompt de Comando ou terminal do Git Bash) na pasta raiz do projeto.
3. Suba os containers:
   ```powershell
   docker compose up -d --build
   ```
4. A aplicação estará disponível em `http://localhost:8080`.

#### No Linux:
1. Instale o Docker e o plugin do Docker Compose.
2. Certifique-se de que o serviço do Docker está rodando:
   ```bash
   sudo systemctl start docker
   ```
3. Execute o comando com permissões adequadas:
   ```bash
   sudo docker compose up -d --build
   ```
4. A aplicação estará disponível em `http://localhost:8080`.

---

### Método 2: Execução Manual (Desenvolvimento Local)

Para este método, você precisará ter o **JDK 21** e o **Maven 3.9+** instalados na sua máquina, além do PostgreSQL e Redis.

#### Configuração Prévia (Banco de Dados e Redis)

##### No Linux (Debian/Ubuntu):
1. **Instalar e configurar o PostgreSQL**:
   ```bash
   sudo apt update
   sudo apt install postgresql postgresql-contrib -y
   sudo systemctl start postgresql
   
   # Criar o usuário e banco de dados requeridos pelo app:
   sudo -u postgres psql -c "CREATE USER portfolio_user WITH PASSWORD 'portfolio_pass';"
   sudo -u postgres psql -c "CREATE DATABASE portfolio OWNER portfolio_user;"
   ```
2. **Instalar e iniciar o Redis**:
   ```bash
   sudo apt install redis-server -y
   sudo systemctl start redis-server
   ```

##### No Windows:
1. **Instalar o PostgreSQL**:
   * Baixe o instalador oficial no site do PostgreSQL e instale.
   * Durante a instalação ou via pgAdmin, crie um banco de dados chamado `portfolio` e um usuário `portfolio_user` com a senha `portfolio_pass` (ou edite o arquivo `src/main/resources/application.yml` com as suas credenciais).
2. **Instalar o Redis**:
   * O Redis não possui suporte oficial nativo para Windows. A melhor forma é executá-lo através do WSL2:
     ```bash
     wsl sudo apt update
     wsl sudo apt install redis-server
     wsl sudo service redis-server start
     ```
   * *Alternativa rápida*: Se você tiver o Docker instalado mas não quiser subir todo o Compose, pode rodar os serviços isolados em containers:
     ```powershell
     docker run --name pg-local -p 5432:5432 -e POSTGRES_DB=portfolio -e POSTGRES_USER=portfolio_user -e POSTGRES_PASSWORD=portfolio_pass -d postgres:15-alpine
     docker run --name redis-local -p 6379:6379 -d redis:7-alpine
     ```

#### Compilando e Rodando a Aplicação Java

Uma vez que o PostgreSQL e o Redis estejam rodando nas portas padrão (`5432` e `6379`), execute as instruções abaixo de acordo com seu SO:

##### No Windows (PowerShell / Command Prompt):
1. **Compilar e buildar o projeto**:
   ```powershell
   mvn clean package -DskipTests
   ```
2. **Rodar o arquivo JAR gerado**:
   ```powershell
   java -jar target/portfolio-aggregator-0.0.1-SNAPSHOT.jar
   ```
3. *Alternativa*: Rodar em modo de desenvolvimento diretamente pelo Maven:
   ```powershell
   mvn spring-boot:run
   ```

##### No Linux (Terminal):
1. **Compilar e buildar o projeto**:
   ```bash
   mvn clean package -DskipTests
   ```
2. **Rodar o arquivo JAR gerado**:
   ```bash
   java -jar target/portfolio-aggregator-0.0.1-SNAPSHOT.jar
   ```
3. *Alternativa*: Rodar em modo de desenvolvimento diretamente pelo Maven:
   ```bash
   mvn spring-boot:run
   ```

---

## Funcionalidades
- **Transações**: Compra e venda de ativos.
- **Portfólio**: Obtenha o valor total do portfólio, PnL (Lucro/Prejuízo) não realizado e as posições atuais.
- **Cache**: O cálculo do portfólio e os preços simulados (mock) são armazenados em cache usando Redis para melhorar performance.

## Dados de Exemplo
Na inicialização, se o banco de dados estiver vazio, ele será preenchido automaticamente com:
- **Usuários**: John Doe (ID: 1), Jane Smith (ID: 2)
- **Ativos**: AAPL, GOOGL, BTC, ETH

---

## Endpoints da API

### 1. Registrar Transação
- **URL**: `POST /transactions`
- **Body**:
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
- **URL**: `GET /transactions?userId=1`

### 3. Obter Resumo do Portfólio
- **URL**: `GET /portfolio?userId=1`
