package org.lucene.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

public class MySameTokenFilter extends TokenFilter {
	private CharTermAttribute cta = null;
	private PositionIncrementAttribute pia = null;
	private AttributeSource.State current;
	private Stack<String> sames = null;
	private SamewordContext samewordContext;

	protected MySameTokenFilter(TokenStream input,SamewordContext samewordContext) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
		pia = this.addAttribute(PositionIncrementAttribute.class);
		sames = new Stack<String>();
		this.samewordContext = samewordContext;
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(sames.size()>0) {
			//将元素出栈，并且获取这个同义词
			String str = sames.pop();
			//还原状态
			restoreState(current);
			cta.setEmpty();
			cta.append(str);
			//设置位置0
			pia.setPositionIncrement(0);
			return true;
		}
		
		if(!this.input.incrementToken()) return false;
		
		if(addSames(cta.toString())) {
			//如果有同义词将当前状态先保存
			current = captureState();
		}
		return true;
	}
	
	private boolean addSames(String name) {
		String[] sws = samewordContext.getSamewords(name);
		if(sws!=null) {
			for(String str:sws) {
				sames.push(str);
			}
			return true;
		}
		return false;
	}
	

}
