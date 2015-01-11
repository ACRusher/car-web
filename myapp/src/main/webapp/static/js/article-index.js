$(function(){

    Date.prototype.Format = function(fmt)
    { //author: meizz
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }
    //获得前十条文章
    getArticle(1,10);
    //设置文章总页数
    setTotalCnt();
    //为上一页和下一个注册事件
    $("#prePage").click(function(){
        var cur=$("#curPage")[0].innerHTML;
        if(cur <= 1){
            alert("没有上一页了!")
            return;
        }
        $("#curPage")[0].innerHTML=cur-1;
        getArticle(cur-1,10);
    });
    $("#nextPage").click(function(){
        var cur=new Number($("#curPage")[0].innerHTML);
        var total=new Number($("#totalPage")[0].innerHTML);
        if(cur>= total){
            alert("没有下一页了!")
            return;
        }

        $("#curPage")[0].innerHTML=cur+1;
        getArticle(cur+1,10);
    });
    $("#firstPage").click(function(){
        var cur=$("#curPage")[0].innerHTML;
        if(cur!=1){
            getArticle(1,10);
            $("#curPage")[0].innerHTML=1;
        }
        return;
    });
    $("#lastPage").click(function(){
        var cur=$("#curPage")[0].innerHTML;
        var total=$("#totalPage")[0].innerHTML;
        if(cur<total){
            getArticle(total,10);
            $("#curPage")[0].innerHTML=total;
        }
        return;
    });

});
function setTotalCnt(){
    var request= $.ajax({
        url : '/QueryArticle/TotalCnt.json',
        type : 'POST'
    });
    request.done(function(cnt){
        if(cnt == undefined){
            cnt=1;
        }
        $("#totalPage")[0].innerHTML=(cnt/10+0.4).toFixed(0);
    });
}
function getArticle( page, pageSize){
    var request= $.ajax({
        url : '/QueryArticle/Article.json',
        type : 'POST',
        data : {
            page : page,
            pageSize : pageSize
        }
    });
    request.done(function(data){
        console.log(data);
        var th=$('#list')[0].children[0];
        $('#list')[0].innerHTML="";
        $('#list')[0].appendChild(th);
        for(i in data){
            article=data[i];
            console.log(article);
            console.log(article.id);
            var item=document.createElement('div');
            item.setAttribute('style','height: 30px;text-align: center;');

            var titleDiv=document.createElement('div');
            titleDiv.setAttribute('style','width: 60%;float: left;');
//            titleDiv.setAttribute('class','item');
            var titleLink=document.createElement('a');
            titleLink.setAttribute('href','/study/article.vm?id='+article['id']);
            var titleText=document.createTextNode(article['title']);
            titleLink.appendChild(titleText);
            titleDiv.appendChild(titleLink);
            var timeDiv=document.createElement('div');
            timeDiv.setAttribute('style','width: 19%;float: left;font-size: 15px;');
            timeDiv.setAttribute('class','item');
            var timeText=document.createTextNode(new Date(article['gmt_created']).Format('yyyy-MM-dd hh:mm:ss'));
            timeDiv.appendChild(timeText);
            var visitedDiv=document.createElement('div');
            visitedDiv.setAttribute('style','width:19%;float: left;font-size: 15px;');
            visitedDiv.setAttribute('class','item');
            visitedDiv.innerHTML=article['visited'];
            item.appendChild(titleDiv);
            item.appendChild(timeDiv);
            item.appendChild(visitedDiv);
            var list=document.getElementById("list");
            list.appendChild(item);
        }
    });
    request.fail(function(jqXHR, textStatus) {
        alert( "Request failed: " + textStatus );
    });
}
/**
 *     <div style="height: 30px;text-align: center">
 <div style="width: 60%;float: left;"><a href="/life/heart.vm"> 测试文章标题</a></div>
 <div style="width: 19%;float: left;font-size: 15px;">2014-10-10 10:11</div>
 <div style="width:19%;float: left;font-size: 15px;">20</div>
 </div>
 */