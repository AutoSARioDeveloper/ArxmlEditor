/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for searching Schema
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.wst.xml.core.contentmodel.modelquery.IExternalSchemaLocationProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.xml.sax.SAXException;

public class ExternalSchemaLocationProvider implements IExternalSchemaLocationProvider {

	DataSaver dataSaver = null;
	SAXFactory saxFactory = null;
	SchemaParsingDOM addShortNameDOM = null;

	public ExternalSchemaLocationProvider() throws ParserConfigurationException, SAXException {
		dataSaver = DataSaver.getInstance();
		saxFactory = SAXFactory.getInstance();
		addShortNameDOM = SchemaParsingDOM.getInstance();
		Bundle bundle = FrameworkUtil.getBundle(DataSaver.class);
		Enumeration<String> et = bundle.getEntryPaths("ARXML_Schema");
		while (et.hasMoreElements()) {
			String SchemaPath = et.nextElement();
			String[] Schemas = SchemaPath.split("/");
			dataSaver.SetSchemaSet(Schemas[1]);
		}
	}

	@Override
	public Map<String, String> getExternalSchemaLocation(URI fileURI) {
		// TODO Auto-generated method stub
		String loc = new String();
		Map<String, String> map = new HashMap<String, String>();
		try {
			saxFactory.read(SetupDocument.document.get());
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		
		loc = dataSaver.getSchemaLocation();
		if (!dataSaver.ContainsSchema(loc)) {
			loc = "AUTOSAR_00045.xsd";
		}
		String SchemaFullLoc = Header.FILE_LOCATION + loc;
		dataSaver.setCurrentSchemaLoc(SchemaFullLoc);
		String organize = Header.AUTOSAR_SCHEMA_KEY + " " + SchemaFullLoc;
		if (!dataSaver.getCurrentSchema().equals(loc) && dataSaver.ContainsSchema(loc)) {
			dataSaver.setCurrentSchema(loc);
			addShortNameDOM.run();
		}
		map.put(IExternalSchemaLocationProvider.SCHEMA_LOCATION, organize);
		return map;
	}

}
