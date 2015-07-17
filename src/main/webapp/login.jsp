<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html class="bg-black">
<head>
    <meta charset="UTF-8">
    <title>Admin Marqet | Log in</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="template/css/AdminLTE.css" rel="stylesheet" type="text/css" />

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body class="bg-black" onload="">
<div class="form-box" id="login-box">
    <div class="header">Sign In</div>
    <form name ="loginForm" action="login.marqet" method="post">
        <div class="body bg-gray">
            <div class="form-group">
                <input type="text" name="email" class="form-control" placeholder="Email"/>
            </div>
            <div class="form-group">
                <input type="password" name="password" class="form-control" placeholder="Password"/>
            </div>
        </div>
        <div class="footer">
            <button type="submit" class="btn bg-olive btn-block">Sign me in</button>
            <p><a href="#">I forgot my password</a></p>
            <a href="register.jsp" class="text-center">Register a new membership</a>
        </div>
    </form>
    <div class="modal fade" id="errorModal">
        <div class="callout callout-danger">
            <h4>Cannot login to MarQet Admin Pages%></h4>
            <p>Please check your email and password</p>
        </div>
        <!-- /.modal-dialog -->
    </div>
</div>

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
    <%
    if(request.getParameter("isError")!=null && Boolean.getBoolean(request.getParameter("isError"))){
       %>
    $(window).load(function () {
        $('#errorModal').modal('show');
    });
    <%
    }
    %>
</script>
</body>
</html>