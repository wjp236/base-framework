package com.platform.base.frramework.trunk.util.socket;

import java.io.*;
import java.net.Socket;

/**
 * Socket客户端
 * 
 * @author BaiLin
 *         
 */
public class SocketClient
{
    private String Address;
    private int Port;
    private Boolean readline;
    
    public SocketClient(String address, int port, Boolean readline)
    {
        this.Address = address;
        this.Port = port;
        this.readline = readline;
    }
    
    public SocketClient(String address, int port)
    {
        this.Address = address;
        this.Port = port;
        this.readline = true;
    }
    
    /**
     * socket通信开始
     * 
     * @param protocol
     * @return 返回报文
     * @throws Exception
     */
    public String SendData(String protocol) throws Exception
    {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try
        {
            StringBuilder buf = new StringBuilder();
            socket = new Socket(this.Address, this.Port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK")), true);
            pw.write(protocol);
            pw.flush();
            socket.shutdownOutput();
            String strLine;
            while((strLine = br.readLine()) != null)
            {
                buf.append(strLine);
                if(!readline)
                    break;
            }
            return buf.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if(socket != null)
            {
                try
                {
                    if(br != null)
                        br.close();
                    if(pw != null)
                        pw.close();
                    socket.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }
    
}
