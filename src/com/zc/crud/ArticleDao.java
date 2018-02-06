package com.zc.crud;

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

/**
 * 增删改查 索引库
 * */
public class ArticleDao {
	@Test
	public void add() throws Exception{
		Article article=new Article(1,"城市","宿迁市一座现代城市");
		Document document=LuceneUtil.javaben2document(article);
		IndexWriter indexWriter
		=new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		indexWriter.addDocument(document);
		indexWriter.close();
	}
	@Test
	public void addAll() throws Exception{
		Article article1=new Article(1,"城市","宿迁市一座美丽城市");
		Article article2=new Article(2,"城市","宿迁市一座卫生城市");
		Article article3=new Article(3,"城市","宿迁市一座文明城市");
		Article article4=new Article(4,"城市","宿迁市一座现代城市");
		Article article5=new Article(5,"城市","宿迁市一座无敌城市");
	}
	
	@Test
	public void delete() throws Exception{
		
	}
	
	@Test
	public void deleteAll() throws Exception{
		
	}
	
	@Test
	public void update() throws Exception{
		
	}
	
	@Test
	public void find() throws Exception{
		List<Article> articleList = new ArrayList<Article>();
		String keywords = "城市";
		QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
		TopDocs topDocs = indexSearcher.search(query,10);
		for(int i = 0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];	
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			System.out.println(document);
			Article article = (Article) LuceneUtil.document2javabean(document,Article.class);
			articleList.add(article);
		}
		for(Article article : articleList){
			System.out.println(article.getId()+":"+article.getTitle()+":"+article.getContent());
		}
	}
}
	




