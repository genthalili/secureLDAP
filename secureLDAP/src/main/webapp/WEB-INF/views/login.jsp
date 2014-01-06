<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<h1>Page de login</h1>

	<h4 style="color: red">${SPRING_SECURITY_LAST_EXCEPTION.message}</h4>


	<form method="post" action="j_spring_security_check">
		<table>
			<tr>
				<td>Login:</td>
				<td><input type="text" name="login"></td>
			</tr>
			<tr>
				<td>Mot de passe:</td>
				<td><input type="password" name="password"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Valider"> <input
					type="reset" value="Annuler"></td>
			</tr>
		</table>
	</form>
</body>
</html>