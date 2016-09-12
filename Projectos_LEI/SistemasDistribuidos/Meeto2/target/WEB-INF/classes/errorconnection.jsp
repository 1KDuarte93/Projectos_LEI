<%-- 
    Document   : errorconnection
    Created on : Dec 3, 2014, 6:28:42 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
        
        <h1>Meeto</h1>
        <p>Erro de conexão.</p>
    </body>
</html>
