package com.platform.base.framework.trunk.core.alarm;


import com.platform.base.framework.trunk.core.exception.AbstractException;

/**
 * Created by mylover on 21/11/2016.
 */
public interface AlarmCenterService {

    default AlarmMessage getAlarmMessage(AbstractException ese) {
        return new AlarmMessage(ese.getErrorCode(), ese.getLevel(),
                ese.getErrorRequestId(), ese.getCustomMsg(), ese.getErrorMsg());
    }

    void sendAlarmMessage(final AlarmMessage var1);

}
