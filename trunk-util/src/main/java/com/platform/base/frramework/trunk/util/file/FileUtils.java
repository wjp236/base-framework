/**
 * 
 */
package com.platform.base.frramework.trunk.util.file;

import java.io.*;

/**
 * 
 * @author wangjp
 *
 */
public class FileUtils {

    public static void writeFile(InputStream is, String path) throws Exception {
        FileOutputStream fos = new FileOutputStream(new File(path));
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        is.close();
        fos.close();
    }

    /**
     * getFileContent
     *
     * @param  in 文件输入流
     * @return String    返回类型
     */
    public static String getFileContent(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("输入流为空");
        }
    }

    /**
     * getFileContent 从文件中获取内容
     * @param file
     * @return String    返回类型
     */
    public static String getFileContent(File file) throws Exception{
        InputStream in = new FileInputStream(file);
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("输入流为空");
        }finally{
            in.close();
        }
    }
}
