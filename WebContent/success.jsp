<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.mit.*"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title> 
</head>
<body>
<jsp:useBean id="obj" class="com.mit.CustomerBean"/> 
<jsp:setProperty property="*" name="obj"/>
   
<%
 
int status=CustomerDAO.insertCustomer(obj); 
	if (status>0) 
	out.println("Inserted successfully.."); 
		else
	out.println("Insertion Fail .."); 

%>
</body>
</html>