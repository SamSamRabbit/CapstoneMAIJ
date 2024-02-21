/**
 * 
 */
package fileImport;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;

/**
 * Class to extract all the .java and .jar files contained within a directory tree
 * @author Jacob Botha
 *
 */
public class FilesExtractorVisitor extends SimpleFileVisitor<Path> {

	private List<Path> javaFilePaths;
	private List<Path> jarFilePaths;

	/**
	 * 
	 * SimpleFileVisitor that records .java and .jar file locations
	 */
	public FilesExtractorVisitor() {
		super();
		this.jarFilePaths = new ArrayList<Path>();
		this.javaFilePaths = new ArrayList<Path>();
	}

	/**
	 * @return the jList of .jar file paths
	 */
	public List<Path> getJarFilePaths() {
		return jarFilePaths;
	}

	/**
	 * Gives the collected .java file paths
	 * @return the list of .java file paths
	 */
	public List<Path> getJavaFilePaths() {
		return javaFilePaths;
	}
	
	//remove comment
		public static void removeComments(Node node) {
			for (Comment child : node.getAllContainedComments()) {
	            child.remove();
	        }
		}

	/* (non-Javadoc)
	 * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if(file.toString().toLowerCase().endsWith(".java")) {
			CompilationUnit cu = JavaParser.parse(file);
			removeComments(cu);
			System.out.println("this is remove comments thing: " + file);
			javaFilePaths.add(file);
		}
		if(file.toString().toLowerCase().endsWith(".jar")) {
			jarFilePaths.add(file);
		}
		return super.visitFile(file, attrs);
	}
}
