package org.frame.web.creator.java.impl;

import java.util.List;
import java.util.Map;

import org.frame.common.io.File;
import org.frame.common.lang.StringHelper;
import org.frame.repository.constant.IRepositoryConstant;
import org.frame.repository.database.IDatabase;
import org.frame.repository.sql.jdbc.IJDBC;
import org.frame.web.creator.java.IJava;

public class Java implements IJava {

	private IJDBC dao;
	
	private String daoType, output, pack, path, superClass, schema;
	
	private boolean removePrefix;
	
	final private String importDate = "import java.util.Date;";
	
	private List<Map<String, String>> data;
	
	public Java(IJDBC dao, String pack, String output, boolean removePrefix, String daoType) {
		this.dao = dao;
		this.removePrefix = removePrefix;
		this.daoType = daoType;
		
		if (StringHelper.isNull(output)) output = "d:/";
		if (StringHelper.isNull(pack)) pack = "default";
		
		this.output = output;
		this.pack = pack;
		
		pack = pack.replace(".", "/");
		
		if (!output.endsWith("/")) output += "/";
		
		path = output + "/java/" + pack ;
		
		if ("spring".equals(daoType)) {
			superClass = "Spring";
		} else {
			superClass = "JDBC";
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void create(String schema, List<String> required, boolean controller, boolean dao, boolean model, boolean service) {
		this.schema = schema;
		
		String sql = this.dao.getDatabase().fields();
		
		for (String table : required) {
			data = (List<Map<String, String>>) this.dao.select(sql, new Object[]{ this.schema, table });
			
			if (model) this.model(table);
			
			if (dao) {
				this.iDao(table);
				this.dao(table);
			}
			
			if (service) {
				this.iService(table);
				this.service(table);
			}
			
			if (controller) this.controller(table);
			
			if (daoType != null) baseDao();
		}
	}
	
	private void baseDao() {
		StringBuffer sbufClass = new StringBuffer();
		sbufClass.append("package org.frame.web.dao;").append("\n");
		sbufClass.append("\n");
		sbufClass.append("import org.frame.repository.sql.").append(superClass.toLowerCase()).append(".impl.").append(superClass).append(";").append("\n");
		sbufClass.append("\n");
		sbufClass.append("public class BaseDao extends ").append(superClass).append(" {").append("\n");
		sbufClass.append("\n");
		sbufClass.append("}");
		
		new File(output + "/org/frame/web/dao/" + "BaseDao.java").byte2file(sbufClass.toString().getBytes());
	}
	
	private void model(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufImport = new StringBuffer();
		sbufImport.append("package ");
		
		if (!"default".equals(pack)) {
			sbufImport.append(pack).append(".");
		}
		
		sbufImport.append("model;").append("\n");
		
		sbufImport.append("\n");
		
		sbufImport.append("import org.frame.repository.annotation.Column;").append("\n");
		sbufImport.append("import org.frame.repository.annotation.Table;").append("\n");
		
		StringBuffer sbufField = new StringBuffer();
		StringBuffer sbufGSetter = new StringBuffer();
		
		boolean isPrimary;
		String column, dataType, type;
		String[] field = null;
		for (Map<String, String> map : data) {
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			dataType = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_TYPE));
			field = this.adjust(column, false);
			type = IDatabase.reference.get(dataType.toLowerCase());
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				isPrimary = true;
			} else {
				isPrimary = false;
			}
			
			if ("Date".equals(type)) {
				if (sbufImport.indexOf(importDate) == -1) {
					sbufImport.append(importDate).append("\n");
				}
			}
			
			sbufField.append("\t").append("@Column(name = \"").append(column).append("\"");
			
			if (isPrimary) {
				sbufField.append(", primary = true)");
			} else {
				sbufField.append(")");
			}
			
			sbufField.append("\n");
			sbufField.append("\t").append("private ").append(type).append(" ").append(field[1]).append(";").append("\n");
			sbufField.append("\n");
			
			sbufGSetter.append("\t").append("public ").append(type).append(" ").append("get").append(field[0]).append("() {").append("\n");
			sbufGSetter.append("\t\t").append("return ").append(field[1]).append(";").append("\n");
			sbufGSetter.append("\t").append("}").append("\n");
			
			sbufGSetter.append("\n");
			
			sbufGSetter.append("\t").append("public void ").append("set").append(field[0]).append("(").append(type).append(" ").append(field[1]).append(") {").append("\n");
			sbufGSetter.append("\t\t").append("this.").append(field[1]).append(" = ").append(field[1]).append(";").append("\n");
			sbufGSetter.append("\t").append("}").append("\n");
		}
		
