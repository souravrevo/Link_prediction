package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class update_prolificity {
	
	public static void main(String[] args) throws SQLException, IOException {
		
		Connection conn = null;
		String sql = "";
		int auth_limit = 0;
		int counter = 1, year = 0;
		float prolificity = 0;
		float paper = 0;
		
		//connecting to DB
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/crawler";
			conn = DriverManager.getConnection(url, "root", "root");
			System.out.println("conn built");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		//fetching the limit of authors
		sql = "select max(auth_id) from auth_misc_info";
		Statement sta = conn.createStatement();
		ResultSet rs = sta.executeQuery(sql);
		if(rs.next() && !rs.getString(1).isEmpty())
			auth_limit = Integer.parseInt(rs.getString(1));
		
		while(counter<auth_limit){
			paper = 0;
			year = 0;
			prolificity = 0;
			
			//fetching the number of papers
			sql = "select count(1) from paper_all_info where year < 2012 and auth_id ="+counter;
			sta = conn.createStatement();
			rs = sta.executeQuery(sql);		
			if(rs.next()){
				paper = Integer.parseInt(rs.getString(1));
			}
			
			//fetching the number of years
			sql = "select IFNULL(max(year) - min(year),0) from paper_all_info where auth_id = "+
					counter+
					" and year != 0 and year < 2012";
			sta = conn.createStatement();
			rs = sta.executeQuery(sql);		
			if(rs.next()){
				year = Integer.parseInt(rs.getString(1));
			}
			
			//calculating prolificity
			if(paper > 0 && year > 0){
				prolificity = paper / year;
			}else{
				prolificity = 0;
			}
			
			
			sql  ="update auth_misc_info set prolificity = " + prolificity + 
					"where auth_id = " + counter;

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();			
			
			counter += 1;
			//conn.close();
		}
		
	}
}