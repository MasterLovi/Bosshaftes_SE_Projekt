<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import= "model.*" %>

<html>
    <head>
        <title>JPA Guest Book Web Application Tutorial</title>
        <link rel="stylesheet" type="text/css" href="Stylesheets/main.css">
        <link rel="stylesheet" type="text/css" href="Stylesheets/header.css">
		<link rel="stylesheet" type="text/css" href="Stylesheets/footer.css">
		
    </head>

    <body style="height: 800px;">
    	<%@ include file="utilities/header.jsp" %>
        <form method="POST" action="ExampleServlet">
            Name: <input type="text" name="name" />
            <input type="submit" value="Add" />

        <hr><ol> <%
            @SuppressWarnings("unchecked") 
            List<exampleJPAEntity> exampleList = (List<exampleJPAEntity>)request.getAttribute("example");
            if (exampleList != null) {
            	for (exampleJPAEntity exampleObject : exampleList) { %>
                	<li> <%= exampleObject %> </li> <%
            	}
            }%>
        </ol><hr>
 
        <iframe src="http://www.objectdb.com/pw.html?web-eclipse"
            frameborder="0" scrolling="no" width="100%" height="30"> </iframe>
            
        <%@ include file="utilities/footer.jsp" %>
     </body>
 </html>