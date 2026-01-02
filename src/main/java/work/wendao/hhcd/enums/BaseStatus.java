package work.wendao.hhcd.enums;

import work.wendao.hhcd.common.BaseEnum;

public enum BaseStatus implements BaseEnum {
    NODELETE(0, "未删除"),
    DELETED(1, "已删除");

    private final int code;
    private final String desc;

    BaseStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }
}
