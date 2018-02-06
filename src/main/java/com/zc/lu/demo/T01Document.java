package com.zc.lu.demo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.zc.lu.LuceneConstants;

public class T01Document
{
    //一.创建Document(读取现有文件创建Document对象)
    private Document getDocument(File file) throws IOException
    {
        //1.创建一个document对象,代表一个文件
        Document document = new Document();

        //2.向改文件中写入key为"content",value为文件内容 等 三个Field
        //index file contents
        Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file));
        //index file name
        Field fileNameField = new Field(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES,
                Field.Index.NOT_ANALYZED);
        //index file path
        Field filePathField = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES,
                Field.Index.NOT_ANALYZED);

        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);

        return document;
    }

    //二.创建IndexWriter(index目录下 生成write.lock文件)
    public IndexWriter createIndexer(String indexDirectoryPath) throws IOException
    {
        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        //create the indexer
        IndexWriter writer = new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_36), true,
                IndexWriter.MaxFieldLength.UNLIMITED);
        return writer;
    }

    //三.开始索引
    private void indexFile(File file) throws IOException
    {
        System.out.println("Indexing " + file.getCanonicalPath());
        //1.获取Document文件(里面包含Field)
        Document document = getDocument(file);
        //2.创建IndexWriter对象
        IndexWriter writer = createIndexer("E:\\Lucene\\Index");
        //3.输出索引文件 至index目录下(_0.fdt等)
        writer.addDocument(document);
    }

    @Test
    public void testGetDoc() throws IOException
    {
        Document doc = new T01Document().getDocument(new File("E:\\Lucene\\Data\\data.txt"));
        System.out.println(doc);
    }

    @Test
    public void testCreateIndexer() throws IOException
    {
        new T01Document().createIndexer("E:\\Lucene\\Index");
    }

    @Test
    public void testIndexFile() throws IOException
    {
        new T01Document().indexFile(new File("E:\\Lucene\\Data\\data.txt"));
    }

    //四.更新索引
    @Test
    public void updateDocument() throws IOException
    {
        File file = new File("E:\\Lucene\\Data\\data.txt");
        Document document = getDocument(file);
        IndexWriter writer = createIndexer("E:\\Lucene\\Index");
        
        //update indexes for file contents
        writer.updateDocument(new Term(LuceneConstants.CONTENTS), document);
        writer.close();
    }

    //五.删除索引
    @Test
    public void deleteDocument() throws IOException
    {
        File file = new File("E:\\Lucene\\Data\\data.txt");
        Document document = getDocument(file);
        IndexWriter writer = createIndexer("E:\\Lucene\\Index");
        
        //deleteDocuments(Term) - 删除所有包含这个词条的文件。       
        //deleteDocuments(Query) - 删除所有与查询匹配的文档。
        writer.deleteDocuments(new Term(LuceneConstants.FILE_NAME));

        writer.commit();
        System.out.println("index contains deleted files: " + writer.hasDeletions());
        System.out.println("index contains documents: " + writer.maxDoc());
        System.out.println("index contains deleted documents: " + writer.numDocs());
    }

}
