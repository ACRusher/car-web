$(function(){
    alert("success!");
});
$(function(){
    $("#queryTbid").click(
        function queryIdByNick(){
            var nick=$("#nick").val();
            var responseObj=$.ajax(
                {
                    url:"/sign/TbidQueryAction.do?nick="+nick,
                    type : "GET",
                    async:true
                }
            );
            $("#tbid").html(responseObj.responseText);
        }
    )
});
