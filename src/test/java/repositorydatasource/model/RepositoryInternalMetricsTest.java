package repositorydatasource.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import datamodel.RepositoryInternalMetrics;

/**
 * Test class forn {@link datamodel.RepositoryInternalMetrics}
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
class RepositoryInternalMetricsTest {

	/**
	 * Test method for {@link datamodel.RepositoryInternalMetrics#RepositoryInternalMetrics(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Collection, java.util.Collection, java.lang.Integer)}
	 * with null values.
	 */
	@Test
	void testRepositoryInternalMetricsNull() {
		RepositoryInternalMetrics repo = new RepositoryInternalMetrics(null, null, null, null, null, null);
		assertNull(repo.getTotalNumberOfIssues(), "Fail in null total number of issues");
		assertNull(repo.getTotalNumberOfCommits(), "Fail in null total number of commits");
		assertNull(repo.getNumberOfClosedIssues(), "Fail in null number of closed issues");
		assertNull(repo.getDaysToCloseEachIssue(), "Fail in null days to close each issue");
		assertNull(repo.getCommitDates(), "Fail in null commit dates");
		assertNull(repo.getLifeSpanMonths(), "Fail in null lifespan months");
	}

	/**
	 * Test method for {@link datamodel.RepositoryInternalMetrics#RepositoryInternalMetrics(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Collection, java.util.Collection, java.lang.Integer)}
	 * with values.
	 */
	@Test
	void testRepositoryInternalMetricsEmptyStrings() {
		RepositoryInternalMetrics repo = new RepositoryInternalMetrics(0, 0, 0, new ArrayList<Integer>(), new HashSet<Date>(), 0);
		assertEquals(0, repo.getTotalNumberOfIssues().intValue(), "Fail in total number of issues");
		assertEquals(0, repo.getTotalNumberOfCommits().intValue(), "Fail in total number of commits");
		assertEquals(0, repo.getNumberOfClosedIssues().intValue(), "Fail in number of closed issues");
		assertEquals(new ArrayList<Integer>(), repo.getDaysToCloseEachIssue(), "Fail in days to close each issue");
		assertEquals(new HashSet<Date>(), repo.getCommitDates(), "Fail in commit dates");
		assertEquals(0, repo.getLifeSpanMonths().intValue(), "Fail in lifespan months");
	}
}
