## Planned

- Fix: Start playlist after login
    - Uninstall app, clear Spotify app data: `adb uninstall me.cpele.runr.debug && adb shell pm clear com.spotify.music`
    - Start app
    - Complete login
    - 👉 App should:
        - Display default pace
        - Display Increase/Decrease buttons 
        - Start a playlist with current pace
        - Display current track cover

- Improve: Show progress after changing pace

- Improve: Create PlayerConnectActivity

## Changelog

- Fix: Let the user cancel login
- Fix: Prevent changing pace when not connected

## Misc.

- http://slowwly.robertomurray.co.uk/delay/1000
