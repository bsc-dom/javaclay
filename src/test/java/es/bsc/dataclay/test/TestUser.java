
/**
 * @file TestUser.java
 * @date May 16, 2013
 * @author dgasull
 */

/**
 * @package testutils
 * @brief Module intended to provide utility functions to tests
 */
package es.bsc.dataclay.test;

import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;


/**
* @class TestUser
* @brief This class represents all the users in the test cases
* @author dgasull
*/
public enum TestUser {
	
	/**User Registrator **/
	MOLECULES_REG("/test_classes/dataservicetest/molecule/registrator/"),
	DS_BASIC_REG("/test_classes/dataservicetest/basic/registrator/"),
	DATA_REG("/test_classes/notunit/data/registrator/"),
	SPECIAL_REG("/test_classes/notunit/special/registrator/"),
	EXEC_REG("/test_classes/notunit/execution/registrator/"),
	LOGIC_REG("/test_classes/logictest/registrator/"),	
	CLASSMGR_REG("/test_classes/classmgrtest/registrator/"),
	SPEC_REG("/test_classes/spectest/registrator/"),
	NETTY_REG("/test_classes/nettytest/registrator/"),
	MGMNT_REG("/test_classes/notunit/management/registrator/"),
	PERFORMANCE_REG("/test_classes/performance/registrator/"),
	PERFORMANCE_REMOTE_REG("/test_classes/performance_remote/registrator/"),
	DOCKING_DEMO_REG("/test_classes/notunit/wasabidemo/registrator/"),
	STUB_REG("/test_classes/stubtest/registrator/"),
	COMPSS_REG("/test_classes/notunit/compss_demo/registrator/"),
	DBTEST_REG("/test_classes/dbhandlertest/registrator/"),
	NEURON_REG("/test_classes/notunit/neuron/registrator/"),
	COLLECTIONS_REG("/test_classes/notunit/collections/registrator/"),
	ASPECTS_REG("/test_classes/notunit/aspects/registrator/"),
	JAVA_IFACES_REG("/test_classes/notunit/java_ifaces/registrator/"),
	EMBEDDED_REG("/test_classes/notunit/embedded/registrator/"),
	RTTEST_REG("/test_classes/rttest/registrator/"),
	GARBAGE_COLLECTOR_REG("/test_classes/notunit/garbage_collector/registrator/"),
	VOLATILE_REG("/test_classes/notunit/volatile/registrator/"),
	IFACES_REG("/test_classes/notunit/ifaces/registrator/"),
	COMPSS_KMEANS_REG("/test_classes/notunit/kmeans/registrator/"),
	COMPSS_KMEANS_DC_REG("/test_classes/notunit/kmeans_dc/registrator/"),
	LIBRARIES_REG("/test_classes/notunit/libraries/registrator/"),
	PERSON_REG("/test_classes/persontest/registrator/"),
	MEMORY_REG("/test_classes/notunit/memory/registrator/"),
	NOTIFICATION_REG("/test_classes/notunit/notification/registrator/"),

