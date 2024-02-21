package astManager;

/**Class that contains the location of a particular node, in the form of the keys used to access
 * it from the astStorage object in ASTHandler.
 * @author Jacob Botha
 *
 */
public class Breadcrumb {
	Double versionNo;
	String classID, packageID, methodID;
	
	public Breadcrumb(Double versionNo, String packageID, String classID, String methodID) {
		this.versionNo = versionNo;
		this.classID = classID;
		this.packageID = packageID;
		this.methodID = methodID;
	}
	/**
	 * @return the projectID
	 */
	public String getClassID() {
		return classID;
	}
	/**
	 * @return the methodID
	 */
	public String getMethodID() {
		return methodID;
	}
	/**
	 * @return the packageID
	 */
	public String getPackageID() {
		return packageID;
	}
	/**
	 * @return the versionNo
	 */
	public Double getVersionNo() {
		return versionNo;
	}

}
