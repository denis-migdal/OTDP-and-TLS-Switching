package trusted;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLEngine;
import shared.Introspection;
import shared.TLSSessionInfo;

public class HandshakerCallback {
	
	private Object handshaker;
	private Object engine;
	
	private TLSSessionInfo sessionInfo = new TLSSessionInfo();

	public HandshakerCallback(SSLEngine engine) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		handshaker = Introspection.extract(engine, "handshaker");
		if(handshaker == null)
			throw new RuntimeException("Introspection failed !");
		
		this.engine = engine;
		
		Introspection.set(handshaker, "_before_newReadMAC", before_newReadMAC() );
		Introspection.set(handshaker, "_before_newReadCipher", before_newReadCipher() );
		/*Introspection.set(engine, "_after_changeReadCiphers", after_changeReadCiphers() );/**/
		

		Introspection.set(handshaker, "_before_newWriteMAC", before_newWriteMAC() );
		Introspection.set(handshaker, "_before_newWriteCipher", before_newWriteCipher() );
		/*Introspection.set(engine, "_after_changeWriteCiphers", after_changeWriteCiphers() );/**/
	}
	
	
	public Runnable before_newReadMAC() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					sessionInfo.extractReadMAC(handshaker);
					
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
	}
	
	public Runnable before_newReadCipher() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					
					sessionInfo.extractReadCipher(handshaker);
					
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
	}
	
	
	

	public Runnable after_changeReadCiphers() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					sessionInfo.updateEngineReadCiphers(engine);
					
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | NoSuchAlgorithmException e) {
					e.printStackTrace();//TODO
				}
			}
		};
		
	}
	
	
	
	public Runnable before_newWriteMAC() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					sessionInfo.extractWriteMAC(handshaker);
					
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
	}
	
	public Runnable before_newWriteCipher() {
		
		return new Runnable() {
			
			@Override
			public void run() {

				try {
					
					sessionInfo.extractWriteCipher(handshaker);
					
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
	}
	
	public Runnable after_changeWriteCiphers() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				
				
				try {
					
					sessionInfo.updateEngineWriteCiphers(engine);
					
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | NoSuchAlgorithmException e) {
					e.printStackTrace();//TODO
				}
			}
		};
		
	}
	
	public TLSSessionInfo getSessionInfo() {
		return sessionInfo;
	}
}
