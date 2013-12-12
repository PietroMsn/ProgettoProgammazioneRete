package server;


import java.io.IOException;

import java.math.BigInteger;

import java.net.InetAddress;

import java.net.UnknownHostException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;

import javax.swing.ImageIcon;

import javax.swing.JOptionPane;


/**
 * Classe che implementa il codice del client. Viene spedita al client al
 * momento del login.
 * 
 * @author Pietro Musoni
 * @author Carlo Tacchella
 */
public class IIOPClientImpl implements IIOPClient {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String myAddress;
	//private String path = "/tmp/";
	//private static final long serialVersionUID = -5477996558315185651L;
	private MainServerClient mainServIIOP;
	public IIOPClient myStub;
	
	public static BigInteger[] ArrayPrimi = new BigInteger[2];


	public IIOPClientImpl(MainServerClient ms) throws RemoteException,
	ClassNotFoundException {
		mainServIIOP = ms;
	}

	public void act() throws RemoteException {
		myStub = (IIOPClient) UnicastRemoteObject.exportObject(this,36000);
		try {
			myAddress = InetAddress.getLocalHost().getHostAddress();

			register();
			finestra_opzioni();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	private void register() throws RemoteException {
		try {
			
			MarshalledObject mo = new MarshalledObject(myStub);
			mainServIIOP.addClient(myAddress, mo);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String getAddress() throws RemoteException {
		return myAddress;
	}

	public BigInteger getKey() throws RemoteException {

		long startTime = System.currentTimeMillis(); 

		BigInteger n= getRandomBigInteger(2048);

		boolean check=true;

		while(check){
			if(!isPrime(n)){		
				n=n.add(BigInteger.ONE);
				//System.out.println(n);
			}
			else 
				check=false;
		}


		long endTime = System.currentTimeMillis(); 
		System.out.println("ho calcolato la chiave in " + (endTime - startTime)/1000 +" s.");

		return n;	
	}

	private static BigInteger getRandomBigInteger( int bitLength){

		SecureRandom rnd = new SecureRandom();

		BigInteger randomNum=BigInteger.ZERO;

		BigInteger tanto= BigInteger.valueOf(10).pow(100);

		//finchè il numero random è inferiore a 1000 ricalcolo
		while(randomNum.compareTo(tanto)==-1){
			randomNum=new BigInteger(bitLength, rnd);
		}
		return randomNum;
	}

	//passiamo al client JRMP i due numeri primi (biginteger)
	public BigInteger[] getArrayPrimi() throws RemoteException{
		try{
			
			ArrayPrimi[0].compareTo(BigInteger.ONE);
			return ArrayPrimi;

		}catch(NullPointerException e){

			return null;

		}
		
	}



	//dato un numero ritorno true se è primo
	static boolean isPrime( BigInteger n){
		return n.isProbablePrime(100);
	}

	public void finestra_opzioni() throws RemoteException {

		//String	serverIP = System.getenv("SERVERIP");
		//String codebase = "http://" + serverIP + ":8000/";
		
		String scelta;
		int s = 0;
		
			while (s != 3) {

				

				scelta = (String) JOptionPane.showInputDialog(null,
						"MENU:\n"
								+ "1) Dai disponibilità di calcolo\n"
								+ "2) Stampa lista Client\n"
								+ "3) Log out", "Menu Admin",
								JOptionPane.INFORMATION_MESSAGE, new ImageIcon(),
								null, null);
				try {
					s = Integer.parseInt(scelta);
				} catch (NumberFormatException nfe) {
					s = 4;
				}
				switch (s) {
					case 1: {
						
	
						int cont=0;
	
						while (cont==0){
							try{
	
								
								ArrayPrimi[0]= getKey();
								ArrayPrimi[1]= getKey();
								try {
									mainServIIOP.scriviChiave(ArrayPrimi);
								} catch (IOException e) {
				
									e.printStackTrace();
								}
								//se esegue questo codice non ci sono buchi nell'array
								System.out.println("L'array è pieno");
								cont=1;
								
								try{
	
									
									Thread.currentThread().sleep(200000000);
								}catch(InterruptedException ie){
								}
	
							}catch(NullPointerException e){
								System.out.println("è uguale a null");
								cont=0;
								
								
								ArrayPrimi[0]= getKey();
								ArrayPrimi[1]= getKey();
							}
						}
	
					/*while(mainServIIOP.counter < 2) {
		                    	mainServIIOP.Key[mainServIIOP.counter] = getKey();
		                    	mainServIIOP.counter++;
		                    }*/
	
					System.exit(0);	               
	
					break;
					}
				case 2: { 
					
	
					break;
				}
				case 3: {
	
					System.exit(0);
					break;
				}
				default: {
	
					scelta = null;
					JOptionPane.showMessageDialog(null,
							"Scelta non valida", "Errore",
							JOptionPane.ERROR_MESSAGE, new ImageIcon());
				}
			}
	

		}
	}
}

