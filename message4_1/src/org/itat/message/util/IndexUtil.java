package org.itat.message.util;

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
	
	/**
	 * 把附件的内容添加到field的content中
	 * @param fieldContents
	 * @param att
	 */
	public static void attach2Index(List<String> fieldContents,Attachment att) {
		try {
			String filename = att.getNewName();
			if(IndexUtil.indexAttachType.contains(FilenameUtils.getExtension(filename))) {
				String realPath = SystemContext.getRealPath();
				String path = realPath+"/upload/"+att.getNewName();
				fieldContents.add(new Tika().parseToString(new File(path)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
	}

	public static IndexField msg2IndexField(Message msg) {
		IndexField field = new IndexField();
		field.setId(msg.getId()+"");
		field.setObjId(msg.getId());
		field.setTitle(msg.getTitle());
		field.setType(MSG_TYPE);
		field.setCreateDate(msg.getCreateDate());
		return field;
	}

}
