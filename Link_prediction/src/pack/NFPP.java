package pack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class NFPP
 */
public class NFPP extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	static int matrix_size = 0;
	static float src_pro = 0, dest_pro = 0;
	static int source = 0, dest = 0; 
	static byte[][] matrix;
	static byte[] friends;
	static byte[] neighbour1;
	static byte[] neighbour2;
	static byte[] neighbour_all;
	
	static void find_neighbour(int node,int level){
		
		if(node != 0){
			for(int i = 0; i < matrix_size; i++){
				if(matrix[node][i] == 1){
					
					if(level == 0)
						friends[i] = 1;
					else if(level == 1)
						neighbour1[i] = 1;
					else if(level == 2)
						neighbour2[i] = 1;
				}
			}
		}
	}
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NFPP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String src = request.getParameter("source");
		ArrayList destination = new ArrayList();
		ArrayList destination_id = new ArrayList();
		ArrayList affiliation = new ArrayList();
		ArrayList email = new ArrayList();
		ArrayList pic = new ArrayList();
		ArrayList score_list = new ArrayList();
		
		int counter = 0;
		
		try{
			//open connection and get matrix dimensions
			String url = "jdbc:mysql://localhost:3306/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt
					.executeQuery("select max(co_auth_id) from auth_coauth_info");
			while (!rs.isLast()){
				if (rs.next()){
					matrix_size = Integer.parseInt(rs.getString(1)) + 1;
				}
			}
			
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select auth_id from auth_coauth_info_old where auth_name = \'"+src+"\' limit 1");
			while (!rs.isLast()){
				if (rs.next()){
					source = Integer.parseInt(rs.getString(1));
					counter++;
				}
			}
			
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}
		
		if(counter == 0){
			session.setAttribute("error", "yes");
			response.sendRedirect("nfpp_result.jsp");
			return;
		}
		else{
			session.setAttribute("error", "no");
		}
		
		matrix = new byte[matrix_size][matrix_size];
		friends = new byte[matrix_size];
		neighbour1 = new byte[matrix_size];
		neighbour2 = new byte[matrix_size];
		neighbour_all = new byte[matrix_size];
		
		try{
			//Now assign matrix values based on data in table
				String url = "jdbc:mysql://localhost:3306/crawler";
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = DriverManager.getConnection(url, "root", "root");
				Statement stmt = con.createStatement();
				ResultSet rs1 = stmt
				.executeQuery("select auth_id, co_auth_id from auth_coauth_info_old");
				while (!rs1.isLast()){
					if (rs1.next()){
						matrix[Integer.parseInt(rs1.getString(1))][Integer.parseInt(rs1.getString(2))] = 1;
						matrix[Integer.parseInt(rs1.getString(2))][Integer.parseInt(rs1.getString(1))] = 1;
				     }
				}
				con.close();
			}catch(Exception e){
				System.out.println("Exception Caught"+e);
		}
		
		//Display matrix
		/*for(int i = 0; i < matrix_size; i++){
			for(int j = 0; j < matrix_size; j++){
				System.out.print(" ");
				System.out.print(matrix[i][j]);
		}
		System.out.println();
		}*/
		
		find_neighbour(source, 0);
		
		for(int i = 0 ; i < friends.length; i++){
			if(friends[i] == 1){
				find_neighbour(i, 1);
			}
		}
		
		for(int i = 0 ; i < neighbour1.length; i++){
			if(neighbour1[i] == 1){
				find_neighbour(i, 2);
			}
		}
		
		//Consolidate all neighbour in a vector
		for(int i = 0; i < matrix_size; i++){
			if((neighbour1[i] == 1) || (neighbour2[i] == 1)){
				neighbour_all[i] = 1;
			}
		}
		
		//Check neighbour conditions
		neighbour_all[source] = 0;
		
		for(int i = 0; i < matrix_size; i++){
			if((friends[i] == 1) && (neighbour_all[i] == 1)){
				neighbour_all[i] = 0;
			}
		}
		
		//Display neighbours_all
		//for(int i = 0; i < matrix_size; i++){
		//	System.out.println(neighbour_all[i]);
		//}
		counter = 0;
		
		for(int k = 1; k < matrix_size; k++){	
			//Iterate for neighbours upto 2 levels
			if(neighbour_all[k] == 1){
				dest = k;
			}
			else{
				continue;
			}
			
			try{
					//extracting prolificity of users
					String url = "jdbc:mysql://localhost:3306/crawler";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, "root", "root");
					
					String sql = "select prolificity, auth_id from auth_misc_info where auth_id in ("
									+source +","
									+dest +")";
					
					Statement sta = con.createStatement();
					ResultSet rs = sta.executeQuery(sql);
					
					while (!rs.isLast()){
						if (rs.next()){
							if(Integer.parseInt(rs.getString(2)) == source )
								src_pro = Float.parseFloat(rs.getString(1));
							else if(Integer.parseInt(rs.getString(2)) == dest )
								dest_pro = Float.parseFloat(rs.getString(1));
					     }
					}
					con.close();
				}catch(Exception e){
					System.out.println("Exception Caught"+e);
			}
			
			float score = Math.abs(src_pro-dest_pro);
			System.out.println("score"+ score);
			
			if (score>3)
				neighbour_all[k] = 0;
			else{
				
				
				if(score == 0)
					score = 100;
				else if(score<.1)
					score=score*100;
				else if(score<1)
					score=score*10;
				else if(score<2)
					score=score*5;
				else
					score=score*2;
				
				score_list.add(score);
				destination_id.add(dest);
				counter++;
				/*try{
					//extracting misc details
					String url = "jdbc:mysql://localhost:3306/crawler";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, "root", "root");
					
					String sql = "select all_info.auth_name, "
							+"misc.auth_affiliation, "
							+"misc.auth_email, misc.photo_link "
							+"from auth_all_info all_info, auth_misc_info misc "
							+"where all_info.auth_id = misc.auth_id "
							+"and all_info.auth_id = " + dest
							+" group by all_info.auth_id";
					
					Statement sta = con.createStatement();
					ResultSet rs = sta.executeQuery(sql);
					
					if (rs.next()){
						destination.add(rs.getString(1));
						destination_id.add(dest);
						affiliation.add(rs.getString(2));
						email.add(rs.getString(3));
						pic.add("http://scholar.google.com"+rs.getString(4)) ;
				     }
					
					con.close();
				}catch(Exception e){
					System.out.println("Exception Caught"+e);
				}*/
				
			}
		}
		
		System.out.println(destination_id);
		System.out.println(source);
		System.out.println(score_list);
		destination_id = Common_interest.calc_interest(source, destination_id, score_list);
		destination.clear();
		System.out.println(destination_id);
		
		for(int p = 0; p < destination_id.size(); p++){
			try{
				//open connection and get matrix dimenxions
				String url = "jdbc:mysql://localhost/crawler";
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = DriverManager.getConnection(url, "root", "root");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select auth_name from auth_all_info where auth_id = \'"+destination_id.get(p)+"\' limit 1");
				while (rs.next()){
				
					destination.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			con.close();
			}catch (Exception e) {
				System.out.println("Exception caught"+e);
			}
		}
		
		
		
		for(int i = 0; i < destination.size(); i++){
			try{
				//open connection and get matrix dimenxion	
				String url = "jdbc:mysql://localhost/crawler";
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = DriverManager.getConnection(url, "root", "root");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt
				.executeQuery("select * from auth_misc_info where auth_id = \'"+destination_id.get(i)+"\'");
				
				while (rs.next()){
						//System.out.println(rs.getString(1));
						affiliation.add(rs.getString(2));
						email.add(rs.getString(3).substring(18));
						pic.add("http://scholar.google.com"+rs.getString(4));
						if(pic.get(i).equals("http://scholar.google.com")){
							pic.remove(i);
							pic.add("images/alt.png");
					    }
						//System.out.println("dest serc: "+destination_id.get(i));
					}
				con.close();
			}catch (Exception e) {
				System.out.println("Exception caught"+e);
			}
		}
		
		if(counter == 0){
			session.setAttribute("error", "yes");
			response.sendRedirect("nfpp_result.jsp");
			return;
		}
		else{
			session.setAttribute("error", "no");
		}
		
		session.setAttribute("affiliation", affiliation);
		session.setAttribute("email", email);
		session.setAttribute("pic", pic);
		session.setAttribute("destination", destination);
		session.setAttribute("destination_id", destination_id);
		session.setAttribute("source", src);
		
		response.sendRedirect("nfpp_result_algo.jsp");
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
