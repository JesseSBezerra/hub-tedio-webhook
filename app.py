#!/usr/bin/env python3
"""
Hub Tedio Webhook Receiver
A simple webhook receiver application for hub-tedio integration
"""

from flask import Flask, request, jsonify
import logging
import json
from datetime import datetime, timezone

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = Flask(__name__)


@app.route('/')
def index():
    """Health check endpoint"""
    return jsonify({
        'status': 'ok',
        'service': 'hub-tedio-webhook',
        'timestamp': datetime.now(timezone.utc).isoformat()
    })


@app.route('/webhook', methods=['POST'])
def webhook():
    """
    Main webhook endpoint to receive events
    """
    try:
        # Get the payload
        payload = request.get_json()
        
        # Log the received webhook
        logger.info(f"Received webhook: {json.dumps(payload, indent=2)}")
        
        # Get headers for verification if needed
        headers = dict(request.headers)
        logger.info(f"Headers: {json.dumps(headers, indent=2)}")
        
        # Process the webhook (extend this based on your needs)
        response = process_webhook(payload, headers)
        
        return jsonify(response), 200
        
    except Exception as e:
        logger.error(f"Error processing webhook: {str(e)}", exc_info=True)
        return jsonify({
            'status': 'error',
            'message': str(e)
        }), 500


def process_webhook(payload, headers):
    """
    Process the webhook payload
    
    Args:
        payload: The JSON payload from the webhook
        headers: Request headers
        
    Returns:
        dict: Response dictionary
    """
    # Example processing - extend based on your requirements
    event_type = headers.get('X-Event-Type', 'unknown')
    
    logger.info(f"Processing event type: {event_type}")
    
    return {
        'status': 'success',
        'message': 'Webhook received and processed',
        'event_type': event_type,
        'timestamp': datetime.now(timezone.utc).isoformat()
    }


if __name__ == '__main__':
    # Run the application
    port = 5000
    logger.info(f"Starting hub-tedio-webhook on port {port}")
    app.run(host='0.0.0.0', port=port, debug=True)
