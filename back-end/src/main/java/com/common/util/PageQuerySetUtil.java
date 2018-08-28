package com.common.util;

import com.github.pagehelper.PageHelper;

import java.util.Map;

/**
 * Created by lego-jspx01 on 2016/5/24.
 */
public class PageQuerySetUtil {

    /**
     * 分页设置
      * @param map
     */
    public static void setPageQuery(Map map){
        if(null!=map){
            int pageIndex = map.get("offset") == null ? -1 : Integer.parseInt(map.get("offset").toString());
            int pageSize  = map.get("limit") == null? Constants.PAGE_SIZE:Integer.parseInt(map.get("limit").toString());
            if(-1!=pageIndex){//-1表示不需要分页
                PageHelper.startPage(pageIndex, pageSize);//页码，每页记录数
            }
        }
    }
}
