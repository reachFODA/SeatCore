package dev.slickcollections.kiwizin.achievements.types;

import dev.slickcollections.kiwizin.achievements.Achievement;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.titles.Title;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class MurderAchievement extends Achievement {
  
  protected MurderReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public MurderAchievement(MurderReward reward, String id, String name, String desc, int reach, String... stats) {
    super("mm-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : nome>%name% : desc>" + desc + "\n \n&fProgresso: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(500), "d1", "Investigador", "&7Vença um total de %reach% partidas\n&7como Detetive para receber:\n \n &8• &6500 Coins", 100,
            "cldetectivewins"));
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(500), "k2", "Trapper", "&7Vença um total de %reach% partidas\n&7como Assassino para receber:\n \n &8• &6500 Coins", 100,
            "clkillerwins"));
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(1500), "d2", "Perito Criminal", "&7Vença um total de %reach% partidas\n&7como Detetive para receber:\n \n &8• &61.500 Coins", 200,
            "cldetectivewins"));
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(1500), "k2", "Traidor", "&7Vença um total de %reach% partidas\n&7como Assassino para receber:\n \n &8• &61.500 Coins",
            200, "clkillerwins"));
    
    Achievement.addAchievement(
        new MurderAchievement(new TitleReward("mmd"), "td", "Detetive", "&7Vença um total de %reach% partidas\n&7como Detetive para receber:\n \n &8• &fTítulo: &6Sherlock Holmes",
            400, "cldetectivewins"));
    Achievement.addAchievement(new MurderAchievement(new TitleReward("mmk"), "tk", "Serial Killer",
        "&7Vença um total de %reach% partidas\n&7como Assassino para receber:\n \n &8• &fTítulo: &4Jeff the Killer", 400, "clkillerwins"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("kCoreMurder", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("kCoreMurder", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "ENCHANTED_BOOK" : "BOOK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface MurderReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements MurderReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("kCoreMurder", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements MurderReward {
    private final String titleId;
    
    public TitleReward(String titleId) {
      this.titleId = titleId;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getTitlesContainer().add(Title.getById(this.titleId));
    }
  }
}
