package repositorydatasource.factories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import datamodel.RepositorySourceType;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;
import repositorydatasource.RepositoryDataSourceFactory;
import repositorydatasource.RepositoryDataSourceFactoryGithub;

/**
 * Test for GitLabRepositoyDataSourceFactory.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class GitLabRepositoyDataSourceFactoryTest {
	
	/**
	 * Test method for {@link repositorydatasource.RepositoyDataSourceFactoryGithub#getRepositoryDataSource()}.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	@Test
	public void testCreateRepositoryDataSource() {
		RepositoryDataSourceFactory rdsf = new RepositoryDataSourceFactoryGithub();
		RepositoryDataSource rds = rdsf.getRepositoryDataSource();
		assertTrue(rds != null && rds.getConnectionType(RepositorySourceType.GitHub) == EnumConnectionType.NOT_CONNECTED);
	}
}
