package server;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class JRMPClientImpl implements JRMPClient {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainServerAdmin mainServJRMP;
    private String serverIP;
    private String codebase;
    private JRMPClient myStub;
    private String Msg;
    private String[] TuaMadre = new String[2];
    
    public JRMPClientImpl(MainServerAdmin ms) throws RemoteException {
    	mainServJRMP = ms;
    }

    public void actAdmin() throws RemoteException {
    	
    	
    	myStub=(JRMPClient) UnicastRemoteObject.exportObject(this);
        //register();
        serverIP = System.getenv("SERVERIP");
        codebase = "http://" + serverIP + ":8000/";
        URL icon;
        String scelta;
        int s = 0;
        try {
            while (s != 2) {
                icon = new URL(codebase + "res/Admin.png");
                scelta = (String) JOptionPane.showInputDialog(null,
                        "MENU:\n"
                                + "1) Invia messaggio da criptare\n"
                                + "2) Log out", "Menu Admin",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(icon),
                        null, null);
                try {
                    s = Integer.parseInt(scelta);
                } catch (NumberFormatException nfe) {
                    s = 4;
                }
                switch (s) {
                    case 1: {
                        icon = new URL(codebase + "res/AddDownload.png");
                        Msg = (String) JOptionPane.showInputDialog(null,
                                "Inserisci messaggio", "Richiedi chiave",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                        icon), null, null);
                        addMessage(Msg);
                        try {
                        	TuaMadre = mainServJRMP.LeggiChiave();
						} catch (IOException e) {
					
							e.printStackTrace();
						}
						System.out.println(TuaMadre[0]+ "\n" +TuaMadre[1]);
                        //PublicKey = mainServJRMP.SearchArrayPrimi();
                        //System.out.println("Ho trovato la chiave! che e :" + PublicKey[0] + PublicKey[1]);
                        //codifyMsg(Msg, PublicKey);
                        
                        break;
                    }
                    
                    case 2: {
                    	//mainServJRMP.removeAdmin(InetAddress.getLocalHost().toString());
                    	System.exit(0);
                        break;
                    }
                    default: {
                        icon = new URL(codebase + "res/Error.png");
                        scelta = null;
                        JOptionPane.showMessageDialog(null,
                                "Scelta non valida", "Errore",
                                JOptionPane.ERROR_MESSAGE, new ImageIcon(icon));
                    }
                }
            }
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }/* catch (UnknownHostException e){
            e.printStackTrace();
        }*/
    }
    
    /*private void register() throws RemoteException {
        try {
        	mainServJRMP.addAdmin(InetAddress.getLocalHost().toString(),new MarshalledObject(myStub));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
    }*/
    // Questa funzione aggiunge un messaggio al file di testo che sarà codificato una volta ricevuta la chiave
    private void addMessage(String msg) {
    	

        try {
        	newFileMsg();
            
		    FileOutputStream fos = new FileOutputStream ("listaMsg.txt", true); // append
		    PrintWriter pw = new PrintWriter (fos);

		    pw.print (msg+"\n");
		    pw.close ();
    
            System.out.println("Hai registrato il messaggio con successo\n");
                
        	///Aggiungi un pannello di attesa !!!!
            System.out.println("Messaggio in attesa della chiave pubblica per la codifica.");
            
        
       } catch (FileNotFoundException e) {
            System.out.println("Impossibile aggiungere il messaggio.");
            e.printStackTrace();
        } catch (RemoteException e) {
        	System.out.println("Impossibile ricevere la chiave pubblica.");
        }
    }
    
    public BigInteger[] codifyMsg(String Msg, BigInteger[] Key) throws RemoteException{
    	 BigInteger[] NumberedMsg = new BigInteger[Msg.length()];
    	BigInteger[] CodifiedMsg = new BigInteger[Msg.length()];
    	NumberedMsg = MsgToArray(Msg); // Questa funzione trasforma il messaggio in stringa in un array di big integer per la codifica
    	
    	for (int i = 0; i < Msg.length(); i++)
    		CodifiedMsg[i] = NumberedMsg[i].modPow(Key[1], BigInteger.ONE).mod(Key[0]); 
    	
    	System.out.println("Messaggio codificato correttamente");
    	return CodifiedMsg;
    }
    
    //trasforma la stringa Msg in un array di bigInteger
    public BigInteger[] MsgToArray(String Msg) {
    	BigInteger[] array = new BigInteger[Msg.length()];
    	int[] intArray = new int[Msg.length()];
  	
    	for (int i = 0; i < Msg.length(); i++){	// da stringa ad array di int con corrisp codice ASCII
            intArray[i] = Msg.charAt(i);
        }
    	
    	for (int i = 0; i < Msg.length(); i++) { // da array di int ad array di BigInteger
    		array[i] = BigInteger.valueOf(intArray[i]);
    	}
    	
    	System.out.println("L'array è il seguente\n");
    	for(int i = 0; i < Msg.length(); i++)
    		System.out.println(array[i]);
    	
    	return array;
    }
    
    
    public void newFileMsg() throws RemoteException {
		String path = "listaMsg.txt";

		try {
			File file = new File(path);

			if (file.exists())
				System.out.println("Il file " + path + " esiste");
			else if (file.createNewFile())
				System.out.println("Il file " + path + " è stato creato");
			else
				System.out.println("Il file " + path + " non può essere creato");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
