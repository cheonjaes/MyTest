<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>

<script language="javascript">
function button1_click(){
	alert("You click the button");
}
function button2_click(){
	alert("kakkakakaaka");
}
</script>

<body>

<table border="1" cellspacing="0">

<tr>
<td>Number</td>
<td>ID</td>
<td>Name</td>
<td>password</td>
<TD>비고 </TD>
</tr>

<tr>
<td>11</td>
<td>22</td>
<td>33</td>
<td>44</td>
<TD>
<INPUT type="button" value="수정" onClick="button1_click()">
<!--INPUT type="button" value="수정" onClick="location.href='modify.jsp?idx=1'"-->
</TD>
</tr>

<tr>
<td>11</td>
<td>22</td>
<td>33</td>
<td>44</td>
<TD>
<INPUT type="button" value="수정" onClick="window.location='list2.jsp'">
</TD>
</tr>

</body>
</html>