/**
 * 
 */
package fileImport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;

/**
 * Represents a project version pending importation into the program.
 * Keeps track of the .java and .jar files and the root directory.
 * @author Jacob Botha
 *
 */
public class PendingImport {
	private List<Path> javaFiles;
	private Path selectedDirectory;
	private List<Path> jarFiles;

	//remove comments method
	private static void removeComments(Node node) {
		for (Comment child : node.getAllContainedComments()) {
            child.remove();
        }
	}
	
	/** Extracts and stores all .jar and .java files beneath the root directory.
	 * @param dir The root directory to search under
	 * @throws IOException if there was a problem traversing the directory
	 */
	public PendingImport(String dir) throws IOException {
		selectedDirectory = Paths.get(dir);
//		CompilationUnit cu = JavaParser.parse(selectedDirectory);
//		removeComments(cu);
		extractFilesFromDirectory();
	}

	/**
	 * Gets all the .java and .jar files beneath a directory, ignoring other files
	 * Stores them in the s
	 * @throws IOException 
	 */
	private void extractFilesFromDirectory() throws IOException {
		FilesExtractorVisitor fileVisitor = new FilesExtractorVisitor();
		Files.walkFileTree(this.selectedDirectory, fileVisitor);
		this.jarFiles = fileVisitor.getJarFilePaths();
		this.javaFiles = fileVisitor.getJavaFilePaths();
	}

	/**
	 * @return list of .jar file paths
	 */
	public List<Path> getJarFiles() {
		return jarFiles;
	}

	/**
	 * @return list of .java file paths
	 */
	public List<Path> getJavaFiles() {
		return javaFiles;
	}

	/**
	 * @return path of the selected root directory
	 */
	public Path getSelectedDirectory() {
		return selectedDirectory;
	}
	
}
