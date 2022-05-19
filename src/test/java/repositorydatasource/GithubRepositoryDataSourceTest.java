package repositorydatasource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import datamodel.Repository;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * Test for GithubRepositoryDataSource.
 * 
 * @author Carlos López Nozal - clopezno
 *
 */
public class GithubRepositoryDataSourceTest {

	/**
	 * RepositoryDataSource under test.
	 * 
	 */
	private static RepositoryDataSource repositoryDataSource;
	
	/**
	 * Factory to instantiate the RepositoryDataSource.
	 * 
	 */
	private static RepositoryDataSourceFactory repositoryDataSourceFactory;
	
	/**
	 * Username to connect to RepositoryDataSource via username and password.
	 * 
	 */
	private static String user = "";
	
	/**
	 * Password to connect to RepositoryDataSource via username and password.
	 * 
	 */
	private static String password = "";
	
	/**
	 * Token to connect to RepositoryDataSource via personal access token.
	 * 
	 */
	private static String token = "";
	
	
	/**
	 * Instantiate the RepositoryDataSource.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Carlos López Nozal - clopezno
	 * @throws Exception if something goes wrong
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		repositoryDataSourceFactory = new RepositoryDataSourceFactoryGithub();
		repositoryDataSource = repositoryDataSourceFactory.getRepositoryDataSource();
				
	}

	/**
	 * Disconnects from RepositoryDataSource and closes references..
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws Exception if something goes wrong
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		if (repositoryDataSource.getConnectionType() != EnumConnectionType.NOT_CONNECTED) repositoryDataSource.disconnect();
		repositoryDataSource = null;
		repositoryDataSourceFactory = null;
	}

	/**
	 * Disconnects from RepositoryDataSource.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws Exception if something goes wrong
	 */
	@BeforeEach
	public void setUp() throws Exception {
		if (repositoryDataSource.getConnectionType() != EnumConnectionType.NOT_CONNECTED) repositoryDataSource.disconnect();
	}

