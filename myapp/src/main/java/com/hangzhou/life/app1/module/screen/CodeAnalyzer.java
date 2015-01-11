package com.hangzhou.life.app1.module.screen;

import com.alibaba.citrus.turbine.dataresolver.Param;
import com.alibaba.citrus.util.CollectionUtil;
import com.alibaba.citrus.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by handsome.guy on 2015/1/4.
 */
public class CodeAnalyzer {
    private final String tab="&nbsp;&nbsp;&nbsp;&nbsp;";

    /**
     * 代码转换入口
     * @param source
     * @return
     */
    public String doAnalyze(@Param("source")String source){
        return analyze(source);
    }

    public String analyze(String source){
        if(StringUtils.isEmpty(source)){
            return "";
        }
        StringBuilder result=new StringBuilder();
//        result.append("<div style=\"width:80%;min-height:1000px;text-align:left;background-color:#f2eada;padding:30px;font-family:Arial,Serif;letter-spacing:0.5px;margin:0 auto;\">");
        //左边编号栏
        StringBuilder left=new StringBuilder();
        left.append("<div style=\"border:3px grey ;border-style:none solid none none;float:left;padding:23px 3px;color:grey\">");
        //右边源代码
        StringBuilder right=new StringBuilder();
        right.append("<div style=\"float:left;padding:20px;\">");
        //将源代码分行
        String[] lines=source.split("\n");
        //控制每行的缩进
        StringBuilder lineHead=new StringBuilder();
        int i=0;
        int maxLength=0;//每行代码的最长字符数
        for(String line : lines){
            line=line.trim();
            if(StringUtils.isEmpty(line)){
                continue;
            }
            i++;
            left.append("<span>").append(i).append("</span><br/>");
            right.append("<span>");
            if(line.startsWith("}")){
                lineHead.delete(0, tab.length());
            }
            String lineSpace=lineHead.toString();//每行的缩进
            int t=lineSpace.length()/6+line.length();//计算字符数
            if(t>maxLength) maxLength=t;
            right.append(lineSpace);
            right.append(analyzeWord(line));
            right.append("</span><br/>");
            if(line.endsWith("{")){
                lineHead.append(tab);
            }
        }
        left.append("</div>");
        right.append("</div>");
        int divHeight=17*lines.length;//高度像素
        int divWidth=80+(maxLength<100?0:maxLength<150?10:20);//宽度百分比
        result.append("<div style=\"width:").append(divWidth).append("%;min-height:")
                .append(divHeight).append("px;text-align:left;background-color:#f2eada;")
                .append("padding:30px;font-family:Arial,Serif;letter-spacing:0.5px;margin:0 auto;\">");
        result.append(left);
        result.append(right);
        result.append("</div>");
        return result.toString();
    }

    /**
     * 每行拆词分析入口
     * @param line
     * @return
     */
    private String analyzeWord(String line){
        //空行过滤
        if(StringUtils.isEmpty(line)){
            return "";
        }
        StringBuilder result=new StringBuilder();
        line=line.trim();
        //注释处理
        if(line.startsWith("//")||line.startsWith("*")||line.startsWith("/*")||line.startsWith("*/")){
            result.append("<span style=\"color:grey\">").append(line).append("</span>");
            return result.toString();
        }
        //将有效源代码 按行拆词
        List<String> words=splitToWords(line);

        String preWord="";
        for(String word : words){
           if(isKeyWord(word)){
               //关键字处理
               result.append("<span style=\"color:"+getKeyColor(word,preWord)+";\">")
                       .append(word).append("</span>&nbsp;");
           }else if(isConst(word)){
               //常量处理
               result.append("<span style=\"color:blue;\">")
                       .append(word).append("</span>&nbsp;");
           }else{
               //其他单词处理
               result.append(analyzePlainWord(word)).append("&nbsp;");
           }
           preWord=word;
        }
        return result.toString();
    }

