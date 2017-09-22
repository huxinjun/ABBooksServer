package com.accountbook.modle.result;

public class Result {
	
	
	public static final int RESULT_OK=0;
	public static final int RESULT_TOKEN_INVALID=1;
	public static final int RESULT_COMMAND_INVALID=2;
	public static final int RESULT_USERINFO_INVALID=3;
	public static final int RESULT_FILE_SAVE_ERROR=4;
	
	
	public static final int RESULT_FAILD=99;
	
	public int status=0;
	public String msg;
	
	public Result() {}
	public Result(int status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	
	
}
