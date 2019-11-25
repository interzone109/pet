<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<!DOCTYPE html>
<html lang="en">
<head>
  <title>User details</title>
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
        <li class="nav-item active"><a href="/">Home</a></li>
    </ul>
</nav>
<div class="container">
    <h1>User details</h1>
    <p>E-mail: ${user.email}</p>
    <p>Role: ${user.role}</p>
</div>
</body>
</html>