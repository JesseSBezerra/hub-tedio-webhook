"""
Tests for hub-tedio-webhook
"""

import unittest
import json
from app import app, process_webhook


class WebhookTestCase(unittest.TestCase):
    """Test cases for webhook application"""

    def setUp(self):
        """Set up test client"""
        self.app = app.test_client()
        self.app.testing = True

    def test_health_check(self):
        """Test the health check endpoint"""
        response = self.app.get('/')
        self.assertEqual(response.status_code, 200)
        
        data = json.loads(response.data)
        self.assertEqual(data['status'], 'ok')
        self.assertEqual(data['service'], 'hub-tedio-webhook')
        self.assertIn('timestamp', data)

    def test_webhook_post(self):
        """Test posting to webhook endpoint"""
        payload = {
            'event': 'test',
            'data': 'test data'
        }
        
        response = self.app.post(
            '/webhook',
            data=json.dumps(payload),
            content_type='application/json',
            headers={'X-Event-Type': 'test'}
        )
        
        self.assertEqual(response.status_code, 200)
        
        data = json.loads(response.data)
        self.assertEqual(data['status'], 'success')
        self.assertIn('timestamp', data)

    def test_webhook_invalid_json(self):
        """Test webhook with invalid JSON"""
        response = self.app.post(
            '/webhook',
            data='invalid json',
            content_type='application/json'
        )
        
        self.assertEqual(response.status_code, 500)

    def test_process_webhook(self):
        """Test webhook processing function"""
        payload = {'test': 'data'}
        headers = {'X-Event-Type': 'custom'}
        
        result = process_webhook(payload, headers)
        
        self.assertEqual(result['status'], 'success')
        self.assertEqual(result['event_type'], 'custom')
        self.assertIn('timestamp', result)


if __name__ == '__main__':
    unittest.main()
