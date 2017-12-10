package com.accountbook.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.accountbook.model.Group;
import com.accountbook.model.UserInfo;
import com.accountbook.service.IGroupService;


/**
 * 创建分组头像的工具
 * @author XINJUN
 *
 */
public class IconUtil {
	
	public static void main(String[] args){
		Group g=new Group();
		g.id="测试";
		g.name="AAA";
		createIcon(g, null);
	}
	
	/**
	 * 绘制分组的icon
	 * @throws IOException 
	 */
	public static String createIcon(Group group,List<String> memberIcons){
		System.out.println("createIcon："+group+",icons:"+memberIcons);
	    int iconSize=500;
	    
	    BufferedImage image = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_RGB );
		Graphics g= image.getGraphics();
		g.setColor(new Color(222,222, 222, 255));
		g.fillRect(0, 0, iconSize, iconSize);
		
		
		try {
			for(int i=0;memberIcons!=null && i<memberIcons.size();i++){
				if(i>8)
					break;
				System.out.println("本地头像路径："+memberIcons.get(i));
				File imgFile = new File(memberIcons.get(i));
				if(!imgFile.exists())
					imgFile=new File(FileUtils.getDefaultImagePath());
				Image srcImage = ImageIO.read(imgFile); //读取图片文件
				int[] position = calcPosition(memberIcons.size()>9?9:memberIcons.size(), i, iconSize, iconSize/10);
				g.drawImage(srcImage, position[0], position[1], position[2], position[3], null);  //将原始图片 按固定大小绘制到image中
			}
			if(memberIcons==null || memberIcons.size()==0){
				
				int padding=(int) (iconSize*(0.1f));
				int fontSize=iconSize-padding*2;
				System.out.println("字体大小："+fontSize);
				g.setFont(new Font("微软雅黑",Font.BOLD,fontSize));
				g.setColor(Color.black);
				if(group.name!=null && group.name.length()>0){
					char charAt=group.name.charAt(0);
					int charWidth = g.getFontMetrics().stringWidth(String.valueOf(charAt));
					System.out.println("char0:"+String.valueOf(charAt));
					System.out.println("charWidth:"+charWidth);
					g.drawString(String.valueOf(charAt), (iconSize-charWidth)/2, iconSize-padding*2);
				}
				
			}
			String relativePath=FileUtils.genarateFileRelativePath(group.id);
			String absolutePath=FileUtils.getImageAbsolutePath(relativePath);
			File outputFile=new File(absolutePath);
			ImageIO.write(image, "jpeg", outputFile);  //写入磁盘
			System.out.println("输入绘制头像："+outputFile.getAbsolutePath());
			return relativePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	    
	}

