import os
import json

config_file_path = os.path.join(os.path.dirname(__file__), "..", "config.json")

def get_api_key() -> str:
    """
    Returns the API key from the config file.
    """
    with open(config_file_path, "r") as config_file:
        config = json.load(config_file)
        return config["api_key"]

def get_steam_login_secure_cookie() -> str:
    """
    Returns the steam login secure cookie from the config file.
    """
    with open(config_file_path, "r") as config_file:
        config = json.load(config_file)
        return config["login_secure_cookie"]