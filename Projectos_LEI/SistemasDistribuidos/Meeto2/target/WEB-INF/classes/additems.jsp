<%-- 
    Document   : additems
    Created on : Dec 4, 2014, 10:33:49 AM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Adicionar items</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto - Adicionar items</h1>
        Reuniao Criada:<br>
        <%= session.getAttribute( "meeting_adicionado_string" ) %>
        <br><br> Items:<br>
        <c:forEach var="reunioesx" items="${session.agendaitems}">
            <c:out value="${reunioesx}"/>
            <br>
        </c:forEach>
            
            
        <s:form action="additem_action">
            <s:textfield label="Item " key="item"/>
            <s:submit value="Adicionar"/>
        </s:form>
            
        <s:form action="sair">
            <s:submit value="Sair"/>
        </s:form>
    </body>
</html>
