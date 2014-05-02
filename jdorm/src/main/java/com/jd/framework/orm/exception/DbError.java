package com.jd.framework.orm.exception;

public enum DbError implements ErrorCode {
    //@formatter:off
    
    CONNECTON_ERROR, 
    IO_ERROR, 
    TIMEOUT,
    TRANSACTION_ERROR,

    DATABASE_NOT_SUPPORT,
    INVALID_SQL,
    
    TABLE_NOT_FOUND, 
    TABLE_EXIST, 
    COLUMN_NOT_FOUND,
    COLUMN_EXIST,
    
    DUPLICATE_KEY,
    RECORD_NOT_FOUND,
    
    //@formatter:on
}
