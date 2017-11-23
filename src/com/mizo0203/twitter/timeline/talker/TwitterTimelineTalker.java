package com.mizo0203.twitter.timeline.talker;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class TwitterTimelineTalker {

  private boolean mVoice_f1 = true;
  private final TwitterStream mTwitterStream;
  private final Talker mTalker;

  public TwitterTimelineTalker(Configuration configuration, Talker talker) {
    mTwitterStream = new TwitterStreamFactory(configuration).getInstance();
    mTwitterStream.addListener(new MyStatusListener());
    mTalker = talker;
  }

  public void start() {
    mTwitterStream.user();
  }

  private class MyStatusListener implements StatusListener {

    public void onStatus(final Status status) {
      if (!"ja".equalsIgnoreCase(status.getLang())) {
        return;
      }
      final StringBuffer buffer = new StringBuffer();
      buffer.append(status.getUser().getName());
      buffer.append("さんから、");
      buffer.append(status.getText());
      System.out.println(buffer);

      // System.out.println("@" + status.getUser().getScreenName() + " | "
      // + status.getText() + " 【 https://twitter.com/" +
      // status.getUser().getScreenName() + "/status/" + status.getId() +
      // " 】");
      // こんな感じでstatusについている名前とかを色々表示させるとさらに欲しい情報にたどり着けると思います


      mTalker.talkAsync(UrlUtil.convURLEmpty(buffer).replaceAll("\n", "。"),
          (mVoice_f1 ? Talker.YukkuriVoice.REIMU : Talker.YukkuriVoice.MARISA));
      mVoice_f1 = !mVoice_f1;

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
