import progressbar
from comment import CommentBot
from time import sleep
import config_handler
from sty import fg, bg, ef, rs


delay = 10 # Delay between comments (in seconds)
steam_ids_commented = []

def main():
    # Get config data
    api_key = config_handler.get_api_key()
    login_secure = config_handler.get_steam_login_secure_cookie()
    root_steam_id = input("Enter your steam ID: ")
    comment = input("What would you like to comment?\n")

    bot = CommentBot(api_key, root_steam_id, login_secure)

    # Get steam IDs
    friends_steam_ids = bot.get_steam_ids()
    print("Got {len(friends_steam_ids)} Steam IDs")

    # Comment on profiles
    for i in range(len(friends_steam_ids)):
        print(f"\n{i}/{len(friends_steam_ids)}")
        steam_id = friends_steam_ids[i]

        if has_commented(steam_id):
            output = fg.red + f"Already commented on {steam_id}" + fg.rs
            print(output)

        sucsess = bot.comment_on_profile(steam_id, comment)
        
        widgets = ['Sleeping: ', progressbar.AnimatedMarker()]
        bar = progressbar.ProgressBar(widgets=widgets).start()

        for i in range(delay*10): 
            sleep(0.1) 
            bar.update(i)   
        print("\n")

    print("Sent Comments, Finished!")
    sleep(delay)    


def has_commented(steamid):
    """
    Checks if the steamid has already been commented on.
    """
    return steamid in steam_ids_commented

#Call main
main()