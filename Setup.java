package server;

import java.rmi.RMISecurityManager;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Codice di inizializzazione dei server.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 */
final class Setup {

	static String serverIP;
	static String serverClass = "server.MainServerImpl";
	static String policy;

	public static void main(String args[]) {
		String serverDir=System.getenv("SERVERDIR");
		policy=serverDir+"/policy";
		serverIP = System.getenv("SERVERIP");
		System.out.println(serverIP);
		System.out.println("Sono il codice di inizializzazione dei server.");
		System.setSecurityManager(new RMISecurityManager());

		try {
			Properties prop = new Properties();
			prop.put("java.security.policy", policy);
			prop.put("java.class.path", serverClass);
			prop.put("javax.net.ssl.keyStore", "mySrvKeystore");
			prop.put("javax.net.ssl.keyStorePassword", "123456");
			System.out.println("Creo il gruppo di attivazione.");
			// creazione del gruppo di attivazione
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(prop, null);
			System.out.println("Gruppo creato.");
			System.out
					.println("Registrazione del gruppo su "
							+ System
									.getProperty("java.rmi.activation.ActivationSystem")
							+ ".");
			// registrazione del gruppo di attivazione
			ActivationGroupID groupID = ActivationGroup.getSystem()
					.registerGroup(groupDesc);
			System.out.println("Gruppo registrato.");
			System.out.println("Creo l'Activation Descriptor.");
			// creazione di un descrittore per il main server
			ActivationDesc actDesc = new ActivationDesc(
					groupID,
					serverClass,
					"file://"+serverDir+"/",
					null);
			// Registro il server attivabile
			System.out.println("Activation Descriptor creato.");
			System.out.println("Ora registro il descrittore.");
			MainServerClient stub_server = (MainServerClient) Activatable.register(actDesc);
			System.out.println("Descrittore registrato.");
			System.out.println(stub_server);
			AuthServerImpl server = new AuthServerImpl(stub_server);

			Properties prop1 = new Properties();
			prop1.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.rmi.registry.RegistryContextFactory");
			prop1.put(Context.PROVIDER_URL, "rmi://" + serverIP);
			InitialContext cxt1 = new InitialContext(prop1);
			cxt1.rebind("AuthServer", server);
			System.out.println("AuthServer esportato tramite JRMP.");

			Properties prop2 = new Properties();
			prop2.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.cosnaming.CNCtxFactory");
			prop2.put(Context.PROVIDER_URL, "iiop://" + serverIP + ":5555");
			InitialContext cxt2 = new InitialContext(prop2);
			cxt2.rebind("AuthServer", server);
			System.out.println("AuthServer esportato tramite IIOP.");

			// 4 debug
			System.out.println("Inizializzazione completata!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
