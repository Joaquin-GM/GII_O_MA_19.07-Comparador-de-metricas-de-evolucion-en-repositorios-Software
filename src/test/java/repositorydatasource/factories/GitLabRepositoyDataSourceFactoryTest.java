package repositorydatasource.factories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import repositorydatasource.RepositoyDataSourceFactoryGitlab;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;
import repositorydatasource.RepositoryDataSourceFactory;

/**
 * Test for GitLabRepositoyDataSourceFactory.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class GitLabRepositoyDataSourceFactoryTest {
	
	/**
	 * Test method for {@link repositorydatasource.RepositoyDataSourceFactoryGitlab#getRepositoryDataSource()}.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testCreateRepositoryDataSource() {
		RepositoryDataSourceFactory rdsf = new RepositoyDataSourceFactoryGitlab();
		RepositoryDataSource rds = rdsf.getRepositoryDataSource();
		assertTrue(rds != null && rds.getConnectionType() == EnumConnectionType.NOT_CONNECTED);
	}
}
