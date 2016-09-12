<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Login</title>
    </head>
    <body>
        <h1>Meeto</h1>
        <s:url action="newaccount_page" var="newaccount"></s:url>
        <p>Digite os seus dados ou crie uma <a href="${newaccount}">nova conta</a>.</p>
        <br>

        <s:form action="login">
            <s:textfield label="Username: " key="username"/>
            <s:password label="Password: " key="password" />
            <s:submit/>
        </s:form>        
    </body>
</html>
