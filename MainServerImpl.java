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
	private BigInteger[] PublicKey = new BigInteger[2];
	
	

	public MainServerImpl(ActivationID id, MarshalledObject<?> data)
			throws Exception {
		
		super(id, 35000, new SslRMIClientSocketFactory(),
				new SslRMIServerSocketFactory());
		
		System.out.println("\nMainServer esportato.");
	}

	
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

			if (file.exists()){
				file.delete();
				file.createNewFile();
			}
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
