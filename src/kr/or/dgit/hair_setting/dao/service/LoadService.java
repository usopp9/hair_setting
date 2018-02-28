package kr.or.dgit.hair_setting.dao.service;

import java.util.Properties;

import kr.or.dgit.erp_application.jdbc.LoadProperties;
import kr.or.dgit.hair_setting.dao.ExecuteSql;


public class LoadService implements DaoService{
	private static final LoadService instance = new LoadService();

	public static LoadService getInstance() {
		return instance;
	}

	private LoadService() {}
	
	@Override
	public void service() {
		LoadProperties loadProperties = new LoadProperties();
		Properties properties = loadProperties.getProperties();
		
	//	ExecuteSql.getInstance().execSQL("use erp_project");
		ExecuteSql.getInstance().execSQL("use " +properties.getProperty("dbname"));
		ExecuteSql.getInstance().execSQL("set foreign_key_checks=0");
		
		String[] tables = properties.get("tables").toString().split(",");
		for(String tblName : tables) {
			ExecuteSql.getInstance().execSQL(getPath(tblName));
		}
		ExecuteSql.getInstance().execSQL("set foreign_key_checks=1");
	}

	private String getPath(String tblName) {
		String path = String.format("%s\\DataFiles\\%s.csv", System.getProperty("user.dir"), tblName);
		String sql = String.format("load data local infile '%s' into table %s character set 'euckr' fields TERMINATED by ','", path,tblName);
		sql = sql.replace("\\", "/");
		return sql;
	}
}
