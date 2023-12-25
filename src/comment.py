import json
import requests
from sty import fg, bg, ef, rs

class Friend:
    def __init__(self, steam_id: str) -> None:
        self.steam_id = steam_id
    
    def get_steam_id(self) -> str:
        return self.steam_id

class CommentBot:
    """
    A class to comment on steam profiles.
    """
    def __init__(self, api_key: str, root_steam_id: int, login_secure: str, session_id: str):
        self.api_key = api_key
        self.root_steam_id = root_steam_id
        self.login_secure = login_secure
        self.session_id = session_id

    def get_friends(self) -> list[Friend]:
        """
        Returns a list of Friends from the users root_stean_id using the steam API.
        @return: A list of Friends which includes their steam IDs.
        """
        friends = []
        endpoint = f"http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key={self.api_key}&steamid={self.root_steam_id}&relationship=friend"
        resp = requests.get(endpoint, timeout=15)

        if(resp.status_code != 200):
            print(f"Error getting friends from steamID {self.root_steam_id}, reason: {resp.reason}")
            return friends

        string = resp.text
        jdata = json.loads(string)
        table = jdata['friendslist']
        friends_table = table['friends']

        for friend in friends_table:
            if "steamid" in friend:
                friends.append(Friend(friend['steamid']))

        return friends


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
        if 'success' in resp and resp['success'] == True:
            output = fg.green + f"Comment on {steam_id} posted" + fg.rs
            print(output)
            return True
        elif 'success' in resp and resp['success'] == False: #Caused by private profiles and comment settings by user   
            output = fg.red + f"Comment on {steam_id} Failed! \nReason: {resp['error']}" + fg.rs

            if "You've been posting too frequently" in resp['error']:
                print(output)
                raise Exception("Rate limit")

            print(output)
            return False
        else:
            if 'error' in resp:
                output = fg.red + f"Comment on {steam_id} Failed! \nReason: {resp['error']}" + fg.rs
                if "You've been posting too frequently" in resp['error']:
                    print(output)
                    raise Exception("Rate limit")
            else:
                output = fg.red + f"Comment on {steam_id} Failed! \nReason: Unknown" + fg.rs
            print(output)
            return False
        
