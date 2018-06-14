/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for making SAX factory
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SAXFactory {
	static SAXFactory saxFactory = null;
	SAXParserFactory parserFactory=null;
	SAXParser parser =null;
	
	private SAXFactory() throws ParserConfigurationException, SAXException{
		parserFactory = SAXParserFactory.newInstance();
		parser = parserFactory.newSAXParser();
	}
	
	public static SAXFactory getInstance() throws ParserConfigurationException, SAXException{
		if (saxFactory == null){
			saxFactory = new SAXFactory();
		}
		return saxFactory;
	}
	
	public void read(String string) throws IOException, SAXException{
		if(parser != null){
			SAXHandler saxhandler = new SAXHandler();
			parser.parse(new InputSource(new StringReader(string)), saxhandler);	
		}
	}
}
