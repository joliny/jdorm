package com.jd.framework.orm.dialect.impl;


import org.apache.commons.lang3.StringUtils;

import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.dialect.SqlType;
import com.jd.framework.orm.dialect.SubStyleType;


public class H2Dialect extends Dialect {
    public static final String NAME = "H2";

    @Override
    protected String getQuotedIdentifier(String name) {
        return "\"" + StringUtils.replace(name, "\"", "\"\"") + "\"";
    }

    @Override
    public String sql_table_drop(String table) {
        return String.format("drop table if exists %s;", getIdentifier(table));
    }

    @Override
    public String sql_table_rename(String oldName, String newName) {
        return String.format("alter table  %s rename to %s;", getIdentifier(oldName), getIdentifier(newName));
    }

    @Override
    public String sql_column_add(String table, String column_definition, String column_position) {
        String sql = String.format("alter table %s add column %s", getIdentifier(table), column_definition);
        if (supportsColumnPosition() && column_position != null) {
            sql = sql + " " + column_position;
        }
        return sql;
    }

    @Override
    public String sql_column_modify(String table, String column_definition, String column_position) {
        String sql = String.format("alter table %s alter column %s", getIdentifier(table), column_definition);
        if (supportsColumnPosition() && column_position != null) {
            sql = sql + " " + column_position;
        }
        return sql;
    }

    @Override
    public String sql_column_drop(String table, String column) {
        return String.format("alter table %s drop column %s;", getIdentifier(table), getIdentifier(column));
    }

    @Override
    public String sql_pagelist(String sql, int pageIndex, int pageSize) {
        sql = sql + " limit " + pageSize;
        if (pageIndex > 0) {
            sql = sql + " offset " + pageIndex*pageSize;
        }
        return sql;
    }

    @Override
    public boolean supportsColumnPosition() {
        return true;
    }

    @Override
    public boolean supportsSequences() {
        return true;
    }

    @Override
    public String getHibernateDialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    @Override
    public String asSqlType(String type, Integer length, Integer scale) {
        if (SubStyleType.TEXT.equals(type)) {
            return SqlType.newInstance("varchar", Integer.MAX_VALUE, null).toString();
        } else if (SubStyleType.BOOLEAN.equals(type)) {
            return "boolean";
        } else if (SubStyleType.INPUTSTREAM.equals(type)) {
            return "binary";
        }
        return super.asSqlType(type, length, scale);
    }

    @Override
    protected void initializeReservedWords() {
        reservedWords.add("CROSS");
        reservedWords.add("CURRENT_DATE");
        reservedWords.add("CURRENT_TIME");
        reservedWords.add("CURRENT_TIMESTAMP");
        reservedWords.add("DISTINCT");
        reservedWords.add("EXCEPT");
        reservedWords.add("EXISTS");
        reservedWords.add("FALSE");
        reservedWords.add("FOR");
        reservedWords.add("FROM");
        reservedWords.add("FULL");
        reservedWords.add("GROUP");
        reservedWords.add("HAVING");
        reservedWords.add("INNER");
        reservedWords.add("INTERSECT");
        reservedWords.add("IS");
        reservedWords.add("JOIN");
        reservedWords.add("LIKE");
        reservedWords.add("LIMIT");
        reservedWords.add("MINUS");
        reservedWords.add("NATURAL");
        reservedWords.add("NOT");
        reservedWords.add("NULL");
        reservedWords.add("ON");
        reservedWords.add("ORDER");
        reservedWords.add("PRIMARY");
        reservedWords.add("ROWNUM");
        reservedWords.add("SELECT");
        reservedWords.add("SYSDATE");
        reservedWords.add("SYSTIME");
        reservedWords.add("SYSTIMESTAMP");
        reservedWords.add("TODAY");
        reservedWords.add("TRUE");
        reservedWords.add("UNION");
        reservedWords.add("UNIQUE");
        reservedWords.add("WHERE");
    }

}
