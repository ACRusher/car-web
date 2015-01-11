package com.hangzhou.life.app1.module.screen;

import com.alibaba.citrus.turbine.dataresolver.Param;
import com.hangzhou.life.app1.module.common.ArticleDAO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by handsome.guy on 2014/12/27.
 */
public class UploadArticle {
    @Resource
    private HttpServletResponse httpServletResponse;
    @Resource
    private ArticleDAO articleDAO;
    public Object doArticle(@Param("t") String title, @Param("c") String content) {
        if(title==null || content==null){
            return "FAIL:参数不能为空.";
        }
        Long id=articleDAO.addArticle(title,content);
        if(id!=null){
            return "SUCSESS:文章id="+id+"title="+title+" content="+content;
        }
        return "FAIL:系统错误.";

    }

}
