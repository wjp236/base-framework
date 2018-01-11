package com.platform.base.framework.trunk.core.alarm;

import com.platform.base.frramework.trunk.util.ip.InetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mylover on 21/11/2016.
 */
public class AlarmMessage extends AbstractMessage  {

    protected static final Logger logger = LoggerFactory.getLogger(AlarmMessage.class);

    public static final String PlaceHolder = "$";
    private int msgType;
    private int alarmType;
    private String alarmId;
    private long time;
    private String localIP;
    private AlarmLevel alarmLevel;
    private String[] keywords;
    private String customMsg;
    private String alarmCenterAddress;
    private String sessionId;

    public AlarmMessage(String alarmId, AlarmLevel alarmLevel, String sessionId, String custMsg, String... keywords) {
        this.alarmId = alarmId;
        this.alarmLevel = alarmLevel;
        this.keywords = keywords;
        this.msgType = MsgType.AlarmMsg.getValue();
        this.customMsg = custMsg;
        this.alarmType = AlarmType.BuildAlarm.getValue();
        this.localIP = InetUtils.getLocalIpByNetCard();
        this.sessionId = sessionId;
    }

    public String createAlarmMessage(String moduleName, String moduleID) {
        StringBuffer sb = new StringBuffer("错误异常:");
        sb.append("requestId:").append(this.sessionId);
        sb.append(",msgType:").append(this.msgType);
        sb.append(",alarmId:").append(this.alarmId);
        sb.append(",alarmType:").append(this.alarmType);
        sb.append(",moduleID:").append(moduleID);
        sb.append(",moduleName:").append(moduleName);
        sb.append(",ip:").append(this.localIP);
        sb.append(",alarmLevel:").append(this.alarmLevel.getValue());
        sb.append(",customMsg:").append(customMsg);
        sb.append(",context:");
        if(this.keywords != null) {
            int len = this.keywords.length;
            for(int i = 0; i < len; ++i) {
                sb.append(this.keywords[i]);
            }
        }
        return sb.toString();
    }

    public String cancelAlarmMessage() {
        return null;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public AlarmLevel getAlarmLevel() {
        return this.alarmLevel;
    }

    public void setAlarmLevel(AlarmLevel alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getKeywords() {
        if(this.keywords != null) {
            StringBuffer sb = new StringBuffer();
            int len = this.keywords.length;

            for(int i = 0; i < len; ++i) {
                sb.append(this.keywords[i]);
                if(i < len - 1) {
                    sb.append("&");
                }
            }

            return sb.toString();
        } else {
            return null;
        }
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getAlarmCenterAddress() {
        return this.alarmCenterAddress;
    }

    public void setAlarmCenterAddress(String alarmCenterAddress) {
        this.alarmCenterAddress = alarmCenterAddress;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
