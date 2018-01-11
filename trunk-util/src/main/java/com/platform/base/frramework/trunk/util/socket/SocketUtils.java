package com.platform.base.frramework.trunk.util.socket;

import com.platform.base.frramework.trunk.util.constants.NetConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author QIANG
 *
 */
public class SocketUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SocketUtils.class);


    public static byte[] readMsg(InputStream is, int readLen) throws Exception {
        return readByte(is, readLen);
    }


    public static byte[] readByte(InputStream is, int readLen) throws Exception {
        int count = readLen;
        byte[] bytesAll = new byte[count];
        int tempLen = 0;
        for (;;) {
            int cha = count - tempLen;
            if (cha <= 0) {
                break;
            }
            byte[] bytes = new byte[cha];
            int len = is.read(bytes);
            if (len > 0) {
                System.arraycopy(bytes, 0, bytesAll, tempLen, len);
                tempLen += len;
                continue;
            } else {
                throw new RuntimeException(NetConstants.INPUT_STREAM_NOT_NORMAL);
                
            }
        }
        return bytesAll;
    }


    /**
     * 关闭输入流
     * 
     * @param is
     */
    public static void closeInput(InputStream is) {
        if (null != is) {
            try {
                is.close();
                is = null;
            } catch (IOException e1) {
                LOG.error(NetConstants.CLOSE_IN_FAIL, e1);
            }
        }
    }


    /**
     * 关闭输出流
     * 
     * @param os
     */
    public static void closeOutput(OutputStream os) {
        if (null != os) {
            try {
                os.close();
                os = null;
            } catch (IOException e1) {
                LOG.error(NetConstants.CLOSE_OUT_FAIL, e1);
            }
        }
    }


    /**
     * 关闭socket
     */
    public static void closeSocket(Socket socket) {
        OutputStream os = null;
        InputStream is = null;
        try {
            if (null != socket && !socket.isClosed()) {
                os = socket.getOutputStream();
                is = socket.getInputStream();
            }
        } catch (IOException e) {
            LOG.error(NetConstants.GET_INPUT_OUTPUT_STREAM_FAIL, e);
        } finally {
            closeInput(is);
            closeOutput(os);
            if (null != socket && !socket.isClosed()) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    LOG.error(NetConstants.CLOSE_SOCKET_FAIL, e);
                }
            }
        }
    }


    /**
     * 关闭ServerSocket
     */
    public static void closeServerSocket(ServerSocket serverSocket) {
        if (null != serverSocket && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                LOG.error(NetConstants.CLOSE_SERVER_SOCKET_FAIL, e);
            }
        }
    }

}
