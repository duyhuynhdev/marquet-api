<%@ page import="java.util.List" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.marqet.WebServer.controller.ResponseController" %>
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
            Element & Information
            <small>it all starts here</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="main.jsp"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active"></li>
            Element & Information></li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <%
            JSONObject elementJSON = ((JSONObject) request.getAttribute("element")).getJSONObject(ResponseController.CONTENT);
            JSONObject informationJSON = ((JSONObject) request.getAttribute("information")).getJSONObject(ResponseController.CONTENT);
        %>
        <h2 class="page-header">Element</h2>

        <div class="row">
            <div class="col-xs-6">
                <div class="box box-danger">
                    <div class="box-header">
                        <i class="fa fa-pagelines"></i>

                        <h3 class="box-title">Logo & Default Avatar</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <div class="row">
                            <div class="col-lg-6 col-xs-6">
                                <div class="small-box"
                                     style="background-image: url(<%out.print("'"+elementJSON.getString("logo")+"'");%>); background-repeat: no-repeat;">
                                    <img src="" width="150" height="150" style="visibility:hidden"/>

                                    <div class="inner">
                                        <p>
                                            Logo
                                        </p>
                                    </div>
                                    <a onclick="update()" class="small-box-footer">
                                        Change <i class="fa fa-edit"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="col-lg-6 col-xs-6">
                                <div class="small-box"
                                     style="background-image: url(<%out.print("'"+elementJSON.getString("defaultAvatar")+"'");%>); background-repeat: no-repeat;">
                                    <img src="" width="150" height="150" style="visibility:hidden"/>

                                    <div class="inner">
                                        <p>
                                            Default Avatar
                                        </p>
                                    </div>
                                    <a onclick="update2()" class="small-box-footer">
                                        Change <i class="fa fa-edit"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /.box -->
            </div>
            <!-- /.col -->

            <div class="col-md-6">
                <div class="box box-info">
                    <div class="box-header">
                        <i class="fa fa-bullhorn"></i>

                        <h3 class="box-title">Big & Small Banner, Point Level Setting</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <div class="callout callout-danger">
                            <form action="updatebigbanner.marqet">
                                <h4>Big Banner Info</h4>

                                <div class="form-group has-error">
                                    <input type="text" class="form-control" id="bigBannerDuration"
                                           name="bigBannerDuration" placeholder="Enter duration"
                                           value=<%out.print("'" + new JSONObject(elementJSON.getString("bigBannerInfo")).get("duration") + "'");%>/>
                                    <input type="text" class="form-control" id="bigBannerPoint" name="bigBannerPoint"
                                           placeholder="Enter point"
                                           value=<%out.print("'" + new JSONObject(elementJSON.getString("bigBannerInfo")).get("point") + "'");%>/>
                                </div>
                                <button type="submit" class="btn btn-danger">Save</button>
                            </form>
                        </div>
                        <div class="callout callout-success">
                            <form action="updatesmallbanner.marqet" method="post">
                                <h4>Small Banner Info</h4>

                                <div class="form-group has-success">
                                    <input type="text" class="form-control" id="smallBannerDuration"
                                           name="smallBannerDuration" placeholder="Enter duration"
                                           value=<%out.print("'" + new JSONObject(elementJSON.getString("smallBannerInfo")).get("duration") + "'");%>/>
                                    <input type="text" class="form-control" id="smallBannerPoint"
                                           name="smallBannerPoint" placeholder="Enter point"
                                           value=<%out.print("'" + new JSONObject(elementJSON.getString("smallBannerInfo")).get("point") + "'");%>/>
                                </div>
                                <button type="submit" class="btn btn-success">Save</button>
                            </form>
                        </div>
                        <div class="callout callout-warning">
                            <form action="updatepoint.marqet" method="post">
                                <h4>Point Level</h4>

                                <div class="form-group has-warning" style="height: 40px">
                                    <div class="col-xs-3">
                                        <input type="text" class="form-control" id="level1" name="level1"
                                               placeholder="Level 1"
                                               value=<%out.print("'" + new JSONObject(elementJSON.getString("pointLevel")).get("level1") + "'");%>/>
                                    </div>
                                    <div class="col-xs-3">
                                        <input type="text" class="form-control" id="level2" name="level2"
                                               placeholder="Level 2"
                                               value=<%out.print("'" + new JSONObject(elementJSON.getString("pointLevel")).get("level2") + "'");%>/>
                                    </div>
                                    <div class="col-xs-3">
                                        <input type="text" class="form-control" id="level3" name="level3"
                                               placeholder="Level 3"
                                               value=<%out.print("'" + new JSONObject(elementJSON.getString("pointLevel")).get("level3") + "'");%>/>
                                    </div>
                                    <div class="col-xs-3">
                                        <input type="text" class="form-control" id="level4" name="level4"
                                               placeholder="Level 4"
                                               value=<%out.print("'" + new JSONObject(elementJSON.getString("pointLevel")).get("level4") + "'");%>/>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-warning">Save</button>
                            </form>
                        </div>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /.box -->
            </div>
            <!-- /.col -->

        </div>
        <!-- /.row -->
        <h2 class="page-header">Information</h2>

        <div class="row">
            <div class="col-xs-12">
                <div class="box box-warning">
                    <div class="row">
                        <form action="updateinformation.marqet" method="post">
                            <div class="col-xs-6">
                                <div class="box-body">
                                    <div class="form-group">
                                        <label>Community Rule</label>
                                        <textarea class="form-control" id="communityRule" name="communityRule"
                                                  rows="3"
                                                  placeholder="Enter community rule"><%out.print(informationJSON.get("communityRule"));%></textarea>
                                    </div>
                                    <div class="form-group">
                                        <label>Point System</label>
                                        <textarea class="form-control" id="pointSystem" name="pointSystem" rows="3"
                                                  placeholder="Enter poitn system"><%out.print(informationJSON.get("pointSystem"));%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="box-body">
                                    <div class="form-group">
                                        <label>Email Support</label>
                                        <input type="text" class="form-control" id="emailSupport"
                                               name="emailSupport"
                                               placeholder="Enter email"
                                               value=<%out.print(informationJSON.get("emailSupport"));%>/>
                                        <label>About</label>
                                        <textarea class="form-control" id="about" name="about" rows="6"
                                                  placeholder="Enter about"><%out.print(informationJSON.get("about"));%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="box-body" align="center">
                                <button type="submit" class="btn btn-warning">Save Information</button>
                            </div>
                        </form>
                    </div>
                </div>
                <!-- /.box -->
            </div>
            <!-- /.col -->

        </div>
        <div class="modal fade" id="updateModal1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Update Logo</h4>
                    </div>
                    <form action="updatelogo.marqet" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                          <input type="file" name="logo" id="logoUpdate" class="btn-file" style="margin-top: 3px"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-success">Submit</button>
                        </div>
                    </form>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>
        <div class="modal fade" id="updateModal2">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Update Default Avatar</h4>
                    </div>
                    <form action="updateavatar.marqet" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                             <input type="file" name="defaultAvatar" id="defaultAvatarUpdate" class="btn-file" style="margin-top: 3px"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-success">Submit</button>
                        </div>
                    </form>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>
    </section>
    <!-- /.content -->
</aside>
<!-- /.right-side -->
</div>
<!-- ./wrapper -->
<jsp:include page="footer.jsp"></jsp:include>
<script type="text/javascript">
    function update(){
        $("#logoUpdate").val('');
        $("#updateModal1").modal('show');
    }
    function update2(){
        $("#defaultAvatarUpdate").val('');
        $("#updateModal2").modal('show');
    }

</script>
</body>
</html>
