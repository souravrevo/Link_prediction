<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>

<head>
  <title>Link Prediction</title>
  <meta name="description" content="website description" />
  <meta name="keywords" content="website keywords, website keywords" />
  <meta http-equiv="content-type" content="text/html; charset=windows-1252" />
  <link rel="stylesheet" type="text/css" href="style/style.css" title="style" />

<script>


</script>

<style>

#style1
{
margin-left:auto;
margin-right:auto;
width:40%;
color:green;
text-align:center;
background-color:#b0e0e6;
}

</style>

</head>

<body>
  <div id="main">
    <div id="header">
      <div id="logo">
        <div id="logo_text">
          <!-- class="logo_colour", allows you to change the colour of the text -->
          <h1><a href="main.jsp">Link<span class="logo_colour">Prediction</span></a></h1>
        </div>
      </div>
       <div align="right">
       </div>
      <div id="menubar">
        <ul id="menu">
          <!-- put class="selected" in the li tag for the selected page - to highlight which page you're on -->
          <li><a href="main.jsp">Home</a></li>
          <li class="selected"><a href="cngf_algo.jsp">CNGF</a></li>
          <li><a href="nfpp_algo.jsp">Prolific NN</a></li>
          <li><a href="random.jsp">Random Walk</a></li>
          <li><a href="pagerank.jsp">Page Rank</a></li>
        </ul>

      </div>
    </div>
    <div id="site_content">
      
      <div id="content">
      
    	<%
ArrayList<Double> scores = new ArrayList<Double>();
ArrayList destination = new ArrayList();
ArrayList destination_id = new ArrayList();
ArrayList affiliation = new ArrayList();
ArrayList email = new ArrayList();
ArrayList pic = new ArrayList();
ArrayList checkDup = new ArrayList();
String source = "";
String err = "";
scores = (ArrayList)session.getAttribute("scores");
destination = (ArrayList)session.getAttribute("destination");
destination_id = (ArrayList)session.getAttribute("destination_id");
affiliation = (ArrayList)session.getAttribute("affiliation");
email = (ArrayList)session.getAttribute("email");
pic = (ArrayList)session.getAttribute("pic");
source = (String)session.getAttribute("source");
err = (String)session.getAttribute("error");

if(!err.equals("yes")){
%><table width="500" height="500">
<tr>
<td>
</td>
<td>
Name
</td>
<td>
Affiliation
</td>
<td>
Email
</td>
</tr>

 <% 
for(int i = 0; i < destination.size(); i++){	
	if(source.toLowerCase().equals(destination.get(i).toString().toLowerCase())){
		continue;
	}
	if(checkDup.contains(destination.get(i))){
		continue;
	}
	checkDup.add(destination.get(i));
	%>
	<tr>
	<td>
	<img src='<%=pic.get(i) %>' alt="N/A" height="42" width="42">
	</td>
	<td>
	<%=destination.get(i)%> 
	</td>
	<td>
	<%=affiliation.get(i)%>
	</td>
	<td>
	<%=email.get(i)%>
	</td>
	</tr>
	<% 
}
}
else{
out.println("No record for author name");
}
%>
</table>
    	    
   	
       
        
      </div>
    </div>
    <div id="footer">
     Project @Social Networks
    </div>
  </div>
</body>
</html>
