<%-- 
    Document   : tarefas
    Created on : Dec 7, 2014, 5:47:49 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Tarefas</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto</h1>
        <p>Tarefas:</p>
        <br>
        <c:forEach var="tarefasesvar" items="${session.tarefas}">
            <c:out value="${tarefasesvar}"/>
            <br>
        </c:forEach>
        
        <p>Selecione a tarefa que pretende marcar.</p>
        <s:form action="fazertarefa">
            <s:textfield label="Id da tarefa" key="id_tarefa"/>
            <s:submit value="Feita!"/>
        </s:form>
            
    </body>
</html>
