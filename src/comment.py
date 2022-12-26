import json
import requests
from sty import fg, bg, ef, rs

class CommentBot:
    """
    A class to comment on steam profiles.
    """
    def __init__(self, api_key: str, root_steam_id: int, login_secure: str, session_id: str):
        self.api_key = api_key
        self.root_steam_id = root_steam_id
        self.login_secure = login_secure
        self.session_id = session_id

    def get_steam_ids(self) -> list[str]:
        """
        Returns a list of steam IDs from the steam API.
        @param api_key: The API key to use.
        @param root_steam_id: The steam ID to get the friends of.
        @return: A list of steam IDs.
        """
        steam_ids = []
        endpoint = f"http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key={self.api_key}&steamid={self.root_steam_id}&relationship=friend"
        resp = requests.get(endpoint, timeout=15)
        string = resp.text
        jdata = json.loads(string)
        table = jdata['friendslist']
        friends = table['friends']

        for friend in friends:
            steam_ids.append(friend['steamid'])

        return steam_ids


    def comment_on_profile(self, steam_id: str, comment: str) -> bool:
        """
        Posts a comment on a steam profile.
        @param steam_id: The steam ID to comment on.
        @param comment: The comment to post.
        @return: True if the comment was posted, False otherwise.
        """
        headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0"}
        cookies = {"steamLoginSecure": self.login_secure, "sessionid": self.session_id}
        data = {"comment": str(comment), "count": "6", "sessionid": self.session_id, 'feature2': "-1"}
        resp = (requests.post(f"https://steamcommunity.com/comment/Profile/post/{steam_id}/-1/",
                                    headers=headers, cookies=cookies, data=data, timeout=15)).json()
        if resp['success']:
            output = fg.green + f"Comment on {steam_id} posted" + fg.rs
            print(output)
            return True
        else: #Caused by private profiles and comment settings by user   
            output = fg.red + f"Comment on {steam_id} Failed!" + fg.rs
            print(output)
            return False
        
