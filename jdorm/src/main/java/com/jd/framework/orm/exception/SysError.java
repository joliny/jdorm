package com.jd.framework.orm.exception;

public enum SysError implements ErrorCode {
    //@formatter:off
    
    NULL_PARAMETER,
    EMPTY_PARAMETER,
    INVALID_PARAMETER,
    
    CLASS_NOT_FOUND,
    CLASS_CAST_ERROR,
    
    ENCODING_ERROR,
    
    VALIDATION_ERROR,
    ASSERT_ERROR,
    
    //@formatter:on
}
