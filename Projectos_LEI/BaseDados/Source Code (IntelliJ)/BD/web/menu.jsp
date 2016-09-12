<%-- 
    Document   : menu
    Created on : Dec 3, 2014, 5:39:26 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto - Menu</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto!</h1>
        <br>
        <p>Olá, <%= session.getAttribute( "username" ) %>.</p>
        <s:url action="createmeeting_page" var="createmeeting"></s:url>
        <p><a href="${createmeeting}">Criar reunião</a></p>
        
        <s:url action="consultarreunioes_page" var="consultarreunioes"></s:url>
        <p><a href="${consultarreunioes}">Ver reunioes</a></p>
        
        <s:url action="convidaruserpage" var="convidaru"></s:url>
        <p><a href="${convidaru}">Convidar users para reuniao</a></p>
        
        <s:url action="actualizarconvites" var="actualizarconvites_"></s:url>
        <p><a href="${actualizarconvites_}">Aceitar convites</a></p>
        
        <s:url action="operacoesitems" var="operacoesitems_"></s:url>
        <p><a href="${operacoesitems_}">Operações em items</a></p>
        
        <s:url action="addactionitem" var="addactionitem_"></s:url>
        <p><a href="${addactionitem_}">Adicionar Action Items</a></p>
        
        <s:url action="tasks" var="tasks_"></s:url>
        <p><a href="${tasks_}">Ver/Marcar tarefas</a></p>
        
        <s:url action="chat" var="chat_"></s:url>
        <p><a href="${chat_}">Chat</a></p>
        
    </body>
</html>
