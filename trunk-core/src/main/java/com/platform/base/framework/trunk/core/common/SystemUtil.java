package com.platform.base.framework.trunk.core.common;

import com.platform.base.frramework.trunk.util.constants.TrunkConstans;
import com.platform.base.frramework.trunk.util.text.UUIDUtils;
import org.slf4j.MDC;

/**
 * Created by mylover on 5/16/16.
 */
public class SystemUtil {

    /**
     * 获取系统流水号
     * @return
     */
    public static String getSystemFlowId() {
        String systemFlowId = MDC.get("requestId");
        if (systemFlowId == null || "".equals(systemFlowId)) {
            systemFlowId = UUIDUtils.randomUUID().toString();
            MDC.put(TrunkConstans.REQUESTID, systemFlowId);
        }
        return systemFlowId;
    }

}
