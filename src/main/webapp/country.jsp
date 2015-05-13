<%@ page import="com.marqet.WebServer.controller.ResponseController" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%--
  Created by IntelliJ IDEA.
  User: hpduy17
  Date: 1/21/15
  Time: 10:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="head.jsp"/>
</head>
<body class="skin-blue">
<jsp:include page="header.jsp"/>
<div class="wrapper row-offcanvas row-offcanvas-left">
    <jsp:include page="left-side.jsp"/>
    <!-- Right side column. Contains the navbar and content of the page -->
    <aside class="right-side">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Country
                <small>it all starts here</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="main.jsp"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Country</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="col-md-6">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">Country Table</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <table class="table table-bordered">
                            <tr>
                                <th style="width: 10px">Code</th>
                                <th>Name</th>
                                <th style="width: 20px">Phone Prefix</th>
                                <th style="width: 80px"></th>
                            </tr>
                            <%
                                JSONObject responseJSON = (JSONObject) request.getAttribute("countryList");
                                JSONArray countryList = responseJSON.getJSONArray(ResponseController.CONTENT);
                                for (int i = 0; i < countryList.length(); i++) {
                                    JSONObject country = countryList.getJSONObject(i);
                                    out.println("<tr>");
                                    out.println("<td>" + country.getString("code") + "</td>");
                                    out.println("<td>" + country.getString("name") + "</td>");
                                    out.println("<td>" + country.getString("phonePrefix") + "</td>");
                                    out.println("<td>");
                                    out.println("<a href='#' onclick=\"update('"+country.getString("code")+"','"+country.getString("name") +"','"+country.getString("phonePrefix")+"')\">" +
                                            "<i class='fa fa-edit'></i></a> | ");
                                    out.println("<a href='deletecountry.marqet?code="+country.getString("code")+"'><i class='fa fa-trash-o'></i></a> | ");
                                    out.println("<a href='city-state.marqet?countryCode=" + country.getString("code") + "'>View Detail <i class='fa fa-arrow-circle-right'></i></a>");
                                    out.println("</td>");
                                    out.println("</tr>");
                                }
                            %>
                        </table>
                    </div>
                    <!-- /.box-body -->
                    <div class="box-footer clearfix">
                        <button class="btn btn-success" id="btnModal1">Add</button>
                        <!---MODAL--->
                        <div class="modal fade" id="addModal1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal"
                                                aria-hidden="true">&times;</button>
                                        <h4 class="modal-title">Add New Country</h4>
                                    </div>
                                    <form action="addcountry.marqet" method="post">
                                        <div class="modal-body">
                                            <input type="text" name="code" id="code" class="form-control" value=""
                                                   placeholder="Enter country code (optional)" title="code">
                                            <input type="text" name="name" id="name" class="form-control" value=""
                                                   required="required" placeholder="Enter country name" title="name">
                                            <input type="text" name="phonePrefix" id="phonePrefix" class="form-control"
                                                   value=""
                                                   required="required" placeholder="Enter phone prefix" title="phone">
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel
                                            </button>
                                            <button type="submit" class="btn btn-success">Submit</button>
                                        </div>
                                    </form>
                                </div>
                                <!-- /.modal-content -->
                            </div>
                            <!-- /.modal-dialog -->
                        </div>
                        <!-- /.modal -->
                        <div class="modal fade" id="updateModal1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal"
                                                aria-hidden="true">&times;</button>
                                        <h4 class="modal-title">Update Country</h4>
                                    </div>
                                    <form action="updatecountry.marqet" method="post">
                                        <div class="modal-body">
                                            <input type="text" name="code" id="codeUpdate" class="form-control" value=""
                                                   placeholder="Enter country code (optional)" title="code" style="display: none">
                                            <input type="text" name="name" id="nameUpdate" class="form-control" value=""
                                                   required="required" placeholder="Enter country name" title="name">
                                            <input type="text" name="phonePrefix" id="phonePrefixUpdate" class="form-control"
                                                   value=""
                                                   required="required" placeholder="Enter phone prefix" title="phone">
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel
                                            </button>
                                            <button type="submit" class="btn btn-success">Submit</button>
                                        </div>
                                    </form>
                                </div>
                                <!-- /.modal-content -->
                            </div>
                            <!-- /.modal-dialog -->
                        </div>
                        <!-- /.modal -->
                    </div>
                </div>
                <!-- /.box -->
            </div>
        </section>
        <!-- /.content -->
    </aside>
    <!-- /.right-side -->
</div>
<!-- ./wrapper -->
<jsp:include page="footer.jsp"></jsp:include>
<script type="text/javascript">
    function update(code,name,phonePrefix){
        $("#codeUpdate").val(code);
        $("#nameUpdate").val(name);
        $("#phonePrefixUpdate").val(phonePrefix);
        $("#updateModal1").modal('show');
    }
</script>
</body>
</html>
