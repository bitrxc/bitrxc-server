package cn.edu.bit.secondclass.myenum;

import com.sun.org.apache.bcel.internal.generic.NEW;

public enum PointExchangedRecordStatus {
    NEW("new"),
    CANCEL("cancel"),
    REJECT("reject"),
    FINISH("finish");


    private String status;

    PointExchangedRecordStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
