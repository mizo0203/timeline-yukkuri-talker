package com.mizo0203.twitter.timeline.talker;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class TwitterTimelineTalker {

  /**
   * ISO 639 言語コード - 日本語 (ja)
   */
  public static final String LANG_JA = Locale.JAPAN.getLanguage();

  private Talker.YukkuriVoice mYukkuriVoice = Talker.YukkuriVoice.REIMU;
  private final TwitterStream mTwitterStream;
  private final Talker mTalker;

  public TwitterTimelineTalker(Configuration configuration, Talker talker) {
    mTwitterStream = new TwitterStreamFactory(configuration).getInstance();
    mTwitterStream.addListener(new OnStatusEvent());
    mTalker = talker;
  }

  public void start() {
    // OnStatusEvent に Twitter タイムラインが通知される
    mTwitterStream.user();
  }

  private static String getUserNameWithoutContext(String name) {
    Pattern p = Pattern.compile("([^@＠]+).+");
    Matcher m = p.matcher(name);
    return m.replaceFirst("$1");
  }

  private class OnStatusEvent implements StatusListener {

    public void onStatus(final Status status) {
      if (!LANG_JA.equalsIgnoreCase(status.getLang())) {
        return;
      }

      final StringBuffer buffer = new StringBuffer();

      if (status.isRetweet()) {
        Status retweetedStatus = status.getRetweetedStatus();
        buffer.append(getUserNameWithoutContext(status.getUser().getName()) + "さんがリツイート。");
        buffer.append(getUserNameWithoutContext(retweetedStatus.getUser().getName()) + "さんから、");
        buffer.append(retweetedStatus.getText());
      } else {
        buffer.append(getUserNameWithoutContext(status.getUser().getName()) + "さんから、");
        buffer.append(status.getText());
      }

      mTalker.talkAsync(UrlUtil.convURLEmpty(buffer).replaceAll("\n", "。"), mYukkuriVoice);

      // 読み上げは、霊夢と魔理沙が交互に行なう
      if (mYukkuriVoice == Talker.YukkuriVoice.REIMU) {
        mYukkuriVoice = Talker.YukkuriVoice.MARISA;
      } else {
        mYukkuriVoice = Talker.YukkuriVoice.REIMU;
      }

    }

    public void onDeletionNotice(StatusDeletionNotice sdn) {
      System.err.println("onDeletionNotice.");
    }

    public void onTrackLimitationNotice(int i) {
      System.err.println("onTrackLimitationNotice.(" + i + ")");
    }

    public void onScrubGeo(long lat, long lng) {
      System.err.println("onScrubGeo.(" + lat + ", " + lng + ")");
    }

    public void onException(Exception excptn) {
      System.err.println("onException.");
    }

    @Override
    public void onStallWarning(StallWarning arg0) {}
  }

}
