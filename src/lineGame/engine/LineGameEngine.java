/*
 * Robert Tweedy
 * LineGameEngine.java
 * Version: 1.0
 * This class contains the logic needed to play the line game.
 * First Published: October 26, 2014
 * Last Modified: October 26, 2014
 * Changelog:
 * -Initial Version
 */

package lineGame.engine;

import java.util.ArrayList;

public class LineGameEngine
{
	private LineRow[] gameRows;
	private boolean gameIsRunning = false;
	private boolean isP1Turn = false;
	private int gameWinner = 0; //0 = game is still going on, 1 = P1, 2 = P2.
	
	public LineGameEngine()
	{
		//Empty Constructor
	}//Constructor
	
	/**
	 * Starts a traditional line game, where the first row has 1 line and each additional
	 * row has 2 more lines.
	 * @param rows The number of rows to have in the game.
	 * @return true if the game could be set up, false if not (ie. rows is <= 0).
	 */
	public boolean startNewGame(int rows)
	{
		boolean canSetupGame = (rows > 0);
		
		if(canSetupGame)
		{
			int startingValue = 1; // The traditional game starts with 1 line on
									// the first row.
			gameRows = new LineRow[rows];
			for(int i = 0; i < rows; i++)
			{
				gameRows[i] = new LineRow(startingValue);
				startingValue = (startingValue + 2); // Each additional row has
														// 2 more lines in it.
			}
			gameIsRunning = true;
			isP1Turn = true;
			gameWinner = 0;
		}// if(canSetupGame)
		
		return canSetupGame;
	}//startNewGame(int rows)
	
	/**
	 * Starts a line game with the specified parameters.
	 * @param rows The number of rows in the game.
	 * @param startingValue The number of lines on the first row.
	 * @param incrementValue The number of lines to add to each additional row.
	 * @return true if the game could be set up, false if not (if the values are <= 0).
	 */
	public boolean startNewGame(int rows, int startingValue, int incrementValue)
	{
		boolean canSetupGame = (rows > 0 && startingValue > 0 && incrementValue >= 0);
		
		if(canSetupGame)
		{
			int currentValue = startingValue;
			gameRows = new LineRow[rows];
			for(int i = 0; i < rows; i++)
			{
				gameRows[i] = new LineRow(currentValue);
				currentValue = (currentValue + incrementValue);
			}
			gameIsRunning = true;
			isP1Turn = true;
			gameWinner = 0;
		}// if(canSetupGame)
		
		return canSetupGame;
	}//startNewGame(int rows, int startingValue, int incrementValue)
	
	/**
	 * Start a line game using the specified number of lines in each row of the given array.
	 * @param premadeRows An array stating the number of lines to place in each row.
	 * @return true if the game can be set up, false otherwise (ie. the array length is <= 0
	 *         or one of the array values is <= 0).
	 */
	public boolean startNewGame(int[] premadeRows)
	{
		boolean canSetupGame = (premadeRows.length > 0);
		int currentRow = 0;
		
		if(canSetupGame)
			gameRows = new LineRow[premadeRows.length];
		
		while(canSetupGame && currentRow < premadeRows.length)
		{
			if(premadeRows[currentRow] <= 0)
			{
				canSetupGame = false;
			}
			else
			{
				gameRows[currentRow] = new LineRow(premadeRows[currentRow]);
			}
			currentRow++;
		}//while
		
		if(canSetupGame)
		{
			gameIsRunning = true;
			isP1Turn = true;
			gameWinner = 0;
		}// if(canSetupGame)
		
		return canSetupGame;
	}//startNewGame(int[] premadeRows)
	
	public boolean isGameRunning()
	{
		return gameIsRunning;
	}//isGameRunning
	
	public boolean isPlayer1Turn()
	{
		return isP1Turn;
	}//isPlayer1Turn
	
	public int getWinner()
	{
		return gameWinner;
	}//getWinner
	
	/**
	 * Gets the total number of lines in this particular game.
	 * @return The total number of lines in the game.
	 */
	public int getTotalLines()
	{
		int result = 0;
		
		if(gameIsRunning)
		{
			for(LineRow row : gameRows)
			{
				result = (result + row.getSize());
			}
		}//if
		return result;
	}//getTotalLines
	
	/*public int getNumberOfRows()
	{
		return gameRows.length;
	}//getNumberOfRows/**/
	
