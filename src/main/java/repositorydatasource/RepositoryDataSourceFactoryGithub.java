package repositorydatasource;



public class RepositoryDataSourceFactoryGithub implements RepositoryDataSourceFactory {

	@Override
	public RepositoryDataSource getRepositoryDataSource() {
		return RepositoryDataSourceUsingGithubAPI.getGithubRepositoryDataSource();
	}

}