    /**
     * 拆词时将源代码中字符串常量作为一个词
     * @param line
     * @return
     */
    private List<String> splitToWords(String line){
        String comment="";//行注释
        if(line.contains("//")){
            String[]arr=line.split("//");
            line=arr[0];
            comment="//"+arr[1];
        }
        List<String> words=new ArrayList<String>();
        String spliter="\\s+";
        int index=0,preIndex=0;
        while((index=line.indexOf('\"',index))!=-1){
            if(index!=0) {
                String temp =line.substring(preIndex,index);
                words.addAll(Arrays.asList(temp.split(spliter)));
            }
            preIndex=index;
            index=line.indexOf('\"',preIndex+1);
            int temp=index+1;
            while(line.charAt(index-1)=='\\'){
                index=line.indexOf('\"',temp);
                temp=index+1;
            }
            if(index==-1) break;
            words.add(line.substring(preIndex,index+1));
            index++;
            preIndex=index;
            if(index>=line.length()) break;
        }
        if(preIndex<=line.length()-1){
            String temp =line.substring(preIndex,line.length());
            words.addAll(Arrays.asList(temp.split(spliter)));
        }
        if(StringUtils.isNotEmpty(comment)){
            words.add(comment);
        }
        return words;
    }
    /**
     * 分析一个非关键字的普通字符串
     * @param word
     * @return
     */
    private String analyzePlainWord(String word){
        if(word.startsWith("\"") && word.endsWith("\"")){
            return "<span style=\"color:green\">"+word+"</span>";
        }
        if(word.startsWith("//")){
            return "<span style=\"color:grey\">"+word+"</span>";
        }
        StringBuilder result=new StringBuilder();
        String[]subWords=word.split(regex);
        Matcher matcher= wordSplitPattern.matcher(word);
        List<String> spliter=new ArrayList<String>();
        while(matcher.find()){
            spliter.add(matcher.group());
        }
        int i=0;
        if(spliter.size()>0 && subWords.length==0){
            result.append(getPlainWordHtml(spliter.get(0)));
        }
        for(String subWord : subWords){
            if(StringUtils.isNotEmpty(subWord)) {
                result.append(getPlainWordHtml(subWord));
            }
            if(i<spliter.size()){
                result.append(getPlainWordHtml(spliter.get(i)));
            }
            i++;
        }
        return result.toString();
    }
    private String getPlainWordHtml(String word){
        if(getPlainColor(word).equals("black")){
            if(word.matches("[\\w]+")&&word.toUpperCase().equals(word)){
                word="<em>"+word+"</em>";
            }
            return word;
        }
        StringBuilder result=new StringBuilder();
        result.append("<span style=\"color:"+getPlainColor(word)+";\">").append(word).append("</span>");
        return result.toString();
    }
    private String getKeyColor(String word,String preWord){
        return "#33a3dc";
    }
    private String getPlainColor(String word){
        if(StringUtils.isNumeric(word) || word.equals("true")||word.equals("false")
                ||word.equals("null")){
            return "blue";
        }
        if(word.contains("@")){
            return "#CCCC00";
        }
        if(isClassName(word)){
            return "#990066";
        }
        return "black";
    }
    private boolean isClassName(String name){
        if(StringUtils.isAlphanumeric(name) && StringUtils.capitalize(name).equals(name)){
            return true;
        }
        return false;
    }

    private boolean isKeyWord(String word){
        if(keyWords.contains(word)){
            return true;
        }
        return false;
    }
    private boolean isConst(String word){
        if(word.equals("true")||word.equals("false")|| StringUtils.isNumeric(word)){
            return true;
        }
        return false;
    }
    private final String regex="[^a-zA-Z0-9_@]+";
    private final Pattern wordSplitPattern=Pattern.compile(regex);


    public static void main(String[] args) {
        String source="if ( true ) {\ni++; \n String s=\"ttt\";\n} else {\ni--;\n}\nreturn;";
        CodeAnalyzer a=new CodeAnalyzer();

        String s="ss,);ee,);";
        System.out.println("~".matches("[\\w]+"));
//        System.out.println(Arrays.toString("s/d".split("/")));
//        System.out.println("/d".split("/")[0].equals(""));


    }
    private static final Set<String> keyWords=new HashSet<String>();
    static {
        String[] arr={ "abstract","assert","boolean","break","byte",
                "case","catch","char","Character","class","native","public",
                "const","continue","default","do","double","else",
                "enum","extends","final","long","package","new",
                "private","protected","return","strictfp","short",
                "static", "finally", "float","goto","for","implements",
                "if","import","instanceof","int","Integer","interface","super",
                "switch","synchronized","this","throw","throws",
                "transient","void","try","volatile","while",};
        keyWords.addAll(Arrays.asList(arr));
    }

}
