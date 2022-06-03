package datamodel;

import java.io.Serializable;

import org.kohsuke.github.GHWorkflowJob;

/**
 * Custom model for GitlabApiJob that implements serializable.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class CustomGithubApiJob implements Serializable {
	private static final long serialVersionUID = -7602110263950506090L;
	
	private transient GHWorkflowJob job;
	
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

	public CustomGithubApiJob(GHWorkflowJob job) {
		super();
		this.job = job;
		this.name = job.getName();
	}

	public GHWorkflowJob getJob() {
		return job;
	}

	public void setJob(GHWorkflowJob job) {
		this.job = job;
	}
}
