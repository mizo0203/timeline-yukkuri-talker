package com.mizo0203.twitter.timeline.talker;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Java アプリケーション起動時に実行されるクラス
 * 
 * @author みぞ@CrazyBeatCoder
 */
public class Application {

  public static void main(String[] args) {
    TwitterTimelineTalker twitterTimelineTalker;
    Talker talker;

    try {
      talker = new Talker();
      twitterTimelineTalker =
          new TwitterTimelineTalker(new Arguments(args).twitterConfiguration, talker);
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.err.println(e.getMessage());
      return;
    }

    twitterTimelineTalker.start();
    talker.talkAsync("アプリケーションを起動しました", Talker.YukkuriVoice.REIMU);
  }

  /**
   * Java アプリケーション起動時に指定する引数のデータクラス
   * 
   * @author みぞ@CrazyBeatCoder
   */
  private static class Arguments {

    private final Configuration twitterConfiguration;

    private Arguments(String[] args) throws IllegalArgumentException {
      if (args.length < Argument.values().length) {
        StringBuilder exceptionMessage = new StringBuilder();
        exceptionMessage.append(Argument.values().length + " つの引数を指定してください。\n");
        for (Argument arg : Argument.values()) {
          exceptionMessage.append((arg.ordinal() + 1) + " つ目: " + arg.detail + "\n");
        }
        throw new IllegalArgumentException(exceptionMessage.toString());
      }

      String consumer_key = args[Argument.CONSUMER_KEY.ordinal()];
      String consumer_secret = args[Argument.CONSUMER_SECRET.ordinal()];
      String access_token = args[Argument.ACCESS_TOKEN.ordinal()];
      String access_token_secret = args[Argument.ACCESS_TOKEN_SECRET.ordinal()];

      twitterConfiguration = new ConfigurationBuilder().setOAuthConsumerKey(consumer_key)
          .setOAuthConsumerSecret(consumer_secret).setOAuthAccessToken(access_token)
          .setOAuthAccessTokenSecret(access_token_secret).build();
    }

  }

  /**
   * Java アプリケーション起動時に指定する引数の定義
   * 
   * @author みぞ@CrazyBeatCoder
   */
  private enum Argument {
    CONSUMER_KEY("Twitter Application's Consumer Key (API Key)"), //
    CONSUMER_SECRET("Twitter Application's Consumer Secret (API Secret)"), //
    ACCESS_TOKEN("Twitter Account's Access Token"), //
    ACCESS_TOKEN_SECRET("Twitter Account's Access Token Secret"), //
    ;

    private final String detail;

    private Argument(String detail) {
      this.detail = detail;
    }
  }
}
