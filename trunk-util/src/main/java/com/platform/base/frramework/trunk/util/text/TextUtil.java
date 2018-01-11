package com.platform.base.frramework.trunk.util.text;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by mylover on 4/22/16.
 */
public class TextUtil {
    private static MessageDigest md = null;
    public static final Random rnd = new Random();
    public static final SimpleDateFormat TimestampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");

    public TextUtil() {
    }

    public static String readContentByStream(InputStream is) {
        BufferedReader reader = null;
        String line = null;
        StringBuilder sb = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while((line = reader.readLine()) != null) {
                sb.append("\t\t" + line.trim()).append("\r\n");
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return sb.toString().trim();
    }

    public static String readContentByReader(String path) throws IOException {
        char[] buf = new char[1024];
        StringBuilder out = new StringBuilder();

        try {
            BufferedReader e = new BufferedReader(new FileReader(new File(path)));
            boolean bin = false;

            int bin1;
            while((bin1 = e.read(buf, 0, buf.length)) >= 0) {
                out.append(buf, 0, bin1);
            }

            e.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return out.toString();
    }

    public static int creatRandom(int t) {
        return Math.abs(rnd.nextInt(t));
    }

    public static String md5(String c) {
        if(md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var2) {
                var2.printStackTrace();
            }
        }

        if(md != null) {
            md.update(c.getBytes());
            return byte2hex(md.digest());
        } else {
            return "";
        }
    }

    public static String formatTimestamp(long time) {
        return TimestampFormat.format(new Date(time));
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if(stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }

            if(n < b.length - 1) {
                hs = hs;
            }
        }

        return hs.toUpperCase();
    }

    public static String readContentByFile(String path) {
        BufferedReader reader = null;
        String line = null;
        StringBuilder sb = new StringBuilder();

        try {
            File e = new File(path);
            reader = new BufferedReader(new FileReader(e));

            while((line = reader.readLine()) != null) {
                sb.append(line.trim());
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return sb.toString().trim();
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException var17) {
            var17.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException var16) {
                var16.printStackTrace();
            }

            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }

            is = null;
        }

        return sb.toString().trim();
    }

    public static String TrimSpace(String str) {
        if(str != null && !str.equals("")) {
            char[] tempChar = (char[])null;

            try {
                char[] e = new char[str.length()];
                int k = 0;

                for(int i = 0; i < str.length(); ++i) {
                    char c = str.charAt(i);
                    if(c != 32) {
                        e[k++] = c;
                    }
                }

                tempChar = new char[k];
                System.arraycopy(e, 0, tempChar, 0, k);
                return new String(tempChar);
            } catch (Exception var6) {
                return str;
            }
        } else {
            return null;
        }
    }

    public static String[] split(String original, String regex, boolean isTogether) {
        int startIndex = original.indexOf(regex);
        int index = 0;
        if(startIndex < 0) {
            return new String[]{original};
        } else {
            ArrayList v;
            String last;
            for(v = new ArrayList(); startIndex < original.length() && startIndex != -1; startIndex = original.indexOf(regex, startIndex + regex.length())) {
                last = original.substring(index, startIndex);
                v.add(last);
                index = startIndex + regex.length();
            }

            if(original.indexOf(regex, original.length() - regex.length()) < 0) {
                last = original.substring(index);
                if(isTogether) {
                    last = regex + last;
                }

                v.add(last);
            }

            return (String[])v.toArray(new String[v.size()]);
        }
    }

    public static String getLastStringBySplit(String original, String regex) {
        try {
            String[] e = split(original, regex);
            return e[e.length - 1].trim();
        } catch (Exception var3) {
            var3.printStackTrace();
            return original;
        }
    }

    public static String[] split(String original, String regex) {
        return split(original, regex, false);
    }

    public static String replace(String str, String substr, String restr) {
        try {
            String[] e = split(str, substr);
            String returnstr = null;
            int len = e.length;
            if(e != null && len > 0) {
                returnstr = e[0];

                for(int i = 0; i < len - 1; ++i) {
                    returnstr = returnstr + restr + e[i + 1];
                }
            }

            return returnstr.trim();
        } catch (Exception var7) {
            var7.printStackTrace();
            return str;
        }
    }

    public static void releaseStringArray(String[] array) {
        if(array != null) {
            for(int i = 0; i < array.length; ++i) {
                array[i] = null;
            }

            array = (String[])null;
        }

    }

    public static String xmlTextDecode(String xmlText) {
        if(xmlText == null) {
            return "";
        } else {
            xmlText = xmlText.replaceAll("&amp;", "&");
            xmlText = xmlText.replaceAll("&lt;", "<");
            xmlText = xmlText.replaceAll("&gt;", ">");
            xmlText = xmlText.replaceAll("&apos;", "\\");
            xmlText = xmlText.replaceAll("&quot;", "\"");
            return xmlText;
        }
    }
}
