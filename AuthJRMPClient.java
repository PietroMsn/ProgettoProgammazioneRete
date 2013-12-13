package server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
* Classe che implementa il codice del client JRMP per l'autenticazione.
* 
* @author Pietro Musoni
* @author Carlo Tacchella
*/
public class AuthJRMPClient implements Runnable {
	private AuthServer as;
	private String serverIP;
	private String codebase;
	private String username;
	private char[] password;

	@SuppressWarnings("unchecked")
	public void run() {

		try {
			System.setProperty("javax.net.ssl.trustStore", "mySrvKeystore");
			System.setProperty("javax.net.ssl.trustStorePassword", "123456");
			System.out.println("Sono nel codice di AuthJRMPClient.");
			System.setSecurityManager(new RMISecurityManager());
			serverIP = System.getenv("SERVERIP");
			codebase = "http://" + serverIP + ":8000/";
			Properties pr = new Properties();
			pr.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.rmi.registry.RegistryContextFactory");
			pr.put(Context.PROVIDER_URL, "rmi://" + serverIP);
			InitialContext ic = new InitialContext(pr);
			Object objRef = ic.lookup("AuthServer");
			System.out.println("Ho recuperato lo stub dell'AuthServer.");
			as = (AuthServer) PortableRemoteObject.narrow(objRef,
					AuthServer.class);
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		} catch (NamingException ne) {
			JOptionPane.showMessageDialog(null,"Problema con il servizio di Naming.");
		}
		
		Object[] message = new Object[4];
		message[0] = "Nome utente:";
		message[1] = new JTextField("");
		message[2] = "Password:";
		message[3] = new JPasswordField("");
		String[] options = { "OK" };
		URL icon;
		
		String scelta = "";
		boolean login=true;
		MarshalledObject<JRMPClient> mo;
		
		while (login) {
			try {
				username=null;
				password=null;
				icon = new URL(codebase + "res/Admin.png");

				scelta = (String) JOptionPane.showInputDialog(null,
						"MENU:\n1) Login\n2) Registra \n3) Esci",
						"Menu Client JRMP", JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(icon), null, null);
				int s = Integer.parseInt(scelta);
				switch (s) {
					case 1: {
						// Visualizzazione della schermata di Login
						icon = new URL(codebase + "res/Admin.png");
						while (username == null || username.equals("")
								|| password == null || password.equals("")) {
							JOptionPane.showOptionDialog(null, message,
									"Autenticazione.", JOptionPane.DEFAULT_OPTION,
									JOptionPane.QUESTION_MESSAGE, new ImageIcon(
											icon), options, options[0]);
							username = ((JTextField) message[1]).getText();
							password = ((JPasswordField) message[3]).getPassword();
						}
	
						mo = (MarshalledObject<JRMPClient>) as.login(username, password);
						if (mo != null) {
							JRMPClient ai = mo.get();
							ai.actAdmin();
							login = false;
						} else {
							icon = new URL(codebase + "res/UserFailed.png");
							JOptionPane.showMessageDialog(null, "Login non valido",
									"Errore.", JOptionPane.ERROR_MESSAGE,
									new ImageIcon(icon));
						}
						break;
					}
					case 2: {
						// Visualizzazione della schermata di Registrazione
						icon = new URL(codebase + "res/Admin.png");
						while (username == null || username.equals("")
								|| password == null || password.equals("")) {
							JOptionPane.showOptionDialog(null, message,
									"Registrazione.", JOptionPane.DEFAULT_OPTION,
									JOptionPane.QUESTION_MESSAGE, new ImageIcon(
											icon), options, options[0]);
							username = ((JTextField) message[1]).getText();
							password = ((JPasswordField) message[3]).getPassword();
						}
						if(!as.registerUser(username, password, 1)){
							icon = new URL(codebase + "res/UserFailed.png");
							JOptionPane.showMessageDialog(null, "Registrazione fallita.",
									"Errore.", JOptionPane.ERROR_MESSAGE,
									new ImageIcon(icon));
						}
						break;
					}
					case 3: {
						System.exit(0);
					}
				}
			} catch (MalformedURLException mue) {
				System.out.println("Indirizzo non valido.");
			} catch (NumberFormatException ne) {
				System.out.println("Scelta non valida.");
			} catch (RemoteException re) {
				System.out.println("Server non raggiungibile. Esco.");
				login=false;
			} catch (ClassNotFoundException cnfe) {
				System.out.println("Problema codebase. Esco.");
				login=false;
			} catch (IOException ioe) {
				System.out.println("Problema di IO. Esco.");
				ioe.printStackTrace();
				login=false;
			}
		}
	}
}
