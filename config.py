"""
Configuration file for hub-tedio-webhook
"""

import os


class Config:
    """Base configuration"""
    SECRET_KEY = os.environ.get('SECRET_KEY', 'dev-secret-key-change-in-production')
    WEBHOOK_SECRET = os.environ.get('WEBHOOK_SECRET', '')
    PORT = int(os.environ.get('PORT', 5000))
    HOST = os.environ.get('HOST', '0.0.0.0')
    DEBUG = os.environ.get('DEBUG', 'False').lower() == 'true'
    
    @classmethod
    def validate(cls):
        """Validate configuration for production use"""
        if not cls.DEBUG and cls.SECRET_KEY == 'dev-secret-key-change-in-production':
            raise ValueError(
                "SECRET_KEY must be set to a secure value in production. "
                "Set the SECRET_KEY environment variable."
            )


class DevelopmentConfig(Config):
    """Development configuration"""
    DEBUG = True


class ProductionConfig(Config):
    """Production configuration"""
    DEBUG = False


config = {
    'development': DevelopmentConfig,
    'production': ProductionConfig,
    'default': DevelopmentConfig
}
