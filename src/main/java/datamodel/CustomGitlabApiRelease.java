package datamodel;

import java.io.Serializable;

import org.gitlab4j.api.models.Release;

/**
 * Custom model for GitlabApiRelease that implements serializable.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class CustomGitlabApiRelease implements Serializable  {
	private static final long serialVersionUID = -5602110263950506090L;

	private transient Release release;

	public CustomGitlabApiRelease(Release release) {
		super();
		this.release = release;
	}

	public Release getRelease() {
		return release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}
}
