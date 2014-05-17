package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

//Calculate interest_count column of auth_all_info table
public class interest {

	public static void main(String []args){
		
		String name = "";
		String interest = "";
		String interest_db = "";
		String auth_id = "";
		
		try{
			//open connection and get matrix dimenxions
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from auth_all_info");
			ResultSet rs1 = null;
		    PreparedStatement stmtp = null;
			while (rs.next()){
					name = rs.getString(1);
					auth_id = rs.getString(2);
					interest = rs.getString(3).toLowerCase();
					
					interest_db = " "+interest+" ";
					interest_db = interest_db.replace(" ", "%");
					rs1 = stmt1
							.executeQuery("select count(*) from paper_all_info where auth_id=\'"+auth_id+"\' and paper like \'"+interest_db+"\'");
					
					while (rs1.next()){
						stmtp = con.prepareStatement("UPDATE auth_all_info SET interest_count=? WHERE auth_id=\'"+auth_id+"\' and auth_interest=\'"+interest+"\'");
						stmtp.setString(1,rs1.getString(1));
						System.out.println(rs1.getString(1));
						int ii = stmtp.executeUpdate();
					}
				}
				
			rs.close();
			rs1.close();
			stmtp.close();
			stmt.close();
			stmt1.close();
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}
		
	}
	
}
