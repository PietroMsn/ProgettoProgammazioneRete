package server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia del server centrale verso gli amministratori.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 */
public interface MainServerJRMP extends Remote{

	/**
	 * 
	 * 
	 * @param address indirizzo del file da scaricare.
	 * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
	 */
    
    
    /**
     * Aggiunge il chiamante alla lista di amministratori collegati.
     * 
     * @param address l'indirizzo IP dell'amministratore.
     * @param stub la referenza remota al server esportato dall'amministratore.
     * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
     */
    
    
    /**
     * Rimuove il chiamante dalla lista di amministratori collegati.
     * 
     * @param address l'indirizzo IP dell'amministratore
     * @param stub lo stub del server esportato dall'amministratore
     * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
     */
    
    
    
    /**
     * Restituisce la lista dei client collegati presso il server centrale.
     * 
     * @return array di tipo <code>String</code> che contiene la lista degli utenti collegati.
     * @throws RemoteException nel caso in cui l'indirizzo passato non sia valido.
     */
    
    
    public String[] LeggiChiave() throws IOException;
    
}
