
public enum Role {
	Normal {
		public int setActionPage(UIComponent uiComponent) {
			uiComponent.showNormalActionPage();
			return uiComponent.getNavigationAnswer(3, 5);
		}
	},
	Librarian {
		public int setActionPage(UIComponent uiComponent) {
			uiComponent.showLibrarianActionPage();
			return uiComponent.getNavigationAnswer(3, 8);
		}
	},
	Admin {
		public int setActionPage(UIComponent uiComponent) {
			uiComponent.showAdminActionPage();
			return uiComponent.getNavigationAnswer(3, 12);
		}
	};
	
	public static boolean isValidRole(String toCheck) {
		for (Role c : Role.values()) {
			if (c.name().equals(toCheck)) {
				return true;
			}
		}
		return false;
	}
	
	public abstract int setActionPage(UIComponent uiComponent);
}
