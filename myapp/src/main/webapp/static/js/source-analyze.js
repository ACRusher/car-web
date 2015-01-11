$(
  function(){
      $("#analyze").click(
          function(){
              var code=$("#content").val();
              var request= $.ajax({
                  url : "/CodeAnalyzer/analyze.json",
                  type:"POST",
                  data:{
                      source : code
                  }
              });
              request.done(function (msg) {
                  $("#htmlCode").val(msg);
              });
              request.fail(function(jqXHR, textStatus) {
                  $("#htmlCode").val( "Request failed: " + textStatus );
              });
          }
      );
  }
);