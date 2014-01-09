<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
	<div id="info" class="alert alert-danger alert-dismissable hidden">
	  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
	  <strong>Error !</strong> <span class="text"></span>
	</div>
	
	<header class="navbar navbar-masthead navbar-default navbar-fixed-top" role="banner">
	  <div class="container">
	    <div class="navbar-header">
	      <button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".bs-navbar-collapse">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a href="/secureLDAP/" class="navbar-brand">LDAP</a>
	    </div>
	    <nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
	      <ul class="nav navbar-nav navbar-right">
	        <li>
	          <a href="#users">Users</a>
	        </li>
	         <li>
	          <a href="#groups">Groups</a>
	        </li>
	        <li>
	          <a href='<spring:url value="/logout"></spring:url>'>Logout</a>
	        </li>
	      </ul>
	    </nav>
	  </div>
	</header>
	
	
	
	<div id="users" class="container container-view">
		<div class="well col-md-2">
			<button class="btn btn-danger btn-lg btn-block" data-toggle="modal" data-target="#add_user">ADD A USER</button>
		</div>
		<div class="well col-md-3 col-md-offset-7">
			<form method="GET" action="/secureLDAP/" class="form-inline pull-right" role="form">
			  <div class="form-group">
			    <label class="sr-only" for="fullname">fullname</label>
			    <input type="text" name="fullname" class="form-control input-lg" placeholder="Find a user by fullname">
			  </div>
			  <button type="submit" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-search"></span></button>
			</form>
		</div>
	
		<table class="table table-striped">
			<thead>
				<th>dn</th>
				<th>fullname</th>
				<th>surname</th>
				<th></th>
			</thead>
			<tbody>
			<c:forEach items="${users}" var="user">
				<tr>
					<td>${user.dn}</td>
					<td>${user.fullName}</td>
					<td>${user.surName}</td>
					<td>
						<div class="btn-group btn-block">
						  <button type="button" class="btn btn-default btn-block dropdown-toggle" data-toggle="dropdown">
						  	<span class="glyphicon glyphicon-cog"></span>
						    <span class="sr-only">Toggle Dropdown</span>
						  </button>
						  <ul class="dropdown-menu" role="menu">
						    <li><a href="#" data-toggle="modal" data-target="#edit_user"><span class="glyphicon glyphicon-edit"></span> Edit</a></li>
						    <li><a href="/secureLDAP/deleteUser?fullname=${user.fullName }"><span class="glyphicon glyphicon-remove"></span> Delete</a></li>
						    <li class="divider"></li>
						    <li><a href="/secureLDAP/resetPassword?fullname=${user.fullName }"><span class="glyphicon glyphicon-refresh"></span> Reset password</a></li>
						  </ul>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div id="groups" class="container container-view">
		<div class="well col-md-2">
			<button class="btn btn-danger btn-lg btn-block" data-toggle="modal" data-target="#add_group">ADD A GROUP</button>
		</div>
	
		<table class="table table-striped">
			<thead>
				<th>dn</th>
				<th>name</th>
				<th>members</th>
				<th></th>
			</thead>
			<tbody>
			<c:forEach items="${groups}" var="group">
				<tr>
					<td>${group.dn}</td>
					<td>${group.name}</td>
					<td>${group.members}</td>
					<td>
						<div class="btn-group btn-block">
						  <button type="button" class="btn btn-default btn-block dropdown-toggle" data-toggle="dropdown">
						  	<span class="glyphicon glyphicon-cog"></span>
						    <span class="sr-only">Toggle Dropdown</span>
						  </button>
						  <ul class="dropdown-menu" role="menu">
						    <li><a href="#" data-toggle="modal" data-target="#edit_group"><span class="glyphicon glyphicon-edit"></span> Edit</a></li>
						    <c:if test="${ group.name != 'administrator' && group.name != 'helpdesk' && group.name != 'groupname'}">
						    	<li><a href="/secureLDAP/deleteGroup?groupname=${group.name }"><span class="glyphicon glyphicon-remove"></span> Delete</a></li>
						    </c:if>
						    <li class="divider"></li>
						    <li><a href="#" data-toggle="modal" data-target="#add_group_user"><span class="glyphicon glyphicon-plus-sign"></span> Add a user</a></li>
						    <li><a href="#" data-toggle="modal" data-target="#remove_group_user"><span class="glyphicon glyphicon-minus-sign"></span> Remove a user</a></li>
						  </ul>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div class="modal fade" id="edit_user" tabindex="-1" role="dialog" >
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" >Edit user</h4>
	      </div>
	      <div class="modal-body">
	      <div class="row">
	      	<form role="form" method="post" class="col-md-8 col-md-offset-2" action="/secureLDAP/updateUser">
	      	  <div class="alert alert-warning">Edit user attributes</div>
	      	  <input type="hidden" name="cn" value=""/>
			  <div class="form-group">
			    <label for="dn">dn</label>
			    <input type="text" name="dn" class="form-control" id="dn" placeholder="dn">
			  </div>
			  <div class="form-group">
			    <label for="fullname">fullname</label>
			    <input type="text" name="fullname" class="form-control" id="fullname" placeholder="fullname">
			  </div>
			  <div class="form-group">
			    <label for="surname">surname</label>
			    <input type="text" name="surname" class="form-control" id="surname" placeholder="surname">
			  </div>
			  <div class="form-group">
			    <label  for="password">password</label>
			    <input type="password" name="password" class="form-control" id="password" placeholder="password">
			  </div>
			</form>
			</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Save changes</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="edit_group" tabindex="-1" role="dialog" >
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" >Edit group</h4>
	      </div>
	      <div class="modal-body">
	      <div class="row">
	      	<form role="form" method="post" class="col-md-8 col-md-offset-2" action="/secureLDAP/updateGroup">
	      	  <div class="alert alert-warning">Edit group attributes</div>
	      	  <input type="hidden" name="cn" value=""/>
			  <div class="form-group">
			    <label class="sr-only" for="dn">dn</label>
			    <input type="text" name="dn" class="form-control" id="dn" placeholder="dn">
			  </div>
			  <div class="form-group">
			    <label class="sr-only" for="groupname">groupname</label>
			    <input type="text" name="groupname" class="form-control" id="groupname" placeholder="fullname">
			  </div>
			</form>
			</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Save changes</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="add_user" tabindex="-1" role="dialog" >
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" >Add a user</h4>
	      </div>
	      <div class="modal-body">
		      <div class="row">
		      	<form role="form" method="post" class="col-md-8 col-md-offset-2" action="/secureLDAP/addUser">
		      	  <div class="alert alert-warning">Complete form below to add a user</div>
				  <div class="form-group">
				    <label class="sr-only" for="fullname">fullname</label>
				    <input type="text" name="fullname" class="form-control" id="fullname" placeholder="fullname">
				  </div>
				  <div class="form-group">
				    <label class="sr-only" for="surname">surname</label>
				    <input type="text" name="surname" class="form-control" id="surname" placeholder="surname">
				  </div>
				  <div class="form-group">
				    <label class="sr-only" for="password">password</label>
				    <input type="password" name="password" class="form-control" id="password" placeholder="password">
				  </div>
				</form>
				</div>
		      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Confirm</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="add_group" tabindex="-1" role="dialog" >
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" ></h4>
	      </div>
	      <div class="modal-body">
	      <div class="row">
	      	<form role="form" method="post" class="col-md-8 col-md-offset-2" action="/secureLDAP/addGroup">
	      	  <div class="alert alert-warning">Complete form below to add a group</div>
			  <div class="form-group">
			    <label class="sr-only" for="groupname">groupname</label>
			    <input type="text" name="groupname" class="form-control" id="groupname" placeholder="groupname">
			  </div>
			  <div class="form-group">
			    <label class="sr-only" for="fullname">fullname</label>
			    <input type="text" name="fullname" class="form-control" id="fullname" placeholder="fullname">
			  </div>
			</form>
			</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Confirm</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	<div class="modal fade" id="add_group_user" tabindex="-1" role="dialog" >
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" >Add a user to the group</h4>
	      </div>
	      <div class="modal-body">
	      <div class="row">
	      	<form role="form" method="post" class="col-md-8 col-md-offset-2" action="/secureLDAP/addUserToGroup">
	      	  <div class="alert alert-warning">Add a user to the group</div>
			  <div class="form-group">
			    <label for="groupname">groupname</label>
			    <input type="text" name="groupname" class="form-control" id="groupname" placeholder="groupname">
			  </div>
			  <div class="form-group">
			    <label class="sr-only" for="fullname">fullname</label>
			    <input type="text" name="fullname" class="form-control" id="fullname" placeholder="fullname">
			  </div>
			</form>
			</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Confirm</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="remove_group_user" tabindex="-1" role="dialog" >
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" >Remove the user from the group</h4>
	      </div>
	      <div class="modal-body">
	      <div class="row">
	      	<form role="form" method="post" class="col-md-8 col-md-offset-2" action="/secureLDAP/removeUserFromGroup">
	      	  <div class="alert alert-warning">Remove a user from the group</div>
			  <div class="form-group">
			    <label for="groupname">groupname</label>
			    <input type="text" name="groupname" class="form-control" id="groupname" placeholder="groupname">
			  </div>
			  <div class="form-group">
			    <label class="sr-only" for="fullname">fullname</label>
			    <input type="text" name="fullname" class="form-control" id="fullname" placeholder="fullname">
			  </div>
			</form>
			</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Confirm</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script>var defaultView = '${defaultView}';var errorMsg = '${errorMsg}';</script>
	<script src="resources/js/index.js"></script>
</body>
</html>