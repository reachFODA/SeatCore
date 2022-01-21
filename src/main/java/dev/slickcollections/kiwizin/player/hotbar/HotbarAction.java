package dev.slickcollections.kiwizin.player.hotbar;

import dev.slickcollections.kiwizin.player.Profile;

public class HotbarAction {
  
  private final String value;
  private final HotbarActionType actionType;
  
  public HotbarAction(String action) {
    String[] splitter = action.split(">");
    this.value = splitter.length > 1 ? splitter[1] : "";
    this.actionType = HotbarActionType.fromName(splitter[0]);
  }
  
  public void execute(Profile profile) {
    if (this.actionType != null) {
      if (this.value.isEmpty()) {
        return;
      }
      
      this.actionType.execute(profile, this.value);
    }
  }
  
  public String getValue() {
    return this.value;
  }
  
  public HotbarActionType getActionType() {
    return this.actionType;
  }
}
