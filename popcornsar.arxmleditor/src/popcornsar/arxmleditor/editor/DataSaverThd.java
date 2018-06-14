/**
*
* ====================================================================================================
* (c) PopcornSAR Co.,Ltd. Team AUTOSAR IDE
* ==================================================================================================== 
* Description:
* This java File is for Saving Data of thread-running
* ====================================================================================================
* Developer: 
* Junnoh Lee: First and Overall code writing
* E-mail : jnlee@popcornsar.com
* ====================================================================================================
* 
*/
package popcornsar.arxmleditor.editor;

public class DataSaverThd {
	boolean needToAddLine = false;
	boolean needToShortname = false;
	int offset = -1;
	int off = -1;
	int lineoffset = -1;
	static DataSaverThd dataSaver = null;
	private DataSaverThd(){
		
	}
	public static DataSaverThd getInstance(){
		if(dataSaver == null)
			dataSaver = new DataSaverThd();
		return dataSaver;
	}
	public boolean is_needAddline(){
		return needToAddLine;
	}
	public void setneedAddLine(int lineoffset, int off){
		this.off = off;
		this.lineoffset = lineoffset;
		needToAddLine = true;
	}
	public void setdonotneedAddLine(){
		needToAddLine = false;
	}
	public void setneedShortname(int lineoffset, int off){
		this.off = off;
		this.lineoffset = lineoffset;
		needToShortname = true;
	}
	public void setdonotneedShortname(){
		needToShortname = false;
	}
	public int getOffset() {
		return this.off;
	}
	public int getLineOffset() {
		return this.lineoffset;
	}
}
