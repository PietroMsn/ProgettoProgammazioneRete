package server;


import java.io.IOException;
import java.math.BigInteger;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia del server centrale verso i client.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 */
public interface MainServerIIOP extends Remote {
	

	/**
	 * Aggiunge un client alla lista di utenti collegati.
	 * 
	 * @param address l'indirizzo IP del client.
	 * @param mo lo stub del mobile server esportato sul client.
	 * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
	 */
	//public void addClient(String address,MarshalledObject mo) throws RemoteException;
	
	/**
	 * 
	 * 
	 * @param ip l'indirizzo IP del client.
	 * @param download l'indirizzo del file appena scaricato.
	 * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
	 */

	
	/**
	 * Metodo invocato dai client per controllare la connessione verso il server.
	 * 
	 * @throws RemoteException nel caso in cui il server non sia raggiungibile.
	 */
	
	
	//public BigInteger[] KeyRequest() throws RemoteException;
	
	public BigInteger[] SearchArrayPrimi() throws RemoteException;
	
	public void scriviChiave(BigInteger[] Primi) throws IOException;
	
	
}
