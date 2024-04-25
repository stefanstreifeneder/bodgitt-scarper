
/**
 * Contains the GUI for an Admin client. 
 * Due to the public interface, which differs between three
 * client roles: Admin - Vendor - Buyer, each client
 * needs his individual access to the database file,
 * in this case this is an object of type GuiControllerAdmin.<br>
 * An Admin client uses graphical components
 * of a Vendor client to add, delete and update a Record 
 * and of a Buyer client to rent and release a Record
 * of a rented state. Additional there are components,
 * which are used of all clients, which reside in suncertify.gui.<br>
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
package suncertify.gui.admin;