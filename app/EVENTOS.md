# Eventos da Evolution API

Documentação dos eventos recebidos via webhook da Evolution API.

## 📋 Eventos Suportados

### 1. contacts.upsert

Evento disparado quando contatos são sincronizados ou atualizados no WhatsApp.

**Estrutura:**
```json
{
  "event": "contacts.upsert",
  "instance": "nome-da-instancia",
  "data": [
    {
      "remoteJid": "558187885943@s.whatsapp.net",
      "pushName": "Nome do Contato",
      "profilePicUrl": "url-da-foto-ou-null",
      "instanceId": "uuid-da-instancia"
    }
  ],
  "destination": "https://seu-webhook.com/api/webhook",
  "date_time": "2025-11-21T21:22:41.518Z",
  "sender": "558196349077@s.whatsapp.net",
  "server_url": "http://localhost:8080",
  "apikey": "sua-api-key"
}
```

**Campos:**
- `event`: Tipo do evento (contacts.upsert)
- `instance`: Nome da instância do WhatsApp
- `data`: Array de contatos
  - `remoteJid`: ID único do contato no WhatsApp
  - `pushName`: Nome exibido do contato
  - `profilePicUrl`: URL da foto de perfil (pode ser null)
  - `instanceId`: UUID da instância
- `destination`: URL do webhook configurado
- `date_time`: Data/hora do evento (ISO 8601)
- `sender`: JID do remetente
- `server_url`: URL do servidor Evolution
- `apikey`: Chave de API da instância

**Quando é disparado:**
- Sincronização inicial de contatos
- Adição de novos contatos
- Atualização de informações de contatos existentes

---

### 2. contacts.update

Evento disparado quando informações de contatos ou grupos são atualizadas.

**Estrutura (Array):**
```json
{
  "event": "contacts.update",
  "instance": "nome-da-instancia",
  "data": [
    {
      "remoteJid": "120363041967849074@g.us",
      "pushName": "Nome do Grupo",
      "profilePicUrl": "https://pps.whatsapp.net/v/...",
      "instanceId": "uuid-da-instancia"
    }
  ],
  "destination": "https://seu-webhook.com/api/webhook",
  "date_time": "2025-11-21T21:22:40.052Z",
  "sender": "558196349077@s.whatsapp.net",
  "server_url": "http://localhost:8080",
  "apikey": "sua-api-key"
}
```

**Estrutura (Objeto Único):**
```json
{
  "event": "contacts.update",
  "instance": "nome-da-instancia",
  "data": {
    "remoteJid": "558197088404@s.whatsapp.net",
    "pushName": "",
    "profilePicUrl": null,
    "instanceId": "uuid-da-instancia"
  },
  "destination": "https://seu-webhook.com/api/webhook",
  "date_time": "2025-11-21T21:25:04.259Z",
  "sender": "558196349077@s.whatsapp.net",
  "server_url": "http://localhost:8080",
  "apikey": "sua-api-key"
}
```

**Campos:**
- Mesma estrutura do `contacts.upsert`
- `data` pode ser um **array** ou **objeto único**
- `remoteJid` terminando em `@g.us` indica que é um grupo
- `remoteJid` terminando em `@s.whatsapp.net` indica que é um contato individual
- `pushName` pode estar vazio (string vazia)

**Quando é disparado:**
- Atualização de nome de contato/grupo
- Atualização de foto de perfil
- Mudanças em informações de grupos
- Alterações em dados de contatos existentes

**Diferença entre upsert e update:**
- `upsert`: Criação ou sincronização inicial de contatos
- `update`: Atualização de contatos/grupos já existentes

---

### 3. messages.upsert

Evento disparado quando uma mensagem é enviada ou recebida.

