# Alien Skin (Fabric, Minecraft 1.21.11, client-side)

Turns you into a grey alien. Purely client-side: nobody else sees it, it never touches the server or your account skin.

- `/alienskin self` - only you (default)
- `/alienskin all`  - every player looks like an alien on your screen
- `/alienskin off`  - back to normal

Works in the world, first-person hands and the tab list. Picks the slim texture automatically for Alex-model skins.
The choice is saved in `config/alienskin.txt`.

## Build
Push to GitHub: Actions runs `.github/workflows/build.yml` and uploads `alien-skin-jar`
(or run `gradle build` locally, jar is in `build/libs/`). Needs Fabric API.

## Notes
- The skin is blocky (a normal skin can't change head size). For the big-head version use the Figura avatar from the alien pack.
- It swaps the texture inside `PlayerListEntry.getSkinTextures()` via a mixin and rebuilds the record by reflection,
  so it keeps working across the 1.21.x skin-record changes. If anything fails it logs once and leaves skins alone.
