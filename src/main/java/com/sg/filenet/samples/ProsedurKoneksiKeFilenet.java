package com.sg.filenet.samples;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
public class ProsedurKoneksiKeFilenet {
	private static Logger logger = LogManager.getLogger(ProsedurKoneksiKeFilenet.class.getName());
	private static String url ="http://192.168.1.187:9080/wsi/FNCEWS40MTOM/";
	private static String user ="p8admin";
	private static String password ="P4ssw0rd";
	private static String optionalJAASStanzaName ="FileNetP8WSI";
	
	public static void main(String[] args) {
		
		
		Connection connection = Factory.Connection.getConnection(url);
		// authentikasi 
		Subject sbjt = UserContext.createSubject(connection, user, password, optionalJAASStanzaName);
		UserContext userContext = UserContext.get();
		userContext.pushSubject(sbjt);
		
		Domain domain = Factory.Domain.fetchInstance(connection, null, null);
		
		// mengambil object store yang ada dalam content engine
		
		ObjectStoreSet objectStores = domain.get_ObjectStores();
		Iterator<ObjectStore> objectStoreIterator = objectStores.iterator();
		while(objectStoreIterator.hasNext()) {
			ObjectStore objectStore = objectStoreIterator.next();
			logger.debug("object store name : "+objectStore.get_Name());
			
		}
		
		//logout dan disconnect dari API Filenet
		UserContext.get().popSubject();
	}
}
