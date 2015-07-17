<%@ page import="java.util.List" %>
<%@ page import="com.marqet.WebServer.pojo.CategoryEntity" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="javax.xml.ws.Response" %>
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
                Category
                <small>it all starts here</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="main.jsp"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Category</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <%
                JSONObject responseJSON = (JSONObject) request.getAttribute("categoryList");
                JSONArray categoryEntityList = responseJSON.getJSONArray(ResponseController.CONTENT);
                int rowNums = categoryEntityList.length() / 4 + 1;
                for (int r = 0; r < rowNums; r++) {
                    out.println("<div class='row'>");
                    for (int c = r * 4; c < (r + 1) * 4 && c < categoryEntityList.length(); c++) {
                        JSONObject category = categoryEntityList.getJSONObject(c);
                        out.println("<div class='col-lg-3 col-xs-6'>");
                        out.println("<div class='small-box' style=\"background-image: url('" + category.getString("coverImg") + "'); background-repeat: no-repeat; background-size: contain;\">");
                        out.println("<div class='inner'>");
                        out.println("<h3>" + category.getLong("id") + "</h3>");
                        out.println("<p>" + category.getString("name") + "</p>");
                        out.println("</div>");
                        out.println("<div class='icon'><i class='fa fa-sitemap'></i></div>");
                        out.println("<div class='small-box-footer'>");
                        out.println("<a href='subcategory.marqet?categoryId=" + category.getLong("id") + "'>View SubCategories" +
                                " <i class='fa fa-arrow-circle-right'></i></a>");
                        out.println(" | <a href='#' onclick=\"update('"+category.getLong("id")+"','"+category.getString("name")+"')\">" +
                                "<i class='fa fa-edit'></i></a>");
                        out.println(" | <a href='deletecategory.marqet?categoryId="+category.getLong("id")+"'><i class='fa fa-trash-o'></i></a>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</div>");
                    }
                    out.println("</div>");
                }
            %>
            <button class="btn btn-success" id="btnModal1">Add</button>
            <!---MODAL--->
            <div class="modal fade" id="addModal1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title">Add New Category</h4>
                        </div>
                        <form action="addcategory.marqet" method="post" enctype="multipart/form-data">
                            <div class="modal-body">
                                <input type="text" name="name" id="name" class="form-control" value=""
                                       required="required" placeholder="Enter category" title="Name">
                                <input type="file" name="coverImage" id="coverImage" class="btn-file" style="margin-top: 3px"/>
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
            <div class="modal fade" id="updateModal1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 id="title1" class="modal-title">Update Category</h4>
                        </div>
                        <form id="formId1" action="updatecategory.marqet" method="post" enctype="multipart/form-data">
                            <div class="modal-body">
                                <input type="text" name="id" id="idUpdate" class="form-control" value=""
                                       required="required" placeholder="Enter category" title="id" style="display: none">
                                <input type="text" name="name" id="nameUpdate" class="form-control" value=""
                                       required="required" placeholder="Enter category" title="Name">
                                <input type="file" name="coverImage" id="coverImageUpdate" class="btn-file" style="margin-top: 3px"/>
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
            <!-- /.modal -->
        </section>
        <!-- /.content -->
    </aside>
    <!-- /.right-side -->
</div>
<!-- ./wrapper -->
<jsp:include page="footer.jsp"></jsp:include>
<script type="text/javascript">
   function update(id,name){
       $("#idUpdate").val(id);
       $("#nameUpdate").val(name);
       $("#coverImageUpdate").val('');
       $("#updateModal1").modal('show');
   }
</script>
</body>
</html>
