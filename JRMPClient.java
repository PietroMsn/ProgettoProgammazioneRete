package server;

import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia dell'utente amministratore verso il server centrale.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 * 
 */
public interface JRMPClient extends Serializable,Remote {
	public static boolean WaitIIOPClient = true;
	public static boolean WaitServer = true;
	public static BigInteger[] Key = new BigInteger[100];
	public static int counter = 0;
	/**
	 * Codice principale eseguito dall'amministratore.
	 * 
	 * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
	 */
    public void actAdmin() throws RemoteException;
    
    /**
     * Notifica l'amministratore che un download e' stato completato.
     * 
     * @param address l'indirizzo IP del client.
	 * @param download l'URL del download completato.
     * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
     */
    public BigInteger[] codifyMsg(String Msg, BigInteger[] PublicKey) throws RemoteException;
}
