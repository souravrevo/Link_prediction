<!DOCTYPE HTML>
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
          <li><a href="cngf_algo.jsp">CNGF</a></li>
          <li><a href="nfpp_algo.jsp">Prolific NN</a></li>
          <li><a href="random.jsp">Random Walk</a></li>
          <li class="selected"><a href="pagerank.jsp">Page Rank</a></li>
        </ul>

      </div>
    </div>
    <div id="site_content">
      
      <div id="content">
        <form name="form1" action="sam" method="get">
	<table align="center">
	<tr>
	<td id="style1"> Name </td>
	<td id="style1"> <input type="text" id="source" name="source"/> </td>
	</tr>
	<tr>
	<td id="style1" colspan="2" align="center"><input type="submit" id="button"></input></td>
	</tr>
	</table>
	</form>
        
   
       
        
      </div>
    </div>
    <div id="footer">
     Project @Social Networks
    </div>
  </div>
</body>
</html>
