## Java Line Game Engine

The LineGameEngine.java file contains the necessary logic for playing the line game. A text-based front-end is provided in the LGConsoleUI.java file.

The easiest way to run the program is to compile it into a .jar and then run it from the command line using "java -jar *filename.jar*"

#### Rules for the line game:
- The playing field consists of multiple rows with 1 or more lines per row.
- Each player takes turns removing 1 or more adjacent lines from a **single** row.
  - It doesn't matter where on the row you remove the line(s) as long as there are no gaps in the set of lines that is being removed.
- The goal of the game is to get your opponent to remove the last line on the board. **Whoever removes the last line _loses_ the game**.
