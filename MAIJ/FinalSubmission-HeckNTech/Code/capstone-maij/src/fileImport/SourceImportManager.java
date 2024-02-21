/**
 * 
 */
package fileImport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import astManager.ASTHandler;

/**
 * Class used to handle importing projects into the program
 * 
 * @author Jacob Botha
 *
 */

public class SourceImportManager {
	private SortedMap<Double, PendingImport> pendingImports;
	private PendingImport selectedImport;

	/**
	 * Keeps track of the project versions to import
	 */
	public SourceImportManager() {
		pendingImports = new TreeMap<Double, PendingImport>();
		selectedImport = null;
	}
	
	/**Adds the selected Import to the pending imports as version number versionNo.
	 * does nothing if no potential import is selected.
	 * @param versionNo
	 */
	public void addVersion(Double versionNo) {
		if(this.selectedImport == null) {
			return;
		}
		PendingImport v = selectedImport;
		while(v != null) {
			v = this.pendingImports.put(versionNo, v);
			versionNo++;
		}
	}
	
	/** Parses the imports into AST
	 * @param astHandler
	 * @return an ASTHandler object which contains the parsed imports.
	 */
	public ASTHandler confirmImport() {
		return new ASTHandler(pendingImports);
	}
	
	/**Gives the list of .java file paths included in the currently selected directory
	 * @return list of paths to .java file under current directory
	 */
	public List<Path> getJavaFiles(){
		return selectedImport.getJavaFiles();
	}
	
	/**Give the currently pending versions and their root directories
	 * @return map of source directory with version numbers
	 */
	public SortedMap<Double, Path> getPendingVersions(){
		SortedMap<Double, Path> returnMap = new TreeMap<Double, Path>();
		for(Double versionNo : this.pendingImports.keySet()) {
			returnMap.put(versionNo, this.pendingImports.get(versionNo).getSelectedDirectory());
		}
		return returnMap;
	}
	
	/**
	 * @return the selectedImport
	 */
	public PendingImport getSelectedImport() {
		return selectedImport;
	}
	
	
	/**Removes the version with the given number if it exists
	 * @param versionNo
	 */
	public void removeVersion(Double versionNo) {
		this.selectedImport = pendingImports.remove(versionNo);
	}
	
	/**Selects a version already added to the pending imports
	 * @param versionNo
	 */
	public void selectImport(Double versionNo) {
		this.selectedImport = this.pendingImports.get(versionNo);
	}

	/**Select a directory to potentially add to the imports
	 * @param directory
	 * @throws IOException
	 */
	public void selectImport(String directory) throws IOException {
		this.selectedImport = new PendingImport(directory);
	}
	
}
