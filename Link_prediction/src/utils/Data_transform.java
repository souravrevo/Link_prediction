package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Data_transform {

	public static void main(String [] args){
		
		int auth_id = 0, status = 0;
		String auth_name = "";
		
		try{
			//open connection and get matrix dimenxions
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			PreparedStatement stmt1 = con.prepareStatement("UPDATE auth_coauth_info SET co_auth_id=? where co_auth_name=?");
			
			 //stmt1.setString(1,lname);
			 //stmt1.setString(2,email);
			 //stmt1.setString(3,pwd);
			ResultSet rs = stmt
					.executeQuery("select distinct auth_name,auth_id from auth_all_info");
			while (!rs.isLast()){
				if (rs.next()){
					auth_name = rs.getString(1);
					auth_id = Integer.parseInt(rs.getString(2));
					stmt1.setString(1,Integer.toString(auth_id));
					stmt1.setString(2,auth_name);
					stmt1.executeUpdate();
					System.out.println("Status: "+stmt1);
				}
			}	   
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}	
		
	}
	
}
