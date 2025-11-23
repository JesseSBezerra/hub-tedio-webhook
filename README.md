# 🎯 TedioHook - Webhook Receptor para Evolution API

Projeto Spring Boot para receber e processar webhooks da Evolution API do WhatsApp.

## 📁 Estrutura do Projeto

```
tediohook/
├── .github/
│   └── workflows/
│       ├── docker-build-push.yml    # GitHub Actions workflow
│       └── README.md                # Instruções do workflow
├── app/
│   ├── src/                         # Código fonte da aplicação
│   ├── Dockerfile                   # Configuração Docker
│   ├── .env.example                 # Exemplo de variáveis de ambiente
│   ├── docker-build-push.sh         # Script build/push (Linux/Mac)
│   ├── docker-build-push.ps1        # Script build/push (Windows)
│   ├── pom.xml                      # Maven dependencies
│   ├── README.md                    # Documentação da aplicação
│   ├── DOCKER.md                    # Documentação Docker
│   └── EVENTOS.md                   # Documentação dos eventos
└── README.md                        # Este arquivo
```

## 🚀 Quick Start

### Usando Docker Hub (Mais Rápido)

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

### Desenvolvimento Local

```bash
cd app
mvn spring-boot:run
```

## 📚 Documentação

- **[app/README.md](app/README.md)** - Documentação completa da aplicação
- **[app/DOCKER.md](app/DOCKER.md)** - Guia Docker completo
- **[app/EVENTOS.md](app/EVENTOS.md)** - Documentação dos eventos suportados
- **[.github/workflows/README.md](.github/workflows/README.md)** - GitHub Actions

## 🐳 Docker Hub

Imagem disponível em: https://hub.docker.com/r/jessebezerra/tediohook

## 🔧 Tecnologias

- Java 17
- Spring Boot 3.1.5
- Maven
- Docker
- GitHub Actions

## 📡 Endpoints

- `POST /api/webhook` - Recebe webhooks
- `GET /api/actuator/health` - Health check

## 🎯 Eventos Suportados

- ✅ `contacts.upsert` - Sincronização de contatos
- ✅ `contacts.update` - Atualização de contatos/grupos
- ✅ `messages.upsert` - Mensagens (texto, imagem, vídeo, áudio, etc)

## 🤝 CI/CD

O projeto usa GitHub Actions para automatizar:
- Build da imagem Docker
- Push para Docker Hub
- Suporte multi-plataforma (amd64, arm64)

## 📄 Licença

Proprietário - TedioInfernal
