package com.sg.filenet.samples;

import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.UserContext;

public class UpdateMetadataDokumen {
	private static String url = "http://192.168.1.187:9080/wsi/FNCEWS40MTOM/";
	private static String user = "p8admin";
	private static String password = "P4ssw0rd";
	private static String optionalJAASStanzaName = "FileNetP8WSI";
	private static String objectStoreName = "PROD-OBJS";
	private static Logger logger = Logger.getLogger(UpdateMetadataDokumen.class.getName());
	private static String documentClass = "Dokumen_Invoice";

	public static void main(String[] argd) {
		Connection connection = Factory.Connection.getConnection(url);
		// authentikasi
		Subject sbjt = UserContext.createSubject(connection, user, password, optionalJAASStanzaName);
		UserContext userContext = UserContext.get();
		userContext.pushSubject(sbjt);

		Domain domain = Factory.Domain.fetchInstance(connection, null, null);

		ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);

		logger.info("object store name : " + objectStore.get_DisplayName());

		String sql1 = "SELECT * FROM " + documentClass
				+ " WHERE Nama_Dokumen= 'InvoiceABC.pdf' Author = 'Suryo Gumilar' ";
		Iterator<RepositoryRow> ceQueryIterator = CEQuery(sql1, objectStore);
		while (ceQueryIterator.hasNext()) {

			RepositoryRow repRow = ceQueryIterator.next();
			Document document = getDocumentFromRepoRow(repRow);

			Properties properties = document.getProperties();
			properties.putObjectValue("Author", "Gumilar Suryo");
			// commit
			document.save(RefreshMode.REFRESH);
		}
		// logout dan disconnect dari API Filenet

		UserContext.get().popSubject();
	}

	public static Iterator<RepositoryRow> CEQuery(String cequery, ObjectStore objStore) {
		SearchScope searchScope = new SearchScope(objStore);
		SearchSQL sqlObject = new SearchSQL(cequery);
		RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, 10, null, true);

		@SuppressWarnings("unchecked")
		Iterator<RepositoryRow> iter = rowSet.iterator();
		return iter;
	}

	public static Document getDocumentFromRepoRow(RepositoryRow repRow) {
		Properties properties = repRow.getProperties();
		Property property = properties.get("This");
		Document doc = null;

		if (property == null) {
			Iterator<?> iterator = properties.iterator();
			while (iterator.hasNext()) {
				Property prop = (Property) iterator.next();
				Object objectValue = prop.getObjectValue();

				if (objectValue instanceof Document) {
					doc = (Document) objectValue;
				}
			}

		} else {
			doc = (Document) property.getObjectValue();
		}
		return doc;
	}
}
