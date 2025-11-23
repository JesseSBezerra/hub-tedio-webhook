# TedioHook - Webhook Receptor para Evolution API

API REST básica desenvolvida em Spring Boot para receber webhooks da Evolution API.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.1.5
- Lombok
- Maven

## 📋 Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+

## ⚙️ Configuração

### Variáveis de Ambiente

O projeto usa variáveis de ambiente para configurações sensíveis. Crie um arquivo `.env` baseado no `.env.example`:

```bash
cp .env.example .env
# Edite o .env com suas configurações
```

**IMPORTANTE:** O arquivo `.env` está no `.gitignore` e **NUNCA** deve ser commitado!

## 🚀 Como Executar

### Opção 1: Docker (Recomendado) 🐳

```bash
docker pull jessebezerra/tediohook:latest

docker run -d \
  --name tediohook-app \
  -p 8102:8102 \
  -e DATABASE_HOST=your-host \
  -e DATABASE_PORT=5432 \
  -e DATABASE_NAME=your-db \
  -e DATABASE_USER=your-user \
  -e DATABASE_PASSWORD=your-password \
  jessebezerra/tediohook:latest
```

A aplicação estará disponível em: `http://localhost:8102/api`

Ver documentação completa: [DOCKER.md](DOCKER.md)

### Opção 2: Local

**Compilar e executar:**
```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8102/api`

## 📡 Endpoints

### Webhook
- **POST** `/api/webhook` - Recebe eventos da Evolution API

### Health Check (Actuator)
- **GET** `/actuator/health` - Verifica status da aplicação
- **GET** `/actuator/info` - Informações da aplicação

## 📨 Eventos Suportados

### contacts.upsert
Sincronização inicial de contatos do WhatsApp.

### contacts.update
Atualização de contatos/grupos existentes. Identifica automaticamente se é grupo (`@g.us`) ou contato (`@s.whatsapp.net`).

### messages.upsert
Mensagens enviadas ou recebidas. Processa:
- Direção (enviada/recebida)
- Tipo de chat (privado/grupo)
- Tipo de mensagem (texto, imagem, vídeo, etc)
- Conteúdo da mensagem
- Timestamp e status

Ver documentação completa em [EVENTOS.md](EVENTOS.md)

## 📦 Estrutura do Projeto

```
src/main/java/com/tedioinfernal/tediohook/
├── controller/
│   └── WebhookController.java (apenas roteamento)
├── service/
│   ├── ContactService.java (processa eventos de contatos)
│   ├── MessageService.java (processa eventos de mensagens)
│   └── MessageContentExtractor.java (extrai conteúdo de mensagens)
├── dto/
│   ├── EvolutionWebhookEvent.java
│   └── MessageEvent.java
└── TedioHookApplication.java
```

## 📝 Exemplo de Uso

### Enviar webhook:
```bash
curl -X POST http://localhost:8102/api/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "event": "messages.upsert",
    "instance": "instance1",
    "data": {
      "message": "Olá, mundo!"
    }
  }'
```

### Health check:
```bash
curl http://localhost:8102/api/actuator/health
```

## 🔍 Logs

O webhook recebido será logado no console com todas as informações do payload.

## 📄 Licença

Este projeto é proprietário da TedioInfernal.
