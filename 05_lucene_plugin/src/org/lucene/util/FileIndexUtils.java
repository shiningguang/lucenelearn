package org.lucene.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class FileIndexUtils {
	private static Directory directory = null;
	static{
		try {
			directory = FSDirectory.open(new File("d:/lucene/files/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Directory getDirectory() {
		return directory;
	}
	
	private static Document generatorDoc(File f) throws IOException {
		if(f.isDirectory()) return null;
		Document doc = new Document();
		Metadata metadata = new Metadata();
		doc.add(new Field("content",new Tika().parse(new FileInputStream(f),metadata),TermVector.WITH_POSITIONS_OFFSETS));
		doc.add(new Field("title",FilenameUtils.getBaseName(f.getName()),Field.Store.YES,Field.Index.ANALYZED,TermVector.WITH_POSITIONS_OFFSETS));
		doc.add(new Field("filename",f.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("path",f.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("type",FilenameUtils.getExtension(f.getName()),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
		int page = 0;
		try {
			page = Integer.parseInt(metadata.get("xmpTPg:NPages"));
		} catch (NumberFormatException e) {
		}
		doc.add(new NumericField("page",Field.Store.YES,true).setIntValue(page));
		doc.add(new NumericField("date",Field.Store.YES,true).setLongValue(f.lastModified()));
		doc.add(new NumericField("size",Field.Store.YES,true).setIntValue((int)(f.length()/1024)));
		return doc;
	}
	
	public static void index(boolean hasNew) {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new MMSegAnalyzer()));
			if(hasNew) {
				writer.deleteAll();
			}
			File file = new File("d:/lucene/example2");
			Document doc = null;
			for(File f:file.listFiles()) {
				doc = generatorDoc(f);
				if(doc!=null)
					writer.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null) writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
