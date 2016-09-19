package org.lucene.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class IndexUtil {
	
	public void index() {
		try {
			File f = new File("d:/lucene/example2/MyBatis 3 User Guide Simplified Chinese.pdf");
			Directory dir = FSDirectory.open(new File("d:/lucene/file2"));
			IndexWriter writer = new IndexWriter(dir,new IndexWriterConfig(Version.LUCENE_35, new MMSegAnalyzer()));
			writer.deleteAll();
			Document doc = new Document();
			doc.add(new Field("content",new Tika().parse(f)));
			writer.addDocument(doc);
			writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String tikaTool(File f) throws IOException, TikaException {
		Tika tika = new Tika();
		Metadata metadata = new Metadata();
		metadata.set(Metadata.AUTHOR, "¿ÕºÅ");
		metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
		String str = tika.parseToString(new FileInputStream(f),metadata);
		for(String name:metadata.names()) {
			System.out.println(name+":"+metadata.get(name));
		}
		return str;
	}
	
	public String fileToTxt(File f) {
		Parser parser = new AutoDetectParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.AUTHOR, "¿ÕºÅ");
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			is = new FileInputStream(f);
			ContentHandler handler = new BodyContentHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class,parser);
			parser.parse(is,handler, metadata,context);
			for(String name:metadata.names()) {
				System.out.println(name+":"+metadata.get(name));
			}
			return handler.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
