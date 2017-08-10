package com.accountbook.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.accountbook.globle.Constants;

/**
 * 启动时获取一些初始的参数
 * @author xinjun
 *
 */
public class ContextInitParamListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Constants.EXTERN_FILE_DIR = arg0.getServletContext().getInitParameter("ExternFileDir");
	}

}
