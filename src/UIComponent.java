
import java.util.List;
import java.util.Scanner;

public class UIComponent {

	private Scanner scanner;

	public UIComponent() {
		this.scanner = new Scanner(System.in);
	}

	public void showHomepage() {
		System.out.println("Welcome to the library:\n"
				+ "  1. Sign in\n"
				+ "  2. Sign up\n"
				+ "  3. Quit");
	}

	public void showSignPage() {
		System.out.println("Please enter your login and your password:");
	}

	public void showNormalActionPage() {
		System.out.println("Please choose an action:\n"
				+ "  3. Quit\n"
				+ "  4. Search books\n"
				+ "  5. List all books");
	}

	public void showLibrarianActionPage() {
		this.showNormalActionPage();
		System.out.println("  6. Add a book\n"
				+ "  7. Modify a book\n"
				+ "  8. Delete a book");
	}

	public void showAdminActionPage() {
		this.showLibrarianActionPage();
		System.out.println("  9. Search a user\n"
				+ " 10. Add a user\n"
				+ " 11. Modify a user\n"
				+ " 12. Delete a user");
	}

	public void showSearchBookAction() {
		System.out.println("Please enter the keyword (isbn, title, author) you are searching for:");
	}

	public void showBooksAction(List<Book> books) {
		System.out.println("Books found:");
		for (Book book : books) {
			System.out.println("\t-> " + book.getIsbn() + " - " 
							+ book.getTitle() + " (" + book.getAuthor() + ")");
		}
	}

	public void showAddBookAction() {
		System.out.println("Please enter the informations of the book you want to add:");
	}

	public void showUpdateBookAction() {
		System.out.println("Please enter the isbn of the book you "
				+ "want to modify and the informations of the new book:");
	}

	public void showDeleteBookAction() {
		System.out.println("Please enter the isbn of the book you want to delete:");
	}

	public void showAddUserAction() {
		System.out.println("Please enter the informations of the new user:");
	}

	public void showSearchUserAction() {
		System.out.println("Please enter the login of the user you are looking for:");
	}

	public void showUserAction(User userToShow) {
		if (userToShow != null) {
			System.out.println(" - Login: " + userToShow.getLogin() 
						+ "\n - Password: " + userToShow.getPassword()
						+ "\n - Role: " + userToShow.getRole().toString());
		} else {
			System.out.println("User not found. Please check the spelling of your request.");
		}
	}

	public void showUpdateUserAction() {
		System.out.println("Please enter the login of the user you "
				+ "want to modify and the informations of the new user:");
	}

	public void showDeleteUserAction() {
		System.out.println("Please enter the login of the user you want to delete:");
	}

	public void showExitAction() {
		System.out.println("Goodbye!");
	}

	public void waitPressingEnter() {
		System.out.println("\nPress enter to continue");
		this.scanner.nextLine();
	}

	public int getNavigationAnswer(int minAnswer, int maxAnswer) {
		try {
			System.out.print("\nYour action: ");
			int action = fromStringToIntAnswer(this.scanner.nextLine());
			if (action < minAnswer || action > maxAnswer) {
				throw new NumberFormatException();
			}
			return action;
		} catch (NumberFormatException e) {
			System.out.println("Incorect answer, please retry:");
			return this.getNavigationAnswer(minAnswer, maxAnswer);
		}
	}

	public String getStringAnswer(String textToPrint) {
		System.out.print(textToPrint + ": ");
		return this.scanner.nextLine();
	}

	public User getSignAnswer() {
		System.out.print("Login: ");
		String login = this.scanner.nextLine();
		System.out.print("Password: ");
		String password = this.scanner.nextLine();
		return new User(login, password);
	}

	public Book getBookAnswer() {
		System.out.print("Isbn: ");
		String isbn = this.scanner.nextLine();
		System.out.print("Title: ");
		String title = this.scanner.nextLine();
		System.out.print("Author: ");
		String author = this.scanner.nextLine();
		return new Book(isbn, title, author);
	}

	public User getUserAnswer() {
		System.out.print("Login: ");
		String login = this.scanner.nextLine();
		System.out.print("Password: ");
		String password = this.scanner.nextLine();
		System.out.print("Role (Admin/Librarian/Normal): ");
		String stringRole;
		do {
			stringRole = this.scanner.nextLine();
		} while (!Role.isValidRole(stringRole));
		return new User(login, password, Role.valueOf(stringRole));
	}

	private int fromStringToIntAnswer(String input) throws NumberFormatException {
		return Integer.parseInt(input);
	}
}
