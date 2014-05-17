package pack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import pack.Common_interest.*;

/**
 * Servlet implementation class cngf
 */
public class cngf extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static int auth_no = 0, coauth_no = 0, matrix_size = 0;
	static double[] v_degree;
	static double[] v_common_degree;
	static int source = 0, dest = 0; //Can vary later
	static byte[][] matrix;
	static byte[][] sub_matrix;
	static byte[] neighbour;
	static byte[] neighbour1;
	static byte[] neighbour2;
	static byte[] neighbour3;
	static byte[] neighbour_all;
	static double guidance = 0, similarity = 0;
	
	static void find_neighbour(int node,int level){
		//Find neighbours at multiple levels
		if((level == 0) && (node != 0) ){
			for(int i = 0; i < matrix_size; i++){
				if(matrix[node][i] == 1){
					neighbour[i] = 1;
				}
			}
		}
		if((level == 1) && (node !=0)){
			for(int i = 0; i < matrix_size; i++){
				if(matrix[node][i] == 1){
					neighbour1[i] = 1;
				}
			}
		}
		if((level == 2) && (node !=0)){
			for(int i = 0; i < matrix_size; i++){
				if(matrix[node][i] == 1){
					neighbour2[i] = 1;
				}
			}
		}
		if((level == 3) && (node !=0)){
			for(int i = 0; i < matrix_size; i++){
				if(matrix[node][i] == 1){
					neighbour3[i] = 1;
				}
			}
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cngf() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		String src = request.getParameter("source");
		ArrayList destination = new ArrayList();
		ArrayList destination_id = new ArrayList();
		ArrayList<Double> scores = new ArrayList<Double>();
		ArrayList affiliation = new ArrayList();
		ArrayList email = new ArrayList();
		ArrayList pic = new ArrayList();
		int counter = 0;
		
		try{
			//open connection and get matrix dimenxions
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select auth_id from auth_coauth_info_old where auth_name like \'"+src+"\' limit 1");
				while (rs.next()){
					System.out.println("id is:");
					System.out.println(rs.getString(1));
					source = Integer.parseInt(rs.getString(1));
					counter++;
			}
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}
		
		System.out.println("counter: "+counter);
		if(counter == 0){
			System.out.println("get out of page");
			session.setAttribute("error", "yes");
			response.sendRedirect("cngf_result_algo.jsp");
			return;
		}
		else{
			session.setAttribute("error", "no");
		}

		try{
			//open connection and get matrix dimenxions
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select max(co_auth_id) from auth_coauth_info_old");
			while (!rs.isLast()){
				if (rs.next()){
					matrix_size = Integer.parseInt(rs.getString(1)) + 1;
				}
			}
					   
			con.close();
		}catch (Exception e) {
			System.out.println("Exception caught"+e);
		}	
		
		matrix = new byte[matrix_size][matrix_size];
		neighbour = new byte[matrix_size];
		neighbour1 = new byte[matrix_size];
		neighbour2 = new byte[matrix_size];
		neighbour3 = new byte[matrix_size];
		neighbour_all = new byte[matrix_size];
		
		v_degree = new double[matrix_size];
		
		try{
		//Now assign matrix values based on data in table
			String url = "jdbc:mysql://localhost/crawler";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs1 = stmt
			.executeQuery("select auth_id,co_auth_id from auth_coauth_info_old where co_auth_id > 0");
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
		for(int i = 0; i < matrix_size; i++){
			for(int j = 0; j < matrix_size; j++){
				System.out.print(" ");
				System.out.print(matrix[i][j]);
		}
		System.out.println();
		}
		
		find_neighbour(source, 0);
		
		for(int i = 0 ; i < neighbour.length; i++){
			if(neighbour[i] == 1){
				find_neighbour(i, 1);
			}
		}
		for(int i = 0 ; i < neighbour1.length; i++){
			if(neighbour1[i] == 1){
				find_neighbour(i, 2);
			}
		}
		for(int i = 0 ; i < neighbour2.length; i++){
			if(neighbour2[i] == 1){
				find_neighbour(i, 3);
			}
		}
		//Consolidate all Neighbours in a vector
		for(int i = 0; i < matrix_size; i++){
			if((neighbour1[i] == 1) || (neighbour2[i] == 1) || (neighbour3[i] == 1)){
				neighbour_all[i] = 1;
			}
		}
		//Check neighbour conditions
		neighbour_all[source] = 0;
		for(int i = 0; i < matrix_size; i++){
			if((neighbour[i] == 1) && (neighbour_all[i] == 1)){
				neighbour_all[i] = 0;
			}
		}
		
		for(int k = 1; k < matrix_size; k++){	
			//Iterate for immediate neighbours
			if(neighbour_all[k] == 1){
				dest = k;
			}
			else{
				continue;
			}
				
			similarity = 0; //Initialize similarity at start of every loop
			
			for(int z = 0; z < v_degree.length; z++){
				v_degree[z] = 0;  //Initialize v_degree
			}
			
		if(!((matrix[source][dest] == 1) && (matrix[dest][source] == 1))){
	//Find v.degree
		for(int i = 0; i < matrix_size ; i++){
			if((matrix[source][i] == 1) && (matrix[dest][i] == 1)){
				//System.out.println(i);
				for(int j = 0; j < matrix_size; j++){
						if(matrix[i][j] == 1){
							v_degree[i] += 1;
						}
					}
			}
		}
		
	//Compute Sub Matrix
		sub_matrix = new byte[matrix_size][matrix_size];
		
		if((matrix[source][dest] == 1) && (matrix[dest][source] == 1)){
			sub_matrix[source][dest] = sub_matrix[dest][source] = 1;
		}
		for(int i = 0; i < matrix_size ; i++){
			if((matrix[source][i] == 1) && (matrix[dest][i] == 1)){
				sub_matrix[source][i] = matrix[source][i];
				sub_matrix[dest][i] = matrix[dest][i];
				sub_matrix[i][source] = matrix[i][source];
				sub_matrix[i][dest] = matrix[i][dest];
			}
		}
		
		//Calculate link between common neighbours
		for(int i = 0; i < matrix_size ; i++){
			for(int j = 0; j < matrix_size ; j++){
				if((matrix[source][i] == 1) && (matrix[dest][i] == 1) && (matrix[source][j] == 1) && (matrix[dest][j] == 1)){
					sub_matrix[i][j] = matrix[i][j];
				}
			}
		}
		
		//Find v_common_degree
		v_common_degree = new double[matrix_size];
		for(int z = 0; z < v_common_degree.length; z++){
			v_common_degree[z] = 0;  //Initialize v_common_degree
		}
			for(int i = 0; i < matrix_size ; i++){
				if((sub_matrix[source][i] == 1) && (sub_matrix[dest][i] == 1)){
					//System.out.println(i);
					for(int j = 0; j < matrix_size; j++){
							if(sub_matrix[i][j] == 1){
								v_common_degree[i] += 1;
							}
						}
				}
			}
		
			//Find Guidance and Similarity
			
			for(int i = 0; i < v_degree.length ; i++){
				if((v_degree[i] != 0) && (v_common_degree[i] != 0)){
					guidance = (v_degree[i])/(Math.log(v_common_degree[i]));
					similarity += guidance;
				}
			}
			if(similarity == Double.POSITIVE_INFINITY){
				similarity = 0;
			}
			if(similarity >= 71.0){
				System.out.println("Potential link predicted between: "+source+"and "+k);
				System.out.println(similarity);
				scores.add(similarity);
				destination_id.add(k);
				//Get destination names from database
				try{
					//open connection and get matrix dimenxions
					String url = "jdbc:mysql://localhost/crawler";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, "root", "root");
					Statement stmt = con.createStatement();
					ResultSet rs = stmt
							.executeQuery("select auth_name from auth_all_info where auth_id = \'"+k+"\' limit 1");
					while (!rs.isLast()){
						if (rs.next()){
							destination.add(rs.getString(1));
						}
					}
					con.close();
				}catch (Exception e) {
					System.out.println("Exception caught"+e);
				}
			}
		}
			
		else{
			System.out.println("Already a link between the the nodes !!!");
		}
		}
		
		destination_id = Common_interest.calc_interest(source, destination_id, scores);
		destination.clear();
		
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
		
		Collections.sort(scores);
		session.setAttribute("scores", scores);
		session.setAttribute("destination", destination);
		session.setAttribute("destination_id", destination_id);
		for(int i = 0; i < destination.size(); i++){
			try{
				//open connection and get matrix dimenxions
				String url = "jdbc:mysql://localhost/crawler";
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = DriverManager.getConnection(url, "root", "root");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt
				.executeQuery("select * from auth_misc_info where auth_id = \'"+destination_id.get(i)+"\' limit 1");	
					while (rs.next()){
						affiliation.add(rs.getString(2));
						email.add(rs.getString(3).substring(18));
						pic.add("http://scholar.google.com"+rs.getString(4));
						if(pic.get(i).equals("http://scholar.google.com")){
							pic.remove(i);
							pic.add("images/alt.png");
						}
					}
				rs.close();
				stmt.close();
				con.close();
			}catch (Exception e) {
				System.out.println("Exception caught"+e);
			}
		}
		session.setAttribute("affiliation", affiliation);
		session.setAttribute("email", email);
		session.setAttribute("pic", pic);
		session.setAttribute("source", src);

		response.sendRedirect("cngf_result_algo.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
