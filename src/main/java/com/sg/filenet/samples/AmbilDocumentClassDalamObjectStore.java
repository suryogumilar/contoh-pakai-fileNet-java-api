package com.sg.filenet.samples;

import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.collection.EngineCollection;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.UserContext;

public class AmbilDocumentClassDalamObjectStore {
	private static Logger logger = LogManager.getLogger(ProsedurKoneksiKeFilenet.class.getName());
	private static String url ="http://192.168.43.216:9080/wsi/FNCEWS40MTOM/";
	private static String user ="p8admin";
	private static String password ="d1tk4p3l2018";
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
		
		String sql = "SELECT * FROM ClassDefinition";
		
		while(objectStoreIterator.hasNext()) {
			ObjectStore objectStore = objectStoreIterator.next();
			logger.debug("object store name : "+objectStore.get_Name()+" mempunyai dokumen class : " );
			Iterator<?> iterator = CEQueryAsObjects(sql, 10, objectStore);
			while (iterator.hasNext()) {
				ClassDefinition def = (ClassDefinition) iterator.next();
				logger.debug("dokumen class : "+def.get_Name()+"\n\t\tsymbolic Name  : "+def.get_SymbolicName());
				
			}
		}
		
		//logout dan disconnect dari API Filenet
		UserContext.get().popSubject();
	}
	public static Iterator<?> CEQueryAsObjects(String cequery, Integer fnetPageSize, ObjectStore objStore) {
		EngineCollection rowSet = fetchObjects(cequery, fnetPageSize, objStore);
		Iterator<?> iter = rowSet.iterator();
		return iter;
	}

	private static EngineCollection fetchObjects(String cequery, Integer fnetPageSize, ObjectStore objStore) {
		SearchScope searchScope = new SearchScope(objStore);
		SearchSQL sqlObject = new SearchSQL(cequery);
		IndependentObjectSet fetchObjects = searchScope.fetchObjects(sqlObject, fnetPageSize, null, true);
		EngineCollection rowSet = fetchObjects;
		return rowSet;
	}
}
