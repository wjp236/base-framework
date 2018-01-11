/**
 * 
 */
package com.platform.base.framework.trunk.model.common;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mylover
 */
@XmlRootElement(name = "Response")
public class Response {

    private String ret_code;
    private String ret_msg;

    public Response() {
    }

    public Response(String statuscode, String statusmsg) {
        super();
        this.ret_code = statuscode;
        this.ret_msg = statusmsg;
    }

    public boolean isSuccess() {
        return "000000".equals(this.ret_code);
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
}