	/**
	 * Gets the number of lines remaining in the game.
	 * @return The number of remaining lines.
	 */
	public int getRemainingLines()
	{
		int result = 0;
		
		if(gameIsRunning)
		{
			for(LineRow row : gameRows)
			{
				result = (result + row.getTotalRemaining());
			}
		}//if
		
		return result;
	}//getRemainingLines
	
	/**
	 * Returns the current layout of the game rows as an arrayList of boolean arrays.
	 * Each position in the boolean array represents one position, whether it is a line or
	 * a space.
	 * @return ArrayList of boolean[].
	 */
	public ArrayList<boolean[]> getLineArray()
	{
		ArrayList<boolean[]> result = new ArrayList<boolean[]>();
		
		if(gameIsRunning)
		{
			for(LineRow row : gameRows)
			{
				result.add(row.getLineRow());
			}
		}
		
		return result;
	}//getLineArray
	
	/**
	 * Takes a turn of the game. Each turn consists of removing 1 or more adjacent lines
	 * on a single row. Whoever removes the last line on the entire board loses.
	 * @param row The row to remove a line from
	 * @param startLine The beginning of the range of lines to remove (inclusive).
	 * @param endLine The end of the range of lines to remove (inclusive).
	 * @return true if successful, false if not (invalid move).
	 */
	public boolean takeTurn(int row, int startLine, int endLine)
	{
		boolean canTakeTurn = (gameIsRunning && row >= 0);
		
		//Take the turn if the startLine and endLine values are valid.
		//If the turn was successful, check to see if the game is still running.
		if(canTakeTurn && gameRows[row].removeLines(startLine, endLine))
		{
			if(getRemainingLines() == 0)
			{
				gameIsRunning = false;
			}
			
			//If the game is running, change whose turn it is; otherwise, declare the winner.
			if(gameIsRunning)
			{
				isP1Turn=!isP1Turn;
			}
			else
			{
				if(isP1Turn)
				{//Player 1 removed the last line, so Player 2 is the winner.
					gameWinner = 2;
				}
				else
				{
					gameWinner = 1;
				}
			}//else
		}//if
		else
		{//The turn couldn't be taken with the given range; nothing has changed.
			canTakeTurn = false;
		}
		
		return canTakeTurn;
	}//takeTurn
	
	/**
	 * This private class stores information regarding a row of lines in the line game.
	 */
	private class LineRow
	{
		private boolean[] lines;
		
		public LineRow(int numLines)
		{
			lines = new boolean[numLines];
			for(int i = 0; i < lines.length; i++)
			{
				lines[i] = true;
			}
		}//Constructor
		
		public int getSize()
		{
			return lines.length;
		}//getSize
		
		public int getTotalRemaining()
		{
			int result = 0;
			
			for(boolean line : lines)
			{
				if(line)
				{
					result++;
				}//if
			}//foreach
			
			return result;
		}//getTotalRemaining
		
		/**
		 * Returns the actual array representing the remaining lines and their positions.
		 * Calling getSize() on this LineRow object is recommended for setting the size of
		 * the storing array to the correct value to store the array that will be returned.
		 * @return Array of lines, with true positions representing lines and false positions
		 *         representing blank spaces.
		 */
		public boolean[] getLineRow()
		{
			return lines;
		}//getLineRow
		
		/**
		 * Removes one or more lines from the row. Lines can only be removed if they are
		 * adjacent to each other (ie. There's no gap between the lines).
		 * @param rangeStart The starting range (inclusive) of lines to remove.
		 * @param rangeEnd The end of the range (inclusive) of lines to remove.
		 * @return true on success, false if there is a gap or the range is otherwise invalid.
		 */
		public boolean removeLines(int rangeStart, int rangeEnd)
		{
			boolean ableToRemove = true;
			int currentPosition = rangeStart;
			
			if(rangeStart > rangeEnd || rangeStart < 0 || rangeEnd >= lines.length)
			{
				ableToRemove = false;
			}
			
			while(ableToRemove && currentPosition <= rangeEnd)
			{
				if(!lines[currentPosition]) //There is a gap between lines
				{
					ableToRemove = false;
				}
				currentPosition++;
			}//while(ableToRemove)
			
			if(ableToRemove)
			{
				for(int i = rangeStart; i <= rangeEnd; i++)
				{
					lines[i] = false;
				}
			}//if(ableToRemove)
			
			return ableToRemove;
		}//removeLines
	}//LineRow
}//LineGameEngine
