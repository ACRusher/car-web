package com.hangzhou.life.app1.module.screen.study;

import com.alibaba.citrus.service.velocity.support.EscapeSupport;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.alibaba.citrus.util.StringEscapeUtil;
import com.hangzhou.life.app1.module.common.ArticleDAO;
import com.hangzhou.life.app1.module.common.dto.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by handsome.guy on 2014/12/27.
 */
public class ArticleDetail {
    @Resource
    private ArticleDAO articleDAO;
    Logger logger= LoggerFactory.getLogger(ArticleDetail.class);
    public Article doArticleDetail(@Param("id")Long id){
        Article article=null;
        try{
            article=articleDAO.getArticleDetail(id);
        }catch (Exception e){
            logger.error("",e);
        }
        return article;
    }
    public boolean doUpdateVisited(@Param("id")Long id){
       Article article=doArticleDetail(id);
        if(article==null){
            return false;
        }
        int visited=article.getVisited()==null?0:article.getVisited()+1;
        return articleDAO.setVisited(id,visited);
    }

    public static void main(String[] args) {
        String s="ife <is> hard ,but &lt;big&gt; you can be the best of yourself&lt;/big&gt;";
        System.out.println(StringEscapeUtil.escapeHtml(s));
    }
}
