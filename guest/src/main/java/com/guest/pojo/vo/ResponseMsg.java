package com.guest.pojo.vo;

public enum ResponseMsg {
    /**
     * 枚举对象代表的信息如对象的msg所示
     */
    SUCCESS(200,"请求成功"),
    FAIL(40000,"请求失败"),


    CLIENT_ERROR(40001,"请求参数错误"),
    NO_TARGET(40002,"数据不存在"),
    ILLEGAL_PICTURE(40003,"图片非法"),
    TIME_ILLEGAL(40004,"日期非法"),
    NO_SUCH_USER(40005,"该用户不存在"),
    ILLEGAL_UPDATE(40006,"更新操作必须提供id"),
    DATA_DUPLICATION(40009,"数据重复"),
    TEST(400010,"测试异常处理"),
    AUTHENTICATE_FAILED(40101,"认证失败"),
    LOGIN_FAILED(40102,"登录失败"),
    PLEASE_LOGIN(40103,"当前未登录,请登录"),
    ILLEGAL_OPERATION(40104,"非法操作, 试图操作不属于自己的数据"),
    PASSWORD_WRONG(40105,"密码错误,请核对后重新输入"),

    SERVER_FAIL(500,"服务器端错误"),
    NO_BUCKET(50001,"存储桶不存在"),
    UPLOAD_FAILED(50002,"上传文件到存储桶失败"),
    BUCKET_KEY_ERROR(50003,"对象存储key错误"),
    SCRIPT_ERROR(50004,"脚本错误"),
    DOWNLOAD_ERROR(50005,"下载文件失败"),
    ;

    public int code;
    public String msg;
    ResponseMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
