package suncertify.gui;

/**
 * Specifies the action commands of the menu items of the menu bar,
 * which were used by all client roles.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public enum ActionCommandMenuItems {	

	/**
	 * Action command to reload the content of the database file, 
	 * of the menu <code>suncertify.gui.MenuFilter</code>.
	 */
	MENUFILTER_REFRESH("MENUFILTER_REFRESH"), 
	
	/**
	 * Action command to search for a name 
	 * of the menu <code>suncertify.gui.MenuFilter</code>.
	 */
	MENUFILTER_NAME("MENUFILTER_NAME"), 
	
	/**
	 * Action command to search for the city 
	 * of the menu <code>suncertify.gui.MenuFilter</code>.
	 */
	MENUFILTER_CITY("MENUFILTER_CITY"), 
	
	/**
	 * Action command to search for all 
	 * of the menu <code>suncertify.gui.MenuFilter</code>.
	 */
	MENUFILTER_ALL("MENUFILTER_ALL"), 
	
	/**
	 * Action command for the check box of the menu  <code>suncertify.gui.MenuFilter</code>
	 * to set the focus of the table of the main window.
	 */
	MENUFILTER_FOCUS("MENUFILTER_FOCUS"),
	
	/**
	 * Action command to rent a Record
	 * of the menu <code>suncertify.gui.buyer.MenuBookingBuyer</code>.
	 */
	MENUBOOKING_RENT("MENUBOOKING_RENT"), 
	
	/**
	 * Action command to release a Record
	 * of the menu <code>suncertify.gui.buyer.MenuBookingBuyer</code>.
	 */
	MENUBOOKING_RELEASE("MENUBOOKING_RELEASE"), 
	
	/**
	 * Action command to read a Record by Record number
	 * of the menu <code>suncertify.gui.seller.MenuExtraSeller</code>.
	 */
	MENUEXTRA_READ("MENUEXTRA_READ"), 
	
	/**
	 * Action command to update a Record 
	 * of the menu <code>suncertify.gui.seller.MenuExtraSeller</code>.
	 */
	MENUEXTRA_UPDATE("MENUEXTRA_UPDATE"), 
	
	/**
	 * Action command to delete a Record 
	 * of the menu <code>suncertify.gui.seller.MenuExtraSeller</code>.
	 */
	MENUEXTRA_DELETE("MENUEXTRA_DELETE"), 
	
	/**
	 * Action command to add a Record 
	 * of the menu <code>suncertify.gui.seller.MenuExtraSeller</code>.
	 */
	MENUEXTRA_ADD("MENUEXTRA_ADD");
	
	/**
	 * Property to identify a value by a <code>String</code>
	 * representation.
	 */
	private String commandAsString = "";
	
	/**
	 * Private constructor.
	 * 
	 * @param command A String representation of the command.
	 */
	private ActionCommandMenuItems(final String command) {
		this.commandAsString = command;
	}

	/**
	 * Overridden method to return a <code>String</code>
	 * representation of the enum's value.
	 */
	@Override
	public String toString() {
		return this.commandAsString;
	}
}
