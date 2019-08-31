## Planned

- Fix: Wrong display when cancelling Spotify login
    - Uninstall app, clear Spotify app data: `adb uninstall me.cpele.runr.debug && adb shell pm clear com.spotify.music`
    - Start app
    - App opens Spotify login
    - Press Back
    - App should:
        - Display an error message: "User is not connected to player" 
        - Display a Retry button
    - Press Retry
    - App should open Spotify login
    - Complete login
    - 👉 App should:
        - Display current pace
        - Display Increase/Decrease buttons 
        - Display song cover
        - Start a playlist with current pace

- Improve: Create PlayerConnectActivity

## Changelog

- Fix: Prevent changing pace when not connected

## Misc.

- http://slowwly.robertomurray.co.uk/delay/1000
