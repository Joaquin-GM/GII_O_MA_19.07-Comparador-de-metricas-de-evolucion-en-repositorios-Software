package datamodel;

import java.io.Serializable;

import org.kohsuke.github.GHRelease;

/**
 * Custom model for GitlabApiRelease that implements serializable.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class CustomGithubApiRelease implements Serializable  {
	private static final long serialVersionUID = -5602110263950506090L;

	private transient GHRelease release;

	public CustomGithubApiRelease(GHRelease release) {
		super();
		this.release = release;
	}

	public GHRelease getRelease() {
		return release;
	}

	public void setRelease(GHRelease release) {
		this.release = release;
	}
}