		sbufImport.append("\n").append("@Table(name = \"").append(table).append("\", simple = false)").append("\n");
		sbufImport.append("public class ").append(tableInfo[0]).append(" {").append("\n").append("\n");
		sbufImport.append(sbufField).append("\n").append(sbufGSetter).append("}");
		
		new File(path + "/model/" + tableInfo[0] + ".java").byte2file(sbufImport.toString().getBytes());
	}
	
	private void iDao(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufClass = new StringBuffer();
		StringBuffer sbufField = new StringBuffer();
		StringBuffer sbufImportMore = new StringBuffer();
		StringBuffer sbufImportUtil = new StringBuffer();
		StringBuffer sbufMethods = new StringBuffer();
		
		sbufClass.append("package ");
		
		if (!"default".equals(pack)) {
			sbufClass.append(pack).append(".");
		}
		
		sbufClass.append(tableInfo[0].toLowerCase()).append(".dao;").append("\n");
		sbufClass.append("\n");
		
		sbufImportUtil.append("import java.util.List;").append("\n");
		sbufImportUtil.append("import java.util.Map;").append("\n");
		sbufImportMore.append("import org.frame.repository.sql.model.Page;").append("\n");
		sbufImportMore.append("import ");
		
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		
		sbufImportMore.append("model.").append(tableInfo[0]).append(";").append("\n");
		sbufImportMore.append("\n");
		
		sbufMethods.append("\t").append("final String SQL_ABANDON_TEMPLET = \"update ").append(table).append(" set flag = :flag where 1 = 1\";").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("final String SQL_DELETE_TEMPLET = \"delete from ").append(table).append(" where 1 = 1\";").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("final String SQL_SELECT_TEMPLET = \"select ");
		
		String column, dataType, type, primary = null;
		String[] field = null;
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			dataType = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_TYPE));
			field = this.adjust(column, false);
			type = IDatabase.reference.get(dataType.toLowerCase());
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				if (StringHelper.isNull(primary)) {
					primary = type + " " + field[1];
				} else {
					primary += ", " + type + " " + field[1];
				}
			}
			
			if ("Date".equals(type)) {
				if (sbufImportUtil.indexOf(importDate) == -1) {
					sbufImportUtil.append(importDate).append("\n");
				}
			}
			
			if (i == data.size() - 1) {
				sbufField.append(type).append(" ").append(field[1]);
				sbufMethods.append(column);
			} else {
				sbufField.append(type).append(" ").append(field[1]).append(", ");
				sbufMethods.append(column).append(", ");
			}
		}
		
		sbufMethods.append(" from ").append(table).append(" where 1 = 1\";").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public List<Integer> abandon(List<Map<String, Object>> parameter);").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public List<Integer> delete(List<Map<String, Object>> parameter);").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public ").append(tableInfo[0]).append(" find(").append(primary).append(");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public Long insert(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public Page pagination(").append(sbufField).append(", Page page);").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public List<").append(tableInfo[0]).append("> select(").append(sbufField).append(");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("public Long update(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufClass.append(sbufImportUtil).append("\n").append(sbufImportMore);
		sbufClass.append("public interface I").append(tableInfo[0]).append("Dao {").append("\n");
		sbufClass.append("\n");
		sbufClass.append(sbufMethods);
		sbufClass.append("}");
		
		new File(path + "/" + tableInfo[0].toLowerCase() + "/dao/I" + tableInfo[0] + "Dao.java").byte2file(sbufClass.toString().getBytes());
	}
	
	private void dao(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufClass = new StringBuffer();
		StringBuffer sbufField = new StringBuffer();
		StringBuffer sbufImportMore = new StringBuffer();
		StringBuffer sbufImportUtil = new StringBuffer();
		StringBuffer sbufMethods = new StringBuffer();
		
		StringBuffer sbufPrimary = new StringBuffer();
		StringBuffer sbufPrimarySql = new StringBuffer();
		StringBuffer sbufPrimaryParameter = new StringBuffer();
		StringBuffer sbufSql = new StringBuffer();
		StringBuffer sbufParameters = new StringBuffer();
		
		sbufClass.append("package ");
		
		if (!"default".equals(pack)) {
			sbufClass.append(pack).append(".");
		}
		
		sbufClass.append(tableInfo[0].toLowerCase()).append(".dao.impl;").append("\n");
		sbufClass.append("\n");
		
		sbufImportUtil.append("import java.util.HashMap;").append("\n");
		sbufImportUtil.append("import java.util.List;").append("\n");
		sbufImportUtil.append("import java.util.Map;").append("\n");
		
		sbufImportMore.append("import org.apache.commons.logging.Log;").append("\n");
		sbufImportMore.append("import org.apache.commons.logging.LogFactory;").append("\n");
		sbufImportMore.append("import org.frame.repository.sql.model.Page;").append("\n");
		sbufImportMore.append("import org.frame.web.dao.BaseDao;").append("\n");
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		
		sbufImportMore.append("model.").append(tableInfo[0]).append(";").append("\n");
		
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		
		sbufImportMore.append(tableInfo[0].toLowerCase()).append(".dao.I").append(tableInfo[0]).append("Dao;\n");
		sbufImportMore.append("\n");
		sbufImportMore.append("import org.springframework.context.annotation.Scope;").append("\n");
		sbufImportMore.append("import org.springframework.stereotype.Repository;").append("\n");
		sbufImportMore.append("\n");
		
		sbufMethods.append("\t").append("@SuppressWarnings(\"unused\")").append("\n");
		sbufMethods.append("\t").append("private Log logger = LogFactory.getLog(").append(tableInfo[0]).append("Dao.class);").append("\n");
		sbufMethods.append("\n");
		
		String column, dataType, type, primary = null;
		String[] field = null;
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			dataType = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_TYPE));
			field = this.adjust(column, false);
			type = IDatabase.reference.get(dataType.toLowerCase());
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				if (StringHelper.isNull(primary)) {
					primary = type + " " + field[1];
				} else {
					primary += ", " + type + " " + field[1];
				}
				
				sbufPrimary.append("\t").append("\t").append(tableInfo[1]).append(".set").append(field[0]).append("(").append(field[1]).append(");").append("\n");
				sbufPrimarySql.append("\t").append("\t").append("sbufSql.append(\" and (:").append(field[1]).append(" is null or ").append(field[1]).append(" = ").append(":").append(field[1]).append(")\");").append("\n");
				sbufPrimaryParameter.append("\t").append("\t").append("parameters.put(\"").append(field[1]).append("\", ").append(field[1]).append(");").append("\n");
			}
			
			if ("Date".equals(type)) {
				if (sbufImportUtil.indexOf(importDate) == -1) {
					sbufImportUtil.append(importDate).append("\n");
				}
			}
			
			if (i == data.size() - 1) {
				sbufField.append(type).append(" ").append(field[1]);
			} else {
				sbufField.append(type).append(" ").append(field[1]).append(", ");
			}
			
			sbufSql.append("\t").append("\t").append("sbufSql.append(\" and (:").append(field[1]).append(" is null or ").append(field[1]).append(" = ").append(":").append(field[1]).append(")\");").append("\n");
			sbufParameters.append("\t").append("\t").append("parameters.put(\"").append(field[1]).append("\", ").append(field[1]).append(");").append("\n");
		}
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("@SuppressWarnings(\"unchecked\")").append("\n");
		sbufMethods.append("\t").append("public List<Integer> abandon(List<Map<String, Object>> parameters) {").append("\n");
		sbufMethods.append("\t").append("\t").append("StringBuffer sbufSql = new StringBuffer(SQL_ABANDON_TEMPLET);").append("").append("\n");
		sbufMethods.append(sbufPrimarySql);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return (List<Integer>) batchExecute(sbufSql.toString(), parameters);").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("@SuppressWarnings(\"unchecked\")").append("\n");
		sbufMethods.append("\t").append("public List<Integer> delete(List<Map<String, Object>> parameters) {").append("\n");
		sbufMethods.append("\t").append("\t").append("StringBuffer sbufSql = new StringBuffer(SQL_DELETE_TEMPLET);").append("").append("\n");
		sbufMethods.append(sbufPrimarySql);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return (List<Integer>) batchExecute(sbufSql.toString(), parameters);").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("public ").append(tableInfo[0]).append(" find(").append(primary).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(" = new ").append(tableInfo[0]).append("();").append("\n");
		sbufMethods.append(sbufPrimary);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return (").append(tableInfo[0]).append(") find(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("public Long insert(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("return (Long) super.insert(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("public Page pagination(").append(sbufField).append(", Page page) {").append("\n");
		sbufMethods.append("\t").append("\t").append("StringBuffer sbufSql = new StringBuffer();").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("sbufSql = new StringBuffer(SQL_SELECT_TEMPLET);").append("\n");
		sbufMethods.append(sbufSql);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("Map<String, Object> parameters = new HashMap<String, Object>();").append("\n");
		sbufMethods.append(sbufParameters);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return pagination(sbufSql.toString(), parameters, ").append(tableInfo[0]).append(".class, page);").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("@SuppressWarnings(\"unchecked\")").append("\n");
		sbufMethods.append("\t").append("public List<").append(tableInfo[0]).append("> select(").append(sbufField).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("StringBuffer sbufSql = new StringBuffer();").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("sbufSql = new StringBuffer(SQL_SELECT_TEMPLET);").append("\n");
		sbufMethods.append(sbufSql);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("Map<String, Object> parameters = new HashMap<String, Object>();").append("\n");
		sbufMethods.append(sbufParameters);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return (List<").append(tableInfo[0]).append(">) select(sbufSql.toString(), parameters, ").append(tableInfo[0]).append(".class);").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Override").append("\n");
		sbufMethods.append("\t").append("public Long update(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("return (Long) super.update(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufClass.append(sbufImportUtil).append("\n").append(sbufImportMore);
		sbufClass.append("@Repository(\"").append(tableInfo[1]).append("Dao\")").append("\n");
		sbufClass.append("@Scope(\"singleton\")").append("\n");
		sbufClass.append("public class ").append(tableInfo[0]).append("Dao extends BaseDao").append(" implements I").append(tableInfo[0]).append("Dao {").append("\n");
		sbufClass.append("\n");
		sbufClass.append(sbufMethods);
		sbufClass.append("}");
		
		new File(path + "/" + tableInfo[0].toLowerCase() + "/dao/impl/" + tableInfo[0] + "Dao.java").byte2file(sbufClass.toString().getBytes());
	}
	
	private void iService(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufClass = new StringBuffer();
		StringBuffer sbufField = new StringBuffer();
		StringBuffer sbufImportMore = new StringBuffer();
		StringBuffer sbufImportUtil = new StringBuffer();
		StringBuffer sbufMethods = new StringBuffer();
		
		sbufClass.append("package ");
		
		if (!"default".equals(pack)) {
			sbufClass.append(pack).append(".");
		}
		
		sbufClass.append(tableInfo[0].toLowerCase()).append(".service;").append("\n");
		sbufClass.append("\n");
		
		sbufImportUtil.append("import java.util.List;").append("\n");
		
		sbufImportMore.append("import org.frame.repository.sql.model.Page;").append("\n");
		sbufImportMore.append("import ");
		
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		
		sbufImportMore.append("model.").append(tableInfo[0]).append(";").append("\n");
		sbufImportMore.append("\n");
		
		String column, dataType, type, primary = null, primarys = null;
		String[] field = null;
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			dataType = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_TYPE));
			field = this.adjust(column, false);
			type = IDatabase.reference.get(dataType.toLowerCase());
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				if (StringHelper.isNull(primary)) {
					primary = type + " " + field[1];
					primarys = "String " + field[1] + "s";
				} else {
					primary += ", " + type + " " + field[1];
					primarys += ", " + "String " + field[1] + "s";
				}
			}
			
			if ("Date".equals(type)) {
				if (sbufImportUtil.indexOf(importDate) == -1) {
					sbufImportUtil.append(importDate).append("\n");
				}
			}
			
			if (i == data.size() - 1) {
				sbufField.append(type).append(" ").append(field[1]);
			} else {
				sbufField.append(type).append(" ").append(field[1]).append(", ");
			}
		}
		
		sbufMethods.append("\t").append("public String abandon(").append(primarys).append(", Integer flag);").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Long create(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Long edit(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Long modify(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Page pagination(").append(sbufField).append(", Page page);").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public List<").append(tableInfo[0]).append("> query(").append(sbufField).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public String remove(").append(primarys).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public ").append(tableInfo[0]).append(" search(").append(primary).append(");").append("\n");
		sbufMethods.append("\n");
		
		sbufClass.append(sbufImportUtil).append("\n").append(sbufImportMore);
		sbufClass.append("public interface I").append(tableInfo[0]).append("Service {").append("\n");
		sbufClass.append("\n");
		sbufClass.append(sbufMethods);
		sbufClass.append("}");
		
		new File(path + "/" + tableInfo[0].toLowerCase() + "/service/I" + tableInfo[0] + "Service.java").byte2file(sbufClass.toString().getBytes());
	}
	
	private void service(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufClass = new StringBuffer();
		StringBuffer sbufField = new StringBuffer();
		StringBuffer sbufImportMore = new StringBuffer();
		StringBuffer sbufImportUtil = new StringBuffer();
		StringBuffer sbufMethods = new StringBuffer();
		
		StringBuffer sbufIf = new StringBuffer();
		
		StringBuffer sbufPrimary = new StringBuffer();
		StringBuffer sbufPrimarys = new StringBuffer();
		StringBuffer sbufPrimaryParameter = new StringBuffer();
		StringBuffer sbufParameter = new StringBuffer();
		
		sbufClass.append("package ");
		
		if (!"default".equals(pack)) {
			sbufClass.append(pack).append(".");
		}
		
		sbufClass.append(tableInfo[0].toLowerCase()).append(".service.impl;").append("\n");
		sbufClass.append("\n");
		
		sbufImportUtil.append("import java.util.ArrayList;").append("\n");
		sbufImportUtil.append("import java.util.HashMap;").append("\n");
		sbufImportUtil.append("import java.util.List;").append("\n");
		sbufImportUtil.append("import java.util.Map;").append("\n");
		sbufImportMore.append("import javax.annotation.Resource;").append("\n");
		sbufImportMore.append("\n");
		sbufImportMore.append("import org.apache.commons.logging.Log;").append("\n");
		sbufImportMore.append("import org.apache.commons.logging.LogFactory;").append("\n");
		sbufImportMore.append("import org.frame.common.lang.StringHelper;").append("\n");
		sbufImportMore.append("import org.frame.repository.sql.model.Page;").append("\n");
		sbufImportMore.append("import org.frame.web.service.BaseService;").append("\n");
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		sbufImportMore.append("model.").append(tableInfo[0]).append(";").append("\n");
		
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		sbufImportMore.append(tableInfo[0].toLowerCase()).append(".dao.I").append(tableInfo[0]).append("Dao;").append("\n");
		
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		sbufImportMore.append(tableInfo[0].toLowerCase()).append(".service.I").append(tableInfo[0]).append("Service;").append("\n");
		sbufImportMore.append("\n");
		sbufImportMore.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
		sbufImportMore.append("import org.springframework.context.annotation.Scope;").append("\n");
		sbufImportMore.append("import org.springframework.stereotype.Repository;").append("\n");
		sbufImportMore.append("\n");
		
		sbufMethods.append("\t").append("private Log logger = LogFactory.getLog(").append(tableInfo[0]).append("Service.class);").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Resource").append("\n");
		sbufMethods.append("\t").append("@Autowired").append("\n");
		sbufMethods.append("\t").append("I").append(tableInfo[0]).append("Dao ").append(tableInfo[1]).append("Dao;").append("\n");
		sbufMethods.append("\n");
		
		String column, dataType, first = null, type, primary = null, primarys = null;
		String[] field = null;
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			dataType = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_TYPE));
			field = this.adjust(column, false);
			type = IDatabase.reference.get(dataType.toLowerCase());
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				if (StringHelper.isNull(primary)) {
					first = field[1];
					primary = type + " " + field[1];
					primarys = "String " + field[1] + "s";
					sbufIf.append(tableInfo[1]).append(".get").append(field[0]).append("() == null");
					sbufPrimary.append(field[1]);
				} else {
					primary += ", " + type + " " + field[1];
					primarys += ", " + "String " + field[1] + "s";
					sbufIf.append(" && ").append(tableInfo[1]).append(".get").append(field[0]).append("() == null");
					sbufPrimary.append(", ").append(field[1]);
				}
				
				sbufPrimarys.append("\t").append("\t").append("\t").append("String[] ").append(field[1]).append(" = ").append(field[1]).append("s.split(\",\");").append("\n");
				sbufPrimaryParameter.append("\t").append("\t").append("\t").append("\t").append("\t").append("map.put(\"").append(field[1]).append("\", ").append(type).append(".valueOf(").append(field[1]).append("[i])").append(");").append("\n");
			}
			
			if ("Date".equals(type)) {
				if (sbufImportUtil.indexOf(importDate) == -1) {
					sbufImportUtil.append(importDate).append("\n");
				}
			}
			
			if (i == data.size() - 1) {
				sbufField.append(type).append(" ").append(field[1]);
				sbufParameter.append(field[1]);
			} else {
				sbufField.append(type).append(" ").append(field[1]).append(", ");
				sbufParameter.append(field[1]).append(", ");
			}
		}
		
		sbufMethods.append("\t").append("public String abandon(").append(primarys).append(", Integer flag) {").append("\n");
		sbufMethods.append("\t").append("\t").append("String result = \"\";").append("\n");
		sbufMethods.append("\t").append("\t").append("if (!StringHelper.isNull(ids)) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("List<Map<String, Object>> parameter = new ArrayList<Map<String, Object>>();").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("Map<String, Object> map;").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append(sbufPrimarys);
		sbufMethods.append("\t").append("\t").append("\t").append("for (int i = 0; i < ").append(first).append(".length; i++) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("map = new HashMap<String, Object>();").append("\n");
		sbufMethods.append(sbufPrimaryParameter);
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("map.put(\"flag\", flag);").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("parameter.add(map);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("} catch (NumberFormatException e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("logger.error(\"id invalid: \" + ids);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("result = new StringHelper().join(").append(tableInfo[1]).append("Dao.abandon(parameter).toArray());").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return result;").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Long create(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("return ").append(tableInfo[1]).append("Dao.insert(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Long edit(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("return ").append(tableInfo[1]).append("Dao.update(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Long modify(").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("if (").append(sbufIf).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("return this.create(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("\t").append("} else {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("return this.edit(").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public Page pagination(").append(sbufField).append(", Page page) {").append("\n");
		sbufMethods.append("\t").append("\t").append("return ").append(tableInfo[1]).append("Dao.pagination(").append(sbufParameter).append(", page);").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public List<").append(tableInfo[0]).append("> query(").append(sbufField).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("return ").append(tableInfo[1]).append("Dao.select(").append(sbufParameter).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public String remove(").append(primarys).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("String result = \"\";").append("\n");
		sbufMethods.append("\t").append("\t").append("if (!StringHelper.isNull(ids)) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("List<Map<String, Object>> parameter = new ArrayList<Map<String, Object>>();").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("Map<String, Object> map;").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append(sbufPrimarys);
		sbufMethods.append("\t").append("\t").append("\t").append("for (int i = 0; i < ").append(first).append(".length; i++) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("map = new HashMap<String, Object>();").append("\n");
		sbufMethods.append(sbufPrimaryParameter);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("parameter.add(map);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("} catch (NumberFormatException e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("\t").append("logger.error(\"id invalid: \" + ids);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("result = new StringHelper().join(").append(tableInfo[1]).append("Dao.delete(parameter).toArray());").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return result;").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("public ").append(tableInfo[0]).append(" search(").append(primary).append(") {").append("\n");
		sbufMethods.append("\t").append("\t").append("return ").append(tableInfo[1]).append("Dao.find(").append(sbufPrimary).append(");").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufClass.append(sbufImportUtil).append("\n").append(sbufImportMore);
		sbufClass.append("@Repository(\"").append(tableInfo[1]).append("Service\")").append("\n");
		sbufClass.append("@Scope(\"singleton\")").append("\n");
		sbufClass.append("public class ").append(tableInfo[0]).append("Service extends BaseService implements I").append(tableInfo[0]).append("Service {").append("\n");
		sbufClass.append("\n");
		sbufClass.append(sbufMethods);
		sbufClass.append("}");
		
		new File(path + "/" + tableInfo[0].toLowerCase() + "/service/impl/" + tableInfo[0] + "Service.java").byte2file(sbufClass.toString().getBytes());
	}
	
	private void controller(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufClass = new StringBuffer();
		StringBuffer sbufField = new StringBuffer();
		StringBuffer sbufImportMore = new StringBuffer();
		StringBuffer sbufImportUtil = new StringBuffer();
		StringBuffer sbufMethods = new StringBuffer();
		
		StringBuffer sbufNew = new StringBuffer();
		StringBuffer sbufCast = new StringBuffer();
		
		StringBuffer sbufPrimary = new StringBuffer();
		StringBuffer sbufParameter = new StringBuffer();
		
		StringBuffer sbufRequest = new StringBuffer();
		StringBuffer sbufRequestCast = new StringBuffer();
		StringBuffer sbufParameterCast = new StringBuffer();
		StringBuffer sbufParameterNew = new StringBuffer();
		StringBuffer sbufQuery = new StringBuffer();
		
		sbufClass.append("package ");
		
		if (!"default".equals(pack)) {
			sbufClass.append(pack).append(".");
		}
		
		sbufClass.append(tableInfo[0].toLowerCase()).append(".controller;").append("\n");
		
		sbufImportMore.append("import javax.annotation.Resource;").append("\n");
		sbufImportMore.append("import javax.servlet.http.HttpServletRequest;").append("\n");
		sbufImportMore.append("\n");
		sbufImportMore.append("import org.apache.commons.logging.Log;").append("\n");
		sbufImportMore.append("import org.apache.commons.logging.LogFactory;").append("\n");
		sbufImportMore.append("import org.frame.common.constant.ISymbolConstant;").append("\n");
		sbufImportMore.append("import org.frame.common.lang.StringHelper;").append("\n");
		sbufImportMore.append("import org.frame.repository.sql.model.Page;").append("\n");
		sbufImportMore.append("import org.frame.web.constant.IWebConstant;").append("\n");
		sbufImportMore.append("import org.frame.web.controller.BaseController;").append("\n");
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		sbufImportMore.append("model.").append(tableInfo[0]).append(";").append("\n");
		
		sbufImportMore.append("import ");
		if (!"default".equals(pack)) {
			sbufImportMore.append(pack).append(".");
		}
		sbufImportMore.append(tableInfo[0].toLowerCase()).append(".service.I").append(tableInfo[0]).append("Service;").append("\n");
		sbufImportMore.append("\n");
		sbufImportMore.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
		sbufImportMore.append("import org.springframework.stereotype.Controller;").append("\n");
		sbufImportMore.append("import org.springframework.ui.Model;").append("\n");
		sbufImportMore.append("import org.springframework.web.bind.annotation.RequestMapping;").append("\n");
		sbufImportMore.append("\n");
		
		sbufMethods.append("\t").append("private Log logger = LogFactory.getLog(").append(tableInfo[0]).append("Controller.class);").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@Resource").append("\n");
		sbufMethods.append("\t").append("@Autowired").append("\n");
		sbufMethods.append("\t").append("I").append(tableInfo[0]).append("Service ").append(tableInfo[1]).append("Service;").append("\n");
		sbufMethods.append("\n");
		
		String column, dataType, type, primary = null;
		String[] field = null;
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			dataType = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_TYPE));
			field = this.adjust(column, false);
			type = IDatabase.reference.get(dataType.toLowerCase());
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				if (StringHelper.isNull(primary)) {
					primary = type + " " + field[1];
					sbufPrimary.append(field[1]);
				} else {
					primary += ", " + type + " " + field[1];
					sbufPrimary.append(", ").append(field[1]);
				}
				
				sbufNew.append(type).append(" ").append(field[1]).append(" = null;").append("\n");
				sbufCast.append(field[1]).append(" = ").append(type).append(".valueOf(ids.split(\",\")[").append(i).append("]);");
			}
			
			if ("Date".equals(type)) {
				if (sbufImportUtil.indexOf(importDate) == -1) {
					sbufImportUtil.append(importDate).append("\n");
				}
			}
			
			if (i == data.size() - 1) {
				sbufField.append(type).append(" ").append(field[1]);
				if ("Integer".equals(type) || "Long".equals(type) || "Float".equals(type) || "Double".equals(type)) {
					sbufParameter.append("n").append(field[0]);
				} else {
					sbufParameter.append(field[1]);
				}
			} else {
				sbufField.append(type).append(" ").append(field[1]).append(", ");
				if ("Integer".equals(type) || "Long".equals(type) || "Float".equals(type) || "Double".equals(type)) {
					sbufParameter.append("n").append(field[0]).append(", ");
				} else {
					sbufParameter.append(field[1]).append(", ");
				}
			}
			
			sbufRequest.append("\t").append("\t").append("String ").append(field[1]).append(" = request.getParameter(\"query_").append(field[1]).append("\");").append("\n");
			
			sbufRequestCast.append("\t").append("\t").append("\t").append("if (StringHelper.isNull(").append(field[1]).append(")) {").append("\n");
			sbufRequestCast.append("\t").append("\t").append("\t").append("\t").append(field[1]).append(" = null;").append("\n");
			sbufRequestCast.append("\t").append("\t").append("\t").append("}").append("\n");
			sbufRequestCast.append("\n");
			
			if ("Integer".equals(type) || "Long".equals(type) || "Float".equals(type) || "Double".equals(type)) {
				sbufParameterNew.append("\t").append("\t").append("\t").append(type).append(" n").append(field[0]).append(";").append("\n");
				
				sbufParameterCast.append("\t").append("\t").append("\t").append("try {").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("if (").append(field[1]).append(" == null) {").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("\t").append("n").append(field[0]).append(" = null;").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("} else {").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("\t").append("n").append(field[0]).append(" = ").append(type).append(".valueOf(").append(field[1]).append(");").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("}").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("} catch (NumberFormatException e) {").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("n").append(field[0]).append(" = null;").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("logger.error(\"parameters invalid. ").append(field[1]).append(": \" + ").append(field[1]).append(");").append("").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
				sbufParameterCast.append("\t").append("\t").append("\t").append("}").append("\n");
				sbufParameterCast.append("\n");
			}
			
			sbufQuery.append("\t").append("\t").append("\t").append("model.addAttribute(\"query_").append(field[1]).append("\", ").append(field[1]).append(");").append("\n");
		}
		
		sbufMethods.append("\t").append("@RequestMapping(value = \"/").append(tableInfo[0].toLowerCase()).append("/abandon.jspx\")").append("\n");
		sbufMethods.append("\t").append("public String abandon(HttpServletRequest request, Model model) {").append("\n");
		sbufMethods.append("\t").append("\t").append("logger.info(\"").append(tableInfo[1]).append(" controller abandon access...\");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("String sign = request.getParameter(\"flag\");").append("\n");
		sbufMethods.append("\t").append("\t").append("String ids = request.getParameter(\"ids\");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("Integer flag = Integer.parseInt(sign);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("if (!StringHelper.isNull(ids)) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("model.addAttribute(ISymbolConstant.INFO_CODE, ").append(tableInfo[1]).append("Service.abandon(ids, flag));").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (NumberFormatException e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("logger.error(\"flag invalid: \" + sign);").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (Exception e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return \"list.jspx\";").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@RequestMapping(value = \"/").append(tableInfo[0].toLowerCase()).append("/detail.jspx\")").append("\n");
		sbufMethods.append("\t").append("public String detail(HttpServletRequest request, Model model) {").append("\n");
		sbufMethods.append("\t").append("\t").append("logger.info(\"").append(tableInfo[1]).append(" controller detail access...\");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("String ids = request.getParameter(\"ids\");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append(sbufNew).append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(" = null;").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("if (!StringHelper.isNull(ids)) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append(sbufCast).append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append(tableInfo[1]).append(" = ").append(tableInfo[1]).append("Service.search(").append(sbufPrimary).append(");").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("} else {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append(tableInfo[1]).append(" = new ").append(tableInfo[0]).append("();").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("model.addAttribute(\"data\", ").append(tableInfo[1]).append(");").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (NumberFormatException e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("logger.error(\"detail id invalid: \" + ids);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (Exception e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return \"detail.jsp\";").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@RequestMapping(value = \"/").append(tableInfo[0].toLowerCase()).append("/list.jspx\")").append("\n");
		sbufMethods.append("\t").append("public String list(HttpServletRequest request, Model model) {").append("\n");
		sbufMethods.append("\t").append("\t").append("logger.info(\"").append(tableInfo[1]).append(" controller list access...\");").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append(sbufRequest);
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("Page page = (Page) request.getAttribute(IWebConstant.PAGE_CODE);").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append(sbufParameterNew);
		sbufMethods.append("\n");
		sbufMethods.append(sbufRequestCast);
		sbufMethods.append(sbufParameterCast);
		sbufMethods.append(sbufQuery);
		sbufMethods.append("\t").append("\t").append("\t").append("model.addAttribute(IWebConstant.PAGE_CODE, ").append(tableInfo[1]).append("Service.pagination(").append(sbufParameter).append(", page));").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (Exception e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return \"list.jsp\";").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@RequestMapping(value = \"/").append(tableInfo[0].toLowerCase()).append("/modify.jspx\")").append("\n");
		sbufMethods.append("\t").append("public String modify(HttpServletRequest request, Model model) {").append("\n");
		sbufMethods.append("\t").append("\t").append("logger.info(\"").append(tableInfo[1]).append(" controller modify access...\");").append("\n");
		sbufMethods.append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append(tableInfo[0]).append(" ").append(tableInfo[1]).append(" = (").append(tableInfo[0]).append(") super.request2bean(request, ").append(tableInfo[0]).append(".class);").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("model.addAttribute(ISymbolConstant.INFO_RESULT, ").append(tableInfo[1]).append("Service.modify(").append(tableInfo[1]).append("));").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (Exception e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return \"list.jspx\";").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufMethods.append("\t").append("@RequestMapping(value = \"/").append(tableInfo[0].toLowerCase()).append("/remove.jspx\")").append("\n");
		sbufMethods.append("\t").append("public String remove(HttpServletRequest request, Model model) {").append("\n");
		sbufMethods.append("\t").append("\t").append("logger.info(\"").append(tableInfo[1]).append(" controller remove access...\");").append("\n");
		sbufMethods.append("\t").append("\t").append("try {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("String ids = request.getParameter(\"ids\");").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("if (!StringHelper.isNull(ids)) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("\t").append("model.addAttribute(ISymbolConstant.INFO_RESULT, ").append(tableInfo[1]).append("Service.remove(ids));").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\t").append("\t").append("} catch (Exception e) {").append("\n");
		sbufMethods.append("\t").append("\t").append("\t").append("e.printStackTrace();").append("\n");
		sbufMethods.append("\t").append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		sbufMethods.append("\t").append("\t").append("return \"list.jspx\";").append("\n");
		sbufMethods.append("\t").append("}").append("\n");
		sbufMethods.append("\n");
		
		sbufClass.append(sbufImportUtil).append("\n").append(sbufImportMore);
		sbufClass.append("@Controller").append("\n");
		sbufClass.append("public class ").append(tableInfo[0]).append("Controller extends BaseController {").append("\n");
		sbufClass.append("\n");
		sbufClass.append(sbufMethods);
		sbufClass.append("}");
		
		new File(path + "/" + tableInfo[0].toLowerCase() + "/controller/" + tableInfo[0] + "Controller.java").byte2file(sbufClass.toString().getBytes());
	}
	
	private String[] adjust(String value, boolean removePrefix) {
		String[] result = new String[2];
		
		String word, temp = "";
		String[] words = value.split("_");
		for (int i = 0; i < words.length; i++) {
			if (removePrefix && i == 0) continue;
			word = words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length());
			temp += word;
		}
		
		result[0] = temp;
		
		temp = temp.substring(0, 1).toLowerCase() + temp.substring(1, temp.length());
		result[1] = temp;
		
		return result;
	}
	
	/*public static void main(String[] args) {
		Java java = new Java(null, null, null, true, false);
		String[] adjusted = java.adjust("sys_user_department", true);
		System.out.println(adjusted[0]);
		System.out.println(adjusted[1]);
	}*/
	
}
