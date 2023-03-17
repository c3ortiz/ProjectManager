<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title>Dashboard</title>
</head>
<body>
    <div class="container">
        <header class="d-flex justify-content-between align-items-center">
            <h1>¡Bienvenid@ ${userSession.firstName}!</h1>
            <a href="/projects/new" class="btn btn-success">Nuevo Proyecto</a>
            <a href="/logout" class="btn btn-danger">Cerrar Sesión</a>
        </header>
        <div class="row">
            <h2>Todos los proyectos</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Proyecto</th>
                        <th>Líder de proyecto</th>
                        <th>Fecha límite</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${otherProjects}" var="project">
                        <tr>
                            <td>${project.title}</td>
                            <td>${project.lead.firstName}</td>
                            <td>${project.dueDate}</td>
                            <td>
                                <a href="/projects/join/${project.id}" class="btn btn-info">Unirme</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <hr>
        <div class="row">
            <h2>Mis proyectos</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Proyecto</th>
                        <th>Líder de proyecto</th>
                        <th>Fecha límite</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${myProjects}" var="project">
                        <tr>
                            <td>${project.title}</td>
                            <td>${project.lead.firstName}</td>
                            <td>${project.dueDate}</td>
                            <td>
                                <c:if test="${project.lead.id == userSession.id}">
                                    <a href="/projects/edit/${project.id}" class="btn btn-warning">Editar</a>
                                </c:if>
                                <c:if test="${project.lead.id != userSession.id}">
                                    <a href="/projects/leave/${project.id}" class="btn btn-danger">Salir</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>