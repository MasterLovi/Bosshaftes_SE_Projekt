<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<div id='rightIcon'>
	<input type="hidden" id="userId" value=<% out.println(session.getAttribute("userid")); %> >
	<a href='#'><i class="bigIcon material-icons">account_circle</i></a>
	<a href='UserSessionServlet?logout=true' title="Logout"><i class="floatRight bigIcon material-icons">exit_to_app</i></a>
</div>