<%-- 
    Document   : operacoesitems
    Created on : Dec 6, 2014, 9:26:04 PM
    Author     : kduarte
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meeto</title>
    </head>
    <body>
        <s:url action="logout_page" var="logout"></s:url>
        <div align="right"><a href="${logout}">Logout</a> </div>
        
        <h1>Meeto</h1>
        <p>Operações em items</p>

        <p>Reuniões</p>
        <c:forEach var="reunioesvar" items="${session.minhasreunioes}">
            <c:out value="${reunioesvar}"/>
            <br>
        </c:forEach>
            <p>Adicionar um item:</p>
            <s:form action="adicionaragendaitem">
                <s:textfield label="Id da reuniao" key="id_reuniao"/>
                <s:textfield label="Item" key="descricao_item"/>
                <s:submit value="Adicionar"/>
            </s:form>

            <p>Modificar um item:</p>
            <s:form action="editaragendaitem">
                <s:textfield label="Id da reuniao" key="id_reuniao"/>
                <s:textfield label="Id do item" key="id_item"/>
                <s:textfield label="Item" key="descricao_item"/>
                <s:submit value="Editar"/>
            </s:form>

            <p>Eliminar um item:</p>
            <s:form action="apagaragendaitem">
                <s:textfield label="Id da reuniao" key="id_reuniao"/>
                <s:textfield label="Id do item" key="id_item"/>
                <s:submit value="Apagar"/>
            </s:form>

            <p>Adicionar uma key decision a um item:</p>
            <s:form action="adicionarkey">
                <s:textfield label="Id da reuniao" key="id_reuniao"/>
                <s:textfield label="Id do item" key="id_item"/>
                <s:textfield label="Decisão chave" key="key"/>
                <s:submit value="Adicionar"/>
            </s:form>
</html>
