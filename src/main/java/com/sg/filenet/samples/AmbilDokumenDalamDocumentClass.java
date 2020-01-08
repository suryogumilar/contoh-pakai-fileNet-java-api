package com.sg.filenet.samples;

import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.filenet.api.collection.EngineCollection;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.UserContext;

public class AmbilDokumenDalamDocumentClass {
	private static String url = "http://192.168.1.187:9080/wsi/FNCEWS40MTOM/";
	private static String user = "p8admin";
	private static String password = "P4ssw0rd";
	private static String optionalJAASStanzaName = "FileNetP8WSI";
	private static String objectStoreName = "PROD-OBJS";
	private static Logger logger = LogManager.getLogger(AmbilDokumenDalamDocumentClass.class.getName());

	public static void main(String[] args) {
		Connection connection = Factory.Connection.getConnection(url);
		// authentikasi
		Subject sbjt = UserContext.createSubject(connection, user, password, optionalJAASStanzaName);
		UserContext userContext = UserContext.get();
		userContext.pushSubject(sbjt);

		Domain domain = Factory.Domain.fetchInstance(connection, null, null);

		ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);

		// untuk mencari atau mengambil seluruh dokumen dalam suatu document class
		// digunakan query language

		String documentClass = "Invoice_Document";
		String cequery = "SELECT * FROM " + documentClass;

		Iterator<?> object = CEQueryAsObjects(cequery, 10, objectStore);
		while (object.hasNext()) {
			Object obj = object.next();
			if (obj instanceof Document) {
				Document doc = (Document) obj;
				Properties properties = doc.getProperties();
				logger.debug("doc:" + doc.get_Name()); // nama dokumen (metadata)
				Property dueDate = properties.get("TanggalInvoice"); // (metadata dokumen)
				Object value = dueDate.getObjectValue();
				logger.debug("doc:" + doc.get_Name() + " bertanggal " + value.toString());

			}
		}
		// logout dan disconnect dari API Filenet

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
