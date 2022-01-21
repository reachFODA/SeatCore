package dev.slickcollections.kiwizin.titles;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TitleManager {
  
  private static final TitleManager TITLE_MANAGER = new TitleManager();
  private final Map<String, TitleController> controllers = new HashMap<>();
  
  public static void joinLobby(Profile profile) {
    TITLE_MANAGER.onJoinLobby(profile);
  }
  
  public static void leaveLobby(Profile profile) {
    TITLE_MANAGER.onLeaveLobby(profile);
  }
  
  public static void leaveServer(Profile profile) {
    TITLE_MANAGER.onLeaveServer(profile);
  }
  
  public static void show(Profile profile, Profile target) {
    TITLE_MANAGER.onLobbyShow(profile, target);
  }
  
  public static void hide(Profile profile, Profile target) {
    TITLE_MANAGER.onLobbyHide(profile, target);
  }
  
  public static void select(Profile profile, Title title) {
    TITLE_MANAGER.onSelectTitle(profile, title);
  }
  
  public static void deselect(Profile profile) {
    TITLE_MANAGER.onDeselectTitle(profile);
  }
  
  public void onJoinLobby(Profile profile) {
    if (profile.getName() == null) {
      return;
    }
    
    Player player = profile.getPlayer();
    if (player != null) {
      this.controllers.values().forEach(controller -> {
        if (controller.getOwner() != null && player.canSee(controller.getOwner())) {
          controller.showToPlayer(player);
        }
      });
    }
    
    TitleController controller = this.getTitleController(profile);
    if (controller != null) {
      controller.enable();
    } else {
      Title title = profile.getSelectedContainer().getTitle();
      if (title != null && !this.controllers.containsKey(profile.getName())) {
        this.onSelectTitle(profile, title);
      }
    }
  }
  
  public void onLeaveLobby(Profile profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller != null) {
      controller.disable();
    }
    
    Player player = profile.getPlayer();
    if (player != null) {
      this.controllers.values().forEach(c -> {
        if (c.getOwner() != null && player.canSee(c.getOwner())) {
          c.hideToPlayer(player);
        }
      });
    }
  }
  
  public void onLeaveServer(Profile profile) {
    TitleController controller = this.controllers.remove(profile.getName());
    if (controller != null) {
      controller.destroy();
    }
  }
  
  public void onLobbyShow(Profile profile, Profile target) {
    Player player = profile.getPlayer();
    TitleController controller = this.getTitleController(target);
    if (controller != null) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
        if (controller.getOwner() != null && player.isOnline() && player.canSee(controller.getOwner())) {
          controller.showToPlayer(player);
        }
      }, 10);
    }
  }
  
  public void onLobbyHide(Profile profile, Profile target) {
    Player player = profile.getPlayer();
    TitleController controller = this.getTitleController(target);
    if (controller != null) {
      controller.hideToPlayer(player);
    }
  }
  
  public void onSelectTitle(Profile profile, Title title) {
    TitleController controller = this.getTitleController(profile);
    if (controller == null) {
      controller = new TitleController(profile.getPlayer(), title.getTitle());
      controller.enable();
      this.controllers.put(profile.getName(), controller);
      return;
    }
    
    controller.setName(title.getTitle());
  }
  
  public void onDeselectTitle(Profile profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller == null) {
      return;
    }
    
    controller.setName("disabled");
  }
  
  public TitleController getTitleController(Profile profile) {
    return this.controllers.get(profile.getName());
  }
}
