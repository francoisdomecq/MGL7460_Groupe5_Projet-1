
public class User {

	private final String login;
	
	private final String password;
	
	private Role role;
	
	User(String login, String password) {
		this.login = login;
		this.password = password;
		this.role = Role.Normal;
	}

	User(String login, String password, Role role) {
		this(login, password);
		this.role = role;
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Role getRole() {
		return this.role;
	}
}
