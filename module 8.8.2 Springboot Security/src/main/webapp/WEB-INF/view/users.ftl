<#-- @ftlvariable name="users" type="java.util.List<ua.step.organaizer.dto.CurrentUser>" -->
<!DOCTYPE html>
<html lang="en">
<head>
  <title>List of Users</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</head>
<body>
<nav role="navigation" class="navbar navbar-expand-sm bg-light navbar-light">
    <ul class="navbar-nav">
    <#if !currentUser??>
        <li class="nav-item active"><a class="nav-link" href="/login">Log in</a></li>
    </#if>

    <#if currentUser?? && currentUser.role == "ADMIN">
        <li class="nav-item active"><a class="nav-link" href="/user/create">Create a new user</a></li>
        <li class="nav-item active"><a class="nav-link" href="/users">View all users</a></li>
    </#if>
        <#if currentUser??>
        <li class="nav-item active"><a class="nav-link" href="/user/${currentUser.id}">View myself</a></li>
        <li class="nav-item active">
            <form action="/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">Log out</button>
            </form>
        </li>
    </#if>
    </ul>
</nav>

<div class="container">
<h1>List of Users</h1>

<table class="table">
    <thead>
    <tr>
        <th>E-mail</th>
        <th>Role</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td><a href="/user/${user.id}">${user.email}</a></td>
        <td>${user.role}</td>
    </tr>
    </#list>
    </tbody>
</table>
</div>
</body>
</html>