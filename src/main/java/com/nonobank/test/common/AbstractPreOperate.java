package com.nonobank.test.common;

import com.nonobank.test.DBResource.entity.BaseInfo;
import com.nonobank.test.utils.ArgsValid;

/**
 * Created by H.W. on 2018/5/31.
 */
public abstract class AbstractPreOperate {
    public void valid(BaseInfo info) {
        ArgsValid.notNull(info, "请求");
        ArgsValid.notEmpty(info.getName(), "节点名");
    }
}
