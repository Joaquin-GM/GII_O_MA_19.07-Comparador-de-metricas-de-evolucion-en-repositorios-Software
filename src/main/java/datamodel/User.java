package datamodel;

import java.io.Serializable;

/**
 * Information of the connected user.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class User implements Serializable{
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -8802110263950506090L;

	private Long id;
	
	/**
	 * URL of the avatar.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private String avatarUrl;
	
	/**
	 * E-mail.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private String email;
	
	/**
	 * Name.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private String name;
	
	/**
	 * Username.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private String username;

	/**
	 * Sets all attributes.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param id 
	 * @param avatarUrl URL of the avatar.
	 * @param email E-mail.
	 * @param name Name.
	 * @param username Username.
	 */
	public User(Long id, String avatarUrl, String email, String name, String username) {
		setId(id);
		setAvatarUrl(avatarUrl);
		setEmail(email);
		setName(name);
		setUsername(username);
	}

	/**
	 * Gets the id.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param id the id to set
	 */
	private void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the avatarUrl.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * Sets the avatarUrl.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param avatarUrl the avatarUrl to set
	 */
	private void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * Gets the email.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param email the email to set
	 */
	private void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the name.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the username.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param username the username to set
	 */
	private void setUsername(String username) {
		this.username = username;
	}
}
