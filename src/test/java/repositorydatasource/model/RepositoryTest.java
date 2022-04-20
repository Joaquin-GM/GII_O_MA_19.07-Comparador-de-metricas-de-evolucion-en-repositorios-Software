package repositorydatasource.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;

/**
 * Test class forn {@link datamodel.Repository}
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoryTest {

	/**
	 * Test method for {@link datamodel.Repository#Repository(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.List, java.util.Set, java.lang.Integer)}
	 * with null values.
	 */
	@Test
	public void testRepositoryNull() {
		Repository repo = new Repository(null, null, null);
		assertNull(repo.getUrl(), "Fail in null url");
		assertNull(repo.getName(), "Fail in null name");
		assertNull(repo.getId(), "Fail in id");
	}
	
	/**
	 * Test method for {@link datamodel.Repository#Repository(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.List, java.util.Set, java.lang.Integer)}
	 * with values.
	 */
	@Test
	public void testRepositoryEmptyStrings() {
		Repository repo = new Repository("", "", 0L);
		assertEquals("", repo.getUrl(), "Fail in url");
		assertEquals("", repo.getName(), "Fail in name");
	}
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	@Test
	public void testSetInternalMetrics() {
		Repository repo = new Repository("", "", 0L);
		RepositoryInternalMetrics repositoryInternalMetrics = new RepositoryInternalMetrics(0, 0, 0, null, null, 0);
		repo.setRepositoryInternalMetrics(repositoryInternalMetrics);
		assertEquals(repositoryInternalMetrics, repo.getRepositoryInternalMetrics());
	}
}