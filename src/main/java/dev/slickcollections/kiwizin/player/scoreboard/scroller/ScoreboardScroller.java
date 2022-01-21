package dev.slickcollections.kiwizin.player.scoreboard.scroller;

import java.util.List;

public class ScoreboardScroller {
  
  private int index;
  private final List<String> frames;
  
  public ScoreboardScroller(List<String> frames) {
    this.index = -1;
    this.frames = frames;
  }
  
  public String next() {
    if (++index >= frames.size()) {
      this.index = 0;
    }
    
    return frames.get(index);
  }
}
