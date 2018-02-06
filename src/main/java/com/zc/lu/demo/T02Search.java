package com.zc.lu.demo;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;

import com.zc.lu.LuceneConstants;
import com.zc.lu.Searcher;

public class T02Search
{
    private Searcher searcher;
    private String indexDir;

    private void searchUsingTermRangeQuery(String searchQueryMin, String searchQueryMax)
            throws IOException, ParseException
    {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        //create the term query object
        Query query = new TermRangeQuery(LuceneConstants.FILE_NAME, searchQueryMin, searchQueryMax, true, false);
        //do the search
        TopDocs hits = searcher.search(query);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime) + "ms");
        for (ScoreDoc scoreDoc : hits.scoreDocs)
        {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }
}
