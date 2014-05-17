package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;

//Calculate interest_level column of auth_all_info table

public class interest_level {

	public static void main(String []args){
		
		String auth_id = "";
		ArrayList arr = new ArrayList();
		int i = 0;
		
		try{
			//open connection and get matrix dimenxions
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select distinct auth_id from auth_all_info");
			PreparedStatement stmtp = null;
			ResultSet rs1 = null; 
				while (rs.next()){
					arr.clear();
					auth_id = rs.getString(1);
					rs1 = stmt1.executeQuery("select distinct interest_count from auth_all_info where auth_id=\'"+auth_id+"\'");
					
					while(rs1.next()){
						arr.add(rs1.getString(1));
					}
					Collections.sort(arr);
					Collections.reverse(arr);
					//System.out.println(arr);
					
					for(i = 1; i <= arr.size(); i++){
						stmtp = con.prepareStatement("UPDATE auth_all_info SET interest_level=? WHERE auth_id=\'"+auth_id+"\' and interest_count=\'"+arr.get(i-1)+"\'");
						stmtp.setString(1,Integer.toString(i));
						//System.out.println(rs1.getString(1));
						int ii = stmtp.executeUpdate();
					}
				
					System.out.println("a");
				}
			rs.close();
			rs1.close();
			stmt1.close();
			stmt.close();
			stmtp.close();
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}
	}
	
}
