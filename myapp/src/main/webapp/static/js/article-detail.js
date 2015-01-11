$(function(){
    var id=location.href.split('?')[1].split('=')[1];
    var request= $.ajax({
        url : '/study/ArticleDetail/ArticleDetail.json',
        type : 'POST',
        data : {
            id : id
        }
    });
    request.done(function(article){
//        $("#title")[0].innerHTML=article['title'];
        $("#content")[0].innerHTML=article['content'];
        parseSource(article['content']);
        updateVisited(article['id']);
    });
    request.fail(function(xhr,textStatus){
        alert("Request failed :"+textStatus);
    });
});
function updateVisited(id){
    var request= $.ajax({
        url : '/study/ArticleDetail/UpdateVisited.json',
        type : 'POST',
        data : {
            id : id
        }
    });
}