/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for input lines or <SHORT-NAME>tag
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.xml.sax.SAXException;

public class InputLinesThd extends Thread {
	boolean isRunning = true;
	DataSaverThd thd_dataSaver = null;
	DataSaver dataSaver = null;
	private static InputLinesThd thd_inputlines = null;
	private InputLinesThd() {
		
	}
	public static InputLinesThd getInstance() {
		if(thd_inputlines == null) {
			thd_inputlines = new InputLinesThd();
		}
		return thd_inputlines;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		thd_inputlines = this;
		thd_dataSaver = DataSaverThd.getInstance();
		try {
			dataSaver = DataSaver.getInstance();
		} catch (ParserConfigurationException | SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (isRunning) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				IDocument document = SetupDocument.document;
				if (thd_dataSaver.needToAddLine || thd_dataSaver.needToShortname) {
					//document.removeDocumentListener(SetupDocument.documentValidator);
					if (thd_dataSaver.needToAddLine && thd_dataSaver.getOffset() != -1
							&& thd_dataSaver.getLineOffset() != -1) {
						thd_dataSaver.setdonotneedAddLine();
						try {
							document.replace(thd_dataSaver.lineoffset, 0, addLine(thd_dataSaver.off));

						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (thd_dataSaver.needToShortname && thd_dataSaver.getOffset() != -1
							&& thd_dataSaver.getLineOffset() != -1) {
						thd_dataSaver.setdonotneedShortname();
						try {
							document.replace(thd_dataSaver.lineoffset, 0, makeString(thd_dataSaver.off));

						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					//document.addDocumentListener(SetupDocument.documentValidator);
				}
				// e.printStackTrace();
			}
		}
	}

	private String addLine(int offset) {
		final String base = "\n";
		String constructed = "\n";
		for (int i = 0; i < offset + 2; i++) {
			constructed += " ";
		}
		constructed += base;
		for (int i = 0; i < offset; i++) {
			constructed += " ";
		}
		return constructed;
	}

	private String makeString(int offset) {
		final String base = "<SHORT-NAME></SHORT-NAME>\n";
		String constructed = "\n";
		for (int i = 0; i < offset + 2; i++) {
			constructed += " ";
		}
		constructed += base;
		for (int i = 0; i < offset; i++) {
			constructed += " ";
		}
		return constructed;
	}
}
