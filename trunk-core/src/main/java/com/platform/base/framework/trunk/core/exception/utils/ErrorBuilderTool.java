package com.platform.base.framework.trunk.core.exception.utils;


import com.platform.base.framework.trunk.core.alarm.AbstractMessage;
import com.platform.base.framework.trunk.core.context.ScriptManager;
import com.platform.base.framework.trunk.core.exception.AbstractException;

/**
 * Created by mylover on 18/11/2016.
 */
public final class ErrorBuilderTool {

    /**
     * script instance
     */
    private static ScriptManager instance = ScriptManager.getScriptManager();

    public static ScriptManager getScriptManager() {
        if (instance == null) {
            throw new RuntimeException(buildError("000002"));
        }
        return instance;
    }

    public static String buildError(String errorCode) {
        return buildError(errorCode, "");
    }

    public static String buildError(String errorCode, String errorMsg, String custom) {
        return buildError(errorCode, errorMsg, null, custom);
    }

    public static String buildError(String errorCode, String custom) {
        return buildError(errorCode, null, null, custom);
    }

    public static String buildError(String errorCode, AbstractMessage.AlarmLevel level) {
        return buildError(errorCode, null, level, null);
    }

    public static String buildError(String errorCode, AbstractMessage.AlarmLevel level, String custom) {
        return buildError(errorCode, null, level, custom);
    }

    /**
     * 构建错误消息和告警消息
     *
     * @param errorCode
     *            已定义的错误码
     * @param errorMsg
     *            已定义的错误信息
     * @param level
     *            告警级别 {@link AbstractMessage.AlarmLevel}
     * @param custom
     *            告警显示的消息 (该参数为空不作告警)
     * @return
     */
    public static String buildError(
            String errorCode, String errorMsg,
            AbstractMessage.AlarmLevel level, String custom) {

        StringBuffer sb = new StringBuffer(AbstractException.Error_Code + "$" + errorCode);
        sb.append("|" + AbstractException.Error_Msg).append("$");

        if (errorCode.startsWith("99")) {
            level = AbstractMessage.AlarmLevel.CRITICAL;
        }

        if (errorMsg != null) {
            sb.append(errorMsg);
        } else {
            sb.append(instance.getCodeCache().get(errorCode));
        }
        if (level != null) {
            sb.append("|").append(AbstractException.Alarm_Level).append("$").append(level.name());
        }
        if (custom != null && custom.length() != 0) {
            sb.append("|").append(AbstractException.Custom_Msg).append("$").append(custom);
        }
        return sb.toString();
    }


}
