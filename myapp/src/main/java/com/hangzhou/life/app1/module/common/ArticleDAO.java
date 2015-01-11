package com.hangzhou.life.app1.module.common;

import com.hangzhou.life.app1.module.common.dto.Article;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by handsome.guy on 2014/12/27.
 */
public class ArticleDAO {
    @Resource
    private SqlMapClient sqlMapClient;
    private Logger logger=LoggerFactory.getLogger(ArticleDAO.class);

    public Long addArticle(String title,String content){
        Article article=new Article();
        article.setTitle(title);
        article.setContent(content);
        Long id=null;
        try {
            id= (Long) sqlMapClient.insert("my_article.insert",article);
        } catch (SQLException e) {
            logger.error("上传文章失败",e);
        }
        return id;
    }

    public Integer getTotalCnt(){
        Integer result=null;
        try {
            result= (Integer) sqlMapClient.queryForObject("my_article.getTotalCnt");
        } catch (SQLException e) {
            logger.error("查询文章数量失败", e);
        }
        return result;
    }
    public boolean setVisited(Long id,Integer visited){
        Map param=new HashMap();
        param.put("id",id);
        param.put("visited",visited);
        try {
            sqlMapClient.update("my_article.updateVisited",param);
        } catch (SQLException e) {
            logger.error("更新访问次数失败", e);
            return false;
        }
        return true;
    }

    public List<Article> queryArticle(int start,int size){
        Map param=new HashMap();
        param.put("start",start);
        param.put("size",size);
        List<Article> result=null;
        try {
            result=sqlMapClient.queryForList("my_article.getArticleByPage",param);
        } catch (SQLException e) {
            logger.error("查询文章列表失败", e);
        }
        return result;
    }

    public Article getArticleDetail(Long id) throws Exception{
        Article result=null;
//        try {
            result= (Article) sqlMapClient.queryForObject("my_article.getArticleDetail",id);
//        } catch (SQLException e) {
//            logger.error("查询文章详情失败", e);
//        }
        return result;
    }

}
