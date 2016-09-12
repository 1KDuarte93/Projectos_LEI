
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Criar reunião</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto!</h1>
        <p>Criar reunião</p>
        <s:form action="createmeeting_action">
            <s:textfield label="Titulo " key="titulo"/>
            <s:textfield label="Objectivo " key="objectivo"/>
            <s:textfield label="Localizacao " key="localizacao"/>
            <s:textfield label="Hora de inicio " key="horainicio"/>
            <s:textfield label="Data " key="data"/>
            <s:textfield label="Duração " key="duracao"/>
            <s:submit/>
        </s:form>
    </body>
</html>
