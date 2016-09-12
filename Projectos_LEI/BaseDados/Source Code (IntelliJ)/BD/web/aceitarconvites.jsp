<%-- 
    Document   : aceitarconvites
    Created on : Dec 5, 2014, 1:14:56 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Aceitar convites</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>

        <h1>Aceitar convites</h1>
        
        <c:forEach var="convitesx" items="${meusconvites}">
            <c:out value="${convitesx}"/>
            <br>
        </c:forEach>
            
            <s:form action="aceitarconv">
                <s:textfield label="Id da reuniao" key="idconvite"/>
                <s:submit value="Aceitar"/>
            </s:form>
        
        <s:form action="sair">
            <s:submit value="Sair"/>
        </s:form>
    </body>
</html>