**Estrutura:**
```json
{
  "event": "messages.upsert",
  "instance": "nome-da-instancia",
  "data": {
    "key": {
      "remoteJid": "558197088404@s.whatsapp.net",
      "fromMe": true,
      "id": "3AE99D6A1DBF3F9A0578"
    },
    "pushName": "Nome do Remetente",
    "status": "SERVER_ACK",
    "message": {
      "conversation": "Texto da mensagem"
    },
    "messageType": "conversation",
    "messageTimestamp": 1763771103,
    "instanceId": "uuid-da-instancia",
    "source": "ios"
  },
  "destination": "https://seu-webhook.com/api/webhook",
  "date_time": "2025-11-21T21:25:03.966Z",
  "sender": "558196349077@s.whatsapp.net",
  "server_url": "http://localhost:8080",
  "apikey": "sua-api-key"
}
```

**Campos:**
- `key`: Identificação da mensagem
  - `remoteJid`: ID do chat
    - Termina com `@s.whatsapp.net`: Contato privado
    - Termina com `@g.us`: Grupo
    - Termina com `@lid`: Canal ou Lista de transmissão
  - `fromMe`: `true` se foi enviada, `false` se foi recebida
  - `id`: ID único da mensagem
- `pushName`: Nome do remetente (pode ser null em mensagens recebidas)
- `status`: Status da mensagem
  - `SERVER_ACK`: Enviada ao servidor
  - `READ`: Lida
  - `PENDING`: Pendente
  - E outros status
- `message`: Conteúdo da mensagem (varia conforme o tipo)
  - Pode conter `messageContextInfo` com metadados adicionais
- `messageType`: Tipo da mensagem (conversation, imageMessage, videoMessage, etc)
- `messageTimestamp`: Timestamp Unix da mensagem
- `source`: Plataforma de origem (ios, android, web, desktop)

**Tipos de mensagem:**
- `conversation`: Mensagem de texto simples
- `extendedTextMessage`: Mensagem de texto com formatação/link
- `imageMessage`: Imagem (contém url, mimetype, width, height, caption opcional)
- `videoMessage`: Vídeo (contém url, mimetype, caption opcional)
- `audioMessage`: Áudio (contém url, mimetype, seconds)
- `documentMessage`: Documento (contém fileName, mimetype, fileLength)
- `stickerMessage`: Figurinha/Sticker
- `locationMessage`: Localização (contém degreesLatitude, degreesLongitude)
- `contactMessage`: Contato compartilhado (contém displayName, vcard)
- E outros tipos

**Estrutura de imageMessage:**
```json
"imageMessage": {
  "url": "https://mmg.whatsapp.net/...",
  "mimetype": "image/jpeg",
  "width": 1200,
  "height": 1600,
  "fileLength": "439195",
  "caption": "Texto opcional da legenda",
  "jpegThumbnail": "base64...",
  ...
}
```

**Quando é disparado:**
- Mensagem recebida de um contato (`fromMe: false`)
- Mensagem enviada pela instância (`fromMe: true`)
- Mensagem em grupo (recebida ou enviada)
- Mensagem em canal ou lista de transmissão

**Exemplos de uso:**
- Mensagem enviada: `fromMe: true`, `status: SERVER_ACK`
- Mensagem recebida: `fromMe: false`, `status: READ`
- Chat privado: `remoteJid` termina com `@s.whatsapp.net`
- Grupo: `remoteJid` termina com `@g.us`
- Canal/Lista: `remoteJid` termina com `@lid`

---

## 🔄 Próximos Eventos

Conforme novos eventos forem sendo recebidos, esta documentação será atualizada com:
- messages.update (mensagens atualizadas)
- connection.update (status da conexão)
- presence.update (status online/offline)
- E outros eventos da Evolution API

---

## 📝 Notas

- Todos os eventos são recebidos no endpoint `POST /api/webhook`
- O controller identifica automaticamente o tipo de evento pelo campo `event`
- Logs detalhados são gerados para cada evento recebido
- A resposta sempre retorna status 200 com informações do processamento
