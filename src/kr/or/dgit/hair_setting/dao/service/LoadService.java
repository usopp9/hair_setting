package kr.or.dgit.hair_setting.dao.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import erp_application.jdbc.LoadProperties;
import kr.or.dgit.hair_setting.dao.ExecuteSql;


public class LoadService implements DaoService{
	private static final LoadService instance = new LoadService();

	public static LoadService getInstance() {
		return instance;
	}

	private LoadService() {}
	
	@Override
	public void service() {
		File f = new File(System.getProperty("user.dir")+"\\resources\\insert_sql.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(f));){
			StringBuilder sb = new StringBuilder();
			String line = null;
			while( (line = br.readLine()) != null ) {
				if ( !line.isEmpty() && !line.startsWith(",")) {
					sb.append(line);
				}
				if (line.endsWith(";")) {
					ExecuteSql.getInstance().execSQL(sb.toString());
					sb.setLength(0);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