	public static int[] calcPosition(int totalCount, int index, int size, int padding) {
		int picSize=0;
		int line1Top=0;
		int line2Top=0;
		int line3Top=0;
	    switch (totalCount) {
	        case 1:
	        	//一个人时调整一下padiing
	        	padding=size/5;
	            picSize = size - padding * 2;
	            return new int[]{padding, padding, picSize, picSize};
	        case 2:
	            picSize = (size - padding * 2) / 2;
	            switch (index) {
	                case 0:
	                	return new int[]{padding, padding, picSize, picSize};
	                case 1:
	                	return new int[]{size / 2, size / 2, picSize, picSize};
	            }
	            break;
	        case 3:
	            picSize = (size - padding * 3) / 2;
	            switch (index) {
	                case 0:
	                	return new int[]{(size - picSize) / 2, padding, picSize, picSize};
	                case 1:
	                	return new int[]{padding, padding * 2 + picSize, picSize, picSize};
	                case 2:
	                	return new int[]{padding * 2 + picSize, padding * 2 + picSize, picSize, picSize};
	            }
	            break;
	        case 4:
	            picSize = (size - padding * 3) / 2;
	            switch (index) {
	                case 0:
	                    return new int[]{padding, padding, picSize, picSize};
	                case 1:
	                    return new int[]{padding * 2 + picSize, padding, picSize, picSize};
	                case 2:
	                    return new int[]{padding, padding * 2 + picSize, picSize, picSize};
	                case 3:
	                    return new int[]{padding * 2 + picSize, padding * 2 + picSize, picSize, picSize};
	            }
	            break;
	        case 5:
	            picSize = (size - padding * 4) / 3;
	            line1Top = (size - (picSize * 2 + padding)) / 2;
	            line2Top = line1Top + picSize + padding;
	            switch (index) {
	                case 0:
	                    return new int[]{line1Top, line1Top, picSize, picSize};
	                case 1:
	                    return new int[]{line2Top, line1Top, picSize, picSize};
	                case 2:
	                    return new int[]{padding, line2Top, picSize, picSize};
	                case 3:
	                    return new int[]{padding * 2 + picSize, line2Top, picSize, picSize};
	                case 4:
	                    return new int[]{padding * 3 + picSize * 2, line2Top, picSize, picSize};
	            }
	            break;
	        case 6:
	            picSize = (size - padding * 4) / 3;
	            line1Top = (size - (picSize * 2 + padding)) / 2;
	            line2Top = line1Top + picSize + padding;
	            switch (index) {
	                case 0:
	                    return new int[]{padding, line1Top, picSize, picSize};
	                case 1:
	                    return new int[]{padding * 2 + picSize, line1Top, picSize, picSize};
	                case 2:
	                    return new int[]{padding * 3 + picSize * 2, line1Top, picSize, picSize};
	                case 3:
	                    return new int[]{padding, line2Top, picSize, picSize};
	                case 4:
	                    return new int[]{padding * 2 + picSize, line2Top, picSize, picSize};
	                case 5:
	                    return new int[]{padding * 3 + picSize * 2, line2Top, picSize, picSize};
	            }
	            break;
	        case 7:
	            picSize = (size - padding * 4) / 3;
	            line1Top = padding;
	            line2Top = padding * 2 + picSize;
	            line3Top = padding * 3 + picSize * 2;
	            switch (index) {
	                case 0:
	                    return new int[]{(size - picSize) / 2, line1Top, picSize, picSize};
	                case 1:
	                    return new int[]{padding, line2Top, picSize, picSize};
	                case 2:
	                    return new int[]{padding * 2 + picSize, line2Top, picSize, picSize};
	                case 3:
	                    return new int[]{padding * 3 + picSize * 2, line2Top, picSize, picSize};
	                case 4:
	                    return new int[]{padding, line3Top, picSize, picSize};
	                case 5:
	                    return new int[]{padding * 2 + picSize, line3Top, picSize, picSize};
	                case 6:
	                    return new int[]{padding * 3 + picSize * 2, line3Top, picSize, picSize};
	            }
	            break;
	        case 8:
	            picSize = (size - padding * 4) / 3;
	            line1Top = padding;
	            line2Top = padding * 2 + picSize;
	            line3Top = padding * 3 + picSize * 2;
	            switch (index) {
	                case 0:
	                    return new int[]{(size - picSize * 2 - padding) / 2, line1Top, picSize, picSize};
	                case 1:
	                    return new int[]{(size - picSize * 2 - padding) / 2 + picSize + padding, line1Top, picSize, picSize};
	                case 2:
	                    return new int[]{padding, line2Top, picSize, picSize};
	                case 3:
	                    return new int[]{padding * 2 + picSize, line2Top, picSize, picSize};
	                case 4:
	                    return new int[]{padding * 3 + picSize * 2, line2Top, picSize, picSize};
	                case 5:
	                    return new int[]{padding, line3Top, picSize, picSize};
	                case 6:
	                    return new int[]{padding * 2 + picSize, line3Top, picSize, picSize};
	                case 7:
	                    return new int[]{padding * 3 + picSize * 2, line3Top, picSize, picSize};
	            }
	            break;
	        case 9:
	            picSize = (size - padding * 4) / 3;
	            line1Top = padding;
	            line2Top = padding * 2 + picSize;
	            line3Top = padding * 3 + picSize * 2;
	            switch (index) {
	                case 0:
	                    return new int[]{padding, line1Top, picSize, picSize};
	                case 1:
	                    return new int[]{padding * 2 + picSize, line1Top, picSize, picSize};
	                case 2:
	                    return new int[]{padding * 3 + picSize * 2, line1Top, picSize, picSize};
	                case 3:
	                    return new int[]{padding, line2Top, picSize, picSize};
	                case 4:
	                    return new int[]{padding * 2 + picSize, line2Top, picSize, picSize};
	                case 5:
	                    return new int[]{padding * 3 + picSize * 2, line2Top, picSize, picSize};
	                case 6:
	                    return new int[]{padding, line3Top, picSize, picSize};
	                case 7:
	                    return new int[]{padding * 2 + picSize, line3Top, picSize, picSize};
	                case 8:
	                    return new int[]{padding * 3 + picSize * 2, line3Top, picSize, picSize};
	            }
	            break;

	    }
		return null;
	}

	
	
	
	
	/**
	 * 更新分组头像,当成员变动时,或者初次创建时
	 */
	public static void updateGroupIcon(IGroupService groupService,String groupId){
		
		Group group=groupService.queryGroupInfo(groupId);
		List<UserInfo> users = groupService.findUsersByGroupId(groupId);
		
		if(TextUtils.isNotEmpty(group.icon)){
			File oldFile=new File(FileUtils.getImageAbsolutePath(group.icon));
			if(oldFile.exists()){
				System.out.println("updateGroupIcon.删除旧图:"+oldFile.getAbsolutePath());
				oldFile.delete();
			}
		}
		System.out.println("updateGroupIcon.users:"+users);
		List<String> icons=new ArrayList<>();
		for(UserInfo user:users){
			String iconPath = FileUtils.getImageAbsolutePath(user.avatarUrl);
			System.out.println("updateGroupIcon.iconPath:"+iconPath);
			icons.add(iconPath);
		}
		
		group.icon=IconUtil.createIcon(group, icons);
		
		System.out.println("updateGroupIcon.更新头像:"+group);
		
		groupService.updateGroupInfo(group);
	}

}
