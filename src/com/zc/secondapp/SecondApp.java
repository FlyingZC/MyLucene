package com.zc.secondapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.zc.entity.Article;
import com.zc.util.LuceneUtil;

public class SecondApp {
	@Test
	public void createIndexDB() throws Exception{
		Article arcticle=new Article(1,"城市","江苏省");
		Document document=LuceneUtil.javaben2document(arcticle);
		IndexWriter indexWriter
		=new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
	}
	
	//根据关键字从索引库中查询符合条件的数据
	@Test
	public void findIndexDB() throws Exception{
		String keywords="城";
		List<Article> articleList=new ArrayList<Article>();
		
		QueryParser queryParser=new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		IndexSearcher indexSearcher=new IndexSearcher(LuceneUtil.getDirectory());
		Query query = queryParser.parse(keywords);
		TopDocs topDocs = indexSearcher.search(query,10);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article) LuceneUtil.document2javabean(document,Article.class);
			articleList.add(article);
		}
		for(Article article : articleList){
			System.out.println(article.getId()+":"+article.getTitle()+":"+article.getContent());
		}

	}
	
}
