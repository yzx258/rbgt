package com.basketball.rbgt.enums;

public enum ErrorCodeAndMsg {

    Student_number_does_not_exist("0001","学号不存在"),
    Insufficient_student_number("0002","学号长度不足"),
    Student_number_is_empty("0003","学号为空"),
    Network_error("9999","网络错误，待会重试"),
    NOT_TOKENT("100001","无效的token"),
    ;

    private String code;
    private String msg;

    ErrorCodeAndMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
