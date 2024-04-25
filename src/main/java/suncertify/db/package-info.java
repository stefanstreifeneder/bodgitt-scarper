
/**
 * The package provides access to the database file. 
 * Clients access the database by a public interface using the facade pattern,
 * which hides the classes and an interface to read and to write
 * to the database file.
 * <br> Additional it establishes a locking system to
 * guarantee persistence. 
 * Further more it contains all crucial exceptions 
 * of the database management system.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
package suncertify.db;