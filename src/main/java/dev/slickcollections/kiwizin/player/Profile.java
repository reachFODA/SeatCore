package dev.slickcollections.kiwizin.player;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.booster.Booster;
import dev.slickcollections.kiwizin.booster.NetworkBooster;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.container.*;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.database.exception.ProfileLoadException;
import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.game.GameTeam;
import dev.slickcollections.kiwizin.hook.FriendsHook;
import dev.slickcollections.kiwizin.player.enums.PlayerVisibility;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.player.scoreboard.KScoreboard;
import dev.slickcollections.kiwizin.titles.TitleManager;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Profile {
  
  private String name;
  private Game<? extends GameTeam> game;
  private Hotbar hotbar;
  private KScoreboard scoreboard;
  private Map<String, Long> lastHit = new HashMap<>();
  private Map<String, Map<String, DataContainer>> tableMap;
  private Player player;
  
  public Profile(String name) throws ProfileLoadException {
    this.name = name;
    this.tableMap = Database.getInstance().load(name);
    this.getDataContainer("kCoreProfile", "lastlogin").set(System.currentTimeMillis());
  }
  
  private static final Map<String, UUID> UUID_CACHE = new HashMap<>();
  private static final Map<String, Profile> PROFILES = new ConcurrentHashMap<>();
  private static final SimpleDateFormat COMPARE_SDF = new SimpleDateFormat("yyyy/MM/dd");
  
  public static Profile createOrLoadProfile(String playerName) throws ProfileLoadException {
    Profile profile = PROFILES.getOrDefault(playerName.toLowerCase(), null);
    if (profile == null) {
      profile = new Profile(playerName);
      PROFILES.put(playerName.toLowerCase(), profile);
    }
    
    return profile;
  }
  
  public static Profile loadIfExists(String playerName) throws ProfileLoadException {
    Profile profile = PROFILES.getOrDefault(playerName.toLowerCase(), null);
    if (profile == null) {
      playerName = Database.getInstance().exists(playerName);
      if (playerName != null) {
        profile = new Profile(playerName);
      }
    }
    
    return profile;
  }
  
  public static Profile getProfile(String playerName) {
    return PROFILES.get(playerName.toLowerCase());
  }
  
  public static Profile unloadProfile(String playerName) {
    UUID_CACHE.remove(playerName.toLowerCase());
    return PROFILES.remove(playerName.toLowerCase());
  }
  
  public static Player findCached(String playerName) {
    UUID uuid = UUID_CACHE.get(playerName.toLowerCase());
    return uuid == null ? null : Bukkit.getPlayer(uuid);
  }
  
  public static boolean isOnline(String playerName) {
    return PROFILES.containsKey(playerName.toLowerCase());
  }
  
  public static Collection<Profile> listProfiles() {
    return PROFILES.values();
  }
  
  public void setHit(String name) {
    this.lastHit.put(name, System.currentTimeMillis() + 8000);
  }
  
  public void update() {
    this.scoreboard.update();
  }
  
  public void refresh() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }
    
    player.setMaxHealth(20.0);
    player.setHealth(20.0);
    player.setFoodLevel(20);
    player.setExhaustion(0.0f);
    player.setExp(0.0f);
    player.setLevel(0);
    player.setAllowFlight(false);
    player.closeInventory();
    player.spigot().setCollidesWithEntities(true);
    for (PotionEffect pe : player.getActivePotionEffects()) {
      player.removePotionEffect(pe.getType());
    }
    
    if (!playingGame()) {
      player.setGameMode(GameMode.ADVENTURE);
      player.teleport(Core.getLobby());
      
      player.setAllowFlight(player.hasPermission("kcore.fly"));
      this.getDataContainer("kCoreProfile", "role").set(StringUtils.stripColors(Role.getPlayerRole(player, true).getName()));
    }
    
    if (this.hotbar != null) {
      this.hotbar.apply(this);
    }
    this.refreshPlayers();
  }
  
  public void refreshPlayers() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }
    
    if (this.hotbar != null) {
      this.hotbar.getButtons().forEach(button -> {
        if (button.getAction().getValue().equalsIgnoreCase("jogadores")) {
          player.getInventory().setItem(button.getSlot(), BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(player, button.getIcon())));
        }
      });
    }
    
    if (!this.playingGame()) {
      for (Player players : Bukkit.getOnlinePlayers()) {
        Profile profile = Profile.getProfile(players.getName());
        if (profile != null) {
          if (!profile.playingGame()) {
            boolean friend = FriendsHook.isFriend(player.getName(), players.getName());
            if ((this.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS || Role.getPlayerRole(players).isAlwaysVisible() || friend) && !FriendsHook
                .isBlacklisted(player.getName(), players.getName())) {
              if (!player.canSee(players)) {
                TitleManager.show(this, profile);
              }
              player.showPlayer(players);
            } else {
              if (player.canSee(players)) {
                TitleManager.hide(this, profile);
              }
              player.hidePlayer(players);
            }
            
            if ((profile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS || Role.getPlayerRole(player).isAlwaysVisible() || friend) && !FriendsHook
                .isBlacklisted(players.getName(), player.getName())) {
              if (!players.canSee(player)) {
                TitleManager.show(profile, this);
              }
              players.showPlayer(player);
            } else {
              if (players.canSee(player)) {
                TitleManager.hide(profile, this);
              }
              players.hidePlayer(player);
            }
          } else {
            player.hidePlayer(players);
            players.hidePlayer(player);
          }
        }
      }
    }
  }
  
  public void save() {
    if (this.name == null || this.tableMap == null) {
      return;
    }
    
    Database.getInstance().save(this.name, this.tableMap);
  }
  
  public void saveSync() {
    if (this.name == null || this.tableMap == null) {
      return;
    }
    
    Database.getInstance().saveSync(this.name, this.tableMap);
  }
  
  public void destroy() {
    this.name = null;
    this.game = null;
    this.hotbar = null;
    this.scoreboard = null;
    this.lastHit.clear();
    this.lastHit = null;
    this.tableMap.values().forEach(containerMap -> {
      containerMap.values().forEach(DataContainer::gc);
      containerMap.clear();
    });
    this.tableMap.clear();
    this.tableMap = null;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isOnline() {
    return this.name != null && isOnline(this.name);
  }
  
  public Player getPlayer() {
    if (this.player == null) {
      this.player = this.name == null ? null : Bukkit.getPlayerExact(this.name);
    }
    
    return this.player;
  }
  
  public void setPlayer(Player player) {
    this.player = player;
    UUID_CACHE.put(this.name.toLowerCase(), player.getUniqueId());
  }
  
  public Game<?> getGame() {
    return this.getGame(Game.class);
  }
  
  public void setGame(Game<? extends GameTeam> game) {
    this.game = game;
    this.lastHit.clear();
    if (this.game != null) {
      TitleManager.leaveLobby(this);
    } else {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
        if (this.isOnline() && !this.playingGame()) {
          TitleManager.joinLobby(this);
        }
      }, 5);
    }
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Game<?>> T getGame(Class<T> gameClass) {
    return this.game != null && gameClass.isAssignableFrom(this.game.getClass()) ? (T) this.game : null;
  }
  
  public Hotbar getHotbar() {
    return this.hotbar;
  }
  
  public void setHotbar(Hotbar hotbar) {
    this.hotbar = hotbar;
  }
  
  public boolean playingGame() {
    return this.game != null;
  }
  
  public List<Profile> getLastHitters() {
    List<Profile> hitters = this.lastHit.entrySet().stream().filter(entry -> entry.getValue() >
            System.currentTimeMillis() && isOnline(entry.getKey()))
        .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).map(entry -> getProfile(entry.getKey()))
        .collect(Collectors.toList());
    // limpar após uso
    this.lastHit.clear();
    return hitters;
  }
  
  public KScoreboard getScoreboard() {
    return this.scoreboard;
  }
  
  public void setScoreboard(KScoreboard scoreboard) {
    if (this.scoreboard != null) {
      this.scoreboard.destroy();
    }
    this.scoreboard = scoreboard;
  }
  
  public void addStats(String table, String... keys) {
    this.addStats(table, 1, keys);
  }
  
  public void addStats(String table, long amount, String... keys) {
    for (String key : keys) {
      if (!table.toLowerCase().contains("murder")) {
        if (key.startsWith("monthly")) {
          String month = this.getDataContainer(table, "month").getAsString();
          String current = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
          if (!month.equals(current)) {
            Map<String, DataContainer> containerMap = this.tableMap.get(table);
            containerMap.keySet().forEach(k -> {
              if (k.startsWith("monthly")) {
                containerMap.get(k).set(0L);
              }
            });
            containerMap.get("month").set(current);
          }
        }
      }
      
      this.getDataContainer(table, key).addLong(amount);
    }
  }
  
  public void setStats(String table, long amount, String... keys) {
    for (String key : keys) {
      this.getDataContainer(table, key).set(amount);
    }
  }
  
  public void updateDailyStats(String table, String date, long amount, String key) {
    long currentExpire = this.getStats(table, date);
    this.setStats(table, System.currentTimeMillis(), date);
    if (amount == 0 || (this.getStats(table, key) > 0 && !COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire)))) {
      this.setStats(table, 0, key);
      return;
    }
    
    this.addStats(table, amount, key);
  }
  
  public int addCoins(String table, double amount) {
    this.getDataContainer(table, "coins").addDouble(amount);
    return (int) amount;
  }
  
  // Com multiplicador
  public int addCoinsWM(String table, double amount) {
    amount = this.calculateWM(amount);
    this.addCoins(table, amount);
    return (int) amount;
  }
  
  public double calculateWM(double amount) {
    double add = 0.0D;
    String booster = this.getBoostersContainer().getEnabled();
    if (booster != null) {
      add = amount * Double.parseDouble(booster.split(":")[0]);
    }
    
    NetworkBooster nb = Booster.getNetworkBooster(Core.minigame);
    if (nb != null) {
      add += amount * nb.getMultiplier();
    }
    
    return (amount > 0.0 && add == 0.0) ? amount : add;
  }
  
  public void removeCoins(String table, double amount) {
    this.getDataContainer(table, "coins").removeDouble(amount);
  }
  
  public long getStats(String table, String... keys) {
    long stat = 0;
    for (String key : keys) {
      stat += this.getDataContainer(table, key).getAsLong();
    }
    
    return stat;
  }
  
  // Resetar diariamente baseado em um Timemillis.
  public long getDailyStats(String table, String date, String key) {
    long currentExpire = this.getStats(table, date);
    if (!COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire))) {
      this.setStats(table, 0, key);
    }
    
    this.setStats(table, System.currentTimeMillis(), date);
    return this.getStats(table, key);
  }
  
  public double getCoins(String table) {
    return this.getDataContainer(table, "coins").getAsDouble();
  }
  
  public String getFormatedStats(String table, String... keys) {
    return StringUtils.formatNumber(this.getStats(table, keys));
  }
  
  public String getFormatedStatsDouble(String table, String key) {
    return StringUtils.formatNumber(this.getDataContainer(table, key).getAsDouble());
  }

  public DeliveriesContainer getDeliveriesContainer() {
    return this.getAbstractContainer("kCoreProfile", "deliveries", DeliveriesContainer.class);
  }

  public PreferencesContainer getPreferencesContainer() {
    return this.getAbstractContainer("kCoreProfile", "preferences", PreferencesContainer.class);
  }
  
  public TitlesContainer getTitlesContainer() {
    return this.getAbstractContainer("kCoreProfile", "titles", TitlesContainer.class);
  }
  
  public BoostersContainer getBoostersContainer() {
    return this.getAbstractContainer("kCoreProfile", "boosters", BoostersContainer.class);
  }

  public SkinsContainer getSkinListContainer() {
    return this.getAbstractContainer("kCoreSkins", "skins", SkinsContainer.class);
  }

  public SkinsContainer getSkinsContainer() {
    return this.getAbstractContainer("kCoreSkins", "info", SkinsContainer.class);
  }
  
  public AchievementsContainer getAchievementsContainer() {
    return this.getAbstractContainer("kCoreProfile", "achievements", AchievementsContainer.class);
  }
  
  public SelectedContainer getSelectedContainer() {
    return this.getAbstractContainer("kCoreProfile", "selected", SelectedContainer.class);
  }
  
  public DataContainer getDataContainer(String table, String key) {
    return this.tableMap.get(table).get(key);
  }
  
  public <T extends AbstractContainer> T getAbstractContainer(String table, String key, Class<T> containerClass) {
    return this.getDataContainer(table, key).getContainer(containerClass);
  }
  
  public Map<String, Map<String, DataContainer>> getTableMap() {
    return this.tableMap;
  }
}