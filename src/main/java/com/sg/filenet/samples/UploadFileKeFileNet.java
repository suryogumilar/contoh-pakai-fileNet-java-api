package com.sg.filenet.samples;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.util.UserContext;
import com.filenet.api.core.Document;

public class UploadFileKeFileNet {
	private static Logger logger = Logger.getLogger(UploadFileKeFileNet.class.getName());
	private static String url = "http://192.168.1.187:9080/wsi/FNCEWS40MTOM/";
	private static String user = "p8admin";
	private static String password = "P4ssw0rd";
	private static String optionalJAASStanzaName = "FileNetP8WSI";
	private static String objectStoreName = "PROD-OBJS";
	private static String docClass = "Dokumen_Invoice";

	public static void main(String[] args) {
		Connection connection = Factory.Connection.getConnection(url);
		// authentikasi
		Subject sbjt = UserContext.createSubject(connection, user, password, optionalJAASStanzaName);
		UserContext userContext = UserContext.get();
		userContext.pushSubject(sbjt);

		Domain domain = Factory.Domain.fetchInstance(connection, null, null);

		ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);

		
		// kita buat terlebih dahulu entry document yang akan menampung kontent binary dari file yang akan diupload
		Document documentNew = Factory.Document.createInstance(objectStore, docClass);

		// isi metadata dokumen
		String fileName = "InvoiceABC";

		com.filenet.api.property.Properties ppProperties = documentNew.getProperties();
		ppProperties.putValue("Tanggal_Invoice", new Date());
		ppProperties.putValue("Nama_Dokumen", fileName); // nama dokumen bisa juga sebagai metadata 
		ppProperties.putValue("Author", "Suryo Gumilar"); // nama dokumen bisa juga sebagai metadata 
		// dst...

		ContentElementList ceList = Factory.ContentElement.createList();
		ContentTransfer ct = Factory.ContentTransfer.createInstance();
		
		// set filename yang akan digunakan supaya bisa diquery
		ct.set_RetrievalName(fileName);

		InputStream in;
		try {
			in = new FileInputStream("C:/pathdokumen_dalam_lokal_machine/invoiceAbc.txt");
		} catch (FileNotFoundException e) {
			return;
		}
		BufferedInputStream bis = new BufferedInputStream(in);
		bis.mark(0);
		try {
			bis.reset();
		} catch (IOException e) {
			return;
		}
		ct.setCaptureSource(bis);
		ceList.add(ct);

		documentNew.set_ContentElements(ceList);

		// input ke dms
		documentNew.checkin(null, CheckinType.MAJOR_VERSION);

		// commit
		documentNew.save(RefreshMode.REFRESH);

		// untuk simpan ke dalam folder kita ambil terlebih dahulu foldernya yang
		// terdapat dalam FileNet Content Engine

		Folder folderDalamFileNet = null;
		String folderName = "/dokumen/invoice_folder/";
		folderDalamFileNet = Factory.Folder.fetchInstance(objectStore, folderName, null);

		ReferentialContainmentRelationship folderFiling = folderDalamFileNet.file(documentNew,
				AutoUniqueName.AUTO_UNIQUE, fileName, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		folderFiling.save(RefreshMode.REFRESH);
		folderDalamFileNet.save(RefreshMode.REFRESH);

		// logout dan disconnect dari API Filenet

		UserContext.get().popSubject();
	}
}
