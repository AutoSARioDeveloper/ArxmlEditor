/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for Saving Data
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DataSaver {
	private static DataSaver datasaver = null;
	private String FileURI = null;
	private String SchemaLocation = null;
	private SAXFactory saxFactory = null;
	private int offset = 0;
	private int previous_line = -1;
	private String currentScheam = "loc";
	private String currentScheamloc = null;
	private Set<String> IdentifiableSet = null;
	private Set<String> needAddLineSet = null;
	private Set<String> SchemaSet = null;
	private List<URI> xmlfileList = null;
	
	private DataSaver() throws ParserConfigurationException, SAXException{
		FileURI = new String();
		SchemaLocation = new String();
		saxFactory = SAXFactory.getInstance();
		IdentifiableSet = new HashSet<String>();
		needAddLineSet = new HashSet<String>();
		SchemaSet = new HashSet<String>(); 
		xmlfileList = new ArrayList<URI>();
	}
	
	public static DataSaver getInstance() throws ParserConfigurationException, SAXException{
		if (datasaver == null){
			datasaver = new DataSaver();
		}
		return datasaver;
	}
	
	public void setFileURI(String arg){	FileURI = arg; }
	public String getFileURI(String arg){ return FileURI; }
	public void setSchemaLocation(String arg){ SchemaLocation = arg; }
	public String getSchemaLocation(){ return SchemaLocation; }
	public SAXFactory getSAXFactory(){ return saxFactory; }	
	public void setOffset(int data){offset = data;}
	public int getOffset(){return offset;}
	public void setCurrentSchema(String schema) {currentScheam = schema;}
	public String getCurrentSchema(){ return currentScheam;}
	public void setCurrentSchemaLoc(String schema) {currentScheamloc = schema;}
	public String getCurrentSchemaLoc(){ return currentScheamloc;}
	public void SetIdentifiableinit(){ IdentifiableSet = new HashSet<String>();}
	public void SetIdentifiableItems(String item){ IdentifiableSet.add(item);}
	public boolean IsIdentifiable(String item) { return IdentifiableSet.contains(item);}
	public void needAddLineSetinit(){ needAddLineSet = new HashSet<String>();}
	public void SetneedAddLineSet(String item){ needAddLineSet.add(item);}
	public boolean IsneedAddLine(String item) { return needAddLineSet.contains(item);}
	public Set<String> getneedline() { return needAddLineSet; }
	public void setPreviousLine(int lineno) { previous_line = lineno;}
	public int getPreviousLine () { return previous_line; }
	public void SetSchemaSet(String item){ SchemaSet.add(item);}
	public boolean ContainsSchema(String item) { return SchemaSet.contains(item);}
	public void clearXMLFileList() { xmlfileList.clear(); }
	public void addXMLFiles(URI fileuri) { xmlfileList.add(fileuri); }
	public List<URI> getXMLfileList() { return xmlfileList; }
	
}
