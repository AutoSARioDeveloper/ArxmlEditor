/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for handler of SAXParser
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {
	private DataSaver dataSaver = null;
	private final String returnnull = null;
	private Locator locator;

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		this.locator = locator;
		this.locator.getSystemId();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		try {
			dataSaver = DataSaver.getInstance();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		if (qName.equals("AUTOSAR")) {
			String loc = atts.getValue("xsi:schemaLocation");
			if (loc == null) {
				throw new SAXException();
			}
			String[] schemaLoc = loc.split(" ");
			if (dataSaver.ContainsSchema(schemaLoc[1])) {
				dataSaver.setSchemaLocation(schemaLoc[1]);
			} else {
				dataSaver.setSchemaLocation(returnnull);
				throw new SAXException();
			}

		} 
	}
}
