<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import= "model.*" %>

<html>
    <head>
        <title>JPA Guest Book Web Application Tutorial</title>
    </head>

    <body>
        <form method="POST" action="ExampleServlet">
            Name: <input type="text" name="name" />
            <input type="submit" value="Add" />
        </form>

        <hr><ol> <%
            @SuppressWarnings("unchecked") 
            List<exampleJPAEntity> exampleList = (List<exampleJPAEntity>)request.getAttribute("exampleJPAEntity");
            if (exampleList != null) {
            	for (exampleJPAEntity exampleObject : exampleList) { %>
                	<li> <%= exampleObject %> </li> <%
            	}
            }%>
        </ol><hr>
 
        <iframe src="http://www.objectdb.com/pw.html?web-eclipse"
            frameborder="0" scrolling="no" width="100%" height="30"> </iframe>
     </body>
 </html>