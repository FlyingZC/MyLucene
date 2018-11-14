package com.zc.firstapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.zc.entity.Article;

/**
 * lucene first demo
 *
 * @author zc
 */
public class FirstApp {
    /**
     * 创建索引库
     * 将Article对象放入索引库中的原始记录表中
     * 从而形成词汇表
     * (1)将javabean->Document对象(所有字段名必须用String类型)
     *
     * @throws IOException
     * @throws LockObtainFailedException
     * @throws CorruptIndexException
     */
    @Test
    public void createIndexDB() throws CorruptIndexException, LockObtainFailedException, IOException {
        Article article = new Article(1, "城市", "宿迁市一个江苏城市");
        //创建Document对象
        Document document = new Document();
        /*将Article对象中的属性绑定到Document对象中
         * Store:是否将id属性值存入 由原始记录表中 转存入词汇表
         * Store.YES:该属性值会存入词汇表
         * Store.NO:该属性值不会存入词汇表
         *
         * Index:是否将id属性值进行分词算法
         * Index.ANALYZED:表示该属性值会进行词汇拆分
         * Index.NOT_ANALYZED:
         * 提倡将非id值都进行词汇拆分
         * */
        document.add(new Field("xid", article.getId().toString(), Store.YES, Index.ANALYZED));
        document.add(new Field("xtitle", article.getTitle().toString(), Store.YES, Index.ANALYZED));
        document.add(new Field("xcontent", article.getContent().toString(), Store.YES, Index.ANALYZED));
        //将document对象写入lucene索引库
        /*Directory:lucene索引库对应于硬盘中的目录.如E:\
         *Analyzer:采用何种分词策略将文本拆分,每种策略都是一个具体的实现类
         *MaxFieldLength:最多将文本拆分成多少个词汇LIMITED表示1w个.即只取 前1w个词汇,若不足1w以实际为准
         * */
        Directory dir = FSDirectory.open(new File("E:\\IndexDB"));
        Version version = Version.LUCENE_30;
        Analyzer analyzer = new StandardAnalyzer(version);
        MaxFieldLength maxFieldLength = MaxFieldLength.LIMITED;
        //该流 将document对象写入lucene索引库
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, analyzer, maxFieldLength);
        indexWriter.addDocument(document);
        //关闭IndexWriter字符流对象
        indexWriter.close();
    }

    /**
     * 根据关键字从索引库中搜索符合条件的内容
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void findIndexDB() throws IOException, ParseException {
        String keywords = "城";
        List<Article> articleList = new ArrayList<Article>();

        Directory directory = FSDirectory.open(new File("E:\\IndexDB"));
        Version version = Version.LUCENE_30;
        Analyzer analyzer = new StandardAnalyzer(version);
        MaxFieldLength maxFieldLength = MaxFieldLength.LIMITED;
        //创建IndexSearcher字符流对象
        IndexSearcher indexSearcher = new IndexSearcher(directory);
        //创建查询解析器对象
        //version:使用分词器的版本,args2:针对document对象中的哪儿属性进行搜索
        QueryParser queryParser = new QueryParser(version, "xcontent", analyzer);
        //创建查询对象 封装查询关键字
        Query query = queryParser.parse(keywords);
        //根据关键字,去索引库中的词汇表搜索
        /*
         * query:表示封装关键字查询对象,其它QueryParser表示查询解析器
         * args2:MAX_RECORD表示如果根据关键字搜索出来的内容较多,只取前MAX_RECORD
         * 个内容,不足则以实际为准
         * */
        int MAX_RECORD = 100;
        TopDocs topDocs = indexSearcher.search(query, MAX_RECORD);
        //迭代器,取出每一个编号.topDocs里的scoreDocs属性为数组
        /*即public class TopDocs{
         * 		private ScoreDoc[] scoreDocs;
         * 		private int totalHits;
         * }
         * public class ScoreDoc{
         * 		private int doc;
         * 		private float score;
         * }
         *
         * */
        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            //取出每一个编号
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            int no = scoreDoc.doc;
            //根据编号去索引库中的原始记录表中查找对应的document对象
            Document document = indexSearcher.doc(no);
            //获取document对象中的三个属性值
            String xid = document.get("xid");
            String xtitle = document.get("xtitle");
            String xcontent = document.get("xcontent");
            //封装到javabean中
            Article article = new Article(Integer.parseInt(xid), xtitle, xcontent);
            //将article对象加入到list集合中
            articleList.add(article);
        }
        for (Article a : articleList) {
            System.out.println(a);
        }
        indexSearcher.close();
    }
}

