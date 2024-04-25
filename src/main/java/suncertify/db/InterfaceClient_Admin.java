package suncertify.db;

/**
 * The public interface with the broadest access level. It does not possess
 * a method or anything else. All methods are inherited, because this 
 * interface is subtype of all public interfaces. 
 * <br> These are:
 * <br> <code>suncertify.db.InterfaceClient_ReadOnly</code>
 * <br> <code>suncertify.db.InterfaceClient_LockPermission</code>
 * <br> <code>suncertify.db.InterfaceClient_Buyer</code>
 * <br> <code>suncertify.db.InterfaceClient_Seller</code>
 * 
 * @see InterfaceClient_LockPermission
 * @see InterfaceClient_ReadOnly
 * @see InterfaceClient_Buyer
 * @see InterfaceClient_Seller
 * @author stefan.streifeneder@gmx.de
 * 
 *
 */
public interface InterfaceClient_Admin extends 
		InterfaceClient_Buyer, InterfaceClient_Seller {
	// all methods are inherited
}
