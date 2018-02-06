package com.zc.lu;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

//测试索引和 搜索
public class LuceneTester
{

    String indexDir = "E:\\Lucene\\Index";

    String dataDir = "E:\\Lucene\\Data";

    Indexer indexer;

    Searcher searcher;

    public static void main(String[] args)
    {
        LuceneTester tester;
        try
        {
            tester = new LuceneTester();
            tester.createIndex();
            tester.search("Mohan");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException
    {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");
    }

    private void search(String searchQuery) throws IOException, ParseException
    {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime));
        for (ScoreDoc scoreDoc : hits.scoreDocs)
        {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }

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

    //TermRangeQuery是在使用的范围内的文本的词条都被搜索,这里只搜索(record2和record6)
    @Test
    public void testSearchUsingTermRangeQuery()
    {
        LuceneTester tester;
        try
        {
            tester = new LuceneTester();
            tester.searchUsingTermRangeQuery("record2.txt", "record6.txt");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
