<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

 <div class="container-fluid" align="center">
 <div class="row form-group justify-content-center">
 <div class="col-sm-3" >
     <h2>Revisions Stats (Wikipedia)</h2>
    </div>
    <div class="col-sm-6" >
     
    </div>
    <div class="col-sm-3" >
     
    </div>
 </div>
 </div>
<form class="form-horizontal" action="result" method = "post">
    <div class="form-group">
      <label class="control-label col-sm-2" for="entity">Entity:</label>
      <div class="col-sm-10">
        <input type="entity" class="form-control" id="entity" placeholder="Enter entity name" name="entity">
      </div>
    </div>
    <div class="form-group">        
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">Submit</button>
      </div>
    </div>
  </form>
</body>
</html>