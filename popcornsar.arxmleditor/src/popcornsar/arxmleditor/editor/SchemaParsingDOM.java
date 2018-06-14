/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for parsing schema for adding lines or <SHORT-NAME> tag
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import java.io.IOException;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class SchemaParsingDOM {
	DocumentBuilder parser = null;
	DataSaver ds = null;
	Document document = null;
	static SchemaParsingDOM schemaParsingDOM = null;

	private SchemaParsingDOM() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			ds = DataSaver.getInstance();
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
		} catch (SAXException e) {
			// TODO Auto-generated catch block
		}
	}

	public static SchemaParsingDOM getInstance() {
		if (schemaParsingDOM == null) {
			schemaParsingDOM = new SchemaParsingDOM();
		}
		return schemaParsingDOM;
	}

	public void run() {
		if (ds.getCurrentSchemaLoc() != null) {
			try {
				document = parser.parse(ds.getCurrentSchemaLoc());
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				return;
			}
		}
		IdentifiableList();
	}

	private void IdentifiableList() {
		Element eRoot = document.getDocumentElement();
		NodeList SimpleTypeSet = eRoot.getElementsByTagName("xsd:simpleType");
		int items = SimpleTypeSet.getLength();
		Element identifiable = null;
		for (int i = 0; i < items; i++) {
			Element cheque = (Element) SimpleTypeSet.item(i);
			if (cheque.getAttribute("name").equals("IDENTIFIABLE--SUBTYPES-ENUM")) {
				identifiable = cheque;
				break;
			}
		}

		NodeList Identiers = identifiable.getElementsByTagName("xsd:enumeration");
		items = Identiers.getLength();
		ds.SetIdentifiableinit();
		for (int i = 0; i < items; i++) {
			Element Identifier = (Element) Identiers.item(i);
			ds.SetIdentifiableItems(Identifier.getAttribute("value"));
		}

		eRoot = document.getDocumentElement();
		SimpleTypeSet = eRoot.getElementsByTagName("xsd:element");
		items = SimpleTypeSet.getLength();
		int i;
		for (i = 0; i < items; i++) {
			Element cheque = (Element) SimpleTypeSet.item(i);
			if (cheque.getAttribute("maxOccurs").equals("1") && cheque.getAttribute("minOccurs").equals("0")
					&& cheque.getAttribute("type").equals("")) {
				if (!cheque.getAttribute("name").contains("-REF")&&!cheque.getAttribute("name").contains("-TREF")) {
					ds.SetneedAddLineSet(cheque.getAttribute("name"));
				}
				if (cheque.getAttribute("name").contains("-REFS")) {
					ds.SetneedAddLineSet(cheque.getAttribute("name"));
				}
			} else if (cheque.getAttribute("name").contains("-IREF")) {
				ds.SetneedAddLineSet(cheque.getAttribute("name"));
			}
		}
		eRoot = document.getDocumentElement();
		SimpleTypeSet = eRoot.getElementsByTagName("xsd:complexType");
		items = SimpleTypeSet.getLength();
		for (i = 0; i < items; i++) {
			Element cheque = (Element) SimpleTypeSet.item(i);
			NodeList needcheque = cheque.getElementsByTagName("xsd:attributeGroup");
			int amount = needcheque.getLength();
			if (amount == 1) {
				Element tempElement = (Element) needcheque.item(0);
				if (tempElement.getAttribute("ref").equals("AR:AR-OBJECT")) {
					ds.SetneedAddLineSet(cheque.getAttribute("name"));
				}
			}
		}
	}

}
