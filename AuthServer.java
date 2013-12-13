package server;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.IOException;

/**
 * Interfaccia del server di autenticazione verso i client.
 * 
 * @author Pietro Musoni	
 * @author Carlo Tacchella
 */
public interface AuthServer extends Remote {

	/**
	 * Esegue il login di un client e ritorna un MobileServer.
	 * 
	 * @param username
	 * @param password
	 * @return un MobileServer che il client esegue se il login ha successo, <code>null</code> altrimenti.
	 * @throws RemoteException nel caso di problemi di comunicazione client\server.
	 * @throws ClassNotFoundException nel caso in cui non trovi la classe da spedire al client.
	 */
	public MarshalledObject<?> login(String username, char[] password)
			throws RemoteException,ClassNotFoundException;
    
    public boolean isAdmin(String username, char[] givenPassword)
            throws IOException;
    
    public boolean isUser(String username, char[] givenPassword)
            throws IOException;

    public void newFileAdmin() throws RemoteException;

    public void newFileUser() throws RemoteException;

    public boolean newAdmin(String nomeCercato, char[] passwordCercata) throws IOException;
    
    public boolean newUser(String nomeCercato, char[] passwordCercata) throws IOException;
	/**
	 * Registra un utente nel sistema.
	 * 
	 * @param username
	 * @param password
	 * @param type - tipologia dei client: 0 utenti, 1 amministratori.
	 * @return <code>true</code> se la registrazione e' andata a buon fine, <code>false</code> altrimenti.
	 * @throws RemoteException nel caso di problemi di comunicazione client\server.
	 */
	public boolean registerUser(String username, char[] password, int type)
			throws RemoteException, IOException;
}
