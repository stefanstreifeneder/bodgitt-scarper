package suncertify.gui;

/**
 * Specifies the method arguments and return values
 * of the method <code>checkRecordHasChanged</code>.
 * Each controller class possesses the method.
 * <br> These are:
 * <br> <code>suncertify.gui.buyer.GuiControllerBuyer</code>
 * <br> <code>suncertify.gui.seller.GuiControllerSeller</code>
 * <br> <code>suncertify.gui.admin.GuiControllerAdmin</code>
 * 
 * <br> The method arguments are:
 * <br> CHANGE_MODE_END_GOON - indicates to display a dialog to exit the 
 * currently proceeding method or to go ahead
 * <br>	CHANGE_MODE_END_GOON_CHECK - indicates to display a dialog to exit the 
 * currently proceeding method, to go ahead or to check again the 
 * values of the Record
 * <br>
 * <br> The return values are:
 * <br> CHANGE_RETURN_EXIT_METH - indicates to exit the proceeding method
 * <br> CHANGE_RETURN_LOCK_WITHOUT_CHECK - indicates to continue the 
 * proceeding of the executing method without checking for new
 * changes of the Record's values
 * <br> CHANGE_RETURN_GOON_BY_CHECK - indicates to continue the 
 * proceeding of the executing method by checking for new
 * changes of the Record's values 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public enum EnumChangeMode {
	
	
	/**
	 * Indicates to display a dialog to exit the 
	 * currently proceeding method or to go ahead
	 * within the proceeding method. Should be used
	 * as a method a argument.
	 */
	CHANGE_MODE_END_GOON,
	
	/**
	 * Indicates to display a dialog to exit the 
	 * currently proceeding method, to go ahead or to check again the 
	 * values of the Record. Should be used
	 * as a method a argument.
	 */
	CHANGE_MODE_END_GOON_CHECK,
	
	/**
	 * Indicates to exit the proceeding method. Should be used
	 * as a return value.
	 */
	CHANGE_RETURN_EXIT_METH,
	
	/**
	 * Indicates to continue the proceeding of the executing method 
	 * without checking for new changes of the Record's values.
	 * Should be used as a return value.
	 */
	CHANGE_RETURN_LOCK_WITHOUT_CHECK,
	
	/**
	 * Indicates to continue the proceeding of the executing method 
	 * by checking for new changes of the Record's values. 
	 * Should be used as a return value.
	 */
	CHANGE_RETURN_GOON_BY_CHECK;
	
	
	
	/**
	 * Constructor, which is never called directly.
	 */
	private EnumChangeMode() {
		// never called directly
	}
}
