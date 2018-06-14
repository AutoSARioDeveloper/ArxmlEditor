/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for making element paths
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EditorTextDOM {
	DocumentBuilder parser = null;
	DataSaver dataSaver = null;
	List<Document> document = null;
	static EditorTextDOM editorTextDOM = null;

	private EditorTextDOM() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			dataSaver = DataSaver.getInstance();
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		document = new ArrayList<Document>();
	}

	public static EditorTextDOM getInstance() {
		if (editorTextDOM == null) {
			editorTextDOM = new EditorTextDOM();
		}
		return editorTextDOM;
	}

	public void read() {
		document.clear();
		List<URI> xmlFileList = dataSaver.getXMLfileList();
		try {
			document.add(parser.parse(new InputSource(new StringReader(SetupDocument.document.get()))));
			for (int i = 0; i < xmlFileList.size(); i++) {
				document.add(parser.parse(xmlFileList.get(i).toString()));
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public List<String> searchAndMakeString(String dest) {
		List<String> toProposal = new ArrayList<String>();
		for (int n = 0; n < document.size(); n++) {
			Element root = (Element) document.get(n).getDocumentElement();
			NodeList List = root.getElementsByTagName(dest);
			int len = List.getLength();
			for (int i = 0; i < len; i++) {
				Node temp = List.item(i);
				NodeList destChilden = temp.getChildNodes();
				List<String> nodes = new ArrayList<String>();
				String fullPath = new String("/");
				for (int j = 0; j < destChilden.getLength(); j++) {
					Node shortName = destChilden.item(j);
					if (shortName.getNodeType() != Node.ELEMENT_NODE)
						continue;
					Element esta = (Element) shortName;

					if (esta.getTagName().equals("SHORT-NAME")) {
						nodes.add(esta.getTextContent());
						break;
					}
				}

				while (temp.getParentNode() != null) {
					temp = temp.getParentNode();
					NodeList toSearchAncestorShortName = temp.getChildNodes();
					for (int j = 0; j < toSearchAncestorShortName.getLength(); j++) {
						Node a = toSearchAncestorShortName.item(j);
						if (toSearchAncestorShortName.item(j).getNodeType() != Node.ELEMENT_NODE)
							continue;
						Element shortName = (Element) a;
						if (shortName.getTagName().equals("SHORT-NAME")) {
							nodes.add(shortName.getTextContent());
							break;
						}
					}
				}
				for (int j = nodes.size() - 1; j >= 0; j--) {
					fullPath += nodes.get(j);
					if (j != 0)
						fullPath += "/";
				}
				toProposal.add(fullPath);
			}
		}
		return toProposal;
	}

}
