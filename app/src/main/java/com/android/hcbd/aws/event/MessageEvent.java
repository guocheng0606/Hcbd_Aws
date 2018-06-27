package com.android.hcbd.aws.event;

/**
 * Created by guocheng on 2017/6/19.
 */
public class MessageEvent {

    /*发出的广播类型*/
    public static final int EVENT_LOGINOUT = 100;
    public static final int EVENT_EDIT_PREFERENCES_SUCCESS = 101;
    public static final int EVENT_INSPECTION_HISTORY_SEARCH = 102;
    public static final int EVENT_PRECHECK_HISTORY_SEARCH = 103;

    public static final int EVENT_GETPREDATA_THREAD = 104;
    public static final int EVENT_GETCARDATA_THREAD = 105;
    public static final int EVENT_DATA_THREAD = 106;

    public static final int EVENT_LEDSHOW_SEARCH = 107;
    public static final int EVENT_CARTYPE_SEARCH = 108;
    public static final int EVENT_CARTYPE_EDIT_SUCCESS = 109;

    public static final int EVENT_IPADDRESS_DEL = 120;

    private int eventId;
    private Object obj;
    private Object obj2;
    public MessageEvent() {
    }

    public MessageEvent(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj2() {
        return obj2;
    }

    public void setObj2(Object obj2) {
        this.obj2 = obj2;
    }
}
