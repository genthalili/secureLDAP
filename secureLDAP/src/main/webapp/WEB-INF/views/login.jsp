<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>LDAP</title>
<link rel="stylesheet" href="resources/css/bootstrap.min.css" media="screen">
<link rel="stylesheet" href="resources/css/todc-bootstrap.min.css" media="screen">
<link rel="stylesheet" href="resources/css/index.css" media="screen">
</head>
<body>

	<div class="container">
		<div class="row col-md-6 col-md-offset-3">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h1 class="panel-title">LDAP Login</h1>
				</div>
				<div class="panel-body">
					<div class="alert alert-warning">
						<span class="glyphicon glyphicon-info-sign"></span>
						Complete form below to login.
						<strong style="color: red"> 
							${SPRING_SECURITY_LAST_EXCEPTION.message}
						</strong>
					</div>
					<form  role="form" method="post" action="j_spring_security_check">
					  <div class="form-group">
					    <label class="sr-only" for="login">Username</label>
					    <input type="text" name="login" class="form-control input-lg" id="login" placeholder="Login">
					  </div>
					  <div class="form-group">
					    <label class="sr-only" for="password">Password</label>
					    <input type="password" name="password" class="form-control input-lg" id="password" placeholder="Password">
					  </div>
					  <button type="submit" class="btn btn-primary btn-lg">Confirm</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
</body>
</html>