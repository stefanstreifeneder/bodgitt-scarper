package suncertify.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Test program.
 * 
 * @author stefan
 *
 */
public class Test_db {
	

	/**
	 * Starts the program.
	 * 
	 * @param args Command line arguments.
	 */
	@SuppressWarnings("resource")
	public static void main(String args[]) {
		
		
		
		// Pfad zur Datenbank:
		// C:\ProgramData\MySQL\MySQL Server 5.7\Data
		String url = "jdbc:mysql://localhost/scjddb?" +
                "user=root&password=isabella";
		Connection con;
		Statement stmt;
		//String query = "select COF_NAME, PRICE from COFFEES";
		//String query = "select * from VENDORS where STAFF='4'";
//		String query = "select * from VENDORS where "
//				+ "NAME='name1' or "
//				+ "CITY='city2' or "
//				+ "TYPES='types3'"
//				;
		String query = "select * from VENDORS"
				//+ "		where NAME like 'name1'"
				;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {

			con = DriverManager.getConnection(url);

			stmt = con.createStatement();

			
			
			//loop
			ResultSet rs = null;
			
			
			for(int i = 0; i < 1000000; i++){
				rs = stmt.executeQuery(query);
				
				if((i%10000) == 0){
					//System.out.println("FindByTest: " + i);
					while (rs.next()) {
						String s = rs.getString("RECNO");
						String s2 = rs.getString("NAME");
						String s3 = rs.getString("CITY");
						String s4 = rs.getString("TYPES");
						String s5 = rs.getString("STAFF");
						String s6 = rs.getString("RATE");
						String s7 = rs.getString("OWNER");
						System.out.println(s + "   " + s2
								+ "   " + s3 + "   " + s4
								+ "   " + s5 + "   " + s6
								+ "   " + s7
								);
					}
				}
				
			}
			
			//old
//			rs = stmt.executeQuery(query);
//			System.out.println("FindByTest");
//			while (rs.next()) {
//				String s = rs.getString("RECNO");
//				String s2 = rs.getString("NAME");
//				String s3 = rs.getString("CITY");
//				String s4 = rs.getString("TYPES");
//				String s5 = rs.getString("STAFF");
//				String s6 = rs.getString("RATE");
//				String s7 = rs.getString("OWNER");
//				System.out.println(s + "   " + s2
//						+ "   " + s3 + "   " + s4
//						+ "   " + s5 + "   " + s6
//						+ "   " + s7
//						);
//			}
			
			
			

			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}

}
