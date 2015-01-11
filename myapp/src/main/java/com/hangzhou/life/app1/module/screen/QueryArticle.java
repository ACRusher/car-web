package com.hangzhou.life.app1.module.screen;

import com.alibaba.citrus.turbine.dataresolver.Param;
import com.hangzhou.life.app1.module.common.ArticleDAO;
import com.hangzhou.life.app1.module.common.dto.Article;
import com.ibatis.sqlmap.client.SqlMapClient;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by handsome.guy on 2014/12/27.
 */
public class QueryArticle {
    @Resource
    private ArticleDAO articleDAO;
    @Resource
    private SqlMapClient sqlMapClient;

    public Object doArticle(@Param("page") Integer page, @Param("pageSize") Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        int start = (page - 1) * pageSize;
        int size = pageSize;
        List<Article> result = articleDAO.queryArticle(start, size);
        return result;
    }

    public Integer doTotalCnt() throws Exception {
//        return articleDAO.getTotalCnt();
        Integer result = (Integer) sqlMapClient.queryForObject("my_article.getTotalCnt");
        return result;
    }
}
