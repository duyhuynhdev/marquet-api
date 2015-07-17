<%--
  Created by IntelliJ IDEA.
  User: hpduy17
  Date: 1/20/15
  Time: 9:42 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html class="bg-black">
<head>
    <meta charset="UTF-8">
    <title>Admin Marqet | Registration Page</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
    <!-- Theme style -->
    <link href="template/css/AdminLTE.css" rel="stylesheet" type="text/css"/>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body class="bg-black">

<div class="form-box" id="login-box">
    <div class="header">Register New Admin</div>
    <form action="register.marqet" method="post" onSubmit="return validate(this);">
        <div class="body bg-gray">
            <div class="form-group">
                <input type="text" id="email" name="email" class="form-control" placeholder="Email"/>
            </div>
            <div class="form-group">
                <input type="text" id="userName" name="userName" class="form-control" placeholder="User Name"/>
            </div>
            <div class="form-group">
                <input type="password" id="password" name="password" class="form-control" placeholder="Password"/>
            </div>
            <div class="form-group">
                <input type="password" id="password2" name="password2" class="form-control"
                       placeholder="Retype password"/>
            </div>
        </div>
        <div class="footer">
            <button type="submit" class="btn bg-olive btn-block">Sign me up</button>
            <a href="login.jsp" class="text-center">I already have a membership</a>
        </div>
    </form>
</div>
<div class="modal fade" id="errorModal">
    <div class="callout callout-danger">
        <h4 id="errorTitle">Cannot register new user%></h4>

        <p id="errorBody">Server is busy</p>
    </div>
    <!-- /.modal-dialog -->
</div>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
    var ck_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    function validate(form) {
        var userName = form.userName.value;
        var email = form.email.value;
        var password = form.password.value;
        var password2 = form.password2.value;
        var errors = [];

        if (!ck_email.test(email)) {
            errors[errors.length] = "You must enter a valid email address.";
        }
        if (userName == '') {
            errors[errors.length] = "You must enter the user name. ";
        }
        if (password == '') {
            errors[errors.length] = "You must enter the password. ";
        } else if (password.length < 6) {
            errors[errors.length] = "Password must contain at least 6 characters. ";
        }
        if (password != password2) {
            errors[errors.length] = "Confirm password is wrong. ";
        }

        if (errors.length > 0) {
            reportErrors(errors);
            return false;
        }
        return true;
    }

    function reportErrors(errors) {
        var msg = "Please Enter Valide Data...\n";
        for (var i = 0; i < errors.length; i++) {
            var numError = i + 1;
            msg += "\n" + numError + ". " + errors[i];
        }
        alert(msg);
    }
    <%
    if(request.getParameter("isError")!=null && Boolean.getBoolean(request.getParameter("isError"))){
       %>
    $(window).load(function () {
        $("#errorBody").val("Server is busy");
        $('#errorModal').modal('show');
    });
    <%
    }
    %>
</script>
</body>
</html>