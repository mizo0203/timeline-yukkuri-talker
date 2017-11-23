package com.mizo0203.twitter.timeline.talker;

import java.io.IOException;

public class RuntimeUtil {

  public static void execute(String[] cmdarray) {
    try {
      Process process = Runtime.getRuntime().exec(cmdarray);
      process.waitFor();
      process.destroy();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
