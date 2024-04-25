package suncertify.gui;

/**
 * This enum reflects the different roles a client can play (<code>BUYER</code>,
 * <code>SELLER</code>, <code>ADMIN</code>).<br>
 * Bodgitt and Scarper (BS) is a broker, which serves two participants. On the
 * one side it offers services of home improvement contractors (
 * <code>SELLER</code>) and on the other side home owners (<code>BUYER</code>)
 * can book the offered services.<br>
 * BS as broker is able to create offers like a seller and is able to
 * rent offers in behalf of a buyer. This role is represented by
 * an <code>ADMIN</code> client, who possesses all functionalities, that the 
 * application provides.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public enum ClientRole {

	/**
	 * A home owner is represented by a Buyer client, who is enabled 
	 * to rent a Record and to release a Record of a rented state.
	 */
	BUYER,

	/**
	 * An improvement contractor is represented by a Seller client,
	 * who is enabled to add, to delete and to modify a Record.
	 */
	SELLER,

	/**
	 * A customer service representative (CSR) of BS
	 * are represented by an Admin client, who possesses all functionalities.
	 */
	ADMIN;

	/**
	 * Constructor, which is never called directly.
	 */
	private ClientRole() {
		// never called directly
	}

}
