package com.sg.filenet.samples;

import javax.security.auth.Subject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
public class AmbilObjectStoreTertentu {
	private static Logger logger = LogManager.getLogger(AmbilObjectStoreTertentu.class.getName());
	private static String url = "http://192.168.1.187:9080/wsi/FNCEWS40MTOM/";
	private static String user = "p8admin";
	private static String password = "P4ssw0rd";
	private static String optionalJAASStanzaName = "FileNetP8WSI";
	private static String objectStoreName="PROD-OBJS";
	public static void main(String[] args) {
		Connection connection = Factory.Connection.getConnection(url);
		// authentikasi
		Subject sbjt = UserContext.createSubject(connection, user, password, optionalJAASStanzaName);
		UserContext userContext = UserContext.get();
		userContext.pushSubject(sbjt);

		Domain domain = Factory.Domain.fetchInstance(connection, null, null);

		ObjectStore objectStore =
				Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);
		
		logger.info("object store name : "+objectStore.get_DisplayName());
		// logout dan disconnect dari API Filenet

		UserContext.get().popSubject();
	}
}
