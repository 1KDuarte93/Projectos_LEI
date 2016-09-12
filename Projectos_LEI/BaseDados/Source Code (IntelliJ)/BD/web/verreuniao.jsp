<%-- 
    Document   : verreuniao
    Created on : Dec 4, 2014, 11:06:43 AM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Visualizar reuniões</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto</h1>
        <p>Reuniões: </p>
        <br>
        <c:forEach var="reunioesvar" items="${session.minhasreunioes}">
            <c:out value="${reunioesvar}"/>
            <br>
        </c:forEach>

    </body>
</html>
