<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="jmpd - fast and lightweight webclient">
  <meta name="author" content="andy@ndyk.de">
  <meta name="author" content="paabelboi@gmail.com">

  <title>jmpd</title>

  <!-- Bootstrap core CSS -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/bootstrap-theme.min.css" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="css/reg.css" rel="stylesheet">
  <link href="assets/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon">

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <![endif]-->
</head>
<body>

  <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/"><span class="glyphicon glyphicon-play-circle"></span> jmpd</a>
      </div>
      <div class="collapse navbar-collapse">

        <ul id="nav_links" class="nav navbar-nav">
          <li><a href="#" data-toggle="modal" data-target="#settings">Settings</a></li>
        </ul>
<!-- 
        <form id="search" class="navbar-form navbar-right" role="search">
          <div class="form-group">
            <input type="text" class="form-control" placeholder="Search">
         </div>
        </form>
-->
        <div class="btn-toolbar navbar-btn navbar-right" role="toolbar">
<!--          
          <div class="btn-group">
            <button type="button" class="btn btn-default" onclick="socket.send('MPD_API_SET_PREV');">
              <span class="glyphicon glyphicon-backward"></span>
            </button>
            <button type="button" class="btn btn-default" onclick="socket.send('MPD_API_SET_STOP');">
              <span id="stop-icon" class="glyphicon glyphicon-stop"></span>
            </button>
            <button type="button" class="btn btn-default" onclick="clickPlay();">
              <span id="play-icon" class="glyphicon glyphicon-pause"></span>
            </button>
            <button type="button" class="btn btn-default" onclick="socket.send('MPD_API_SET_NEXT');">
              <span class="glyphicon glyphicon-forward"></span>
            </button>
            <div class="btn btn-toolbar btn-default">
              <span id="volume-icon" class="glyphicon glyphicon-volume-up"></span>
              <div id="volumeslider"></div>
            </div>
          </div>

          
          <audio id="player" preload="none" src="http://mpd:8000/mpd.ogg"></audio>
          <div class="btn-group">
            <button type="button" class="btn btn-default" onclick="clickLocalStop()">
              <span id="stop-icon" class="glyphicon glyphicon-stop"></span>
            </button>
            <button type="button" class="btn btn-default" onclick="clickLocalPlay()">
              <span id="play-icon" class="glyphicon glyphicon-play"></span>
            </button>
            <div class="btn btn-toolbar btn-default">
              <span id="volume-icon" class="glyphicon glyphicon-volume-up"></span>
              <div id="localvolumeslider"></div>
            </div>
          </div>
-->
          
        </div>
      </div><!--/.nav-collapse -->
    </div>
  </div>

  <div class="container starter-template">
    <div class="row">

      <div class="col-md-10 col-xs-12">
        <div class="notifications top-right"></div>

<!--         
        <div class="panel panel-primary">
-->
          <!-- Default panel contents -->
<!-- 
          <div class="panel-heading"><b id="panel-heading">Queue</b></div>

          <div class="panel-body">
            <h1>
              <span id="track-icon" class="glyphicon glyphicon-play"></span>
              <span id="currenttrack"></span>
            </h1>
            <h4>
              <span id="album" class="text"></span>
              <span id="artist" class="text pull-right"></span>
            </h4>
            <p id="counter" class="text pull-right">&nbsp;&nbsp;</p>

            <div id="progressbar"></div>


          </div> --> <!-- /.panel-body -->

<!-- 
          <ol id="breadcrump" class="breadcrumb">
          </ol>
 -->
 
          <!-- Table -->
          <!--
          <table id="salamisandwich" class="table table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>Title</th>
                <th>Duration</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
          -->
          
<!--    </div>  -->
		<!-- /.panel -->
<!--         
        <ul class="pager">
          <li id="prev" class="page-btn hide"><a href="">Previous</a></li>
          <li id="next" class="page-btn"><a href="">Next</a></li>
        </ul>
-->
      </div><!-- /.col-md-10 -->

      <div class="col-md-2 col-xs-12" >
        <div class="btn-toolbar">
<!--           
          <div class="btn-group-vertical btn-block btn-group-lg" data-toggle="buttons">
            <button id="btnrandom" type="button" class="btn btn-default">
              <span class="glyphicon glyphicon-random"></span> Random
            </button>
            <button id="btnconsume" type="button" class="btn btn-default">
              <span class="glyphicon glyphicon-fire"></span> Consume
            </button>
            <button id="btnsingle" type="button" class="btn btn-default">
              <span class="glyphicon glyphicon-star"></span> Single
            </button>
            <button id="btnrepeat" type="button" class="btn btn-default">
              <span class="glyphicon glyphicon-repeat"></span> Repeat
            </button>
          </div>
-->

          <div id="btn-responsive-block" class="btn-group-vertical btn-block btn-group-lg">
<!--          
            <button type="button" class="btn btn-default" onclick="updateDB();">
              <span class="glyphicon glyphicon-refresh"></span> Update DB
            </button>
            <button type="button" class="btn btn-default" onclick="socket.send('MPD_API_RM_ALL');">
              <span class="glyphicon glyphicon-trash"></span> Clear queue
            </button>
-->
          </div>

  	      <div id="btn-responsive-block" class="btn-group-vertical btn-block btn-group-lg" data-toggle="buttons">
  	        <button type="button" class="btn btn-default" id="btnnotify">
  		     <span class="glyphicon glyphicon-comment"></span> Notifications
  	        </button>
  	      </div>
        </div>
      </div><!-- /.col-md-2 -->
    </div><!-- /.row -->
  </div><!-- /.container -->

  <!-- Modal -->
  <div class="modal fade" id="settings" tabindex="-1" role="dialog" aria-labelledby="settingsLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h2 class="modal-title" id="settingsLabel"><span class="glyphicon glyphicon-wrench"></span> Settings</h2>
        </div>
        <div class="modal-body">          
          <table id="services" class="table table-hover">
            <thead>
                <tr>
                  <th>
                  	<label class="control-label" for="Id">Id</label>
            		<input type="text" class="form-control" id="Id" />
              	  </th>
                  <th>                  	
                    <label class="control-label" for="Name">Name</label>
            		<input type="text" class="form-control" id="Name" />
              	  </th>
                  <th>                  	
                    <label class="control-label" for="Url">Url</label>
            		<input type="text" class="form-control" id="Url" />
              	  </th>
              	  <th>                  	
                	<button type="button" class="btn btn-default" id="addbtn">+</button>
              	  </th>       
                </tr>              
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
        <div class="modal-footer">
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->


  <div class="modal fade bs-example-modal-sm" id="wait" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1>Searching...</h1>
        </div>
        <div class="modal-body">
          <div class="progress progress-striped active">
            <div class="progress-bar"  role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
              <span class="sr-only">Please Wait</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>


  <!-- Bootstrap core JavaScript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="js/jquery-1.10.2.min.js"></script>
  <script src="js/jquery.cookie.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/bootstrap-notify.js"></script>
  <script src="js/bootstrap-slider.js"></script>
  <script src="js/sammy.js"></script>
  <script src="js/datajs-1.1.2.js"></script>
  <script src="js/reg.js"></script>
</body>
</html>
