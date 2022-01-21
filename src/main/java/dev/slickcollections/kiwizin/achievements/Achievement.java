package dev.slickcollections.kiwizin.achievements;

import dev.slickcollections.kiwizin.achievements.types.BedWarsAchievement;
import dev.slickcollections.kiwizin.achievements.types.MurderAchievement;
import dev.slickcollections.kiwizin.achievements.types.SkyWarsAchievement;
import dev.slickcollections.kiwizin.achievements.types.TheBridgeAchievement;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Achievement {
  
  private static final List<Achievement> ACHIEVEMENTS = new ArrayList<>();
  
  protected String id;
  protected String name;
  
  public Achievement(String id, String name) {
    this.id = id;
    this.name = name;
  }
  
  public static void setupAchievements() {
    SkyWarsAchievement.setupAchievements();
    BedWarsAchievement.setupAchievements();
    TheBridgeAchievement.setupAchievements();
    MurderAchievement.setupAchievements();
  }
  
  public static void addAchievement(Achievement achievement) {
    ACHIEVEMENTS.add(achievement);
  }
  
  public static Collection<Achievement> listAchievements() {
    return ACHIEVEMENTS;
  }
  
  @SuppressWarnings("unchecked")
  public static <T extends Achievement> List<T> listAchievements(Class<T> type) {
    return listAchievements().stream().filter(achievement -> achievement.getClass().equals(type)).map(achievement -> (T) achievement).collect(Collectors.toList());
  }
  
  protected abstract void give(Profile profile);
  
  protected abstract boolean check(Profile profile);
  
  public abstract ItemStack getIcon(Profile profile);
  
  public void complete(Profile profile) {
    profile.getAchievementsContainer().complete(this);
    this.give(profile);
  }
  
  public boolean canComplete(Profile profile) {
    return profile.isOnline() && !this.isCompleted(profile) && this.check(profile);
  }
  
  public boolean isCompleted(Profile profile) {
    return profile.getAchievementsContainer().isCompleted(this);
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
}
