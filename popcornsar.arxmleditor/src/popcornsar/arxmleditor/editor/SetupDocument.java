/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for calling document and validating
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.filebuffers.IDocumentSetupParticipantExtension;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SetupDocument implements IDocumentSetupParticipant, IDocumentSetupParticipantExtension {
	protected static DocumentValidator documentValidator = null;
	protected static IDocument document = null;
	protected static URI uri = null;
	DataSaver dataSaver = null;
	EditorTextDOM editorTextDOM = null;
	SAXFactory factory = null;
	IFile file = null;
	Thread ThdInputLines = null;
	static String previous_qname = "";
	static int previous_line = -1;
	int no = 0;
	
	public class DocumentValidator implements IDocumentListener {
		DataSaver dataSaver = null;
		EditorTextDOM editorTextDOM = null;
		DataSaverThd thd_DataSaver = null;
		IFile files;
		public DocumentValidator(IFile file) {
			try {
				dataSaver = DataSaver.getInstance();
				editorTextDOM = EditorTextDOM.getInstance();
			} catch (ParserConfigurationException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			thd_DataSaver = DataSaverThd.getInstance();
			files = file;
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			// TODO Auto-generated method stub
			document = event.getDocument();
			uri = files.getRawLocationURI();
		}

		@Override
		public void documentChanged(DocumentEvent event) {
			// TODO Auto-generated method stub			
			
			int offset = event.getOffset();
			String string_thisline = null;
			int lineoffset = -1;
			int off = 0;
			int lineno = -1;
			try {
				lineno = document.getLineOfOffset(offset);
				int length = document.getLineLength(lineno);
				int initoffset = document.getLineOffset(lineno);
				string_thisline = document.get(initoffset, length);
				lineoffset = initoffset;
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			if (string_thisline == null) {
				return;
			}
			dataSaver.setOffset(offset);
			try {
				while (document.getChar(lineoffset++) != '<')
					off++;
				while (document.getChar(lineoffset++) != '>');				;
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			DocumentBuilderFactory buildderfactory= DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = null;
			try {
				db = buildderfactory.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.setErrorHandler(new ErrorHandlerDOM());
			Document docXml = null;
		    try {
		    	docXml = db.parse(new InputSource( new StringReader( string_thisline )));
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return;
			}
		    Element element = docXml.getDocumentElement();
		    String qName = element.getTagName();
		    if (dataSaver.IsIdentifiable(qName) && !previous_qname.equals(qName) && previous_line != lineno  ) {
		    	previous_qname = qName;
		    	previous_line = lineno;
		    	thd_DataSaver.setneedShortname(lineoffset, off);
		    	ThdInputLines.interrupt();
		    	return;
		    }
		    else if (dataSaver.IsneedAddLine(qName)&& !previous_qname.equals(qName) && previous_line != lineno ) {
		    	previous_qname = qName;
		    	previous_line = lineno;
		    	thd_DataSaver.setneedAddLine(lineoffset, off);
		    	ThdInputLines.interrupt();	  
		    	return;
		    }
		    else {
		    	previous_qname = "";
		    	previous_line = -1;
		    }
		    no++;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void setup(IDocument document, IPath location, LocationKind locationKind) {
		// TODO Auto-generated method stub
		this.document = document;
		System.out.println();
		try {
			dataSaver = DataSaver.getInstance();
			factory = SAXFactory.getInstance();
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		if (locationKind == LocationKind.IFILE) {
			ThdInputLines = InputLinesThd.getInstance();
			if (!ThdInputLines.isAlive()) {
				ThdInputLines.start();
			}
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(location);
			documentValidator = new DocumentValidator(file);
			document.addDocumentListener(documentValidator);
		}
		
	}
	@Override
	public void setup(IDocument document) {
		// TODO Auto-generated method stub
	}
}
