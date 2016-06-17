<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="/js/jquery-2.2.0.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="/js/jquery.qrcode.min.js"></script>
	</head>
	<body>
	<h1>wechat ceshi</h1>
	<div id="output"></div>
	<script>
	jQuery('#output').qrcode({width: 400,height: 400,text: "${wechat_code_url}"});
</script>
	</body>
</html>