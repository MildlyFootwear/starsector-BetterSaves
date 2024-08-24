# BetterSaves

## Description

Changes the behavior of making and loading saves in Starsector.  

The vanilla save directory will be used to store the latest copies of the saves of all your characters. Each character will get its own subdirectory that it will save to and load from.  

Meaning, the Load Game button on the main menu will act more like a character select, then while that character is loaded only their saves will be shown.  

Iron mode saves should not be affected by any changes.  

Outside of the game, saves will be named with the character name followed by cycle, month, day, hour. "Sheliak Parrish c206 03 02 04", for example.

Short demonstration: https://www.youtube.com/watch?v=4fRm43ifCx4

## Known Issues  

- Only saves made after the mod is installed will be properly organized unless you move old saves into the new directory manually. All subsequent saves will be properly organized, however, meaning old characters will still work.  
- Due to Starsector's plugin interface prohibiting copying files, every time the game saves it'll save twice. Once to the main save folder and once to the character's sub folder.  
- "Save" and "Save Copy" will function identically, with both always creating a new save slot, keeping the old save. Not a big issue, but worth noting.  
- Deleting a save on the main menu won't delete the saves in a character's sub folder.  
