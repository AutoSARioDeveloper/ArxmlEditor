/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for header of default string and bundle
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class Header {
	private static final Bundle bundle = FrameworkUtil.getBundle(DataSaver.class);
	public static final String FILE_LOCATION = new String(bundle.getEntry("ARXML_Schema").toString());;
	public static final String AUTOSAR_SCHEMA_KEY = "http://autosar.org/schema/r4.0";	
}
