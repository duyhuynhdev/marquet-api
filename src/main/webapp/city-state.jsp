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
                City and State
                <small>it all starts here</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="main.jsp"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="country.marqet">Country</a></li>
                <li class="active">City and State</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="col-md-6">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">City Table</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <table class="table table-bordered">
                            <tr>
                                <th style="width: 10px">Code</th>
                                <th>Name</th>
                                <th style="width: 80px"></th>
                            </tr>
                            <%
                                JSONObject responseCityJSON = (JSONObject) request.getAttribute("cityList");
                                JSONArray cityList = responseCityJSON.getJSONArray(ResponseController.CONTENT);
                                for (int i = 0; i < cityList.length(); i++) {
                                    JSONObject city = cityList.getJSONObject(i);
                                    out.println("<tr>");
                                    out.println("<td>" + city.getString("code") + "</td>");
                                    out.println("<td>" + city.getString("name") + "</td>");
                                    out.println("<td>");
                                    out.println("<a href='#' onclick=\"update('"+city.getString("code")+"','"+city.getString("name")+"')\"><i class='fa fa-edit'></i></a> | ");
                                    out.println("<a href='deletecity.marqet?code="+city.getString("code")+"&countryCode="+request.getAttribute("countryCode")+"'>" +
                                            "<i class='fa fa-trash-o'></i></a>");
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
                                        <h4 class="modal-title">Add New City</h4>
                                    </div>
                                    <form action="addcity.marqet" method="post">
                                        <div class="modal-body">
                                            <input type="text" name="countryCode" id="countryCode1" class="form-control" value=<%out.print("'"+request.getAttribute("countryCode")+"'");%>
                                                    required="required"  title="country code" style="display: none">
                                            <input type="text" name="code" id="cityCode" class="form-control" value=""
                                                   placeholder="Enter city code (optional)" title="code">
                                            <input type="text" name="name" id="cityName" class="form-control" value=""
                                                   required="required" placeholder="Enter city name" title="name">
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
                                        <h4 class="modal-title">Update City</h4>
                                    </div>
                                    <form action="updatecity.marqet" method="post">
                                        <div class="modal-body">
                                            <input type="text" name="countryCode" id="countryCode1Update" class="form-control" value=<%out.print("'"+request.getAttribute("countryCode")+"'");%>
                                                    required="required"  title="country code" style="display: none">
                                            <input type="text" name="code" id="cityCodeUpdate" class="form-control" value=""
                                                   placeholder="Enter city code (optional)" title="code" style="display: none">
                                            <input type="text" name="name" id="cityNameUpdate" class="form-control" value=""
                                                   required="required" placeholder="Enter city name" title="name">
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
            <div class="col-md-6">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">State Table</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <table class="table table-bordered">
                            <tr>
                                <th style="width: 10px">Code</th>
                                <th>Name</th>
                                <th style="width: 80px"></th>
                            </tr>
                            <%
                                JSONObject responseStateJSON = (JSONObject) request.getAttribute("stateList");
                                JSONArray stateList = responseStateJSON.getJSONArray(ResponseController.CONTENT);
                                for (int i = 0; i < stateList.length(); i++) {
                                    JSONObject state = stateList.getJSONObject(i);
                                    out.println("<tr>");
                                    out.println("<td>" + state.getString("code") + "</td>");
                                    out.println("<td>" + state.getString("name") + "</td>");
                                    out.println("<td>");
                                    out.println("<a href='#' onclick=\"update2('"+state.getString("code") +"','"+state.getString("name") +"')\"><i class='fa fa-edit'></i></a> | ");
                                    out.println("<a href='deletestate.marqet?code="+state.getString("code") +"&countryCode="+request.getAttribute("countryCode")+"'>" +
                                            "<i class='fa fa-trash-o'></i></a>");
                                    out.println("</td>");
                                    out.println("</tr>");
                                }
                            %>
                        </table>
                    </div>
                    <!-- /.box-body -->
                    <div class="box-footer clearfix">
                        <button class="btn btn-success" id="btnModal2">Add</button>
                        <!---MODAL--->
                        <div class="modal fade" id="addModal2">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal"
                                                aria-hidden="true">&times;</button>
                                        <h4 class="modal-title">Add New State</h4>
                                    </div>
                                    <form action="addstate.marqet" method="post">
                                        <div class="modal-body">
                                            <input type="text" name="countryCode" id="countryCode2" class="form-control" value=<%out.print("'"+request.getAttribute("countryCode")+"'");%>
                                                    required="required"  title="country code" style="display: none">
                                            <input type="text" name="code" id="code" class="form-control" value=""
                                                   placeholder="Enter state code (optional)" title="code">
                                            <input type="text" name="name" id="name" class="form-control" value=""
                                                   required="required" placeholder="Enter state name" title="name">
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
                        <!---MODAL--->
                        <div class="modal fade" id="updateModal2">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal"
                                                aria-hidden="true">&times;</button>
                                        <h4 class="modal-title">Update State</h4>
                                    </div>
                                    <form action="updatestate.marqet" method="post">
                                        <div class="modal-body">
                                            <input type="text" name="countryCode" id="countryCode2Update" class="form-control" value=<%out.print("'"+request.getAttribute("countryCode")+"'");%>
                                                    required="required"  title="country code" style="display: none">
                                            <input type="text" name="code" id="stateCodeUpdate" class="form-control" value=""
                                                   placeholder="Enter state code (optional)" title="code" style="display: none">
                                            <input type="text" name="name" id="stateNameUpdate" class="form-control" value=""
                                                   required="required" placeholder="Enter state name" title="name">
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
    function update(code,name){
        $("#cityCodeUpdate").val(code);
        $("#cityNameUpdate").val(name);
        $("#updateModal1").modal('show');
    }
    function update2(code,name){
        $("#stateCodeUpdate").val(code);
        $("#stateNameUpdate").val(name);
        $("#updateModal2").modal('show');
    }
</script>
</body>
</html>
