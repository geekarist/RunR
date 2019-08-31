## Planned

- Fix: Wrong display when cancelling Spotify login
    - Uninstall app, clear Spotify app data: `adb uninstall me.cpele.runr.debug && adb shell pm clear com.spotify.music`
    - Start app
    - App opens Spotify login
    - Press Back
    - ✅ App should:
        - Display an error message: "User is not connected to player" 
        - Display a Retry button
    - Press Retry
    - ✅ App should open Spotify login
    - ✅ Complete login
    - 👉 App should:
        - Display current pace
        - Display Increase/Decrease buttons 
        - Display song cover
        - Start a playlist with current pace
- Fix: Change pace when not connected
    - Uninstall app, clear Spotify app data: `adb uninstall me.cpele.runr.debug && adb shell pm clear com.spotify.music`
    - Cancel login
    - Increase pace
    - 👉 App should:
        - Not crash
        - Display 'not connected' message and Retry button
    - Retry should work
- Improve: Create PlayerConnectActivity

## Misc.

- http://slowwly.robertomurray.co.uk/delay/1000
