package com.zc.zlu;

import com.zc.lu.demo.T01Document;
import org.apache.lucene.document.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
// 静态导入
import static com.zc.constant.MyConstant.*;
public class T01 {
    public static File docFile = new File(DOC_FILE);
    /**测试 创建文档document*/
    @Test
    public void testGetDoc() throws IOException {
        Document doc = T01Document.getDocument(docFile);
        System.out.println(doc);
    }

    /**测试创建indexWriter*/
    @Test
    public void testCreateIndexerWriter() throws IOException {
        T01Document.createIndexWriter(IDX_DIR);
    }

    /**测试 对文档进行索引*/
    @Test
    public void testIndexFile() throws IOException {
        T01Document.indexFile(docFile, IDX_DIR);
    }
}
