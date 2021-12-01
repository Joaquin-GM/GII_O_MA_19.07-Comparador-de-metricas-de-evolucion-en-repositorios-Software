package repositorydatasource;

/**
 * Factory of GitLab Reposistory Data Source.
 * 
 * @author MALB
 *
 */
public class RepositoyDataSourceFactoryGitlab implements RepositoryDataSourceFactory {

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSourceFactory#getRepositoryDataSource()
	 */
	@Override
	public RepositoryDataSource getRepositoryDataSource() {
		return RepositoryDataSourceUsingGitlabAPI.getGitLabRepositoryDataSource();
	}

}
