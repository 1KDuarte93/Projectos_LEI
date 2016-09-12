<%-- 
    Document   : convidaruser
    Created on : Dec 5, 2014, 12:15:29 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Erro de conexão</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto - Erro de conexão</h1>
        <c:forEach var="reunioesvar" items="${session.minhasreunioes}">
            <c:out value="${reunioesvar}"/>
            <br>
        </c:forEach>
            
        <s:form action="convidaruser">
            <s:textfield label="Id da reuniao" key="id_reuniao"/>
            <s:textfield label="Username" key="userconvidado"/>
            <s:submit/>
        </s:form>
            
        <s:form action="sair">
            <s:submit value="Sair"/>
        </s:form>
    </body>
</html>
