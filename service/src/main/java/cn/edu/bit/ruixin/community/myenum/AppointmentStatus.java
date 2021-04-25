package cn.edu.bit.ruixin.community.myenum;

import lombok.Data;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/25
 */
public enum AppointmentStatus {

    NEW("new"),
    RECEIVE("receive"),
    REJECT("reject"),
    CANCEL("cancel"),
    SIGNED("signed"),
    ILLEGAL("illegal"),
    MISSED("missed"),
    FINISH("finished");

    private String status;

    AppointmentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
