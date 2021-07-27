package com.liang.mybatis.core.io;

import java.io.InputStream;

public class Resources {

    /**
     * 根据配置文件的路径，i将配置文件加载成字节流，存储在内存中
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path){

        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
