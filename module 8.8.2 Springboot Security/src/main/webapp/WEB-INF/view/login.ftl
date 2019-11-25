<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="error" type="java.util.Optional<String>" -->
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Log in</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/">Home</a></li>
    </ul>
</nav>

<div class = "container">
<div>You can use: demo@localhost / demo</div>
    <form role="form" action="/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form-group">
            <label for="email" >Email address</label>
            <input type="email" name="email" id="email" required autofocus class="form-control"/>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" name="password" id="password" required class="form-control"/>
        </div>
        <div class="form-check">
            <label for="remember-me">Remember me</label>
            <input type="checkbox" name="remember-me" id="remember-me" class="form-check-label"/>
        </div>
        <button type="submit" class="btn btn-primary">Sign in</button>
    </form>
    <#if error.isPresent()>
      <div class="alert alert-danger">The email or password you have entered is invalid, try again.</div>
    </#if>
</div>
</body>
</html>