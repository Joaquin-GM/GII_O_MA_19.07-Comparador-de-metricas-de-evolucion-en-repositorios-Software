package repositorydatasource;

/**
 * Factory interface of repository data sources.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public interface RepositoryDataSourceFactory {
	/**
	 * Returns a repository data source.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return A repository data source.
	 */
	RepositoryDataSource getRepositoryDataSource();
}
