package com.hdr.demo.javafx;

import org.apache.commons.lang3.SystemUtils;
public class LockScreenCmd {

    private static final String command;

    static{
        String os = SystemUtils.OS_NAME.toLowerCase();
        if (os.contains("win")) {
            command = "Rundll32.exe user32.dll,LockWorkStation";
        } else if (os.contains("mac")) {
            command = "open -a ScreenSaverEngine";
        }else{
            command = "loginctl lock-session";
        }
    }

    public static String getCommand() {
        return command;
    }
}
