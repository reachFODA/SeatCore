package dev.slickcollections.kiwizin.database.data.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.player.enums.*;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class PreferencesContainer extends AbstractContainer {
  
  public PreferencesContainer(DataContainer dataContainer) {
    super(dataContainer);

    JSONObject preferences = this.dataContainer.getAsJsonObject();
    if (!preferences.containsKey("ag")) {
      preferences.put("ag", 0);
    }
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changePlayerVisibility() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pv", PlayerVisibility.getByOrdinal((long) preferences.get("pv")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changePrivateMessages() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pm", PrivateMessages.getByOrdinal((long) preferences.get("pm")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changeBloodAndGore() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("bg", BloodAndGore.getByOrdinal((long) preferences.get("bg")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changeProtectionLobby() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pl", ProtectionLobby.getByOrdinal((long) preferences.get("pl")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeChatMention() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("cm", ProtectionLobby.getByOrdinal((long) preferences.get("cm")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeAutoGG() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("ag", ProtectionLobby.getByOrdinal((long) preferences.get("ag")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public PlayerVisibility getPlayerVisibility() {
    return PlayerVisibility.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pv"));
  }
  
  public PrivateMessages getPrivateMessages() {
    return PrivateMessages.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pm"));
  }
  
  public BloodAndGore getBloodAndGore() {
    return BloodAndGore.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("bg"));
  }
  
  public ProtectionLobby getProtectionLobby() {
    return ProtectionLobby.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pl"));
  }

  public ChatMention getChatMention() {
    return ChatMention.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("cm"));
  }

  public AutoGG getAutoGG() {
    return AutoGG.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("ag"));
  }
}
