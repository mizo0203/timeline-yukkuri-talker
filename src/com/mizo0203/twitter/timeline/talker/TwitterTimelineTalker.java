package com.mizo0203.twitter.timeline.talker;

import twitter4j.*;
import twitter4j.conf.Configuration;

import java.util.Collections;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterTimelineTalker {

  /**
   * ISO 639 言語コード - 日本語 (ja)
   */
  public static final String LANG_JA = Locale.JAPAN.getLanguage();

  private final RequestHomeTimelineTimerTask mRequestHomeTimelineTimerTask;

  public TwitterTimelineTalker(Configuration configuration, Talker talker) {
    Twitter twitter = new TwitterFactory(configuration).getInstance();
    mRequestHomeTimelineTimerTask = new RequestHomeTimelineTimerTask(twitter, talker);
  }

  static /* package */ String getUserNameWithoutContext(String name) {
    Pattern p = Pattern.compile("([^@＠]+).*");
    Matcher m = p.matcher(name);
    return m.replaceFirst("$1");
  }

  public void start() {
    new Timer().schedule(mRequestHomeTimelineTimerTask, 0L, TimeUnit.MINUTES.toMillis(1));
  }

  private static class RequestHomeTimelineTimerTask extends TimerTask {

    private static final int HOME_TIMELINE_COUNT_MAX = 200;
    private static final int HOME_TIMELINE_COUNT_MIN = 1;

    private final Twitter mTwitter;
    private final Talker mTalker;

    private Talker.YukkuriVoice mYukkuriVoice = Talker.YukkuriVoice.REIMU;

    /**
     * mStatusSinceId より大きい（つまり、より新しい） ID を持つ HomeTimeline をリクエストする
     */
    private long mStatusSinceId = 1L;

    private boolean mIsUpdatedStatusSinceId = false;

    private RequestHomeTimelineTimerTask(Twitter twitter, Talker talker) {
      mTwitter = twitter;
      mTalker = talker;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
      try {
        // mStatusSinceId が未更新ならば、 Status を 1 つだけ取得する
        int count = mIsUpdatedStatusSinceId ? HOME_TIMELINE_COUNT_MAX : HOME_TIMELINE_COUNT_MIN;
        Paging paging = new Paging(1, count, mStatusSinceId);
        ResponseList<Status> statusResponseList = mTwitter.getHomeTimeline(paging);

        if (statusResponseList.isEmpty()) {
          return;
        }

        // mStatusSinceId を、取得した最新の ID に更新する
        mStatusSinceId = statusResponseList.get(0).getId();
        mIsUpdatedStatusSinceId = true;

        // Status が古い順になるよう、 statusResponseList を逆順に並び替える
        Collections.reverse(statusResponseList);

        for (Status status : statusResponseList) {
          onStatus(status);
        }

      } catch (TwitterException e) {
        e.printStackTrace();
      }
    }

    private void onStatus(final Status status) {
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
  }
}
