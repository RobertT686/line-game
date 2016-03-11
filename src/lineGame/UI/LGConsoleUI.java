/*
 * Robert Tweedy
 * LGConsoleUI.java
 * Version: 1.0
 * This java program provides a text-based interface to the LineGameEngine for playing the
 *  Line game.
 * First Published: October 26, 2014
 * Last Modified: October 26, 2014
 * Changelog:
 * -Initial Version
 */

package lineGame.UI;

import java.util.Scanner;
import lineGame.engine.LineGameEngine;

public class LGConsoleUI
{
	private static Scanner keyboardInput = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		boolean consoleRunning = true;
		LineGameEngine lgEngine = new LineGameEngine();
		
		System.out.println("Welcome to the line game!");
		if(askYesNoQuestion("Would you like to see the rules?"))
		{
			System.out.println();
			System.out.println("It's very easy: 2 people take turns removing 1 or more");
			System.out.println("adjacent lines in a row. Whoever removes the last line on");
			System.out.println("the whole board loses the game. Player 1 always goes first.");
		}
		
		System.out.println();
		
		while(consoleRunning)
		{
			gameSetup(lgEngine);
			runGame(lgEngine);
			if(!askYesNoQuestion("Would you like to play again?"))
			{
				consoleRunning = false;
			}
		}//while(consoleRunning)
		
