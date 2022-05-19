package datamodel;

import java.io.Serializable;

import org.gitlab4j.api.models.Job;

/**
 * Custom model for GitlabApiJob that implements serializable.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class CustomGitlabApiJob implements Serializable {
	private static final long serialVersionUID = -7602110263950506090L;
	
	private transient Job job;
	
	/**
	 * Store as variables in this class the ones of Job used to calculate metrics, use these instead of the Job ones to calculations.
	 * This is needed because during the imports the Job is set to null.
	 */
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomGitlabApiJob(Job job) {
		super();
		this.job = job;
		this.name = job.getName();
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
