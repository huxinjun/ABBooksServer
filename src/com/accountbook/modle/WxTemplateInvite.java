package com.accountbook.modle;

import java.util.List;

/**
 * 邀请模板
 * @author Administrator
 *
 */
public class WxTemplateInvite {
	
	public String touser;
	public String template_id;
	public String page;
	public String form_id;
	
	
	public List<KeyWord> data;
	
	
	
	public static class KeyWord{
		private String value;
		private String color;
		
		
		
		public KeyWord(String value, String color) {
			super();
			this.value = value;
			this.color = color;
		}



		@Override
		public String toString() {
			return "{\"value\":\""+value+"\",\"color\":\""+color+"\"}";
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		
		sb.append("\"touser\":\""+touser+"\",");
		sb.append("\"template_id\":\""+template_id+"\",");
		sb.append("\"page\":\""+page+"\",");
		sb.append("\"form_id\":\""+form_id+"\",");
		
		sb.append("\"data\":{");
		
		for(int i=0;data!=null && i<data.size();i++)
			sb.append("\"keyword"+(i+1)+"\":"+data.get(i).toString()+(i==data.size()-1?"":","));
		
		sb.append("}");
		
		
		
		sb.append("}");
		return sb.toString();
	}

}
