package server;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia dei client verso il server centrale e il server di autenticazione.
 * 
 * @author Federico De Meo
 * @author Oscar Maraia
 *
 */
public interface IIOPClient extends Serializable, Remote {

	/**
	 * Codice principale eseguito dal client.
	 * 
	 * @throws RemoteException nel caso di problemi di comunicazione client\server.
	 */
	public void act() throws RemoteException;

	/**
	 * Esegue il download di un file tramite protocollo HTTP.
	 * 
	 * @param address l'indirizzo del file da scaricare.
	 * @throws RemoteException nel caso di problemi di comunicazione client\server.
	 * @throws MalformedURLException nel caso in cui l'indirizzo passato non sia valido.
	 */
	
	
	/**
	 * Recupera l'indirizzo IP della macchina client.
	 * 
	 * @return una stringa che rappresenta l'indirizzo.
	 * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
	 */
	public String getAddress() throws RemoteException;
	
	public void finestra_opzioni() throws RemoteException;
	
	public BigInteger getKey() throws RemoteException;
	
	public BigInteger[] getArrayPrimi() throws RemoteException;
}
