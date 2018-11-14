package com.zc.lu;

import java.io.File;
import java.io.FileFilter;

// 2.txt文件过滤器
public class TextFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
}
