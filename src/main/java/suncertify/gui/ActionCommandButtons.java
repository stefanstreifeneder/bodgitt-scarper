package suncertify.gui;

/**
 * Specifies the action commands of all buttons.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public enum ActionCommandButtons {	
	
	/**
	 * Action command of the button called "Update", which belongs
	 * to the update window 
	 * <code>suncertify.gui.seller.WindowUpdateSeller</code> of the seller.
	 */
	WINUP_UPDATE("WINUP_UPDATE"), 
	
	/**
	 * Action command of the button called "Cancel", which belongs
	 * to the update window 
	 * <code>suncertify.gui.seller.WindowUpdateSeller</code> of the seller.
	 */
	WINUP_EXIT("WINUP_EXIT"), 
	
	/**
	 * Action command of the button called "Delete", which belongs
	 * to the delete window 
	 * <code>suncertify.gui.seller.WindowDeleteSeller</code> of the seller.
	 */
	WINDEL_DELETE("WINDEL_DELETE"), 
	
	/**
	 * Action command of the button called "Cancel", which belongs
	 * to the delete window 
	 * <code>suncertify.gui.seller.WindowDeleteSeller</code> of the seller.
	 */
	WINDEL_EXIT("WINDEL_EXIT"), 
	
	/**
	 * Action command of the button called "Add", which belongs
	 * to the add window 
	 * <code>suncertify.gui.seller.WindowAddSeller</code> of the seller.
	 */
	WINADD_ADD("WINADD_ADD"), 
	
	/**
	 * Action command of the button called "Cancel", which belongs
	 * to the add window 
	 * <code>suncertify.gui.seller.WindowAddSeller</code> of the seller.
	 */
	WINADD_EXIT("WINADD_EXIT"), 
	
	/**
	 * Action command of the button called "Delete Record", which belongs
	 * to the panel <code>suncertify.gui.seller.PanelUpDelAddSeller</code>
	 * of the seller.
	 */
	PANUDRA_DELETE("PANUDRA_DELETE"), 
	
	/**
	 * Action command of the button called "Update Record", which belongs
	 * to the panel <code>suncertify.gui.seller.PanelUpDelAddSeller</code>
	 * of the seller.
	 */
	PANUDRA_UPDATE("PANUDRA_UPDATE"), 
	
	/**
	 * Action command of the button called "Add Record", which belongs
	 * to the panel <code>suncertify.gui.seller.PanelUpDelAddSeller</code>
	 * of the seller.
	 */
	PANUDRA_ADD("PANUDRA_ADD"), 
	
	/**
	 * Action command of the button called "Read Record", which belongs
	 * to the panel <code>suncertify.gui.seller.PanelUpDelAddSeller</code>
	 * of the seller.
	 */
	PANUDRA_READ("PANUDRA_READ"), 
	
	/**
	 * Action command of the button called "Search/Refresh", which belongs
	 * to the general search panel <code>suncertify.gui.PanelSearch</code>.
	 */
	PANSEARCH("PANSEARCH"), 
	
	/**
	 * Action command of the button called "Rent Record", which belongs
	 * to the panel <code>suncertify.gui.buyer.PanelRentBuyer</code>
	 * of a buyer client.
	 */
	PANRR_RENT("PANRR_RENT"), 
	
	/**
	 * Action command of the button called "Release Record", which belongs
	 * to the panel <code>suncertify.gui.buyer.PanelRentBuyer</code> 
	 * of a buyer client.
	 */
	PANRR_RELEASE("PANRR_RELEASE"),
	
	
	/**
	 * Action command of the button called "Reload DB", which belongs
	 * to the button of class <code>suncertify.gui.ButtonReloadDB</code>.
	 */
	RELOAD_DB("RELOAD_DB");
	
	
	/**
	 * Property to identify a value by a <code>java.lang.String</code>
	 * representation.
	 */
	private String commandAsString = "";

	/**
	 * Private constructor.
	 * 
	 * @param command A String representation of the command.
	 */
	private ActionCommandButtons(final String command) {
		this.commandAsString = command;
	}

	/**
	 * Overridden method to return a <code>java.lang.String</code>
	 * representation of the enum's value.
	 */
	@Override
	public String toString() {
		return this.commandAsString;
	}
}
