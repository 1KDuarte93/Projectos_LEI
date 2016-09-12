<%-- 
    Document   : newaccount
    Created on : Dec 3, 2014, 6:04:11 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Nova conta</title>
    </head>
    <body>
        <h1>Meeto</h1>
        <p>Nova conta</p>
        <s:form action="newaccount">
            <s:textfield label="Username: " key="username"/>
            <s:password label="Password: " key="password" />
            <s:submit/>
        </s:form>
    </body>
</html>
