package server;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import java.io.File;
import java.io.FileOutputStream;


import java.io.PrintWriter;
import java.nio.CharBuffer;


import javax.rmi.PortableRemoteObject;

/**
 * Classe che implementa il codice del server di autenticazione dual-export.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 */
public class AuthServerImpl implements AuthServer {

    private MainServerIIOP persRef;
    private MainServerJRMP adminRef;
    

    public AuthServerImpl(Remote ms) throws RemoteException {
        UnicastRemoteObject.exportObject(this, 20000);
        System.out.println("Auth server up!!");
        PortableRemoteObject.exportObject(this);
        persRef = (MainServerIIOP)ms;
        adminRef = (MainServerJRMP)ms;
      
    }

    public MarshalledObject<Serializable> login(String username, char[] password)
            throws RemoteException, ClassNotFoundException {
      
        try{
        	
	        if (isUser(username,password)) {
	            IIOPClient mc = new IIOPClientImpl(persRef);
	            return new MarshalledObject<Serializable>(mc);
	        }
	        
	        else if(isAdmin(username,password)){
	            JRMPClient ac = new JRMPClientImpl(adminRef);
	            return new MarshalledObject<Serializable>(ac);
	        }
	        
	        else{
	            System.out.println("Login fallito.");
	            return null;
	        }
	        
        }catch(IOException e){
        	System.out.println("Login fallito.");
            e.printStackTrace();
            return null;
        }
       
    }

    public boolean registerUser(String nome, char[] password, int type) throws RemoteException, IOException {

        String StringPassword = CharBuffer.wrap(password).toString();
        
        if(type == 0) {
		    newFileUser();
            
		    //se esiste listaUser.txt fa la write sul file di testo
		
	    	if (newUser(nome, password)){
		    	//scrivi nome password sul file di testo
                try {
		        	FileOutputStream fos = new FileOutputStream (".listaUser.txt", true); // append
		        	PrintWriter pw = new PrintWriter (fos);

		        	pw.print (nome+"\n"+StringPassword+"\n");
		        	pw.close ();
    
                } catch (FileNotFoundException e) {
		    	    System.out.println("Non ho trovato il file");
                    return false;
                }
		    	
                return true;
		    }
		    else {
                return false;		    	
                
            }        
        }
		
        else if(type == 1) {
		    newFileAdmin();

		  //se esiste listaAdmin.txt fa la write sul file di testo
		
	    	if (newAdmin(nome, password)){
		    	//scrivi nome password sul file di testo
                try {
		    	FileOutputStream fos = new FileOutputStream (".listaAdmin.txt", true); // append
		    	PrintWriter pw = new PrintWriter (fos);
                
                System.out.println("La password è :" + StringPassword);
		    	pw.print (nome+"\n"+StringPassword+"\n");
		    	pw.close ();
                }catch (FileNotFoundException e) {
		    	    System.out.println("Non ho trovato il file");
                    return false;
                }
		    	System.out.println("ohi grandioso sei registrato! ma non ti sei loggato");
                return true;
		    }
		    else {
                System.out.println("esiste già un nome così nel registro!");
                return false;
            }          
        }
        return false;
	}

    // Se il file non esiste, lo crea. 
	public void newFileUser() throws RemoteException {
		String path = ".listaUser.txt";

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

	// Se il file non esiste, lo crea.
    public void newFileAdmin() throws RemoteException {
		String path = ".listaAdmin.txt";

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

	// ritorna true se il nomeCercato non è preente nel file listaAdmin, false altrimenti
    public boolean newAdmin(String nomeCercato, char[] passwordCercata) throws IOException {
		String name = " ";
		

		FileReader f;
		f=new FileReader(".listaAdmin.txt");

		BufferedReader b;
		b=new BufferedReader(f);


		while(name != null ){

			name=b.readLine();
			b.readLine();

			if(nomeCercato.equals(name)) {
				b.close();
				f.close();
				return false;
			}
        
		}
		b.close();
		f.close();
        return true;
    }

    // ritorna true se il nomeCercato non è preente nel file listaUser, false altrimenti
    public boolean newUser(String nomeCercato, char[] passwordCercata) throws IOException {
		String name = " ";
		
		FileReader f;
		f=new FileReader(".listaUser.txt");

		BufferedReader b;
		b=new BufferedReader(f);


		while(name != null ){

			name=b.readLine();
			b.readLine();

			if(nomeCercato.equals(name)) {
				b.close();
				f.close();
				return false;
			}
        
		}
		b.close();
		f.close();
        return true;
    }

    //ritorna true se il nome utente è presente nel file listaAdmin, false altrimenti
    public boolean isAdmin(String nomeCercato, char[] passwordCercata) throws IOException {
		String name = " ";
		String password="";
        String givenPassword = CharBuffer.wrap(passwordCercata).toString();

		FileReader f;
		f=new FileReader(".listaAdmin.txt");

		BufferedReader b;
		b=new BufferedReader(f);


		while(name != null ){

			name=b.readLine();
			password=b.readLine();
           
			if(nomeCercato.equals(name) && givenPassword.equals(password)) {
				b.close();
				f.close();
				return true;
			}

			else if(nomeCercato.equals(name) && !givenPassword.equals(password)){
                System.out.println("password errata");		
			}

		}
		b.close();
		f.close();
        return false;
    }

  //ritorna true se il nome utente è presente nel file listaUser, false altrimenti
	public boolean isUser(String nomeCercato, char[] passwordCercata) throws IOException {
		String name = " ";
		String password="";
        String given2Password = CharBuffer.wrap(passwordCercata).toString();
        try {
		    FileReader f;
		    f=new FileReader(".listaUser.txt");

		    BufferedReader b;
		    b=new BufferedReader(f);
        
			while(name != null ){
	
				name=b.readLine();
				password=b.readLine();
	
				if(nomeCercato.equals(name) && given2Password.equals(password)) {
					b.close();
					f.close();
					return true;
				}
				else if(nomeCercato.equals(name) && !given2Password.equals(password)){
	                System.out.println("password errata");	
				   
				}
	
			}

			b.close();
			f.close();
		} catch (FileNotFoundException e) {
	        System.out.println("File non trovato");
	    }
		return false;
	}
}
