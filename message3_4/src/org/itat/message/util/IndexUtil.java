package org.itat.message.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.itat.message.vo.Attachment;
import org.itat.message.vo.IndexField;
import org.itat.message.vo.Message;

public class IndexUtil {
	public static final String MSG_TYPE = "Message";
	public static final String ATTACHMENT_TYPE = "Attachment";
	
	public final static int ADD_OP = 1;
	public final static int DEL_OP = 2;
	public final static int UPDATE_OP = 3;
	
	public static List<String> indexAttachType = null;
	static {
		indexAttachType = new ArrayList<String>();
		indexAttachType.add("doc");
		indexAttachType.add("docx");
		indexAttachType.add("pdf");
		indexAttachType.add("txt");
	}

	public static IndexField msg2IndexField(Message msg) {
		IndexField field = new IndexField();
		try {
			field.setId("0_"+msg.getId());
			field.setObjId(msg.getId());
			field.setParentId(0);
			field.setTitle(msg.getTitle());
			field.setType(MSG_TYPE);
			field.setCreateDate(msg.getCreateDate());
			String content = msg.getContent();
			field.setContent(new Tika().parseToString(new ByteArrayInputStream(content.getBytes())));
			return field;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IndexField attach2Field(Attachment att) throws IOException, TikaException {
		IndexField field = new IndexField();
		String filename = att.getNewName();
		if(indexAttachType.contains(FilenameUtils.getExtension(filename))) {
			String realPath = SystemContext.getRealPath();
			String path = realPath+"/upload/"+att.getNewName();
			System.out.println(path);
			field.setContent(new Tika().parseToString(new File(path)));
			field.setId(att.getMessage().getId()+"_"+att.getId());
			field.setObjId(att.getId());
			field.setParentId(att.getMessage().getId());
			field.setTitle(att.getMessage().getTitle());
			field.setType(ATTACHMENT_TYPE);
			field.setCreateDate(att.getCreateDate());
			return field;
		} else {
			return null;
		}
	}
}
