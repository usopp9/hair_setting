package kr.or.dgit.hair_setting.dao.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import kr.or.dgit.erp_application.jdbc.LoadProperties;
import kr.or.dgit.hair_setting.dao.ExecuteSql;


public class BackupService implements DaoService{
	private static final BackupService instance = new BackupService();

	public static BackupService getInstance() {
		return instance;
	}

	public BackupService() {
	}
	@Override
	public void service() {
		LoadProperties loadProperties = new LoadProperties();
		Properties properties = loadProperties.getProperties();
		
		ExecuteSql.getInstance().execSQL("use " +properties.getProperty("dbname"));
		
		checkBackupDir();
		
		String[] tables = properties.get("tables").toString().split(",");
		for(String tblName : tables) {
			String sql = String.format("select * from %s", tblName);
			exportData(sql,tblName);
		}
	}

	private void exportData(String sql,String tblName){
		try {
			ResultSet rs = ExecuteSql.getInstance().execQuerySQL(sql);
			int columnCnt = rs.getMetaData().getColumnCount();
			StringBuilder sb = new StringBuilder();
	//		System.out.println("columnCnt"+ columnCnt);
			while(rs.next()) {
				for(int i =1;i<= columnCnt;i++) {
					//int인지 String인지 구분이 안갈때 .getObject
					sb.append(rs.getObject(i)+",");
				}
				//마지막 ,를 공백으로 
				sb.replace(sb.length()-1, sb.length(), "");
				//한줄 띄우기
				sb.append("\r\n");
			}
			writBackupFile(sb.toString(),tblName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writBackupFile(String result, String tblName) {
		String resPath =System.getProperty("user.dir")+ "\\BackupFiles\\"+tblName+".csv" ;
		resPath =resPath.replace("\\","/");
		
		try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(resPath),"euc-kr");) {
			osw.write(result);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	private void checkBackupDir() {
		//파일 존재 유무 확인
		File backupDir = new File(System.getProperty("user.dir") +"\\BackupFiles");
		if(backupDir.exists()) {
			//backupDir.listFiles(), backupDir안에 있는 file들
			for( File file :backupDir.listFiles()) {
				file.delete();
				System.out.printf("%s Delete Success! %n",file.getName());
			}
		}else {
			//폴더를 만들어줌
			backupDir.mkdir();
			System.out.printf("%s Create Success! %n",backupDir.getName());
		}
		}
}
