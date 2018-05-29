package com.nonobank.test.utils;

import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.ValidException;

import javax.xml.bind.ValidationException;
import java.util.Collection;

/**
 * Created by H.W. on 2018/5/29.
 */
public class ArgsValid {
    public static <T> T notNull(T argument, String field) {
        if (argument == null) {
            throw new ValidException(field + "不能为空");
        } else {
            return argument;
        }
    }

    public static <T extends CharSequence> T notEmpty(T arg, String filed) {
        if (StringUtils.isEmpty(arg)) {
            throw new ValidException(filed + "不能为空");
        } else {
            return arg;
        }
    }

    public static <E, T extends Collection<E>> T notEmpty(T arg, String filed) {
        if (arg == null || arg.isEmpty()) {
            throw new ValidException(filed + "不能为空");
        } else {
            return arg;
        }
    }
}
