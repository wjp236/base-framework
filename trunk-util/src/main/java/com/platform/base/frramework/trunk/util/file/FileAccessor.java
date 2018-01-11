package com.platform.base.frramework.trunk.util.file;

import java.io.*;

/**
 * Created by mylover on 4/22/16.
 */
public class FileAccessor {

    public static final String CSV_SPACE_SIGN = ",";

    public static synchronized void appendByBufferedWriter(String path, byte[] content, int available) {
        BufferedOutputStream bw = null;

        try {
            File e = new File(path);
            if(!e.exists()) {
                e.createNewFile();
                System.out.println("created file: " + e.getName());
            }

            bw = new BufferedOutputStream(new FileOutputStream(e, true));
            bw.write(content, 0, available);
            bw.flush();
            bw.close();
            System.out.println("file data [" + content.length + "] already append finished.");
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException var12) {
                    ;
                }

                bw = null;
            }

        }

    }

    public static synchronized void appendByFileWriter(String path, byte[] content, int available) {
        try {
            FileWriter e = new FileWriter(path, true);
            e.append(new String(content, 0, available));
            e.flush();
            e.close();
            System.out.println("file data [" + content.length + "] already append finished.");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void saveStreamFile(InputStream is, String path) throws IOException {
        FileOutputStream fos = null;
        byte[] temp = (byte[])null;

        try {
            File e = new File(path);
            if(e.exists()) {
                e.delete();
                System.out.println("deleted file: " + e.getName());
            }

            e.createNewFile();
            fos = new FileOutputStream(e);
            temp = new byte[1024];
            boolean i = false;

            int i1;
            while((i1 = is.read(temp)) > -1) {
                if(i1 < temp.length) {
                    byte[] b = new byte[i1];
                    System.arraycopy(temp, 0, b, 0, b.length);
                    fos.write(b);
                } else {
                    fos.write(temp);
                }
            }
        } catch (IOException var17) {
            throw var17;
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }

                is = null;
            }

            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }

                fos = null;
            }

            temp = (byte[])null;
        }

    }

    public static void deleteAllFile(String path) {
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("this path doesn\'t exists [" + path + "]");
        } else {
            listFile(file);
        }
    }

    private static void listFile(File file) {
        if(file.isDirectory()) {
            File[] t = file.listFiles();

            for(int i = 0; i < t.length; ++i) {
                File f = t[i];
                System.out.println(f.getAbsolutePath());
                if(f.isDirectory()) {
                    listFile(f);
                    System.out.println("delete dir: " + f.getAbsolutePath());
                    f.delete();
                } else {
                    f.delete();
                }
            }
        } else {
            file.delete();
        }

    }

    /**
     * 获取文件列表
     * @param listener
     * @param path
     * @param suffix
     * @param isdepth
     */
    public static void getListFiles(FileAccessorListener listener, String path, String suffix, boolean isdepth) {
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("this path doesn\'t exists [" + path + "]");
        } else {
            listFile(new FileAccessorListener[]{listener}, file, suffix, isdepth);
        }
    }

    public static void getListFiles(FileAccessorListener[] listener, String path, String suffix, boolean isdepth) {
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("this path doesn\'t exists [" + path + "]");
        } else {
            listFile(listener, file, suffix, isdepth);
        }
    }

    private static void listFile(FileAccessorListener[] listener, File file, String suffix, boolean isdepth) {
        try {
            int beginIndex;
            if(file.isDirectory()) {
                File[] e = file.listFiles();

                for(beginIndex = 0; beginIndex < e.length; ++beginIndex) {
                    if(isdepth || e[beginIndex].isFile()) {
                        listFile(listener, e[beginIndex], suffix, isdepth);
                    }
                }
            } else {
                String var11 = file.getAbsolutePath();
                System.out.println(var11);
                String tmpsuffix;
                int i;
                if(!suffix.equals("")) {
                    beginIndex = var11.lastIndexOf(".");
                    if(beginIndex != -1) {
                        tmpsuffix = var11.substring(beginIndex + 1, var11.length());
                        if(tmpsuffix.equals(suffix)) {
                            i = var11.lastIndexOf(File.separator);
                            String key = var11.substring(i + 1, beginIndex);
                            System.out.println("key: " + key + ", value: " + var11);
                            if(listener != null) {
                                for(int i1 = 0; i1 < listener.length; ++i1) {
                                    listener[i1].onFileAccessorNotify(key, var11, file.lastModified());
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("key: " + suffix + ", value: " + var11);
                    beginIndex = var11.lastIndexOf(File.separator);
                    tmpsuffix = suffix;
                    if(beginIndex != -1) {
                        tmpsuffix = var11.substring(beginIndex + 1);
                    }

                    if(listener != null) {
                        for(i = 0; i < listener.length; ++i) {
                            listener[i].onFileAccessorNotify(tmpsuffix, var11, file.lastModified());
                        }
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

}
