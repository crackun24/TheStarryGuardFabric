package com.thestarryguard.thestarryguard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Lang {//语言文件类
    File lang_file;//语言文件的路径
    public final String FILE_NAME = "lang.properties";//语言文件的名字
    public final String ERROR_MSG;
    public final String INVALID_PAGE;
    public final String ILLEGAL_PAGE;
    public final String PAGE_TITLE;
    public final String PAGE_ENTRY;
    public final String NO_DATA;
    public final String ENABLE_QUERY;
    public final String DISABLE_QUERY;

    private Lang(String err_msg, String invalid_page, String illegal_page, String page_tile, String page_entry, String no_data
            , String enable_query, String disable_query) {
        ERROR_MSG = err_msg;
        INVALID_PAGE = invalid_page;
        ILLEGAL_PAGE = illegal_page;
        PAGE_TITLE = page_tile;
        PAGE_ENTRY = page_entry;
        NO_DATA = no_data;
        ENABLE_QUERY = enable_query;
        DISABLE_QUERY = disable_query;
    }

    public static Lang LoadLang(File path) throws IOException {
        if (!path.exists())//判断文件是否存在
        {
            return new Lang("§cInternal error.", "§cNot such page.", "§cInvalid page count.",
                    "Totally %s entries,Page: %s / %s \n", "%s %s %s, %s ago.\n",
                    "§cNo data.", "§2Enable point check.", "§cDisable point check.");//返回默认的语言配置
        }

        Reader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);//以UTF-8的字符集读取配置文件数据
        Properties prop = new Properties();//创建一个properties文件的解析对象

        prop.load(reader);//解析配置文件

        return new Lang(prop.getProperty("error").toString(), prop.getProperty("page_not_found").toString()
                , prop.getProperty("illegal_page"), prop.get("page_title").toString(), prop.get("page_entry").toString()
                , prop.getProperty("no_data").toString(),prop.getProperty("enable_point_query").toString(),
                prop.getProperty("disable_point_query")
        );
    }
}
