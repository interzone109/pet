<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="form" type="eu.kielczewski.example.domain.UserCreateForm" -->
<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Create user</title>
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
<h1>Create a new user</h1>
<div class = "container">
<form role="form" name="form" action="" method="post" >
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <div class="form-group">
        <label for="email">Email address</label>
        <input class="form-control" type="email" name="email" id="email" value="${form.email}" required autofocus/>
    </div>
    <div class="form-group">
        <label for="password">Password</label>
        <input class="form-control" type="password" name="password" id="password" required/>
    </div>
    <div class="form-group">
        <label for="passwordRepeated">Repeat password</label>
        <input class="form-control" type="password" name="passwordRepeated" id="passwordRepeated" required/>
    </div>
    <div class="form-group">
        <label for="role">Role</label>
        <select class="form-control" name="role" id="role" required>
            <option <#if form.role == 'USER'>selected</#if>>USER</option>
            <option <#if form.role == 'ADMIN'>selected</#if>>ADMIN</option>
        </select>
    </div>
    <button class="btn btn-primary" type="submit">Save</button>
</form>
 </div>
 
<@spring.bind "form" />
<#if spring.status.error>
<ul>
    <#list spring.status.errorMessages as error>
        <li>${error}</li>
    </#list>
</ul>
</#if>
</div>
</body>
</html>