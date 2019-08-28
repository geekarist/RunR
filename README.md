## Planned

- Fix: Wrong display when cancelling Spotify login
    - Uninstall app (`adb uninstall me.cpele.runr.debug`)
    - Clear Spotify app data (`adb shell pm clear com.spotify.music`)
    - Start app
    - App opens Spotify login
    - Press Back
    - App should:
        - Display only an error message: "User is not connected to player" 
        - Display a Retry button
    - Press Retry
    - App should open Spotify login
    - Complete login
    - App should:
        - Display current pace
        - Display Increase/Secrease buttons 
        - Display song cover
        - Start a playlist with current pace

## Misc.

- http://slowwly.robertomurray.co.uk/delay/1000
