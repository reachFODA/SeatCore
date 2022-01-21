package dev.slickcollections.kiwizin.game;

import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameTeam {
  
  private static final String[] ALPHABET = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
  private final String name;
  private final int size;
  private final Game<?> game;
  private final String location;
  private final List<UUID> members;
  
  public GameTeam(Game<?> game, String location, int size) {
    this.name = ALPHABET[game.listTeams().size()] + "-MCGT";
    this.game = game;
    this.location = location;
    this.size = size;
    this.members = new ArrayList<>(size);
  }
  
  public void reset() {
    this.members.clear();
  }
  
  public void addMember(Player player) {
    this.members.add(player.getUniqueId());
  }
  
  public void removeMember(Player player) {
    this.members.remove(player.getUniqueId());
  }
  
  public String getName() {
    return this.name;
  }
  
  public Game<?> getGame() {
    return this.game;
  }
  
  public boolean isAlive() {
    return !this.members.isEmpty();
  }
  
  public boolean canJoin() {
    return this.canJoin(1);
  }
  
  public boolean canJoin(int players) {
    return (this.members.size() + players) <= this.size;
  }
  
  public boolean hasMember(Player player) {
    return this.members.contains(player.getUniqueId());
  }
  
  public int getTeamSize() {
    return this.size;
  }
  
  public Location getLocation() {
    return BukkitUtils.deserializeLocation(this.location);
  }
  
  public List<Player> listPlayers() {
    return this.members.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
  }
}
