package com.nonobank.test.commons;

/**
 * Created by H.W. on 2018/4/10.
 */
public enum  OperaEnum {
    _AUTO("_AUTO", 1),_NULL("_NULL", -1);

    private String rule;
    private int id;

    private OperaEnum(String rule, int id) {
        this.rule = rule;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getRule() {
        return this.rule;
    }

    public static OperaEnum getOperaEnum(String str) {
        for (OperaEnum e : OperaEnum.values()) {
            if (e.getRule().equalsIgnoreCase(str)) {
                return e;
            }
        }
        return _NULL;
    }
}