	/**User Creators **/
	MOLECULES_CREATOR ("/test_classes/dataservicetest/molecule/creator/"),
	DS_BASIC_CREATOR("/test_classes/dataservicetest/basic/creator/"),
	DATA_CREATOR("/test_classes/notunit/data/creator/"),
	SPECIAL_CREATOR("/test_classes/notunit/special/creator/"),
	EXEC_CREATOR("/test_classes/notunit/execution/creator/"),
	LOGIC_CREATOR("/test_classes/logictest/creator/"),	
	CLASSMGR_CREATOR("/test_classes/classmgrtest/creator/"),
	SPEC_CREATOR("/test_classes/spectest/creator/"),
	NETTY_CREATOR("/test_classes/nettytest/creator/"),
	MGMNT_CREATOR("/test_classes/notunit/management/creator/"),
	PERFORMANCE_CREATOR("/test_classes/performance/creator/"),
	PERFORMANCE_REMOTE_CREATOR("/test_classes/performance_remote/creator/"),
	DOCKING_DEMO_CREATOR("/test_classes/notunit/wasabidemo/creator/"),
	STUB_CREATOR("/test_classes/stubtest/creator/"),
	COMPSS_CREATOR("/test_classes/notunit/compss_demo/creator/"),
	DBTEST_CREATOR("/test_classes/dbhandlertest/creator/"),
	NEURON_CREATOR("/test_classes/notunit/neuron/creator/"),
	COLLECTIONS_CREATOR("/test_classes/notunit/collections/creator/"),
	ASPECTS_CREATOR("/test_classes/notunit/aspects/creator/"),
	EMBEDDED_CREATOR("/test_classes/notunit/embedded/creator/"),
	RTTEST_CREATOR("/test_classes/rttest/creator/"),
	GARBAGE_COLLECTOR_CREATOR("/test_classes/notunit/garbage_collector/creator/"),
	VOLATILE_CREATOR("/test_classes/notunit/volatile/creator/"),
	IFACES_CREATOR("/test_classes/notunit/ifaces/creator/"),
	COMPSS_KMEANS_CREATOR("/test_classes/notunit/kmeans/creator/"),
	COMPSS_KMEANS_DC_CREATOR("/test_classes/notunit/kmeans_dc/creator/"),
	LIBRARIES_CREATOR("/test_classes/notunit/libraries/creator/"),
	PERSON_CREATOR("/test_classes/persontest/creator/"),
	MEMORY_CREATOR("/test_classes/notunit/memory/creator/"),
	NOTIFICATION_CREATOR("/test_classes/notunit/notification/creator/"),
	JAVA_IFACES_CREATOR("/test_classes/notunit/java_ifaces/creator/"),

	/**User Consumer**/
	MOLECULES_CONSUMER ("/test_classes/dataservicetest/molecule/consumer/"),
	LOGIC_CONSUMER("/test_classes/logictest/consumer/"),
	DS_BASIC_CONSUMER("/test_classes/dataservicetest/basic/consumer/"),
	DATA_CONSUMER("/test_classes/notunit/data/consumer/"),
	SPECIAL_CONSUMER("/test_classes/notunit/special/consumer/"),
	EXEC_CONSUMER("/test_classes/notunit/execution/consumer/"),
	CLASSMGR_CONSUMER("/test_classes/classmgrtest/consumer/"),
	SPEC_CONSUMER("/test_classes/spectest/consumer/"),
	NETTY_CONSUMER("/test_classes/nettytest/consumer/"),
	MGMNT_CONSUMER("/test_classes/notunit/management/consumer/"),
	PERFORMANCE_CONSUMER("/test_classes/performance/consumer/"), 
	PERFORMANCE_REMOTE_CONSUMER("/test_classes/performance_remote/consumer/"),
	DOCKING_DEMO_CONSUMER("/test_classes/notunit/wasabidemo/consumer/"),
	COMPSS_CONSUMER("/test_classes/notunit/compss_demo/consumer/"),
	STUB_CONSUMER("/test_classes/stubtest/consumer/"),
	DBTEST_CONSUMER("/test_classes/dbhandlertest/consumer/"),
	NEURON_CONSUMER("/test_classes/notunit/neuron/consumer/"),
	COLLECTIONS_CONSUMER("/test_classes/notunit/collections/consumer/"),
	ASPECTS_CONSUMER("/test_classes/notunit/aspects/consumer/"),
	EMBEDDED_CONSUMER("/test_classes/notunit/embedded/consumer/"),
	RTTEST_CONSUMER("/test_classes/rttest/consumer/"),
	GARBAGE_COLLECTOR_CONSUMER("/test_classes/notunit/garbage_collector/consumer/"),
	VOLATILE_CONSUMER("/test_classes/notunit/volatile/consumer/"),
	IFACES_CONSUMER("/test_classes/notunit/ifaces/consumer/"),
	COMPSS_KMEANS_CONSUMER("/test_classes/notunit/kmeans/consumer/"),
	COMPSS_KMEANS_DC_CONSUMER("/test_classes/notunit/kmeans_dc/consumer/"),
	LIBRARIES_CONSUMER("/test_classes/notunit/libraries/consumer/"),
	PERSON_CONSUMER("/test_classes/persontest/consumer/"),
	MEMORY_CONSUMER("/test_classes/notunit/memory/consumer/"),
	NOTIFICATION_CONSUMER("/test_classes/notunit/notification/consumer/"),
	JAVA_IFACES_CONSUMER("/test_classes/notunit/java_ifaces/consumer/");

