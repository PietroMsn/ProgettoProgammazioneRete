package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
//import java.util.ArrayList;



import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

/**
 * Implementazione del server centrale.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 */
public class MainServerImpl extends Activatable implements MainServerIIOP,
		MainServerJRMP {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean WaitIIOPClient = true;
	public static boolean WaitServer = true;
	public static BigInteger[] Key = new BigInteger[100];
	public static int counter = 0;
	private BigInteger[] PublicKey = new BigInteger[2];
	
	//private ArrayList<String> freeClientList;
	//private HashMap<String, IIOPClient> clientList;
	//private HashMap<String, JRMPClient> adminList;
	//private static final long serialVersionUID = -6988728787044913015L;

	public MainServerImpl(ActivationID id, MarshalledObject<?> data)
			throws Exception {
		
		super(id, 35000, new SslRMIClientSocketFactory(),
				new SslRMIServerSocketFactory());
		//clientList = new HashMap<String, IIOPClient>();
		//freeClientList = new ArrayList<String>();
		//adminList = new HashMap<String, JRMPClient>();
		System.out.println("\nMainServer esportato.");
	}

	/*public void addClient(String address, MarshalledObject mo)
			throws RemoteException {
		try {
			System.out.println("L'utente all'indirizzo " + address
					+ " si e' registrato.");
			clientList.put(address, (IIOPClient) mo.get());
			freeClientList.add(address);
			Set<String> keySet = clientList.keySet();
			for(Object key:keySet)
				System.out.println(key);
			
			if(clientList.isEmpty())
				System.out.println("è vuota per niente!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}*/

	/*public void addAdmin(String address, MarshalledObject stub)
			throws RemoteException {
		try {
			adminList.put(address, (JRMPClient) stub.get());
			System.out.println("L'amministratore all'indirizzo " + address
					+ " si e' registrato.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}*/

	/*public void removeAdmin(String address) throws RemoteException{
	    adminList.remove(address);
	}
	
	

	public String[] getClient() throws RemoteException {
		return clientList.keySet().toArray(new String[0]);
	}*/

	
  
	/* Questa funzione (Se chiamata dal client JRMP), toglie l'attesa per i client IIOP, in modo
	  che questi iniziino a cercare la chiave (Key). Sarà L'IIOP che trova la chiave a sbloccare il server 
	  (WaitServer = false) e quindi a restituire la chiave al client JRMP per codificare il messaggio*/
	
	/*public BigInteger[] KeyRequest() throws RemoteException{
		BigInteger[] PublicKey = new BigInteger[2];
		WaitIIOPClient = false;
		
		while (WaitServer){
			System.out.println("server aspetta");
			if (counter == 2)
				WaitServer = false;
		}
		
		
		
		//PublicKey = getPublicKey(Key[0],Key[1]);
		return PublicKey;
	}*/
	
	
	/*public BigInteger[] SearchArrayPrimi() throws RemoteException{
		BigInteger[] temp = new BigInteger[2];
		Set<String> keySet = clientList.keySet();
		
		
		
		
		for(Object key:keySet){
			
		     IIOPClient value = clientList.get(key);
		     System.out.println(key);
		     temp = value.getArrayPrimi();
		     
		     if(!temp.equals(null))
		    	 	return temp;
			     
		     
		}
		return null;
	}*/
	     /* controllare che l'array risultante da getArrayPrimi 
	     sia vuoto per ogni client dell'hashmap, altrimenti abbiamo ottenuto p e q*/
	
	private BigInteger[] getPublicKey(BigInteger[] ArrayPQ) throws RemoteException{
		
		System.out.println("è entrato in getPublic...");
		
			
			final BigInteger[] PublicKey = new BigInteger[2];
			
			
		final BigInteger temp1 = ArrayPQ[0].subtract(BigInteger.ONE);
		final BigInteger temp2 = ArrayPQ[1].subtract(BigInteger.ONE);
		final BigInteger temp = temp1.multiply(temp2);
		PublicKey[0] = ArrayPQ[0].multiply(ArrayPQ[1]);
		BigInteger Random = BigInteger.valueOf(2); //random grande ma non troppo!!!
		
		while (!Euclide(Random, temp) && Random.compareTo(PublicKey[0]) == 1) {
			Random.add(BigInteger.ONE);	
		}
		
		PublicKey[1] = Random;
		
				
			
	
		return PublicKey;
	}
	
	public String[] LeggiChiave() throws IOException {
		String[] chiave = new String[2];
		
        //String givenPassword = CharBuffer.wrap(passwordCercata).toString();
		try {
			FileReader f;
			f=new FileReader(".listaChiavi.txt");
		
			BufferedReader b;
			b=new BufferedReader(f);
	
	
			
			chiave[0]=b.readLine();
			chiave[1]=b.readLine();
			
	           
				
			b.close();
			f.close();
	} catch(FileNotFoundException e) {
		return null;
	}
        return chiave;
    }
	
	
	public void scriviChiave(BigInteger[] Primi) throws IOException{
		
		PublicKey = getPublicKey(Primi);
		try {
        	newFileMsg();
            
		    FileOutputStream fos = new FileOutputStream (".listaChiavi.txt", true); // append
		    PrintWriter pw = new PrintWriter (fos);

		    pw.print (PublicKey[0]+"\n" +PublicKey[1]);
		    pw.close ();
    
            System.out.println("Hai fatto il file con la chiave\n");
                        
       } catch (FileNotFoundException e) {
            System.out.println("Impossibile aggiungere il messaggio.");
            e.printStackTrace();
        } catch (RemoteException e) {
        	System.out.println("Impossibile ricevere la chiave pubblica.");
        }
	}
	
	public void newFileMsg() throws RemoteException {
		String path = ".listaChiavi.txt";

		try {
			File file = new File(path);

			if (file.exists())
				file.delete();	

			else  {
				file.createNewFile();
				System.out.println("Il file " + path + " è stato creato");
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static boolean Euclide(BigInteger Random, BigInteger temp) {
		BigInteger r;
		
		r = Random.mod(temp);
		
		while(!r.equals(BigInteger.ZERO)) {
			Random = temp;
			temp = r;
			r = Random.mod(temp);
		}
		if(temp.equals(BigInteger.ONE))
			return true;
		else 
			return false;
	}


	
	
	
}
