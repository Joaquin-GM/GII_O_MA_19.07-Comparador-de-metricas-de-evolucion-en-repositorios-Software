package datamodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Stores the metrics that can be obtained directly from the repository and used to calculate the rest of the metrics.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoryInternalMetrics implements Serializable{
	
	/**
	 * Serial.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 6572932423075014481L;
	/*Metrics that are obtained directly from the repository.*/
	
	private Date date = null;
	
	/**
	 * Total number of issues.
	 */
	private Integer totalNumberOfIssues = null;
	/**
	 * Total number of commits.
	 */
	private Integer totalNumberOfCommits = null;
	/**
	 * Number of closed issues.
	 */
	private Integer numberOfClosedIssues = null;
	/**
	 * Days to close each issue.
	 */
	private Collection<Integer> daysToCloseEachIssue = null;
	/**
	 * Dates of commits.
	 */
	private Collection<Date> commitDates = null;
	/**
	 * Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 */
	private Integer lifeSpanMonths = null;
	
	/**
	 * Jobs (default successful ones).
	 */
	private Collection<CustomGitlabApiJob> jobs = null;
	
	/**
	 * Releases (default successful ones).
	 */
	private Collection<CustomGitlabApiRelease> releases = null;
	
	/**
	 * Jobs (default successful ones).
	 */
	private Collection<CustomGithubApiJob> ghJobs = null;
	
	/**
	 * Releases (default successful ones).
	 */
	private Collection<CustomGithubApiRelease> ghReleases = null;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(date);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RepositoryInternalMetrics))
			return false;
		RepositoryInternalMetrics other = (RepositoryInternalMetrics) obj;
		return Objects.equals(date, other.date);
	}

	public RepositoryInternalMetrics() {}
	
	/**
	 * Sets all the metrics, unless jobs.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param totalNumberOfIssues Total number of issues.
	 * @param totalNumberOfCommits Total number of commits.
	 * @param numberOfClosedIssues Number of closed issues.
	 * @param daysToCloseEachIssue Days to close each issue.
	 * @param commitDates Dates of commits.
	 * @param lifeSpanMonths Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 */
	public RepositoryInternalMetrics(Integer totalNumberOfIssues, Integer totalNumberOfCommits, Integer numberOfClosedIssues,
			Collection<Integer> daysToCloseEachIssue, Collection<Date> commitDates, Integer lifeSpanMonths) {
		setDate(new Date());
		setTotalNumberOfIssues(totalNumberOfIssues);
		setTotalNumberOfCommits(totalNumberOfCommits);
		setNumberOfClosedIssues(numberOfClosedIssues);
		setDaysToCloseEachIssue(daysToCloseEachIssue);
		setCommitDates(commitDates);
		setLifeSpanMonths(lifeSpanMonths);
	}
	
	/**
	 * Sets all the metrics.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param totalNumberOfIssues Total number of issues.
	 * @param totalNumberOfCommits Total number of commits.
	 * @param numberOfClosedIssues Number of closed issues.
	 * @param daysToCloseEachIssue Days to close each issue.
	 * @param commitDates Dates of commits.
	 * @param lifeSpanMonths Number of months that have passed since the creation of the repository
	 * @param jobs of the repository.
	 * @param releases of the repository.
	 * until the date of last activity.
	 */
	public RepositoryInternalMetrics(Integer totalNumberOfIssues, Integer totalNumberOfCommits, Integer numberOfClosedIssues,
			Collection<Integer> daysToCloseEachIssue, Collection<Date> commitDates, Integer lifeSpanMonths, List<CustomGitlabApiJob> jobs, List<CustomGitlabApiRelease> releases) {
		setDate(new Date());
		setTotalNumberOfIssues(totalNumberOfIssues);
		setTotalNumberOfCommits(totalNumberOfCommits);
		setNumberOfClosedIssues(numberOfClosedIssues);
		setDaysToCloseEachIssue(daysToCloseEachIssue);
		setCommitDates(commitDates);
		setLifeSpanMonths(lifeSpanMonths);
		setJobs(jobs);
		setReleases(releases);
	}

	/**
	 * Gets the date.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param date the date to set
	 */
	private void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the Total number of issues.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the totalNumberOfIssues
	 */
	public Integer getTotalNumberOfIssues() {
		return totalNumberOfIssues;
	}

	/**
	 * Sets the Total number of issues.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param totalNumberOfIssues Total number of issues.
	 */
	private void setTotalNumberOfIssues(Integer totalNumberOfIssues) {
		this.totalNumberOfIssues = totalNumberOfIssues;
	}

	/**
	 * Gets the Total number of commits.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the totalNumberOfCommits
	 */
	public Integer getTotalNumberOfCommits() {
		return totalNumberOfCommits;
	}

	/**
	 * Sets the Total number of commits.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param totalNumberOfCommits Total number of commits.
	 */
	private void setTotalNumberOfCommits(Integer totalNumberOfCommits) {
		this.totalNumberOfCommits = totalNumberOfCommits;
	}

	/**
	 * Gets the Number of closed issues.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the numberOfClosedIssues
	 */
	public Integer getNumberOfClosedIssues() {
		return numberOfClosedIssues;
	}

	/**
	 * Sets the Number of closed issues.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param numberOfClosedIssues Number of closed issues.
	 */
	private void setNumberOfClosedIssues(Integer numberOfClosedIssues) {
		this.numberOfClosedIssues = numberOfClosedIssues;
	}

	/**
	 * Gets the Days to close each issue.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the daysToCloseEachIssue
	 */
	public Collection<Integer> getDaysToCloseEachIssue() {
		return daysToCloseEachIssue;
	}

	/**
	 * Sets the Days to close each issue.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param daysToCloseEachIssue Days to close each issue.
	 */
	private void setDaysToCloseEachIssue(Collection<Integer> daysToCloseEachIssue) {
		this.daysToCloseEachIssue = daysToCloseEachIssue;
	}

	/**
	 * Gets the Dates of commits.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the commitDates
	 */
	public Collection<Date> getCommitDates() {
		return commitDates;
	}

	/**
	 * Sets the Dates of commits.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param commitDates Dates of commits.
	 */
	private void setCommitDates(Collection<Date> commitDates) {
		this.commitDates = commitDates;
	}

	/**
	 * Gets the Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 */
	public Integer getLifeSpanMonths() {
		return lifeSpanMonths;
	}

	/**
	 * Sets the Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param lifeSpanMonths Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 */
	private void setLifeSpanMonths(Integer lifeSpanMonths) {
		this.lifeSpanMonths = lifeSpanMonths;
	}
	
	/**
	 * Gets the jobs of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @return list of the jobs of the repository
	 */
	public Collection<CustomGitlabApiJob> getJobs() {
		return jobs;
	}
	
	/**
	 * Sets the jobs of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param jobs list of the jobs of the repository
	 */
	public void setJobs(List<CustomGitlabApiJob> jobs) {
		this.jobs = jobs;
	}
	

	/**
	 * Gets the releases of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @return list of the releases of the repository
	 */
	public Collection<CustomGitlabApiRelease> getReleases() {
		return releases;
	}

	/**
	 * Sets the jobs of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param releases list of the releases of the repository
	 */
	public void setReleases(List<CustomGitlabApiRelease> releases) {
		this.releases = releases;
	}
	
	/**
	 * Gets the jobs of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @return list of the jobs of the repository
	 */
	public Collection<CustomGithubApiJob> getGHJobs() {
		return ghJobs;
	}
	
	/**
	 * Sets the jobs of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param jobs list of the jobs of the repository
	 */
	public void setGHJobs(List<CustomGithubApiJob> jobs) {
		this.ghJobs = jobs;
	}
	
	/**
	 * Gets the releases of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @return list of the releases of the repository
	 */
	public Collection<CustomGithubApiRelease> getGHReleases() {
		return ghReleases;
	}

	/**
	 * Sets the jobs of a repository.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param releases list of the releases of the repository
	 */
	public void setGHReleases(List<CustomGithubApiRelease> releases) {
		this.ghReleases = releases;
	}
}