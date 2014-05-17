package pack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Common_interest {
	
	static ArrayList calc_interest(int source_id, ArrayList destination_id, ArrayList scores){
		
		double final_score = 0;
		int total_common = 0, number_common = 0, i = 0, j = 0, k = 0;
		ArrayList source_interest = new ArrayList();
		ArrayList source_interest_level = new ArrayList();
		ArrayList sorted_list = new ArrayList();
		ArrayList list_return = new ArrayList();
	
		try{
			//open connection and get matrix dimenxions
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from auth_all_info where auth_id = \'"+source_id+"\'");
				while (rs.next()){
					source_interest.add(rs.getString(3).toLowerCase());
					source_interest_level.add(rs.getString(5));
			}
			rs.close();
			stmt.close();
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}
		
		for(i = 0; i < destination_id.size() ; i++){
			total_common = 0;
			number_common = 0;
		
			try{
				//open connection and get matrix dimenxions
				String url = "jdbc:mysql://localhost/crawler";
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = DriverManager.getConnection(url, "root", "root");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select * from auth_all_info where auth_id = \'"+destination_id.get(i)+"\'");
					while (rs.next()){
						
						for(j = 0; j < source_interest.size(); j++){
							
							if(source_interest.get(j).equals(rs.getString(3).toLowerCase())){
								total_common++;
								number_common += Integer.parseInt(rs.getString(5)) + Integer.parseInt(source_interest_level.get(j).toString());
							}
						}
												
					}
				rs.close();
				stmt.close();
				con.close();
			}catch (Exception e) {
				System.out.println("Exception caught"+e);
			}
			
			final_score = (1 + number_common)*(2 - (1.0f/(1+total_common)))*(1-(1.0f/(Double.parseDouble(scores.get(i).toString()))));
			//if(Math.floor(final_score)){
			if(Integer.toString(((int)final_score)).length() < 2){
				sorted_list.add(Double.toString(final_score)+"-"+destination_id.get(i));
			}else{
			sorted_list.add(Double.toString(final_score)+"-"+destination_id.get(i));
			}
		}
		System.out.println(destination_id);
		System.out.println("raw scores: "+scores);
		System.out.println("sorted final scores: "+sorted_list);
		Collections.sort(sorted_list);
		Collections.reverse(sorted_list);
		for(k = 0; k < sorted_list.size(); k++){ 
			String []str =	sorted_list.get(k).toString().split("-");
			list_return.add(str[1]);
		}
		
		return (list_return);
	}
	
	public static void main(String []args){
		
		
	}

}
