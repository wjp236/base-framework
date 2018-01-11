package com.platform.base.framework.trunk.core.exception;


import com.platform.base.framework.trunk.core.alarm.AbstractMessage;
import com.platform.base.frramework.trunk.util.text.TextUtil;
import com.platform.base.frramework.trunk.util.constants.TrunkConstans;
import org.slf4j.MDC;

/**
 * Created by mylover on 3/29/16.
 */
public abstract class AbstractException extends Exception {

    public static final int LOGIC_ERROR = 0;
    public static final int LOWER_ERROR = 1;
    public static final String Error_Code = "Error_Code";
    public static final String Error_Msg = "Error_Msg";
    public static final String Alarm_Level = "Alarm_Level";
    public static final String Custom_Msg = "Custom_Msg";
    private String errorRequestId = MDC.get(TrunkConstans.REQUESTID);
    private String errorCode;
    private String errorMsg;
    private String customMsg = "@@@@";
    private AbstractMessage.AlarmLevel level;
    private int errorType;

    public AbstractException(String message) {
        super(message);
        this.level = AbstractMessage.AlarmLevel.NORMAL;
        this.errorType = 0;
        this.errorType = 0;
        this.handleExceptionMessage(message);
    }

    public final void handleExceptionMessage(String message) {
        String[] result = TextUtil.split(message, "|");
        int len = result.length;

        for(int i = 0; i < len; ++i) {
            String str = result[i];
            if(str != null && str.length() != 0) {
                String[] array = TextUtil.split(str, "$");
                if(array != null && array.length != 0) {
                    String value = array.length >= 2?array[1]:null;
                    if("Error_Code".equals(array[0])) {
                        this.errorCode = value;
                    } else if("Error_Msg".equals(array[0])) {
                        this.errorMsg = value;
                    } else if("Alarm_Level".equals(array[0])) {
                        this.level = value == null? AbstractMessage.AlarmLevel.NORMAL: AbstractMessage.AlarmLevel.valueOf(value);
                    } else if("Custom_Msg".equals(array[0])) {
                        this.customMsg = value;
                    }
                }
            }
        }
    }

    public AbstractException(Throwable cause) {
        super(cause);
        this.level = AbstractMessage.AlarmLevel.NORMAL;
        this.errorType = 0;
        this.errorType = 1;
        AbstractException.ErrorDesc errorDesc = this.getErrorDesc(cause);
        if(errorDesc != null) {
            this.errorCode = errorDesc.errorCode;
            this.errorMsg = errorDesc.errorMsg;
            this.customMsg = errorDesc.customMsg;
            this.level = errorDesc.level;
        }

    }

    public abstract AbstractException.ErrorDesc getErrorDesc(Throwable var1);

    public String buildError() {
        StringBuffer sb = new StringBuffer("Error_Code$" + this.errorCode);
        sb.append("|Error_Msg").append("$");
        if(this.errorMsg != null) {
            sb.append(this.errorMsg);
        } else {
            sb.append("");
        }
        if(this.level != null) {
            sb.append("|").append("Alarm_Level").append("$").append(this.level.name());
        }

        if(this.customMsg != null && this.customMsg.length() != 0) {
            sb.append("|").append("Custom_Msg").append("$").append(this.customMsg);
        }

        return sb.toString();
    }

    public boolean isAlarm() {
        return this.customMsg != null && !this.customMsg.equals("@@@@");
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public String getCustomMsg() {
        return this.customMsg;
    }

    public int getErrorType() {
        return this.errorType;
    }

    public AbstractMessage.AlarmLevel getLevel() {
        return this.level;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setCustomMsg(String customMsg) {
        this.customMsg = customMsg;
    }

    public void setLevel(AbstractMessage.AlarmLevel level) {
        this.level = level;
    }
    public String getErrorRequestId() {
        return errorRequestId;
    }

    public void setErrorRequestId(String errorRequestId) {
        this.errorRequestId = errorRequestId;
    }

    public static class ErrorDesc {
        public String errorCode;
        public String errorMsg;
        public String customMsg = "@@@@";
        public AbstractMessage.AlarmLevel level;

        public ErrorDesc(String errorCode, String errorMsg, String customMsg, AbstractMessage.AlarmLevel level) {
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
            this.customMsg = customMsg;
            this.level = level;
        }
    }

}
