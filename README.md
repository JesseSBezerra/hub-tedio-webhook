# hub-tedio-webhook

A simple webhook receiver application for processing hub-tedio events.

## Features

- Simple HTTP webhook receiver
- JSON payload processing
- Request logging
- Health check endpoint
- Configurable via environment variables

## Installation

1. Clone the repository:
```bash
git clone https://github.com/JesseSBezerra/hub-tedio-webhook.git
cd hub-tedio-webhook
```

2. Create a virtual environment and install dependencies:
```bash
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## Usage

### Running the application

**Basic run:**
```bash
python app.py
```

**With environment variables:**
```bash
PORT=8080 DEBUG=true python app.py
```

**Using Docker:**
```bash
docker build -t hub-tedio-webhook .
docker run -p 5000:5000 hub-tedio-webhook
```

**Using Docker Compose:**
```bash
docker-compose up
```

The webhook receiver will start on `http://localhost:5000`

### Environment Variables

- `PORT`: Port to run the server on (default: 5000)
- `HOST`: Host to bind to (default: 0.0.0.0)
- `DEBUG`: Enable debug mode (default: False)
- `SECRET_KEY`: Secret key for Flask app
- `WEBHOOK_SECRET`: Secret for webhook verification (optional)

### Endpoints

- `GET /`: Health check endpoint
  - Returns service status and timestamp

- `POST /webhook`: Main webhook receiver
  - Accepts JSON payloads
  - Logs all received webhooks
  - Returns processing status

### Example Usage

Send a test webhook:

```bash
curl -X POST http://localhost:5000/webhook \
  -H "Content-Type: application/json" \
  -H "X-Event-Type: test" \
  -d '{"event": "test", "data": "hello world"}'
```

Health check:

```bash
curl http://localhost:5000/
```

## Development

The application uses Flask for simplicity and ease of deployment. Extend the `process_webhook` function in `app.py` to add custom webhook processing logic.

## License

MIT License
