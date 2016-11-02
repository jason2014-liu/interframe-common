/**
* TODO
* @Project: interframe-common
* @Title: LuceneDemo.java
* @Package com.interframe.common.lucene
* @author jason
* @Date 2016年11月2日 上午9:53:53
* @Copyright
* @Version 
*/
package com.interframe.common.lucene;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * TODO
 * 
 * @ClassName: LuceneDemo
 * @author jason
 */
public class LuceneDemo {

	private static Analyzer analyzer = new SmartChineseAnalyzer();
	private static Directory directory = new RAMDirectory();

	/**
	 * TODO
	 * 
	 * @Title: main
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException 
	 */
	public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
		// TODO Auto-generated method stub
		String testText = "国家级中心城市";
		testSplitChinese(testText);

		createIndex();
		searchIndex();

	}

	/**
	 * TODO 中文分词
	 * 
	 * @Title: testSplitChinese
	 * @param text
	 * @throws IOException
	 */
	public static void testSplitChinese(String text) throws IOException {

		// Analyzer analyzer = new SmartChineseAnalyzer();

		TokenStream tokenizer = analyzer.tokenStream("text", new StringReader(text));
		tokenizer.reset();

		CharTermAttribute offAtt = (CharTermAttribute) tokenizer.addAttribute(CharTermAttribute.class);

		System.out.println("分词结果：");
		// 循环打印分词结果，及分词出现的位置
		while (tokenizer.incrementToken()) {
			System.out.println(offAtt.toString());
		}
		tokenizer.close();
	}

	public static void createIndex() throws IOException {

		// Analyzer analyzer = new SmartChineseAnalyzer();

		// Directory directory = new RAMDirectory();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, iwc);

		Document doc = null;

		doc = new Document();
		doc.add(new TextField("title", "国家级中心城市", Store.YES));
		doc.add(new TextField("content", "成都,北京,上海,深圳", Store.YES));
		writer.addDocument(doc);

		doc = new Document();
		doc.add(new TextField("title", "副省级城市", Store.YES));
		doc.add(new TextField("content", "成都,厦门", Store.YES));
		writer.addDocument(doc);

		writer.commit();
		writer.close();

	}

	public static void searchIndex() throws IOException, ParseException, InvalidTokenOffsetsException {

		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));

		// Query query = new QueryParser("title",
		// analyzer).parse("title:'国家级'");
		TermQuery query = new TermQuery(new Term("title", "国家级"));

		// Finds the top n hits for query
		TopDocs topdoc = searcher.search(query, 10);

		// The total number of hits for the query.
		System.out.println("命中个数:" + topdoc.totalHits);

		// The top hits for the query.
		ScoreDoc[] hits = topdoc.scoreDocs;

		// highlighter
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));

		if (hits != null && hits.length > 0) {
			for (int i = 0; i < hits.length; i++) {
				Document hitdoc = searcher.doc(hits[i].doc);
				System.out.println("title:" + hitdoc.get("title"));
				
				//highlighter 
				TokenStream ts = analyzer.tokenStream("title", new StringReader(hitdoc.get("title")));
				String title = highlighter.getBestFragment(ts, hitdoc.get("title"));
				System.out.println("highlighter title:" + title);
			}
		}

	}

}
