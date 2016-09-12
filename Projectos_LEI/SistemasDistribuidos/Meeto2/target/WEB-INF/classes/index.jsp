
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
        Img do logo do Meeto       

        <s:url action="loginverif" var="tologinpage"></s:url>
        <p><a href="${tologinpage}">Login/Register</a></p>
        
    </body>
</html>