	private String userName;
	private AccountRole role;
	private PasswordCredential credential;
	private String classPath;
	private String srcPath;
	private String libPath;
	private AccountID accountID;
	
	/**
	 * @brief Constructor of users with their classpaths
	 * @param userClassPaths Classpaths of the user. Different classpaths means different namespaces.
	 */
	private TestUser(final String newuserPath) {
		String workingDir = System.getProperty("user.dir");
		this.setUserName(this.name());
		this.setCredential(new PasswordCredential(this.name()));
		this.setRole(AccountRole.NORMAL_ROLE);
		this.setSrcPath(workingDir + newuserPath + "src/");
		this.setLibPath(workingDir + newuserPath + "lib/");
		this.setClassPath(workingDir + newuserPath + "bin/");
	}
	
	/**
	 * @brief Get the {@link #userName}
	 * @return the userName
	 * @author dgasull
	 */

	public String getUserName() {
		return userName;
	}

	/**
	 * @brief Set the {@link #userName}
	 * @param newuserName the userName to set
	 * @author dgasull
	 */
	public void setUserName(final String newuserName) {
		if (newuserName == null) { 
			throw new IllegalArgumentException("Username cannot be null");
		}
		this.userName = newuserName;
	}


	/**
	 * @brief Get the UserScene::role
	 * @return the role
	 * @author dgasull
	 */

	public AccountRole getRole() {
		return role;
	}

	/**
	 * @brief Set the UserScene::role
	 * @param newrole the role to set
	 * @author dgasull
	 */
	public void setRole(final AccountRole newrole) {
		if (newrole == null) { 
			throw new IllegalArgumentException("Role cannot be null");
		}
		this.role = newrole;
	}

	/**
	 * @brief Get the UserScenario::credential
	 * @return the credential
	 * @author dgasull
	 */

	public PasswordCredential getCredential() {
		return credential;
	}

	/**
	 * @brief Set the UserScenario::credential
	 * @param newcredential the credential to set
	 * @author dgasull
	 */
	public void setCredential(final PasswordCredential newcredential) {
		if (newcredential == null) { 
			throw new IllegalArgumentException("Credential cannot be null");
		}
		this.credential = newcredential;
	}

	/**
	* @brief Get the TestUser::accountID
	* @return the accountID
	* @author dgasull
	*/
	
	public AccountID getAccountID() {
		return accountID;
	}

	/**
	* @brief Set the TestUser::accountID
	* @param newaccountID the accountID to set
	* @author dgasull
	*/
	public void setAccountID(final AccountID newaccountID) {
		if (newaccountID == null) { 
			throw new IllegalArgumentException("AccountID cannot be null");
		}
		this.accountID = newaccountID;
	}

	/**
	* @brief Get the TestUser::classPath
	* @return the classPath
	* @author dgasull
	*/
	
	public String getClassPath() {
		return classPath;
	}

	/**
	* @brief Set the TestUser::classPath
	* @param newclassPath the classPath to set
	* @author dgasull
	*/
	public void setClassPath(final String newclassPath) {
		if (newclassPath == null) { 
			throw new IllegalArgumentException("classPath cannot be null");
		}
		this.classPath = newclassPath;
	}

	/**
	* @brief Get the TestUser::srcPath
	* @return the srcPath
	* @author dgasull
	*/
	
	public String getSrcPath() {
		return srcPath;
	}

	/**
	* @brief Set the TestUser::srcPath
	* @param newsrcPath the srcPath to set
	* @author dgasull
	*/
	public void setSrcPath(final String newsrcPath) {
		if (newsrcPath == null) { 
			throw new IllegalArgumentException("srcPath cannot be null");
		}
		this.srcPath = newsrcPath;
	}

	/**
	 * @brief Get the TestUser::libPath
	 * @return the libPath
	 */
	public String getLibPath() {
		return libPath;
	}

	/**
	 * @brief Set the TestUser::libPath
	 * @param libPath the libPath to set
	 */
	public void setLibPath(final String newlibPath) {
		if (newlibPath == null) {
			throw new IllegalArgumentException("libPath cannot be null");
		}
		this.libPath = newlibPath;
	}




	
}