	/**
	 * Disconnects from RepositoryDataSource.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws Exception if something goes wrong
	 */
	@AfterEach
	public void tearDown() throws Exception {
		if (repositoryDataSource.getConnectionType() != EnumConnectionType.NOT_CONNECTED) repositoryDataSource.disconnect();
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getGitLabRepositoryDataSource()}.
	 * <p>
	 * It ensures that there can only be one instance of RepositoryDataSource.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Carlos López Nozal  - clopezno
	 */
	@Test
	public void testGetGithubRepositoryDataSource() {
		assertTrue(repositoryDataSource == RepositoryDataSourceUsingGithubAPI.getGithubRepositoryDataSource(), getErrorMsg("testGetGithubRepositoryDataSource", "Only one instance of repositoryDataSource is allowed"));
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#connect()}.
	 * <p>
	 * It ensures that a correct connection does not throw an exception and that the connection type is 'CONNECTED'.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testConnectOK() {
		// TODO Asumir que hay conexión a Internet
		assertDoesNotThrow(() ->{ repositoryDataSource.connect();}, getErrorMsg("testConnectOK", "Exception when trying to connect"));
		assertEquals(EnumConnectionType.CONNECTED, repositoryDataSource.getConnectionType(), getErrorMsg("testConnectOK", "Connection type must be 'CONNECTED'"));
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#connect()}.
	 * <p>
	 * It ensures that a failed connection connection throws an exception and that the connection type is 'NOT_CONNECTED'.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Disabled("There must be no internet connection")
	@Test
	public void testConnectFAILED() {
		// TODO Quitar Disabled y sustituir pos asumir que no hay conexión a internet.
		assertThrows(RepositoryDataSourceException.class, () -> {}, getErrorMsg("testConnectFAILED", "Exception must be thrown if connection error occurs"));
		assertEquals(EnumConnectionType.NOT_CONNECTED, repositoryDataSource.getConnectionType(), getErrorMsg("testConnectFAILED", "Connection type must be 'NOT_CONNECTED'"));
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#connect(java.lang.String, java.lang.String)}.
	 * <p>
	 * 
	 * If user and password are specified, it ensures that no exception is thrown and that the connection type is 'LOGGED'.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testConnectUserPasswordOk() {
		assumeTrue(user != null && !user.equals("") && password != null && !password.equals(""), "The test can not be performed if a correct username and password are not specified");
		assertDoesNotThrow(() -> {
			repositoryDataSource.connect(user, password);
		}, getErrorMsg("testConnectUserPasswordOk", "Incorrect username and password or the test threw an exception when it should not"));
		assertEquals(EnumConnectionType.LOGGED, repositoryDataSource.getConnectionType(), getErrorMsg("testConnectUserPasswordOk", "Connection type must be 'LOGGED'"));
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#connect(java.lang.String, java.lang.String)}.
	 * <p>
	 * Test the method using a set of wrong user-password pairs that should raise an exception and the connection type is 'NOT_CONNECTED'.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@ParameterizedTest(name = "Run with User = \"{0}\" and Password = \"{1}\" must throw an exception.")
	@CsvFileSource(resources = "/testConnectUserPasswordWrong.csv", numLinesToSkip = 1, delimiter = ';', encoding = "UTF-8")
	public void testConnectUserPasswordWrong(String user, String password) {
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.connect(user, password);
		}, getErrorMsg("testConnectUserPasswordWrong", "Wrong user-password should throw an exception"));
		assertEquals(EnumConnectionType.NOT_CONNECTED, repositoryDataSource.getConnectionType(), getErrorMsg("testConnectUserPasswordWrong", "Connection type must be 'NOT_CONNECTED'"));
	}
		
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#connect(java.lang.String)}.
	 * <p>
	 * If a correct token is specified, it ensures that no exception is thrown and that the connection type is 'LOGGED'.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testConnectPivateTokenOK() {
		assumeTrue(token != null && !token.equals(""), "The test can not be performed if a correct token is not specified");
		assertDoesNotThrow(() -> {
			repositoryDataSource.connect(token);
		}, getErrorMsg("testConnectPivateTokenOK", "IWrong token or the test threw an exception when it should not"));
		assertEquals(EnumConnectionType.LOGGED, repositoryDataSource.getConnectionType(), getErrorMsg("testConnectPivateTokenOK", "Connection type must be 'LOGGED'"));
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#connect(java.lang.String)}.
	 * <p>
	 * Test the method using a set of tokens that should raise an exception and the connection type is 'NOT_CONNECTED'.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@ParameterizedTest(name = "Run with Token = \"{0}\" must throw an exception.")
	@CsvFileSource(resources = "/testConnectTokenWrong.csv", numLinesToSkip = 1, delimiter = ';', encoding = "UTF-8")
	public void testConnectPivateTokenWrong(String token) {
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.connect(token);
		}, getErrorMsg("testConnectPivateTokenWrong", "Wrong token should throw an exception"));
		assertEquals(EnumConnectionType.NOT_CONNECTED, repositoryDataSource.getConnectionType(), getErrorMsg("testConnectPivateTokenWrong", "Connection type must be 'NOT_CONNECTED'"));
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#disconnect()}.
	 * <p>
	 * It is ensured that it disconnects correctly after being connected.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testDisconnectOK() {
		// TODO Assumption?
		try {
			repositoryDataSource.connect();
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testDisconnectOK", "Connection error"));
		}
		assertDoesNotThrow(() -> {
			repositoryDataSource.disconnect();
		}, getErrorMsg("testDisconnectOK", "An exception should not be thrown"));
		assertEquals(EnumConnectionType.NOT_CONNECTED, repositoryDataSource.getConnectionType(), "Connection type must be 'NOT_CONNECTED'");
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#disconnect()}.
	 * <p>
	 * It makes sure that an exception is thrown while trying to disconnect without being connected.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testDisconnectFailed() {
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.disconnect();
		}, getErrorMsg("testDisconnectFailed", "No exception was thrown when trying to disconnect when it was disconnected"));
		assertEquals(EnumConnectionType.NOT_CONNECTED, repositoryDataSource.getConnectionType(), "Connection type must be 'NOT_CONNECTED'");
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * It ensures that an exception is thrown while trying to obtain a public repository when disconnected.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetPublicRepositoryWhenDisconnected() {
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.getRepository("https://github.com/clopezno/libre-gift");
		}, getErrorMsg("testGetPublicRepositoryWhenDisconnected", "An exception must be thrown when trying to obtain a public repository without connection"));
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * It ensures that an exception is thrown while trying to obtain a private repository when disconnected.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetPrivateRepositoryWhenDisconnected() {
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.getRepository("https://gitlab.com/mlb0029/KnowResult");
		}, getErrorMsg("testGetPrivateRepositoryWhenDisconnected", "An exception must be thrown when trying to obtain a private repository without connection"));
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * It ensures that an exception is thrown while trying to obtain a non-existent repository when disconnected.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetNonExistentRepositoryWhenDisconnected() {
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.getRepository("https://gitlab.com/mlb0029/KnowRlt");
		}, getErrorMsg("testGetNonExistentRepositoryWhenDisconnected", "An exception must be thrown when trying to obtain a non-existent repository without connection."));
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetPublicRepositoryWhenConnected() {
		try {
			repositoryDataSource.connect();
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetPublicRepositoryWhenConnected", "Connection error"));
		}
		try {
			Repository repository = repositoryDataSource.getRepository("https://github.com/clopezno/libre-gift");
			assertNotNull(repository, "Returns null when obtaining a public repository with public connection");
			//assertEquals(8760234, repository.getId().intValue(), "It does not return the correct ID.");
		}catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetPublicRepositoryWhenConnected", "Exception when obtaining a public repository with public connection"));
		}
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetPrivateRepositoryWhenConnected() {
		try {
			repositoryDataSource.connect();
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testDisconnectOK", "Connection error"));
		}
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.getRepository("https://gitlab.com/mlb0029/KnowResult");
		}, getErrorMsg("testGetRepositoryWhenDisconnected", "An exception must be thrown when trying to obtain a private repository with public connection."));
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetNonExistentRepositoryWhenConnected() {
		try {
			repositoryDataSource.connect();
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetNonExistentRepositoryWhenConnected", "Connection error"));
		}
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.getRepository("https://gitlab.com/mlb0029/KnowRlt");
		}, getErrorMsg("testGetRepositoryWhenDisconnected", "An exception must be thrown when trying to obtain a non-existent repository with public connection."));
	}	
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetPublicRepositoryWhenLogged() {
		assumeTrue(token != null && !token.equals(""));
		try {			
			repositoryDataSource.connect(token);
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetPublicRepositoryWhenLogged", "Connection error"));
		}
		try {
			Repository repository = repositoryDataSource.getRepository("https://github.com/clopezno/libre-gift");
			assertNotNull(repository, "Returns null when obtaining a public repository with loged connection");
			//assertEquals(8760234, repository.getId().intValue(), "It does not return the correct ID.");
			assertEquals("https://github.com/clopezno/libre-gift", repository.getUrl(), "It does not return the correct URL");
			assertEquals("libre-gift", repository.getName(),"It does not return the correct Name");
		}catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetPublicRepositoryWhenLogged", "Exception when obtaining a public repository with public connection"));
		}
		//fail( getErrorMsg("testGetPublicRepositoryWhenLogged", Constants.TestErrorMessages.NOT_IMPLEMENTED_SECURITY_REASONS));
	}

	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testOwnPrivateGetRepositoryWhenLogged() {
		assumeTrue(token != null && !token.equals(""));
		try {			
			repositoryDataSource.connect(token);
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testOwnPrivateGetRepositoryWhenLogged", "Connection error"));
		}
		try {
			Repository repository = repositoryDataSource.getRepository("https://gitlab.com/mlb0029/KnowResult");
			assertNotNull(repository, "Returns null when obtaining a public repository with loged connection");
			assertEquals(8760239, repository.getId().intValue(), "It does not return the correct ID.");
			assertEquals("https://gitlab.com/mlb0029/KnowResult", repository.getUrl(), "It does not return the correct URL");
			assertEquals("KnowResult", repository.getName(),"It does not return the correct Name");
		}catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testOwnPrivateGetRepositoryWhenLogged", "Exception when obtaining a public repository with public connection"));
		}
		//fail( getErrorMsg("testOwnPrivateGetRepositoryWhenLogged", Constants.TestErrorMessages.NOT_IMPLEMENTED_SECURITY_REASONS));
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Disabled("Not yet implemented")
	@Test
	public void testAnotherPrivateGetRepositoryWhenLogged() {
		assumeTrue(token != null && !token.equals(""), getErrorMsg("testAnotherPrivateGetRepositoryWhenLogged", "Username and password not entered"));
		try {
			repositoryDataSource.connect(token);
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetPublicRepositoryWhenLogged", "Connection error"));
		}
	}
	
	/**
	 * Test method for {@link repositorydatasource.RepositoryDataSourceUsingGitlabAPI#getRepository(java.lang.String)}.
	 * <p>
	 * 
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testGetNonExistentRepositoryWhenLogged() {
		assumeTrue(token != null && !token.equals(""));
		try {			
			repositoryDataSource.connect(token);
		} catch (RepositoryDataSourceException e) {
			fail(getErrorMsg("testGetNonExistentRepositoryWhenLogged", "Connection error"));
		}
		assertThrows(RepositoryDataSourceException.class, () -> {
			repositoryDataSource.getRepository("https://gitlab.com/mlb0029/Knosult");
		}, "Must throw an exception if the repository doesn't exists");
		//fail( getErrorMsg("testGetNonExistentRepositoryWhenLogged", Constants.TestErrorMessages.NOT_IMPLEMENTED_SECURITY_REASONS));
	}
	
	@Disabled("Not yet implemented")
	@Test
	public void testGetRepository() {//TODO Parametrized test
		// TODO
		fail("Not yet implemented");
	}

	/**
	 * Generates error message by test method name and a message passed by parameter.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param testName Test method name.
	 * @param msg Error message.
	 * @return Custom error message.
	 */
	private String getErrorMsg(String testName, String msg) {
		return "Failed test: " + testName + ". Message: " + msg + ".";
	}
}
