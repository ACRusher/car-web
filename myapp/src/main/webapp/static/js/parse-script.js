function parseSource(html){
    if(html==undefined || html==""){
        return;
    }
    var startTag="<script>";
    var endTag="</script>";
    var N=startTag.length;
    var M=endTag.length;
    var start=html.indexOf(startTag);
    while(start!=-1){
        var end=html.indexOf(endTag,start+N);
        if(end==-1){
            return;
        }
        var source=html.substring(start+N,end);
        source="\""+source+"\"";
        source.replace(/\\s+/,"");
        console.log(source);
        eval(source);//解析js脚本
        start=html.indexOf(startTag,end+M);
    }
    return;
}