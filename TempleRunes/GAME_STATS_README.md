# Game Stats Logger

## What it does:
Automatically saves game statistics to a JSON file every time a game ends (win or loss).

## File Location:
The stats are saved to: `~/TempleRunes/game_stats.json`
(In your home directory under a TempleRunes folder)

## Example Output:

```json
{
  "timestamp" : "2025-11-22 20:43:15",
  "score" : 25,
  "timeSeconds" : 147,
  "timeFormatted" : "02:27",
  "difficulty" : "NORMAL",
  "result" : "WIN"
}
```

or

```json
{
  "timestamp" : "2025-11-22 20:45:32",
  "score" : 12,
  "timeSeconds" : 89,
  "timeFormatted" : "01:29",
  "difficulty" : "NORMAL",
  "result" : "LOSS"
}
```

## When it logs:
- ✅ When player wins (reaches exit door)
- ✅ When player loses (caught by guardian, negative score, out of time, trap)

## Fields:
- **timestamp**: Date and time of game end
- **score**: Final score
- **timeSeconds**: Total time played in seconds
- **timeFormatted**: Time in MM:SS format
- **difficulty**: Game difficulty level
- **result**: "WIN" or "LOSS"

## Future improvements:
You could extend this to:
- Append to file instead of overwrite (track multiple games)
- Add more stats (runes collected, guardians avoided, etc.)
- Create a leaderboard/high scores system
