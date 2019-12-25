package com.shoppurs.logging;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.shoppurs.connection.DaoConnection;

public class MyLogger {
	
	@Autowired
	private DaoConnection daoConnection;
	
	@Autowired
	private MyException myException;
	
	public void addExceptionLog(Exception e,String userType,String userCode,
			String className,String methodName,String userName,String dbUserName,String dbPassword) {
		
		if(TextUtils.isEmpty(dbUserName)) {
			dbUserName = "shoppurs_master";
			dbPassword ="Wh@tNxt$24680#";
		}
		
		if(TextUtils.isEmpty(userCode)) {
			userCode = "shoppurs";
		}
		
		StringBuilder sb = new StringBuilder();
		for(StackTraceElement th : e.getStackTrace()) {
			sb.append(th.toString()+"\n\r");
		}
		
		
		
		JdbcTemplate loggerJdbc = daoConnection.getDynamicDataSource(DaoConnection.LOGGER_DB_NAME,
				dbUserName,dbPassword);
		
		String sql = "INSERT INTO `logger_master`\r\n" + 
				"(`ID`,\r\n" + 
				"`USER_TYPE`,\r\n" + 
				"`USER_CODE`,\r\n" + 
				"`CLASS_NAME`,\r\n" + 
				"`METHOD_NAME`,\r\n" + 
				"`EXCEPTION_TYPE`,\r\n" + 
				"`MESSAGE`,\r\n" + 
				"`STACK_TRACE`,\r\n" + 
				"`CREATED_BY`,\r\n" + 
				"`UPADTED_BY`,\r\n" + 
				"`CREATED_DATE`,\r\n" + 
				"`UPADTED_DATE`)\r\n" + 
				"VALUES\r\n" + 
				"(?,?,?,?,?,?,?,?,?,?,now(),now())";
		try {
			loggerJdbc.update(sql,0,userType,userCode,className,methodName,
					e.getClass().getName(),e.getMessage(),sb.toString(),userName,userName);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public MyException generateExceptionObject(Exception e,String userType,String userCode,
			String className,String methodName,String userName) {
		if(TextUtils.isEmpty(userCode)) {
			userCode = "shoppurs";
		}
		myException.setUserType(userType);
		myException.setUserCode(userCode);
		myException.setClassName(className);
		myException.setMethodName(methodName);
		myException.setExceptionType(e.getClass().getName());
		myException.setMessage(e.getMessage());
		StringBuilder sb = new StringBuilder();
		for(StackTraceElement th : e.getStackTrace()) {
			sb.append(th.toString()+"\n\r");
		}
		myException.setStackTrace(sb.toString());
		myException.setCreatedBy(userName);
		myException.setUpdatedBy(userName);
		return myException;
	}
}
