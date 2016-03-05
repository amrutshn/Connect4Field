/**
 * Connect4FieldInterface.java
 * 
 * Version:
 *    	1.1 November,21 2015
 *
 * Revisions:
 *      Initial revision
 * 
 */

/**
 * This is an interface for RMI
 * 
 * @author		Pooja Ketan Shah
 * @author		Amrut Shenoy
 * 
 */

import java.rmi.*;

public interface Connect4FieldInterface extends Remote{
	public void connect(Connect4FieldInterface c) throws RemoteException;
	public void initBoard() throws RemoteException;
	public void setClient(Connect4FieldInterface c) throws RemoteException;
	public Connect4FieldInterface getClient() throws RemoteException;
	public void send(String msg) throws RemoteException;
	public boolean didLastMoveWin()throws RemoteException;
	public boolean isItaDraw()throws RemoteException;
	public void dropPieces(int col, char gamePiece)throws RemoteException;
	public boolean checkIfPiecedCanBeDroppedIn(int col)throws RemoteException;
	public void setPieces(int col, char piece)throws RemoteException;
	public int getColumn()throws RemoteException;
	public char getPiece()throws RemoteException;
	public boolean checkGameIsOver() throws RemoteException;
	public boolean checkForTurn(int turn)throws RemoteException;
	public int getSize() throws RemoteException;
	public char[][] getConnectBoard() throws RemoteException;
	public int getWinner() throws RemoteException;
}
