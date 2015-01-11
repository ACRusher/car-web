
$(function(){
    $("#upload").click(
        function(){
            var title=$("#title").val();
            var content=$("#content").val();
            var request= $.ajax({
//                url : "/multievent/SayHello2/English.json",
                url : "/UploadArticle/article.json",
                type:"POST",
                data:{
                    t : title,
                    c : content
                }
            });
            request.done(function (msg) {
                alert("success : "+msg.toString());
            });
            request.fail(function(jqXHR, textStatus) {
                alert( "Request failed: " + textStatus );
            });

        }
    );
    $("#preSee").click(
        function(){
            $("#preSeeContainer")[0].innerHTML=$("#content")[0].value;
            parseSource($("#content")[0].value);//解析源码中的js脚本
        }
    )
    });
