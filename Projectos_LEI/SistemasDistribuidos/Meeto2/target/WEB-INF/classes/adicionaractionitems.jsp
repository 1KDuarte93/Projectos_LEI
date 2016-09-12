<%-- 
    Document   : adicionaractionitems
    Created on : Dec 7, 2014, 4:14:18 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Action items</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto - Action items</h1>
        <c:forEach var="reunioesvar" items="${session.minhasreunioes}">
            <c:out value="${reunioesvar}"/>
            <br>
        </c:forEach>
            <br>
        <p>Adicionar um Action Item</p>
        <s:form action="addactionitemuser">
            <s:textfield label="Id da reuniao" key="idreuniao"/>
            <s:textfield label="Username" key="usertask"/>
            <s:textfield label="Descrição" key="descricao"/>
            <s:submit value="Adicionar"/>
        </s:form>
        
    </body>
</html>
