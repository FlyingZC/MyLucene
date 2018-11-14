package com.zc.lu.demo;

import com.zc.constant.MyConstant;
import com.zc.lu.LuceneConstants;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class T01Document {

    public static IndexWriter indexWriter = null;

    static {
        try {
            indexWriter = createIndexWriter(MyConstant.IDX_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 一.创建Document(读取现有文件创建Document对象)
    // 被索引的文档用 Document 对象表示
    public static Document getDocument(File file) throws IOException {
        // 1.创建一个document对象,代表一个文件
        Document document = new Document();

        // 2.向改文件中写入key为"content",value为文件内容 等 三个Field
        // index file contents. 索引 文件内容
        Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file));
        // index file name. 索引 文件名
        Field fileNameField = new Field(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES,
                Field.Index.NOT_ANALYZED);
        // index file path. 索引 文件路径
        Field filePathField = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES,
                Field.Index.NOT_ANALYZED);

        // 向document中加入field
        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);

        return document;
    }

    // 二.创建IndexWriter对象(该操作会在index目录下 生成write.lock文件)
    // IndexWriter 通过函数 addDocument() 将文档添加到索引中, 实现创建索引的过程
    public static IndexWriter createIndexWriter(String indexDirectoryPath) throws IOException {
        // this directory will contain the indexes. 该目录将会包含多个index
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        // create the indexer. 创建索引, 使用标准分词器StandardAnalyzer
        IndexWriter writer = new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_35), true,
                IndexWriter.MaxFieldLength.UNLIMITED);
        return writer;
    }

    // 三.开始索引
    // IndexWriter 通过函数 addDocument() 将文档添加到索引中， 实现创建索引的过程
    // Lucene 的索引是应用反向索引
    // 会在index目录下生成 _0.fdt 和 _0.fdx
    public static void indexFile(File file, String indexDir) throws IOException {// 向指定目录给file创建索引
        System.out.println("Indexing " + file.getCanonicalPath());
        // 1.获取Document文件(里面包含Field)
        Document document = getDocument(file);
        // 2.创建IndexWriter对象
        IndexWriter writer = createIndexWriter(indexDir);
        // 3.输出索引文件 至index目录下(_0.fdt等)
        writer.addDocument(document);
    }

    /**
     * 索引指定目录下的所有文件
     * @param dataDir  需要进行索引的目录
     * @throws Exception
     */
    public int index(String dataDir, String indexDir) throws Exception {
        //遍历索引目录下的所有文件
        File[] files = new File(dataDir).listFiles();
        for (File f : files) {
            indexFile(f, indexDir);
        }
        //返回索引的文件数量
        // return getI.numDocs();
        return 0;
    }

    // 四.更新索引
    public static void updateDocument(File file, String indexDir) throws IOException {
        Document document = getDocument(file);
        IndexWriter writer = createIndexWriter(indexDir);

        // update indexes for file contents. 更新索引
        writer.updateDocument(new Term(LuceneConstants.CONTENTS), document);
        writer.close();
    }

    // 五.删除索引
    public static void deleteDocument(File file, String indexDir) throws IOException {
        Document document = getDocument(file);
        IndexWriter writer = createIndexWriter(indexDir);

        // deleteDocuments(Term) - 删除所有包含这个词条的文件。
        // deleteDocuments(Query) - 删除所有与查询匹配的文档。
        writer.deleteDocuments(new Term(LuceneConstants.FILE_NAME));
        writer.commit();
        writer.close();

        System.out.println("index contains deleted files: " + writer.hasDeletions());
        System.out.println("index contains documents: " + writer.maxDoc());
        System.out.println("index contains deleted documents: " + writer.numDocs());
    }

}
