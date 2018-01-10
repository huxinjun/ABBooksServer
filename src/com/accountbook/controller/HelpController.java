package com.accountbook.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.globle.Constants;
import com.accountbook.modle.result.Result;
import com.accountbook.service.impl.TokenServiceImpl;

/**
 * 帮助
 * @author xinjun
 *
 */

@Controller
@RequestMapping("/help")
public class HelpController {
	
	
	
	private static ArrayList<Help> helps;
	private static ArrayList<Help> helpsSimple;
	
	@ResponseBody
	@RequestMapping(value="/list")
	public Object getList(){
		if(helps==null)
			loadHelpList();
		return new Result().put(Result.RESULT_OK, "查询成功！").put("list", helpsSimple);
	}
	
	@ResponseBody
	@RequestMapping(value="/detail")
	public Object getDetail(String fileName){
		if(helps==null)
			loadHelpList();
		Help help = Help.findByFileName(helps, fileName);
		String detail=help!=null?help.content:null;
		detail=detail.replaceAll("<img src='", "<img src='"+Constants.HOST+"/images/");
		return new Result().put(Result.RESULT_OK, "查询成功！").put("content", detail);
	}
	
	/**
	 * 初始化
	 */
	public static void loadHelpList() {
		helps=new ArrayList<>();
		helpsSimple=new ArrayList<>();
		try {
			// 配置文件位于当前目录中的config目录下
			InputStream resourceAsStream = TokenServiceImpl.class.getClassLoader().getResourceAsStream("help_list.txt");
			BufferedReader br=new BufferedReader(new InputStreamReader(resourceAsStream));
			String line;
			while((line=br.readLine())!=null){
				Help help=new Help();
				if(line.contains("=")){
					help.name=line.split("=")[0];
					help.fileName=line.split("=")[1];
					//读取文件内容
					InputStream fileSteam = TokenServiceImpl.class.getClassLoader().getResourceAsStream(help.fileName);
					int length = fileSteam.available();
					byte[] bytes=new byte[length];
					fileSteam.read(bytes, 0, length);
					fileSteam.close();
					help.content=new String(bytes);
				}else
					help.name=line;
				
				helps.add(help);
				
				Help helpSimple=new Help();
				helpSimple.name=help.name;
				helpSimple.fileName=help.fileName;
				helpsSimple.add(helpSimple);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		loadHelpList();
        System.out.println(helpsSimple);
	}
	
	@SuppressWarnings("serial")
	public static class Help implements Cloneable,Serializable{
		public String name;
		public String fileName;
		public String content;
		@Override
		public String toString() {
			return "Help [name=" + name + ", fileName=" + fileName + ", content=" + content + "]";
		}
		
		public static Help findByName(List<Help> helps,String name){
			if(helps!=null){
				for(Help help:helps)
					if(help.name.equals(name))
						return help;
			}
			return null;
		}
		public static Help findByFileName(List<Help> helps,String filename){
			if(helps!=null){
				for(Help help:helps)
					if(help.fileName.equals(filename))
						return help;
			}
			return null;
		}
	}
}
