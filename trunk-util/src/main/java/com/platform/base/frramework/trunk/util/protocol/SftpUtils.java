package com.platform.base.frramework.trunk.util.protocol;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.platform.base.frramework.trunk.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

public class SftpUtils {
	private final Logger logger = LoggerFactory.getLogger(SftpUtils.class);
	/**IP**/
	private  String host;
	/**端口**/
	private  int port;
	/**用户名**/
	private  String user;
	/**密码**/
	private  String password;
	/**文件所在目录**/
	private  String path;
	/**文件本地缓存目录**/
	private String tmpPath;
	
	/**
	 * 
	 * @param host system
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 * @param path 文件所在目录
	 * @param temPath 本地缓存文件目录
	 */
	public SftpUtils(String host, int port, String user, String password,
                     String path, String temPath) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.path = path;
		this.tmpPath = temPath;
	}
	
	/**
	 *要获取的文件名
	 * @author zhangwei<zhangwei@enn.com>
	 * @param fileName 文件名称
	 * @param extraPath 额外的目录，如果没有传null或者""
	 * @return
	 */
	public File get(String fileName, String extraPath) {
		File splitFile = null;
		ChannelSftp sftp = null;
		Session session = null;
		try {
			JSch jSch = new JSch();
			session = jSch.getSession(user, host, port);
			if(session == null) {
				throw new Exception("session is null!");
			}
			logger.info("=====" + fileName +"=====sftp session created.");
			session.setPassword(password);
			Hashtable<String, String> config = new Hashtable<String, String>();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(30000);
			session.connect();
			logger.info("=====" + fileName +"=====sftp session connected.");
			logger.info("=====" + fileName +"=====open sftp channel. ");
			//创建sftp通信通道
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			logger.info("=====" + fileName +"=====sftp channel connected. ");
			//进入指定文件夹
			String sftpPath = path;
			if(!StringUtils.isBlank(extraPath)) {
				sftpPath = sftpPath + extraPath;
			}
			sftp.cd(sftpPath);
			Vector v = sftp.ls(fileName);
			logger.info("=====" + fileName +"=====sftp file size = " + v.size());
			if(v.size() > 0) {
				logger.info("=====tmpPath=" + tmpPath + "======");
				String filePath = tmpPath + "/" + fileName;
				File tmp = new File(tmpPath);
				if(!tmp.exists()) {
					tmp.mkdirs();
				}
				File tmpFile = new File(filePath);
				InputStream inputStream = sftp.get(fileName);
				OutputStream outputStream = new FileOutputStream(tmpFile);
				byte[] b = new byte[4096];
				int n;
				while((n =inputStream.read(b)) != -1) {
					outputStream.write(b, 0, n);
				}
				outputStream.flush();
				outputStream.close();
				inputStream.close();
				splitFile = new File(filePath);
			}
		} catch (Exception e) {
			logger.error(String.format("sftpUtils getFile error:%s, host:%s, port:%s, user:%s, fileName:%s,path:%s",  e, host, port, user, fileName,path+extraPath));
		} finally {
			if(sftp != null) {
				sftp.disconnect();
			}
			if(session != null && session.isConnected()) {
				session.disconnect();
			}
		}
		return splitFile;
	}
	
	/**
	 * 判断远程sftp服务器上是否包含该文件
	 * @author zhangwei<zhangwei@enn.com>
	 * @param fileName
	 * @return
	 */
	public boolean Contains(String fileName, String extraPath) {
		boolean result = false;
		ChannelSftp sftp = null;
		Session session = null;
		try {
			JSch jSch = new JSch();
			session = jSch.getSession(user, host, port);
			if(session == null) {
				throw new Exception("session is null!");
			}
			logger.info("sftp session created.");
			session.setPassword(password);
			Hashtable<String, String> config = new Hashtable<String, String>();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(30000);
			session.connect();
			logger.info("sftp session connected.");
			logger.info("open sftp channel. ");
			//创建sftp通信通道
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			logger.info("sftp channel connected. ");
			//进入指定文件夹
			String sftpPath = path;
			if(!StringUtils.isBlank(extraPath)) {
				sftpPath = sftpPath + extraPath;
			}
			sftp.cd(sftpPath);
			Vector v = sftp.ls(fileName);
			logger.info("sftp file size = " + v.size());
			if(v.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			logger.error(String.format("sftpUtils getFile error:%s, host:%s, port:%s, user:%s, fileName:%s",  e.getMessage(), host, port, user, fileName));
		} finally {
			if(sftp != null) {
				sftp.disconnect();
			}
			if(session != null && session.isConnected()) {
				session.disconnect();
			}
		}
		return result;
	}
	
	/**
	 * 操作完成后删除本地缓存文件
	 * @author zhangwei<zhangwei@enn.com>
	 * @param fileName
	 */
	public void delete(String fileName) {
		String filePath = tmpPath + "/" + fileName;
		File tmpFile = new File(filePath);
		if(tmpFile.exists()) {
			tmpFile.delete();
		}
		logger.info("fileName deleted.");
	}

	/**
	 * 获取从sftp缓存到本地的文件
	 * @author zhangwei<zhangwei@enn.com>
	 * @return
	 */
	public File getLocalFile(String fileName) {
		File file = null;
		String filePath = tmpPath + "/" + fileName;
		File tmpFile = new File(filePath);
		if(tmpFile.exists()) {
			file = tmpFile;
		}
		return file;
	}
	/**
	 * 
	 * @Title: upload 
	 * @Description: 上传文件到sftp服务器
	 * @author chenming<chenmingf@ennew.cn>
	 * @param isMkdir 是否需要创建新目录（新目录包含到path中，以“/”结尾）
	 * @param uploadFile  要上传的文件名称（加后缀）
	 * @return void    返回类型
	 */
	public void upload(boolean isMkdir,String uploadFile){
		ChannelSftp sftp = null;
		Session session = null;
		FileInputStream fileInputStream = null;
        try {
        	JSch jSch = new JSch();
			session = jSch.getSession(user, host, port);
			if(session == null) {
				throw new Exception("session is null!");
			}
			logger.info("------【"+uploadFile+"】sftp session created.");
			session.setPassword(password);
			Hashtable<String, String> config = new Hashtable<String, String>();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(30000);
			session.connect();
			logger.info("------【"+uploadFile+"】sftp session connected.");
			logger.info("------【"+uploadFile+"】open sftp channel. ");
			//创建sftp通信通道
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			logger.info("------【"+uploadFile+"】sftp channel connected. ");
			if(isMkdir){
				//path：sftp服务器目录(含需要创建的目录)
				mkdir(path, sftp);
			}
			logger.info("------【"+uploadFile+"】start uploading files. ");
			File file = new File(tmpPath+uploadFile);
			fileInputStream = new FileInputStream(file);
            sftp.put(fileInputStream, file.getName());
            fileInputStream.close();
            logger.info("------【"+uploadFile+"】file upload successfully. ");
        } catch (Exception e) {
			logger.error(String.format("File upload error:%s, host:%s, port:%s, user:%s, uploadFile:%s",  e, host, port, user,uploadFile));
		} finally {
			if(sftp != null) {
				sftp.disconnect();
			}
			if(session != null && session.isConnected()) {
				session.disconnect();
			}
		}
	}
	/**
     * 
     * @Title: mkdir 
     * @Description: 创建目录
     * @author chenming<chenmingf@ennew.cn>
     * @param dirName
     * @param sftp
     * @throws Exception    设定文件 
     * @return void    返回类型
     */
    public static void mkdir(String dirName,ChannelSftp sftp) throws Exception{
    	if (isDirExist(dirName,sftp)) {
            sftp.cd(dirName);
        }
        String pathArry[] = dirName.split("/");
        StringBuffer filePath = new StringBuffer("/");
        for (String path : pathArry) {
            if (path.equals("")) {
                continue;
            }
            filePath.append(path + "/");
            if (isDirExist(filePath.toString(),sftp)) {
                sftp.cd(filePath.toString());
            } else {
                // 建立目录
                sftp.mkdir(filePath.toString());
                // 进入并设置为当前目录
                sftp.cd(filePath.toString());
            }

        }
        sftp.cd(dirName);
    }
    /**
     * 
     * @Title: isDirExist 
     * @Description: 判断目录是否存在
     * @author chenming<chenmingf@ennew.cn>
     * @param directory
     * @param sftp
     * @return boolean    返回类型
     */
    public static boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }
}
