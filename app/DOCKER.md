# 🐳 Docker - TedioHook

Instruções para executar o projeto usando Docker.

## 📋 Pré-requisitos

- Docker instalado
- Docker Compose instalado (opcional, mas recomendado)
- Conta no Docker Hub (para push de imagens)

## 🚀 Opção 1: Docker Hub (Recomendado)

### Pull e executar:
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

### Ver logs:
```bash
docker logs -f tediohook-app
```

### Parar:
```bash
docker stop tediohook-app
docker rm tediohook-app
```

## 🔧 Opção 2: Build Local

### Build da imagem:
```bash
cd app
docker build -t tediohook:latest .
```

### Executar container:
```bash
docker run -d \
  --name tediohook-app \
  -p 8102:8102 \
  -e DATABASE_HOST=your-host \
  -e DATABASE_PORT=5432 \
  -e DATABASE_NAME=your-db \
  -e DATABASE_USER=your-user \
  -e DATABASE_PASSWORD=your-password \
  tediohook:latest
```

### Parar e remover:
```bash
docker stop tediohook-app
docker rm tediohook-app
```

## 🏥 Health Check

Verificar saúde da aplicação:
```bash
curl http://localhost:8102/api/actuator/health
```

Ou via Docker:
```bash
docker inspect --format='{{json .State.Health}}' tediohook-app
```

## 📊 Informações da Imagem

### Características:
- **Base**: Amazon Corretto 17 Alpine (imagem leve e multi-arch)
- **Multi-stage build**: Otimiza tamanho final
- **Usuário não-root**: Maior segurança
- **Health check**: Monitora saúde da aplicação
- **Porta exposta**: 8102
- **Plataformas**: linux/amd64, linux/arm64

### Tamanho aproximado:
- Imagem final: ~200MB
- Build stage é descartado após build

## 🔐 Variáveis de Ambiente

Você pode sobrescrever qualquer configuração via variáveis de ambiente:

```bash
docker run -d \
  -p 8102:8102 \
  -e SERVER_PORT=8102 \
  -e DATABASE_HOST=seu-host \
  -e DATABASE_PORT=5432 \
  -e DATABASE_NAME=seu-banco \
  -e DATABASE_USER=seu-usuario \
  -e DATABASE_PASSWORD=sua-senha \
  tediohook:latest
```

## 🐛 Troubleshooting

### Container não inicia:
```bash
docker logs tediohook-app
```

### Verificar se a porta está em uso:
```bash
# Windows
netstat -ano | findstr :8102

# Linux/Mac
lsof -i :8102
```

### Entrar no container:
```bash
docker exec -it tediohook-app sh
```

### Rebuild completo (sem cache):
```bash
docker-compose build --no-cache
docker-compose up -d
```

## 🚢 Build e Push para Docker Hub

### Opção 1: GitHub Actions (Recomendado) 🤖

O repositório possui um workflow automatizado que faz build e push quando você:
- Faz push para `main`, `master` ou `develop`
- Cria uma tag `v*` (ex: `v1.0.0`)
- Dispara manualmente via Actions tab

**Configuração necessária:**
1. Adicionar secret `DOCKER_PASSWORD` no GitHub
2. Ver instruções em: `.github/workflows/README.md`

### Opção 2: Script Automatizado (Windows)

```powershell
.\docker-build-push.ps1
```

### Opção 3: Script Automatizado (Linux/Mac)

```bash
chmod +x docker-build-push.sh
./docker-build-push.sh
```

### Opção 4: Manual

```bash
# 1. Build da imagem
docker build -t jessebezerra/tediohook:latest .

# 2. Login no Docker Hub
docker login -u jessebezerra

# 3. Push da imagem
docker push jessebezerra/tediohook:latest
```

### Usar imagem do Docker Hub

Depois do push, qualquer pessoa pode usar a imagem:

```bash
# Pull da imagem
docker pull jessebezerra/tediohook:latest

# Executar
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

## 📝 Notas

- O health check usa o endpoint do Actuator
- A aplicação demora ~30-40s para iniciar completamente
- Logs são exibidos no stdout/stderr do container
- Configurações do banco estão no `application.properties`
- Imagem disponível em: https://hub.docker.com/r/jessebezerra/tediohook
