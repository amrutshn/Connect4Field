/**
 * Connect4FieldServer.java
 * 
 * Version:
 *    	1.1 November,21 2015
 *
 * Revisions:
 *      Initial revision
 * 
 */

/**
 * This is a server class which implements interface methods for RMI
 * 
 * @author		Pooja Ketan Shah
 * @author		Amrut Shenoy
 * 
 */

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Connect4FieldServer extends UnicastRemoteObject implements Connect4FieldInterface, Runnable{

	private ArrayList<Connect4FieldInterface> myClients;

	public static boolean gameIsOver = false;
	public static int ROWS = 9;
	public static int COLS = 25;
	static char drawBoard[][]=new char[ROWS][COLS];
	public Connect4FieldInterface client = null;
	static int endCount=25;
	public int rowNumber=-1;
	public int colNumber=-1;
	public static char token;
	public static char gamePiece;
	public static int col;
	public static int currentTurn=0;
	static Object o = new Object();
	public static int winner=-1;


	/**
	 * This is a default constructor which will initialize new 
	 * arraylist to keep track of number of clients connected
	 * @throws RemoteException
	 */
	protected Connect4FieldServer() throws RemoteException {
		myClients = new ArrayList<Connect4FieldInterface>();
	}

	/**
	 * When the client's request comes, it will add the client objects in arraylist.
	 * Only four clients can be connected to the same game.
	 */
	@Override
	public void connect(Connect4FieldInterface c) throws RemoteException {
		Connect4FieldInterface client = getClient();
		if(myClients.size()<4){
			myClients.add(c);
			client.send("Client connected!");
		}
		else{
			client.send("No more clients can be added...");
		}
	}

	/**
	 * Initializes the board of Connect4Field.
	 */
	@Override
	public void initBoard() throws RemoteException {
		for(int row=0;row<ROWS;row++){
			for(int col=row;col<COLS-row;col++){
				drawBoard[row][col]='o';
			}
		}
	}

	/**
	 * Sets the client object
	 */
	@Override
	public void setClient(Connect4FieldInterface c) throws RemoteException {
		client = c;

	}

	/**
	 * Returns the client object
	 */
	@Override
	public Connect4FieldInterface getClient() throws RemoteException {
		return client;
	}

	/**
	 * Sends the message to the client
	 */
	@Override
	public void send(String msg) throws RemoteException {
		System.out.println(msg);
	}

	/**
	 * Checks if the last move made by a particular player is a winner
	 */
	@Override
	public boolean didLastMoveWin() throws RemoteException {

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
	 * Checks if the board is full, if yes then its a draw.
	 */
	@Override
	public boolean isItaDraw() throws RemoteException {
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
	 * Drops the game piece in column as mentioned in parameters.
	 * @param col: Input Column of the board
	 * @param gamePiece: Game piece to be dropped in the boards
	 */
	@Override
	public void dropPieces(int col, char gamePiece) throws RemoteException {
		for(int rows=ROWS-1; rows>=0;rows--){
			if(drawBoard[rows][col] == 'o'){
				rowNumber = rows;
				colNumber = col;
				break;
			}

			else
				continue;
		}
		drawBoard[rowNumber][col]=gamePiece;

		token = gamePiece;

	}

	/**
	 * Checks if piece can be dropped in a particular location
	 */
	@Override
	public boolean checkIfPiecedCanBeDroppedIn(int col) throws RemoteException {
		if(col<0 || col > COLS-1){
			return false;
		}
		else{
			for(int checkRow=ROWS-1; checkRow>=0;checkRow--){
				if(drawBoard[checkRow][col]=='o'){
					System.out.println("Piece can be dropped in column " + col);
					return true;
				}
			}
		}
		System.out.println("Piece can't be dropped in column " + col);
		return false;
	}

	/**
	 * Gets the input of column from the user and 
	 * starts the thread for each move made by the player
	 */
	@Override
	public void setPieces(int col, char piece) throws RemoteException {

		Thread P1 = null;
		Connect4FieldServer.col=col;
		System.out.println("col: " + col);
		currentTurn=(currentTurn+5)%4;
		if(gameIsOver==true){
			for(int index=0;index<myClients.size();index++){
				myClients.get(index).send("Game over");
			}
		}
		if(gamePiece!=piece){
			try {
				gamePiece=piece;
				System.out.println("Starting a new Thread for gamePiece: "+gamePiece);
				P1=new Thread(new Connect4FieldServer());
			} catch (RemoteException e) {e.printStackTrace();}
			P1.start();
			try {
				P1.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				client.send("Other player's turn, please wait for your turn");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gets the column number inputed by the user
	 */
	@Override
	public int getColumn() throws RemoteException {
		return col;
	}

	/**
	 * Gets the game piece of a particular user
	 */
	@Override
	public char getPiece() throws RemoteException {
		return gamePiece;
	}

	/**
	 * Checks if the game is over
	 */
	@Override
	public boolean checkGameIsOver() throws RemoteException {
		return gameIsOver;
	}

	/**
	 * Checks if it is current player's turn
	 */
	@Override
	public boolean checkForTurn(int turn) throws RemoteException {
		if(turn==currentTurn){
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of players connected
	 */
	@Override
	public int getSize() throws RemoteException {
		return myClients.size();
	}

	/**
	 * Prints the updated board
	 * @param board: 	Board to be displayed
	 * @throws RemoteException
	 */
	public static void printBoard(char[][]board)throws RemoteException{
		for(int row=0;row<ROWS;row++){
			for(int col=row;col<COLS-row;col++){
				System.out.print(board[row][col]);
			}
			System.out.println();
			for(int index=0;index<=row;index++){
				System.out.print(" ");
			}
		}
		System.out.println();
	}

	public static void main(String args[]){
		Connect4FieldServer server;
		try {	
			server = new Connect4FieldServer();
			server.initBoard();
			// Binds the Server to the registry
			Naming.rebind("rmi://localhost/ConnectService", server);
			System.out.println("Waiting for clients to be connected...");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Retrieves the updated board and broadcasts to all the clients
	 */
	public char[][] getConnectBoard() throws RemoteException {
		return drawBoard;
	}

	/**
	 * Gets the winner of the game if any
	 */
	@Override
	public int getWinner() throws RemoteException {
		return winner;
	}

	/**
	 * Thread that has been started enters this method
	 */
	@Override
	public void run() {
		synchronized(o){
			if(!gameIsOver){
				try {
					if(isItaDraw()){
						gameIsOver = true;
						client.send("It is a draw!");
					}
					else{
						int inputColumn=getColumn();
						char inputGamePiece= getPiece();
						if(checkIfPiecedCanBeDroppedIn(inputColumn)){
							dropPieces(inputColumn, inputGamePiece);		
						}
						if (didLastMoveWin() ) {
							gameIsOver = true;
							winner=currentTurn;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					printBoard(drawBoard);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}			
		}
	}
}
