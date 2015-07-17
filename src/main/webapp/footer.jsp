<%--
  Created by IntelliJ IDEA.
  User: hpduy17
  Date: 1/22/15
  Time: 12:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    if (request.getAttribute("isError") != null && (Boolean) request.getAttribute("isError")) {
%>
<div class="modal fade" id="errorModal">
    <div class="callout callout-danger">
        <h4><%out.print(request.getAttribute("errorTitle"));%></h4>
        <p><%out.print(request.getAttribute("errorMessage"));%></p>
    </div>
    <!-- /.modal-dialog -->
</div>
<%
    }
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<%--<script src="//code.jquery.com/jquery-1.10.2.js"></script>--%>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
<!-- AdminLTE App -->
<script src="template/js/AdminLTE/app.js" type="text/javascript"></script>
<!-- AdminLTE for demo purposes -->
<script src="template/js/AdminLTE/demo.js" type="text/javascript"></script>
<!--Personal Script--->
<script type="text/javascript">
    $(document).ready(function () {
        var myBackup1 = $('#addModal1').clone();
        var myBackup2 = $('#addModal2').clone();
        $("#btnModal1").click(function () {
            $("#addModal1").modal('show');
        });
        $("#btnModal2").click(function () {
            $("#addModal2").modal('show');
        });

    });
    <%
    if(request.getAttribute("isError")!=null && (Boolean)request.getAttribute("isError")){
       %>
    $(window).load(function () {
        $('#errorModal').modal('show');
    });
    <%
    }
    %>
</script>