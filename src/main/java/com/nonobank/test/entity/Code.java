package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/11.
 */
public interface Code {
    public interface Res{
        public static final String SUCCESS 					= "0000000";//操作成功
        public static final String EMPTY_PARAMETER			= "0000001";//参数为空
        public static final String NOT_ENOUGTH_PARAMETER	= "0000002";//参数个数不足
        public static final String DATA_EXISTED				= "0000003";//数据已存在
        public static final String DATA_NOT_EXISTED			= "0000004";//数据不存在
        public static final String DATABASE_EXCEPTION		= "0000005";//数据库异常
        public static final String PARAMETER_TYPE_ERROR		= "0000006";//参数类型错误
        public static final String PARAMETER_VALID_ERROR	= "0000007";//参数未通过验证
        public static final String TIMEOUT 					= "0000008";//请求超时
        public static final String CONNECT_REFUSE 			= "0000009";//拒绝连接错误
        public static final String VALID_ERROR	 			= "0000010";//参数验证错误
        public static final String UNKNOWN_ERROR			= "9999999";//未知错误
    }
}
