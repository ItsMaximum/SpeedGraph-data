# SpeedGraph-data
Scrapes data from https://www.speedrun.com/ needed to create animated leaderboard progression graphs.

# Dependencies
This code requires [gson](https://github.com/google/gson/releases) to be referenced in the build path.

# Usage
In the Java script, set ```GAME_ID``` to the abbreviation used for the game's URL, set ```CATEGORY_Name``` to the name of the targeted category, set ```NTH_DAY``` to the number of days between each colum on the spreadsheet (7 is recommended), fill the ```SUBCATEGORIES``` array with all targeted subcategory values, and set ```LEVEL``` to the name of the targeted level, if applicable.

In the Python script, set ```directory``` to the name of a subdirectory containing the exported ```.txt``` data file renamed to ```data.txt``` and any square profile pictures in ```.png``` format that you would like to include for the runners.
