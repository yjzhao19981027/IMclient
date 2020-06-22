package sample.Util;

public class CmdUtil {
    public static String getCmdHead(Object cmd){
        String str = (String) cmd;
        for (int i = 0, len = str.length() ; i < len ; i ++)
            if (str.charAt(i) == ' ')
                return str.substring(0,i);
        return null;
    }

    public static String getCmdContent(Object cmd){
        String str = (String) cmd;
        for (int i = 0, len = str.length() ; i < len ; i ++)
            if (str.charAt(i) == ' ')
                return str.substring(i + 1,len);
        return null;
    }
}
