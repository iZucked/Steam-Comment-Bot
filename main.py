import progressbar
from src.comment import CommentBot
from time import sleep
from src.config_handler import get_api_key, get_steam_login_secure_cookie, get_session_id
from sty import fg, bg, ef, rs

DELAY = 1   # Delay between comments (in seconds)
steam_ids_commented = []

def main():
    # Get config data
    api_key = get_api_key()
    login_secure = get_steam_login_secure_cookie()
    session_id = get_session_id()
    root_steam_id = input("Enter your steam ID: ")
    comment = input("What would you like to comment: ")

    bot = CommentBot(api_key, root_steam_id, login_secure, session_id)

    # Get steam IDs
    friends_steam_ids = bot.get_steam_ids()
    print(f"Got {len(friends_steam_ids)} Steam IDs")

    bot.comment_on_profile(root_steam_id, comment)

    # Comment on profiles
    for i in range(len(friends_steam_ids)):
        print(f"\n{i}/{len(friends_steam_ids)}")
        steam_id = friends_steam_ids[i]

        if has_commented(steam_id):
            output = fg.red + f"Already commented on {steam_id}" + fg.rs
            print(output)
        else:
            steam_ids_commented.append(steam_id)

        sucsess = bot.comment_on_profile(steam_id, comment)
        
        widgets = ['Sleeping: ', progressbar.AnimatedMarker()]
        bar = progressbar.ProgressBar(widgets=widgets).start()

        for i in range(DELAY*10):
            sleep(0.1) 
            bar.update(i)   
        print("\n")

    print("Sent Comments, Finished!")
    sleep(delay)    


def has_commented(steam_id) -> bool:
    """
    Checks if the steam_id has already been commented on.
    @param steam_id: The steam ID to check
    @return: True if the steam ID has already been commented on, False otherwise
    """
    return steam_id in steam_ids_commented

#Call main
main()