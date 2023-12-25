import progressbar
from src.comment import CommentBot, Friend
from time import sleep
from src.config_handler import get_api_key, get_steam_login_secure_cookie, get_session_id
from sty import fg, bg, ef, rs

REGULAR_DELAY = 1   # Delay between comments (in seconds)
RATE_LIMIT_DELAY = 60 # Delay between comments when rate limited
steam_ids_commented: list[str] = []

def main():
    # Get config data
    api_key = get_api_key()
    login_secure = get_steam_login_secure_cookie()
    session_id = get_session_id()
    root_steam_id = input("Enter your steam 64ID: ")
    comment = input("What would you like to comment: ")
    delay = REGULAR_DELAY

    bot = CommentBot(api_key, root_steam_id, login_secure, session_id)

    # Get steam IDs
    friends: list[Friend] = bot.get_friends()
    print(f"Got {len(friends)} Steam IDs")

    # Comment on profiles
    for i in range(len(friends)):
        print(f"\n{i}/{len(friends)}")
        steam_id = friends[i].get_steam_id()

        if has_commented(steam_id):
            output = fg.red + f"Already commented on {steam_id}" + fg.rs
            print(output)
        else:
            steam_ids_commented.append(steam_id)

        try:
            sucsess = bot.comment_on_profile(steam_id, comment)
            delay = REGULAR_DELAY
        except Exception as e:
            if "Rate limit" in str(e):
                print("Steam only allows 200 posts in a 24hr period, please try again later")
                return
        
        widgets = ['Sleeping: ', progressbar.AnimatedMarker()]
        bar = progressbar.ProgressBar(widgets=widgets, maxval=delay*10).start()

        for i in range(delay*10):
            sleep(0.1) 
            bar.update(i)   
        print("\n")

    print("Sent Comments, Finished!")


def has_commented(steam_id) -> bool:
    """
    Checks if the steam_id has already been commented on.
    @param steam_id: The steam ID to check
    @return: True if the steam ID has already been commented on, False otherwise
    """
    return steam_id in steam_ids_commented

#Call main
if __name__ == "__main__":
    main()