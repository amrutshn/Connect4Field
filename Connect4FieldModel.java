/**
 * Connect4FieldModel.java
 * 
 * Version:
 *    	1.1 November,6 2015
 *
 * Revisions:
 *      Initial revision
 */

/**
 * This program implements the Connect4Field game with a modified board.
 * It is a game between a player and computer or player and player.
 * 
 * @author		Pooja Ketan Shah
 * @author		Amrut Shenoy
 *
 */

import java.util.Scanner;


public class Connect4FieldModel{

	public static int ROWS = 9;
	public static int COLS = 25;
	public static int x=0;
	public int rowNumber=-1;
	public int colNumber=-1;
	public static char token;
	static char drawBoard[][]=new char[ROWS][COLS];
	char board[][] = new char[ROWS][COLS];
	String name;
	char gamePiece;

	/**
	 * This is a default constructor which initializes the board.
	 */
	public Connect4FieldModel(){
		initBoard();
	}
	
	public char[][] initBoard(){
		for(int row=0;row<ROWS;row++){
			for(int col=row;col<COLS-row;col++){
				drawBoard[row][col]='o';
			}
		}
		return drawBoard;
	}
	
	/**
	 * This method will check if pieces can be dropped on the board. 
	 * It returns false if column number is negative or 
	 * greater than the maximum size of board
	 * 
	 * @param	column		Input the column number
	 * 
	 * @return				True or false. If column is negative or greater than
	 * 						maximum size of board, it will return false else true			
	 * 
	 */
	public boolean checkIfPiecedCanBeDroppedIn(int column) {
		if(column<0 || column > COLS-1){
			return false;
		}
		else{
			for(int checkRow=ROWS-1; checkRow>=0;checkRow--){
				if(drawBoard[checkRow][column]=='o'){
					System.out.println("Piece can be dropped in column " + column);
					return true;
				}
			}
		}
		System.out.println("Piece can't be dropped in column " + column);
		return false;
	}

	/**
	 * This method will check drop pieces on the board.
	 * 
	 * @param	column		Input the column number
	 * @param	gamePiece	Input the game piece
	 * 
	 */
	public void dropPieces(int column, char gamePiece) {
		for(int rows=ROWS-1; rows>=0;rows--){
			if(drawBoard[rows][column] == 'o'){
				rowNumber = rows;
				colNumber = column;
				break;
			}

			else
				continue;
		}
		drawBoard[rowNumber][column]=gamePiece;
		
		token = gamePiece;
		
	}

	/**
	 * This method will check if the last move has won
	 * 
	 * 
	 * @return			True or false. If there are four consecutive elements 
	 * 					in row, column or diagonal, it will return true 
	 * 					otherwise false
	 * 
	 */
	public boolean didLastMoveWin() {

		//Check for vertical column
		int count=0;
		int rowNo = rowNumber;
		for(int row=0; row<ROWS;row++){
			for(int col=0; col<COLS; col++){
				if(row==rowNo && col==colNumber){
					if(drawBoard[rowNo][colNumber] != ' ' && drawBoard[rowNo][colNumber]==token){
						rowNo = rowNo + 1;
						count++;
					}
					if(count==4)
						return true;
				}
			}
		}

		//Check for horizontal row
		count=0;
		for(int col=0;col<COLS;col++){
			if(drawBoard[rowNumber][col] != ' ' && drawBoard[rowNumber][col] == token){
				count++;
				if(count == 4){
					return true;
				}
			}
			else{
				count = 0;
				continue;
			}
		}

		//Check for diagonal
		count = 0;
		rowNo = 0;
		//top-left diagonal
		int colNo = colNumber - rowNumber;
		int row1 = 0;
		int col1 = colNo;
		while(row1 < ROWS && col1 < COLS){
			if(drawBoard[row1][col1]==token && drawBoard[row1][col1]!=' '){
				count++;
				row1++;
				col1++;
				if(count == 4)
					return true;
			}
			else{
				count = 0;
				row1++;
				col1++;
			}
		}
		//top-right diagonal
		count = 0;
		colNo=colNumber + rowNumber;
		row1=0;
		col1=colNo;
		while(row1 < ROWS && col1>=0){
			if(drawBoard[row1][col1]==token && drawBoard[row1][col1]!=' '){
				count++;
				row1++;
				col1--;
				if(count == 4)
					return true;
			}
			else{
				count = 0;
				row1++;
				col1--;
			}
		}
		return false;
	
	}

	/**
	 * This method will check if the board is full.
	 * 
	 * @return				True or false. If board is full, it will return true
	 * 						else it will return false			
	 * 
	 */
	public boolean isItaDraw() {
		for(int row=0;  row<ROWS; row++){
			for(int col=0; col<COLS; col++){
				if(drawBoard[row][col]!=' ' && drawBoard[row][col]=='o'){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This method will return the current board state.
	 * @return	drawBoard
	 */
	public char[][] getBoard(){
		return drawBoard;
	}

		//This method gets asks the player to enter column no.
		public int nextMove() {
			Scanner sc = new Scanner(System.in);
			System.out.println("Please enter column no:");
			int column = sc.nextInt();
			return column;
		}

}
