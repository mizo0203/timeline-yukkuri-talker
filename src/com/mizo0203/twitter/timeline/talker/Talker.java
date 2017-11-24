package com.mizo0203.twitter.timeline.talker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Talker {

  private static final String AQUESTALK_PI_PATH = "./aquestalkpi/AquesTalkPi";

  private final ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();

  public Talker() throws IllegalStateException, SecurityException {
    File file = new File(AQUESTALK_PI_PATH);
    if (!file.isFile()) {
      throw new IllegalStateException(file.getPath() + " に AquesTalk Pi がありません。\n"
          + "https://www.a-quest.com/products/aquestalkpi.html\n" + "からダウンロードしてください。");
    }
    if (!file.canExecute()) {
      throw new IllegalStateException(file.getPath() + " に実行権限がありません。");
    }
  }

  public void talkAsync(final String text, final YukkuriVoice voice) {
    mSingleThreadExecutor.submit(new Runnable() {

      @Override
      public void run() {
        try {
          File file = new File("text.txt");
          PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
          pw.println(text);
          pw.flush();
          pw.close();
          RuntimeUtil.execute(new String[] {AQUESTALK_PI_PATH, "-v", voice.value, "-f", "text.txt",
              "-o", "out.wav"});
          RuntimeUtil.execute(new String[] {"sh", "-c", "aplay < out.wav"}); // 起動コマンドを指定する
          Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
      }

    });
  }

  public static enum YukkuriVoice {

    /**
     * ゆっくりボイス - 霊夢
     */
    REIMU("f1"), //

    /**
     * ゆっくりボイス - 魔理沙
     */
    MARISA("f2"), //
    ;

    private final String value;

    private YukkuriVoice(String value) {
      this.value = value;
    }
  }

}
