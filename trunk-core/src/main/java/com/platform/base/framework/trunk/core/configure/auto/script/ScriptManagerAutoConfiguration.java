package com.platform.base.framework.trunk.core.configure.auto.script;

import com.platform.base.framework.trunk.core.context.ScriptManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScriptManagerAutoConfiguration {

    @Bean
    public ScriptManager loadScriptManager() {
        ScriptManager scriptManager = ScriptManager.getScriptManager();
        scriptManager.setLocalDirPath("classpath:/local");
        scriptManager.setScriptDirPath("classpath:/script");
        return scriptManager;
    }

}
