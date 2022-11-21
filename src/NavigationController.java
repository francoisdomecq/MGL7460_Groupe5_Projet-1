import java.util.List;

public class NavigationController {

	public static void main(String[] args) {
		NavigationController navigationController = new NavigationController();
		navigationController.setHomepage();
	}

	private Action[] actions = new Action[] {
		new Action() { public void execute() { setActionPage(); } },
		new Action() { public void execute() { setSignInPage(); } },
		new Action() { public void execute() { setSignUpPage(); } },
		new Action() { public void execute() { setQuitAction(); } },
		new Action() { public void execute() { setSearchBookAction(); } },
		new Action() { public void execute() { setListBooksAction(); } },
		new Action() { public void execute() { setAddBookAction(); } },
		new Action() { public void execute() { setUpdateBookAction(); } },
		new Action() { public void execute() { setDeleteBookAction(); } },
		new Action() { public void execute() { setSearchUserAction(); } },
		new Action() { public void execute() { setAddUserAction(); } },
		new Action() { public void execute() { setUpdateUserAction(); } },
		new Action() { public void execute() { setDeleteUserAction(); } }
	};

	private LibraryModel library;
	
	private UIComponent uiComponent;
	
	private User loggedUser;

	public NavigationController() {
		this.library = new LibraryModel();
		this.uiComponent = new UIComponent();
	}

	public void setHomepage() {
		this.uiComponent.showHomepage();
		int navigationAnswer = this.uiComponent.getNavigationAnswer(1, 3);
		this.actions[navigationAnswer].execute();
	}
	
	private void setActionPage() {
		int navigationAnswer = this.loggedUser.getRole().setActionPage(this.uiComponent);
		this.actions[navigationAnswer].execute();
	}

	private void setQuitAction() {
		this.uiComponent.showExitAction();
		this.library.finalize();
		System.exit(0);
	}

	private void setSignInPage() {
		this.uiComponent.showSignPage();
		do {
			this.loggedUser = this.uiComponent.getSignAnswer();
		} while (!this.library.isUserCorrect(this.loggedUser));
		this.loggedUser = this.library.getUserByLogin(this.loggedUser.getLogin());
		this.setActionPage();
	}

	private void setSignUpPage() {
		this.uiComponent.showSignPage();
		this.loggedUser = this.uiComponent.getSignAnswer();
		this.library.addUser(this.loggedUser);
		this.setActionPage();
	}

	private void setSearchBookAction() {
		this.uiComponent.showSearchBookAction();
		String keywordToSearchFor = this.uiComponent.getStringAnswer("Keyword of the book you are looking for");
		List<Book> findedBooks = this.library.getBooksByKeyword(keywordToSearchFor);
		this.uiComponent.showBooksAction(findedBooks);
		this.uiComponent.waitPressingEnter();
		this.setActionPage();
	}

	private void setListBooksAction() {
		List<Book> books = this.library.getBooks();
		this.uiComponent.showBooksAction(books);
		this.uiComponent.waitPressingEnter();
		this.setActionPage();
	}

	private void setAddBookAction() {
		this.uiComponent.showAddBookAction();
		Book toAdd = this.uiComponent.getBookAnswer();
		this.library.addBook(toAdd);
		this.setActionPage();
	}

	private void setUpdateBookAction() {
		this.uiComponent.showUpdateBookAction();
		String titleToUpdate = this.uiComponent.getStringAnswer("Isbn of the book to modify");
		Book updatedBook = this.uiComponent.getBookAnswer();
		this.library.updateBookByIsbn(titleToUpdate, updatedBook);
		this.setActionPage();
	}

	private void setDeleteBookAction() {
		this.uiComponent.showDeleteBookAction();
		String titleToDelete = this.uiComponent.getStringAnswer("Isbn of the book to delete");
		this.library.deleteBookByIsbn(titleToDelete);
		this.setActionPage();
	}

	private void setSearchUserAction() {
		this.uiComponent.showSearchUserAction();
		String loginToFind = this.uiComponent.getStringAnswer("Login of the user you are looking for");
		User findedUser = this.library.getUserByLogin(loginToFind);
		this.uiComponent.showUserAction(findedUser);
		this.uiComponent.waitPressingEnter();
		this.setActionPage();
	}

	private void setAddUserAction() {
		this.uiComponent.showAddUserAction();
		User toAdd = this.uiComponent.getUserAnswer();
		this.library.addUser(toAdd);
		this.setActionPage();
	}

	private void setUpdateUserAction() {
		this.uiComponent.showUpdateUserAction();
		String loginToUpdate = this.uiComponent.getStringAnswer("Login of the user you want to modify");
		User updatedUser = this.uiComponent.getUserAnswer();
		this.library.updateUserByLogin(loginToUpdate, updatedUser);
		this.setActionPage();
	}

	private void setDeleteUserAction() {
		this.uiComponent.showDeleteUserAction();
		String loginToDelete = this.uiComponent.getStringAnswer("Login of the user to delete");
		this.library.deleteUserByLogin(loginToDelete);
		this.setActionPage();
	}
	
	private interface Action {
		void execute();
	}
}
