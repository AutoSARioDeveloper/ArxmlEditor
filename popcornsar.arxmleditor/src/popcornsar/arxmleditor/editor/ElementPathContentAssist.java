/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for showing contentassist
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public class ElementPathContentAssist implements ICompletionProposalComputer {
	EditorTextDOM editorTextDOM = null;
	List<String> pathList = null;
	Image image = null;
	DataSaver dataSaver = null;
	URI currentURI = null;

	@SuppressWarnings("unchecked")
	@Override
	public List<ICompletionProposal> computeCompletionProposals(CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		IProject project = getCurrentProject();
		List<URI> xmlfileList = dataSaver.getXMLfileList();

		IResource resource[] = null;
		try {
			IResource members[] = project.members();
			resource = members;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (SetupDocument.uri == null) {
			for (int i = 0; i < resource.length; i++) {
				if (resource[i].getFileExtension().equals("arxml")) {
					xmlfileList.add(resource[i].getRawLocationURI());
				}
			}
		}
		else {
			for (int i = 0; i < resource.length; i++) {
				if (resource[i].getFileExtension().equals("arxml")) {
					if (!SetupDocument.uri.equals(resource[i].getRawLocationURI())) {
						xmlfileList.add(resource[i].getRawLocationURI());
					}
				}
			}
		}

		editorTextDOM.read();
		dataSaver.clearXMLFileList();
		Node selectedNode = (Node) ContentAssistUtils.getNodeAt(context.getViewer(), context.getInvocationOffset());
		Element tag = null;
		if (selectedNode instanceof Element) {
			tag = (Element) selectedNode;
		} else if (selectedNode instanceof Text && selectedNode.getParentNode() instanceof Element) {
			tag = (Element) selectedNode.getParentNode();
		}
		if (tag == null || tag.getAttribute("DEST").isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		IDOMNode textNode = null;
		if (selectedNode instanceof Text) {
			textNode = (IDOMNode) selectedNode;
		} else {
			if (selectedNode.getChildNodes().getLength() == 1 && selectedNode.getChildNodes().item(0) instanceof Text) {
				// Cursor at the end of a text node
				textNode = (IDOMNode) selectedNode.getChildNodes().item(0);
			} else {
				// Cursor between two tags, no text node yet
			}
		}
		// Determine selected text and offsets

		String selectedText = null;
		int textNodeOffset = -1;
		int cursorOffsetWithinTextNode = -1;
		IDOMNode elementNode = null;
		if (textNode != null) {
			selectedText = textNode.getStartStructuredDocumentRegion().getText();
			textNodeOffset = textNode.getStartOffset();
			cursorOffsetWithinTextNode = context.getInvocationOffset() - textNodeOffset;
		} else {
			selectedText = "";
			textNodeOffset = context.getInvocationOffset();
			elementNode = (IDOMNode) selectedNode;
			int betweenoffset;
			for (betweenoffset = elementNode.getStartOffset(); betweenoffset < elementNode
					.getEndOffset(); betweenoffset++)
				try {
					if (context.getDocument().getChar(betweenoffset) == '>') {
						break;
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					return null;
				}
			cursorOffsetWithinTextNode = context.getInvocationOffset() - betweenoffset - 1;
		}
		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		if (cursorOffsetWithinTextNode < 0)
			return null;
		String searchPrefix = selectedText.substring(0, cursorOffsetWithinTextNode);
		pathList = editorTextDOM.searchAndMakeString(tag.getAttribute("DEST"));
		for (String searchResult : findPath(searchPrefix)) {
			proposals.add(new CompletionProposal(searchResult, textNodeOffset, selectedText.length(),
					searchResult.length(), image, null, null, null));
		}
		return proposals;
	}

	@Override
	public List<String> computeContextInformation(CompletionProposalInvocationContext arg0, IProgressMonitor arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	private static IProject getCurrentProject() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window.getActivePage();

		IEditorPart activeEditor = activePage.getActiveEditor();
		IProject project = null;
		if (activeEditor != null) {
			IEditorInput input = activeEditor.getEditorInput();

			project = input.getAdapter(IProject.class);
			if (project == null) {
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null) {
					project = resource.getProject();
				}
			}
		}
		return project;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sessionEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionStarted() {
		// TODO Auto-generated method stub
		try {
			dataSaver = DataSaver.getInstance();
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		String path = "/icons/XML_ContentAssist.gif";
		URL url = FileLocator.find(bundle, new Path(path), null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
		image = imageDesc.createImage();
		editorTextDOM = EditorTextDOM.getInstance();
	}

	private List<String> findPath(String searchPrefix) {
		List<String> result = new ArrayList<String>();
		for (String fullpath : pathList) {
			if (fullpath.startsWith(searchPrefix)) {
				result.add(fullpath);
			}
		}
		return result;
	}
}
