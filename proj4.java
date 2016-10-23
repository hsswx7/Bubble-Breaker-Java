
/**
 * @author Harprabh Sangha//hsswx7
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class proj4 extends Application {
	
	public Bubble[][] cell =  new Bubble[20][20]; // creating global 2d array like cell in ex 13 ch 16 ticTacToe cells
	public Color [][] poopColor = new Color[20][20];  // array for the colors 
	public int colorNumber = 0;
	public int potentialScore = 0;
	public int score = 0;
	public int grouped = 0;
	public boolean gone = false; //boolean to check if all bubbles popped 
	public boolean alone = false; //checking to see if only single bubbles are left 
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		launch(args); // launching the application 	
	}
	public void start(Stage mainStage)
	{
		String gameoverText = "Game Over! You have Popped all Bubbles! Click New Game To Replay";
		String gameLostText = "Game Over! Game Lost, Not all bubbles Burst";
		mainStage.setTitle("Bubble Breaker Game");		
		Button btExit; // making the Exit button 
		Button btNew;
		btExit = new Button();
		btExit.setText("Quit"); // setting text for exit button
		btNew = new Button("New");
		
		
		HBox btns = new HBox();
		btns.setSpacing(5);
		btns.setAlignment(Pos.CENTER);
		btns.getChildren().add(btExit); // adding buttons 
		btns.getChildren().add(btNew);
		BorderPane mainPane = new BorderPane(), topPane = new BorderPane();             		
		mainPane.setTop(topPane);                    
		topPane.setLeft(btns);
		topPane.setCenter(new Label("Potential Score: " + potentialScore));
		topPane.setRight(new Label("Score: " + score));
		mainPane.setBottom(game()); // calling the game to create the bubbles 
		mainPane.getBorder();
		// Create a scene and place it in the stage
	    Scene scene = new Scene(mainPane, 800, 800 );
	    mainStage.setScene(scene); // Place the scene in the stage
	    mainStage.show(); // Display the stage
	    mainStage.setTitle("Bubble Breaker"); // Set the stage title
	    
	    
	    
	 //---- Handling The mouse click on the bottom pane of the Main Pain to change the bubbles ----- 
	    mainPane.getBottom().setOnMouseClicked((e)->
		{
			gone = allDeletedGameWon (gone,mainPane,gameoverText, gameLostText); //sending to function to check if all deleted 
			//gameOverLost();
			if (!gone)  // if not all coolers popped 
			{
				for (int column = 0; column <20 ; column++)
				{
					for (int row =0; row < 20; row++)
					{	
						if (cell[column][row].clicked == true && cell[column][row].doubleClicked == false && cell[column][row].algorithm == false)
						{
							//System.out.println("going to handelMouseClicked");
							unSelectAll(column,row);
							//System.out.println("Column: " + column + " row: " + row);
							handleMouseClickedFirst(column, row);
							potentialScore= pScore();
							//System.out.println("Potential Score: " + potentialScore);
							topPane.setCenter(new Label("Potential Score: " + potentialScore));
							return;	
						}
						else if (cell[column][row].doubleClicked == true && cell[column][row].algorithm == true)
						{
							checkAloneDoubleClicked(); //checking if the user is clicking on only one bubble 
							if (grouped > 1) // if their is more than one then bubble in the group then it eliminates 
							{
								grouped =0;
								handelDoubleClicked(column,row,gone,mainPane,gameoverText,gameLostText); // Handles scoring and passes bubbles to scoring 
								topPane.setCenter(new Label("Potential Score: " + potentialScore));
								topPane.setRight(new Label("Score: " + score));
							}
							else 
							{
								grouped =0;
								cell[column][row].doubleClicked = false;
								return;
							}
						}
					}
				}
			}
		});
		btExit.setOnAction((ActionEvent e) -> {
			System.exit(0); // exiting the java program
		});
		btNew.setOnAction((ActionEvent e) -> { //code for new button
				score=0;
				start(mainStage); // creating the bubble grid again for new game
		});
		
	}
	
	public GridPane game()
	{
		GridPane bubbleGrid = new GridPane();
		bubbleGrid.setAlignment(Pos.CENTER);
		// bubbleGrid.setPadding(new Insets(5, 5, 5, 5));
		//bubbleGrid.setHgap(2);
		//bubbleGrid.setVgap(2);	
		for (int column = 0; column <20 ; column++)
		{
			for (int row =0; row < 20; row++)
			{
				cell [column][row] = new Bubble(); //putting bubble into the 2d array 
				poopColor[column][row] = colorRandomizer ();
				cell[column][row].setFill(poopColor[column][row]);
				//cell[column][row].setRadius(10);
				bubbleGrid.add(cell [column][row], column, row); //adding bubble to the grid
			}
		}
		return bubbleGrid;
	}
	private static Color colorRandomizer ()
	{
		int randomColorChoice  = 0;
		randomColorChoice = ((int)(Math.random() * 4)); // choosing random choice between 0-3 for colors  
		//System.out.println("Choice: " + randomColorChoice);
		if (randomColorChoice == 0)
		{
			return Color.ROYALBLUE;
		}
		else if (randomColorChoice == 1)
		{
			return Color.FIREBRICK;
		}
		else if (randomColorChoice == 2)
		{
			return Color.GREEN;
		}
		else if (randomColorChoice == 3)
		{
			return Color.DARKORANGE;
		}
		else
			return null;
	}
	private boolean allDeletedGameWon (boolean gone, BorderPane mainPane,String gameoverText,String gameLostText)
	{ 
		int allCircleCounter =0;
		for (int column = 0; column <20 ; column++)
		{
			for (int row =0; row < 20; row++)
			{	
				if ( poopColor[column][row] == Color.TRANSPARENT)
				{
					allCircleCounter++;
				}
			}
		}
		if (allCircleCounter < 400)
		{
			gone = false;
		}
		else if (allCircleCounter >= 400)
		{
			gone = true;
			mainPane.setBottom(new Label(gameoverText));
		}
		//System.out.println("Bubbles: " + allCircleCounter);
		return gone;
	}
	private boolean gameOverLost ()
	{
		for(int column = 0; column < 20; column++)
			for(int row = 0; row < 20; row++)
			{
				Bubble start = cell[column][row];
				if(start.getFill() != Color.TRANSPARENT)
				{
					if(column != 0) //left bounds check
						if(cell[column-1][row].getFill() == start.getFill()) //left circle color check
							return false;
					if(column != 19) //right bounds check
						if(cell[column+1][row].getFill() == start.getFill()) //right circle color check
							return false;
					if(row != 0) //lower bounds check
						if(cell[column][row-1].getFill() == start.getFill()) //lower circle color check
							return false;
					if(row != 19) //upper bounds check
						if(cell[column][row+1].getFill() == start.getFill()) //upper circle color check
							return false;
				}
			}
		return true;
	}
	private int checkAloneDoubleClicked() //checks to see if the bubbles double clicked is alone of in a group 
	{
		for (int count = 0; count < 20; count++)
		{
			for (int rcount =0; rcount <20; rcount++)
			{
				if (cell[count][rcount].algorithm == true)
				{
					grouped++;	
				}
			}
		}
		return grouped;
	}
	private void unSelectAll(int column, int row) 
	{
		potentialScore = 0;
		for (int c = 0; c <20 ; c++)
		{
			for (int r = 0; r < 20; r++)
			{	
				//System.out.println("column: " + c + " row: " + r);
				cell[c][r].clicked = false;
				//cell[c][r].picked = false;
				cell[c][r].algorithm = false;
				cell[c][r].doubleClicked = false;
				cell[c][r].setStroke(Color.TRANSPARENT);
			}
		}
		cell[column][row].setStroke(Color.YELLOW);
		cell[column][row].clicked = true;
		//cell[column][row].picked = true;
		
		
	}
	private void handleMouseClickedFirst(int column, int row) 
	{
		//System.out.println("In HandleMOuse ClickedFirst");
		//cell[column][row].setStroke(Color.YELLOW);
		cell[column][row].algorithm = true;
		upClick(column,row);  //searching for the bubbles above 
		rightClick(column,row);
		leftClick(column,row);
		downClick(column,row);
		//System.out.println(column+ " " + row);
	}
	private void upClick (int column, int row) //searches for the bubbles above 
	{
		if (row ==0) //if i reached the end of bound 
		{
			return;
		}
		if (cell[column][row-1].clicked == true )
		{
			return;
		}
		if (poopColor[column][row-1] == poopColor[column][row] )
		{
			cell[column][row-1].setStroke(Color.YELLOW);
			cell[column][row-1].clicked = true;
			//cell[column][row-1].picked = true;
			cell[column][row-1].algorithm = true;
			rightClick(column,row-1);
			leftClick(column,row-1);
			upClick(column,row-1);
		}
		
		
	}
	private void rightClick (int column, int row) // searching for the right side bubbles 
	{
		if (column == 19) //if bounds hit function stops 
		{
			return;
		}
		if (cell[column+1][row].clicked == true)
		{
			return;
		}
		if (poopColor[column+1][row] == poopColor[column][row])
		{
			cell[column+1][row].setStroke(Color.YELLOW);
			cell[column+1][row].clicked = true;
			//cell[column+1][row].picked = true;
			cell[column+1][row].algorithm = true;
			rightClick(column+1,row);
			upClick(column+1,row);
			downClick(column+1,row);
		}
	}
	private void leftClick (int column, int row)
	{
		if (column == 0)
		{
			return;
		}
		if (cell[column-1][row].clicked == true)
		{
			return;
		}
		if (poopColor[column-1][row] == poopColor[column][row])
		{
			cell[column-1][row].setStroke(Color.YELLOW);
			cell[column-1][row].clicked = true;
			//cell[column-1][row].picked = true;
			cell[column-1][row].algorithm = true;
			downClick(column-1,row);
			upClick(column-1,row);
			leftClick(column-1,row);
		}
	}
	private void downClick (int column, int row)
	{
		if (row == 19)
		{
			return;
		}
		if (cell[column][row+1].clicked == true )
		{
			return;
		}
		if (poopColor[column][row+1] == poopColor[column][row])
		{
			cell[column][row+1].setStroke(Color.YELLOW);
			//cell[column][row+1].picked = true;
			cell[column][row+1].clicked = true;
			cell[column][row+1].algorithm = true;
			rightClick(column,row+1);
			downClick(column,row+1);
			leftClick(column,row+1);
		}
	}
	private int pScore()
	{
		int potentialScore = 0;
		
		for (int column = 0; column <20 ; column++)
		{
			for (int row =0; row < 20; row++)
			{
				if (cell[column][row].clicked == true && cell[column][row].doubleClicked == false && cell[column][row].algorithm == true)
				{
					potentialScore++;
				}
			}
		}
		potentialScore = potentialScore*(potentialScore-1);
		return potentialScore;
	}
	private void handelDoubleClicked(int column, int row,boolean gone,BorderPane mainPane,String gameoverText, String gameLostText)
	{
		for (int c = 0; c <20 ; c++)
		{
			for (int r =0; r <20; r++)
			{	
		
				if (cell[c][r].algorithm == true)
				{
					cell[c][r].setFill(Color.TRANSPARENT);
					cell[c][r].setStroke(Color.TRANSPARENT);
					poopColor[c][r] = Color.TRANSPARENT;
					cell[c][r].clicked = false;
					//cell[column][row].picked = false;
					cell[c][r].doubleClicked = false;
					cell[c][r].deleted = true;
				}
			}
		}
		score += potentialScore;
		potentialScore = 0; 
		//System.out.println("Column: " + column + " row: " + row);
		for(int column1 = 0; column1 < 20; column1++)
		{
        	for(int row1 = 19; row1 >= 0; row1--)
				if(cell[column1][row1].deleted == true)
					raiseBubbles(column1, row1);
		}
		
		// Checking if all deleted 
		gone = allDeletedGameWon (gone,mainPane,gameoverText,gameLostText); //sending to function to check if all deleted 
		if(gone)
		{
			
			try {
				writetoFile ();
			} 
			catch (FileNotFoundException e) 
			{
				//System.out.println("made it here");
				try  // makes file if no file
				{
					makeFile(); // if not File 
				} 
				catch (FileNotFoundException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			catch (IOException e) 
			{
				System.out.println("fail");
				e.printStackTrace();
			}
			if (ChckedScore())
			{
				mainPane.setBottom(new Label(gameoverText + " High Score! Added to file"));
			}
			else
			{
				mainPane.setBottom(new Label(gameoverText));
			}
		}
		else if (gameOverLost ())
		{
			
			try {
				writetoFile ();
			} 
			catch (FileNotFoundException e) 
			{
				//System.out.println("made it here");
				try  // makes file if no file
				{
					makeFile(); // if not File 
				} 
				catch (FileNotFoundException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			catch (IOException e) 
			{
				System.out.println("fail");
				e.printStackTrace();
			}
			if (ChckedScore())
			{
				mainPane.setBottom(new Label(gameLostText + "  High Score! Added to file"));
			}
			else
			{
				mainPane.setBottom(new Label(gameLostText));
			}
		}
		//raiseBubbles(column, row);
	}
	public void raiseBubbles(int column, int row)
	{
		//System.out.println("Column: " + x + " row: " + y);
		if(row == 0)
		{
			return;
		}
		int start = row;
		row--;
		while(cell[column][row].deleted == true)
		{
			if(row > 0)
				row--;
			else
				break;
		}
		if(cell[column][row].deleted == false)
		{
			cell[column][start].setFill(cell[column][row].getFill());
			//System.out.println("Poopcolor Before Change: " + poopColor[column][row]);
			poopColor[column][start] =(Color) (cell[column][row].getFill());
			//System.out.println("Poopcolor After Change: " + poopColor[column][start]);
			cell[column][row].setFill(Color.TRANSPARENT);
			poopColor[column][row] = Color.TRANSPARENT;
			cell[column][start].deleted = false;
			cell[column][start].algorithm= false;
			cell[column][row].deleted = true;
			cell[column][row].algorithm= true;
		}
	}
	public void makeFile() throws FileNotFoundException
	{
		PrintWriter writer = new PrintWriter("Score.txt");
		writer.println(score);
		writer.close();
	}
	public void writetoFile () throws IOException 
	{	
		int line =0;
		try
		{
			File inputfile = new File("Score.txt");
			Scanner input = new Scanner(inputfile);
			// read one line at a time
			while( input.hasNextInt()) // verify there are more lines
			 // or will get an exception
			{
				line = input.nextInt();
				//System.out.println(line);
	
			}
			// release resources to operating system when done
			input.close();
		}
		catch (FileNotFoundException e)
		{
			try 
			{
				makeFile();
			} 
			catch (FileNotFoundException e1) 
			{
				//e1.printStackTrace();
			}
			//System.out.println(e.getMessage());
		}  
		if (score > line)  //only write if the score is higher 
		{
			PrintWriter writer = new PrintWriter("Score.txt");
			writer.println(score);
			writer.close();
		}
	}
	public boolean ChckedScore()
	{
		int line =0;
		try
		{
			File inputfile = new File("Score.txt");
			Scanner input = new Scanner(inputfile);
			// read one line at a time
			while( input.hasNextInt()) // verify there are more lines
			 // or will get an exception
			{
				line = input.nextInt();
				//System.out.println(line);
	
			}
			// release resources to operating system when done
			input.close();
		}
		catch (FileNotFoundException e)
		{
			try 
			{
				makeFile();
			} 
			catch (FileNotFoundException e1) 
			{
				//e1.printStackTrace();
			}
			System.out.println(e.getMessage());
		} 
		
		if (score >= line)
		{
			return true;
		}
		else 
		return false; 
	}
}


class Bubble extends Circle   //poop the answer is poop 
{
	public boolean clicked = false; // true  if user clicks on the circle 
	//public boolean picked = false;   // true if clicked circle changes stroke to yellow
	public boolean doubleClicked = false; // true if both clicked and picked are true and user clicked on that circle 
	public boolean algorithm = false;  // to check if the bubble has gone through too be grouped  
	public boolean deleted = false; // to checked if the bubble is deleted or not
	int colorCode= 0;
	public Bubble()
	{
		super(14);
		setStroke(Color.TRANSPARENT);
		setStrokeWidth(6);
		this.setOnMouseClicked(e -> 
		{    																//taken from ex 13 ch 16 TicTacToe
			if (this.deleted == false)
			{
				//System.out.println("I cliked suck it");
				if (this.clicked == false && this.doubleClicked == false)
				{
					//System.out.println("in");
					clicked(this); //so now i know that it has been clicked on
					hilight(this); // makes the stroke yellow
					//picked(this); //so i know its highlighted 
					return;
					
				}
				if (this.clicked == true && this.algorithm == true)
				{
					//System.out.println("Coming into this");
					doubleClicked(this);
				}
			}
	     });
	}
	private void clicked (Bubble bubble)
	{
		this.clicked = true; 
	}
	private void doubleClicked (Bubble bubble)
	{
		this.doubleClicked = true;
	}
	/*private void picked (Bubble bubble)
	{
		this.picked = true;
	}*/
	private void hilight (Bubble bubble)
	{
		this.setStroke(Color.YELLOW);
	}
}



