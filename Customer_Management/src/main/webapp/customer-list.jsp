<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Customer Management Application</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>
	<div class="row">
		<!-- <div class="alert alert-success" *ngIf='message'>{{message}}</div> -->

		<div class="container">
			
			<hr>
			<div class="container text-left">
			<form action = create method="post">
				<input type="submit" value="Add new customer">
			</form>
				
			</div>
			<br>
			
			<h3 class="text-center">List of Customers</h3>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Street</th>
						<th>Address</th>
						<th>City</th>
						<th>State</th>
						<th>Email</th>
						<th>Phone</th>
					</tr>
				</thead>
				<tbody>
					
					<c:forEach var="customer" items="${customers}">

						<tr>
							<td><c:out value="${customer.first_name}" /></td>
							<td><c:out value="${customer.last_name}" /></td>
							<td><c:out value="${customer.street}" /></td>
							<td><c:out value="${customer.address}" /></td>
							<td><c:out value="${customer.city}" /></td>
							<td><c:out value="${customer.state}" /></td>
							<td><c:out value="${customer.email}" /></td>
							<td><c:out value="${customer.phone}" /></td>
							<td>
							<form action = edit method="post">
							<input type="hidden" name="uuid" value="<c:out value='${customer.uuid}' />" />
								<input type="submit" value="Edit">
							</form>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<form action = delete method="post">
								<input type="hidden" name="uuid" value="<c:out value='${customer.uuid}' />" />
								<input type="submit" value="Delete">
							</form>
							
							</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</div>
</body>
</html>