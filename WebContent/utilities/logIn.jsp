<%@ page language="java" pageEncoding="utf-8" contentType="text/html"%>
<!DOCTYPE html>
<form id='loginForm' action='UserSessionServlet' method='post'>
	<table>
	<tr>
		<td><input type='text' name='username' class='inputText' placeholder='Username' required='' /></td>
		<td><input type='password' name='password' class='inputText' placeholder='Password' required='' /></td>
	</tr>
	<tr>
		<td><a href='register.jsp' class="button fullSize">Registrieren</a></td>
		<td><input type='submit' class='inputSubmit button' value='Login'/></td>
	</tr>
	</table>
</form>