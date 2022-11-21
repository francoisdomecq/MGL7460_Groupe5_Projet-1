import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LibraryModel {

	private static final String DATABASE_DIRECTORY = "jdbc:sqlite:" 
											+ System.getProperty("user.dir")
											+ "\\db_files\\";
	
	private static final String DATABASE_NAME = "library.db";
	
	private static final String CONNECTION_STRING = DATABASE_DIRECTORY + DATABASE_NAME;
	
	private Connection connection;
	
	private Statement statement;
	
	private PreparedStatement preparedStatement;

	public LibraryModel() {
		try {
			this.connection = DriverManager.getConnection(LibraryModel.CONNECTION_STRING);
			this.statement = this.connection.createStatement();
			this.createUserTable();
			this.createBookTable();
			this.createFirstAdmin();
			System.out.println("Library opened!");
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	protected void finalize() {
		try {
			this.preparedStatement.close();
			this.statement.close();
			this.connection.close();
			System.out.println("Library closed!");
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createUserTable() {
		String sql = "CREATE TABLE IF NOT EXISTS user ("
				+ "id interger PRIMARY KEY, "
				+ "login varchar(50), "
				+ "password varchar(50), "
				+ "role varchar(15)"
				+ ");";
		try {
			this.statement.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createBookTable() {
		String sql = "CREATE TABLE IF NOT EXISTS book ("
				+ "id interger PRIMARY KEY, "
				+ "isbn varchar(13), "
				+ "title varchar(50), "
				+ "author varchar(50)"
				+ ");";
		try {
			this.statement.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createFirstAdmin() {
		User firstAdmin = new User("admin", "admin", Role.Admin);
		if (!this.isUserCorrect(firstAdmin)) {
			this.addUser(firstAdmin);
		}
	}

	public List<Book> getBooks() {
		List<Book> toReturn = new ArrayList<Book>();
		String sql = "SELECT isbn, title, author FROM book;";
		try (ResultSet results = this.statement.executeQuery(sql)) {
			while (results.next()) {
				toReturn.add(new Book(results.getString(1),
									results.getString(2),
									results.getString(3)));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return toReturn;
	}

	public List<Book> getBooksByKeyword(String keyword) {
		List<Book> toReturn = new ArrayList<Book>();
		String lowerKeyword = keyword.toLowerCase(Locale.CANADA_FRENCH);
		String sql = "SELECT isbn, title, author FROM book "
				+ "WHERE LOWER(isbn) = ? OR "
				+ "LOWER(title) = ? OR "
				+ "LOWER(author) = ?;";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, lowerKeyword);
			this.preparedStatement.setString(2, lowerKeyword);
			this.preparedStatement.setString(3, lowerKeyword);
			ResultSet results = this.preparedStatement.executeQuery();
			while (results.next()) {
				toReturn.add(new Book(results.getString(1),
									results.getString(2), 
									results.getString(3)));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return toReturn;
	}

	public void addBook(Book bookToAdd) {
		String sql = "INSERT INTO book (isbn, title, author) " 
				 	+ "VALUES (?, ?, ?);";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, bookToAdd.getIsbn());
			this.preparedStatement.setString(2, bookToAdd.getTitle());
			this.preparedStatement.setString(3, bookToAdd.getAuthor());
			int rowsAffected = this.preparedStatement.executeUpdate();
			if (rowsAffected != 0) {
				System.out.println("Book added !");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateBookByIsbn(String isbnToUpdate, Book updatedBook) {
		String sql = "UPDATE book "
				+ "SET isbn = ?, "
				+ "title = ?, "
				+ "author = ? "
				+ "WHERE isbn = ?;";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, updatedBook.getIsbn());
			this.preparedStatement.setString(2, updatedBook.getTitle());
			this.preparedStatement.setString(3, updatedBook.getAuthor());
			this.preparedStatement.setString(4, isbnToUpdate);
			int rowsAffected = this.preparedStatement.executeUpdate();
			if (rowsAffected != 0) {
				System.out.println("Book updated !");
			} else {
				System.out.println("Book not found. "
						+ "Please make sure the isbn of the book to update is valid.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteBookByIsbn(String isbnToDelete) {
		String sql = "DELETE FROM book "
				+ "WHERE isbn = ?;";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, isbnToDelete);
			int rowsAffected = this.preparedStatement.executeUpdate();
			if (rowsAffected != 0) {
				System.out.println("Book deleted !");
			} else {
				System.out.println("Book not found. "
						+ "Please make sure the isbn of the book to delete is valid.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isUserCorrect(User toVerify) {
		User findedUser = this.getUserByLogin(toVerify.getLogin());
		boolean isUserFound = findedUser != null;
		boolean isLoginCorrect = toVerify.getLogin().equals(findedUser.getLogin());
		boolean isPasswordCorrect = toVerify.getPassword().equals(findedUser.getPassword());
		return isUserFound && isLoginCorrect && isPasswordCorrect;
	}
	
	public User getUserByLogin(String loginToFind) {
		User toReturn = null;
		String sql = "SELECT login, password, role FROM user "
					+ "WHERE login = ? "
					+ "LIMIT 1;";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, loginToFind);
			ResultSet result = this.preparedStatement.executeQuery();
			if (result.next()) {
				toReturn = new User(result.getString(1),
									result.getString(2),
									Role.valueOf(result.getString(3)));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return toReturn;
	}
	
	public void addUser(User userToAdd) {
		String sql = "INSERT INTO user (login, password, role) "
				+ "VALUES (?, ?, ?);";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, userToAdd.getLogin());
			this.preparedStatement.setString(2, userToAdd.getPassword());
			this.preparedStatement.setString(3, userToAdd.getRole().toString());
			int rowsAffected = this.preparedStatement.executeUpdate();
			if (rowsAffected != 0) {
				System.out.println("User added !");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateUserByLogin(String loginToUpdate, User updatedUser) {
		String sql = "UPDATE user "
				+ "SET login = ?, "
				+ "password = ?, "
				+ "role = ? "
				+ "WHERE login = ?;";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, updatedUser.getLogin());
			this.preparedStatement.setString(2, updatedUser.getPassword());
			this.preparedStatement.setString(3, updatedUser.getRole().toString());
			this.preparedStatement.setString(4, loginToUpdate);
			int rowsAffected = this.preparedStatement.executeUpdate();
			if (rowsAffected != 0) {
				System.out.println("User updated !");
			} else {
				System.out.println("User not found. "
						+ "Please make sure the login of the user to update is valid.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteUserByLogin(String loginToDelete) {
		String sql = "DELETE FROM user "
				+ "WHERE login = ?";
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
			this.preparedStatement.setString(1, loginToDelete);
			int rowsAffected = this.preparedStatement.executeUpdate();
			if (rowsAffected != 0) {
				System.out.println("User deleted !");
			} else {
				System.out.println("User not found. "
						+ "Please make sure the login of the user to delete is valid.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
