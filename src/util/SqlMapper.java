package com.cbscap.util;

public class SqlMapper {

	private String colName;
	private boolean colType;
	
	public SqlMapper(String colName){
		this.colName = colName;
		this.colType = true;
	}
	
	public SqlMapper(String colName, boolean colType){
		this.colName = colName;
		this.colType = colType;
	}
	
	public String getColName() { return colName; }
	public boolean isString() { return colType; }

	public void setColName(String colName) {this.colName = colName;}	
	public void setIsString(boolean colType) {this.colType = colType;}
	
}