		System.out.println("---END OF LINE---");

	}//main
	
	private static void gameSetup(LineGameEngine engine)
	{
		boolean validAnswer = false;
		int rows = 0;
		int startLines = 0;
		int additionalLines = 0;
		String answerString = "";
		
		while(!validAnswer)
		{
			answerString = askMultipleChoiceQuestion("Please enter how many rows you would like this game to have: ");
			
			try
			{
				rows = Integer.parseInt(answerString);
				if(rows <= 0)
				{
					throw new NumberFormatException();
				}
				validAnswer = true;
			}
			catch(NumberFormatException e)
			{
				System.out.println("That is an invalid number of rows.");
			}
		}//while
		
		if(askYesNoQuestion("Would you like to configure the initial and additional lines?"))
		{
			validAnswer = false;
			
			while(!validAnswer)
			{
				answerString = askMultipleChoiceQuestion("Please enter how many lines you would like the first row to have: ");
				
				try
				{
					startLines = Integer.parseInt(answerString);
					if(startLines <= 0)
					{
						throw new NumberFormatException();
					}
					validAnswer = true;
				}
				catch(NumberFormatException e)
				{
					System.out.println("That is an invalid number of lines.");
				}
			}//while
			
			validAnswer = false;
			
			while(!validAnswer)
			{
				answerString = askMultipleChoiceQuestion("Please enter how many lines you would like to add to each additional row: ");
				
				try
				{
					additionalLines = Integer.parseInt(answerString);
					if(additionalLines < 0)
					{
						throw new NumberFormatException();
					}
					validAnswer = true;
				}
				catch(NumberFormatException e)
				{
					System.out.println("That is an invalid number of lines.");
				}
			}//while
			
			engine.startNewGame(rows, startLines, additionalLines);
		}//if
		else
		{
			engine.startNewGame(rows);
		}
	}//gameSetup
	
	private static void runGame(LineGameEngine engine)
	{
		int currentRound = 1;
		int currentRow;
		int firstLine = 0;
		int lastLine = 0;
		boolean validAnswer;
		
		while(engine.isGameRunning())
		{
			System.out.println("Current Round: " + currentRound);
			System.out.print("Current Turn: ");
			if(engine.isPlayer1Turn())
			{
				System.out.println("Player 1");
			}
			else
			{
				System.out.println("Player 2");
			}
			
			System.out.println("Remaining lines: " + engine.getRemainingLines());
			
			currentRow = 1;
			
			for(boolean[] row : engine.getLineArray())
			{
				System.out.print(currentRow + ":");
				for(boolean line : row)
				{
					if(line)
					{
						System.out.print(" | ");
					}
					else
					{
						System.out.print(" _ ");
					}
				}//for(line:row)
				
				System.out.println();
				currentRow++;
			}//for(row:LineArray)
			
			System.out.println();
			
			validAnswer = false;
			
			while(!validAnswer)
			{
				try
				{
					currentRow = (Integer.parseInt(askMultipleChoiceQuestion("Please select a row: ")) - 1);
					if(currentRow < 0 || currentRow >= engine.getLineArray().size())
					{
						throw new NumberFormatException();
					}
					boolean atLeastOneLineRemains = false;
					for(boolean line : engine.getLineArray().get(currentRow))
					{
						if(line)
						{
							atLeastOneLineRemains = true;
						}
					}
					if(!atLeastOneLineRemains)
					{
						throw new NumberFormatException();
					}
					validAnswer = true;
				}//try
				catch(NumberFormatException e)
				{
					System.out.println("That is an invalid row.");
				}
			}//while(!validAnswer)
			
			validAnswer = false;
			
			while(!validAnswer)
			{
				try
				{
					firstLine = (Integer.parseInt(askMultipleChoiceQuestion("Please select the first line to remove: ")) - 1);
					if(firstLine < 0 || firstLine >= engine.getLineArray().get(currentRow).length ||
					   !engine.getLineArray().get(currentRow)[firstLine])
					{
						throw new NumberFormatException();
					}
					validAnswer = true;
				}//try
				catch(NumberFormatException e)
				{
					System.out.println("That is an invalid starting line.");
				}
			}//while(!validAnswer)
			
			validAnswer = false;
			
			while(!validAnswer)
			{
				try
				{
					lastLine = (Integer.parseInt(askMultipleChoiceQuestion("Please select the last line to remove: ")) - 1);
					if(lastLine < firstLine || lastLine >= engine.getLineArray().get(currentRow).length ||
					   !engine.getLineArray().get(currentRow)[lastLine])
					{
						throw new NumberFormatException();
					}
					validAnswer = true;
				}//try
				catch(NumberFormatException e)
				{
					System.out.println("That is an invalid ending line.");
				}
			}//while(!validAnswer)
			
			if(engine.takeTurn(currentRow, firstLine, lastLine))
			{
				if(engine.isPlayer1Turn() && engine.isGameRunning())
				{
					currentRound++;
				}
			}
			else
			{
				System.out.println("You may only remove multiple adjacent lines.");
			}
			
			System.out.println();
		}//while(engine.isGameRunning())
		
		System.out.println("After " + currentRound + " rounds, Player " + 
						   engine.getWinner() + " has won the game! Congrats!");
		System.out.println();
		
	}//runGame
	
	/**
	 * Waits for input to a yes or no question. Accepts the values of "Y", "y", "N", and "n".
	 * @param prompt The prompt to present for the question, usually the question itself.
	 *        The string " (Y/N): " will be appended to the prompt to indicate valid answers.
	 * @return true if Yes, false if No.
	 */
	private static boolean askYesNoQuestion(String prompt)
	{
		boolean result = false;
		String questionResponse = "";
		
		while((!questionResponse.equalsIgnoreCase("y")) && (!questionResponse.equalsIgnoreCase("n")))
		{
			System.out.print(prompt + " (Y/N): ");
			questionResponse = keyboardInput.nextLine().trim();	
		}//while
		
		if(questionResponse.equalsIgnoreCase("y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		
		return result;
	}//askYesNoQuestion
	
	/**
	 * Waits for input to a multiple choice question.
	 * @param prompt The prompt to present the user, usually the question and valid choices.
	 *        The prompt needs to be formatted correctly to display nicely on the screen.
	 * @return the entered answer.
	 */
	private static String askMultipleChoiceQuestion(String prompt)
	{
		String questionResponse = "";
		
		System.out.print(prompt);
		questionResponse = keyboardInput.nextLine().trim();
		
		return questionResponse;
	}

}//LGConsoleUI
