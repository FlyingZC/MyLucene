package com.zc.lu.demo;

import com.zc.constant.MyConstant;
import com.zc.lu.LuceneConstants;
import com.zc.lu.Searcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public class T02Search {
    private static Searcher searcher;
    private static final String indexDir = MyConstant.IDX_DIR;

    private static void searchUsingTermRangeQuery(String searchQueryMin, String searchQueryMax)
            throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        // create the term query object
        // 当用户有请求时， Query 代表用户的查询语句
        Query query = new TermRangeQuery(LuceneConstants.FILE_NAME, searchQueryMin, searchQueryMax, true, false);

        // do the search
        // IndexSearcher 通过函数 search() 搜索搜索 Lucene Index
        TopDocs hits = searcher.search(query);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime) + "ms");
        // IndexSearcher 计算计算 term weight 和score 并且将结果返回给用户
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
        }
        // 返回给用户的文档集合用 TopDocsCollector 表示表示。
        searcher.close();
    }
}
