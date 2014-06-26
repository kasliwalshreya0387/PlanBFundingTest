<%-- 
    Document   : response
    Created on : 25-Jun-2014, 21:02:07
    Author     : ctsuser
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    
    <body>
        <jsp:useBean id="mybean" scope="session" class="com.pfb.ZipCodeBean" />
     
        <h1>Distance between the zipcodes is ,
            <% 
out.print(request.getAttribute("distance").toString());
%> KM</h1>
    </body>

</html>
