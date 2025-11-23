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
        
        # Log the received webhook (sanitized for production)
        logger.info("Received webhook event")
        if app.debug:
            logger.debug(f"Payload: {json.dumps(payload, indent=2)}")
        
        # Get headers for verification if needed
        headers = dict(request.headers)
        if app.debug:
            logger.debug(f"Headers: {json.dumps(headers, indent=2)}")
        
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
    import os
    port = int(os.environ.get('PORT', 5000))
    host = os.environ.get('HOST', '0.0.0.0')
    debug = os.environ.get('DEBUG', 'False').lower() == 'true'
    
    logger.info(f"Starting hub-tedio-webhook on {host}:{port}")
    app.run(host=host, port=port, debug=debug)
