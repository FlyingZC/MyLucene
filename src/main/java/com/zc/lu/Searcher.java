package com.zc.lu;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//04 搜索
public class Searcher
{

    IndexSearcher indexSearcher;

    QueryParser queryParser;

    Query query;

    //创建queryParser,QueryParser解析用户输入,并输入到Lucene理解的格式的查询
    public Searcher(String indexDirectoryPath) throws IOException
    {
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        indexSearcher = new IndexSearcher(indexDirectory);
        queryParser = new QueryParser(Version.LUCENE_36, LuceneConstants.CONTENTS,
                new StandardAnalyzer(Version.LUCENE_36));
    }

    //二.搜索
    public TopDocs search(String searchQuery) throws IOException, ParseException
    {
        //1.通过 QueryParser 解析搜索表达式创建一个查询对象。
        query = queryParser.parse(searchQuery);
        //2.通过调用IndexSearcher.search()方法搜索。
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    //二.重载的搜索方法
    public TopDocs search(Query query) throws IOException, ParseException
    {
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    //获取文件
    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException
    {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException
    {
        indexSearcher.close();
    }
}
