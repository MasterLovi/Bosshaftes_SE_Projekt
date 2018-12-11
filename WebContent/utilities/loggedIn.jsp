<%@ page language="java" pageEncoding="utf-8" contentType="text/html"%>
<!DOCTYPE html>
<div id='rightIcon'>
	<input type="hidden" id="userId" value=<% out.println(session.getAttribute("userid")); %> >
	<a href="#" onClick="loadUserRoutePopup()"><i class="bigIcon material-icons">person_pin_circle</i></a>
	<a href='UserSessionServlet?logout=true' title="Logout"><i class="floatRight bigIcon material-icons">exit_to_app</i></a>
</div>