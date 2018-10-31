<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<form id='loginForm' action='LoginServlet' method='post'>
	<table>
	<tr>
		<td><input type='text' name='username' class='inputText' placeholder='Username' required='' /></td>
		<td><input type='password' name='password' class='inputText' placeholder='Password' required='' /></td>
	</tr>
	<tr>
		<td><a href='register.jsp'>Registrieren</a></td>
		<td><input type='submit' class='inputSubmit' value='Login'/></td>
	</tr>
	</table>
</form>