package dev.slickcollections.kiwizin.player.fake;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.libraries.profile.Mojang;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FakeManager {
  
  public static final String STEVE =
          "ewogICJ0aW1lc3RhbXAiIDogMTU4Nzc0NTY0NTA2NCwKICAicHJvZmlsZUlkIiA6ICJlNzkzYjJjYTdhMmY0MTI2YTA5ODA5MmQ3Yzk5NDE3YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVfSG9zdGVyX01hbiIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ZDNiMDZjMzg1MDRmZmMwMjI5Yjk0OTIxNDdjNjlmY2Y1OWZkMmVkNzg4NWY3ODUwMjE1MmY3N2I0ZDUwZGUxIgogICAgfQogIH0KfQ==:m4AHOr3btZjX3Rlxkwb5GMf69ZUo60XgFtwpADk92DgX1zz+ZOns+KejAKNpfVZOxRAVpSWwU8+ZNgiEvOdgyTFEW4yVXthQSdBYsKGtpifxOTb8YEXznmq+yVfA1iWZx2P72TbTmbZgG/TyOViMvyqUQsVmaZDCSW/M+ImDTmzrB3KrRW25XY9vaWshNvsaVH8SfrIOm3twtiLc7jRf+sipyxWcbFsw/Kh+6GyCKgID4tgTsydu5nhthm9A5Sa1ZI8LeySSFLzU5VirZeT3LvybHkikART/28sDaTs66N2cjFDNcdtjpWb4y0G9aLdwcWdx8zoYlVXcSWGW5aAFIDLKngtadHxRWnhryydz6YrlrBMflj4s6Qf9meIPI18J6eGWnBC8fhSwsfsJCEq6SKtkeQIHZ9g0sFfqt2YLG3CM6ZOHz2pWedCFUlokqr824XRB/h9FCJIRPIR6kpOK8barZTWwbL9/1lcjwspQ+7+rVHrZD+sgFavQvKyucQqE+IXL7Md5qyC5CYb2WMkXAhjzHp5EUyRq5FiaO6iok93gi6reh5N3ojuvWb1o1cOAwSf4IEaAbc7ej5aCDW5hteZDuVgLvBjPlbSfW9OmA8lbvxxgXR2fUwyfycUVFZUZbtgWzRIjKMOyfgRq5YFY9hhAb3BEAMHeEPqXoSPF5/A=";
  public static final String ALEX =
      "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==:W60UUuAYlWfLFt5Ay3Lvd/CGUbKuuU8+HTtN/cZLhc0BC22XNgbY1btTite7ZtBUGiZyFOhYqQi+LxVWrdjKEAdHCSYWpCRMFhB1m0zEfu78yg4XMcFmd1v7y9ZfS45b3pLAJ463YyjDaT64kkeUkP6BUmgsTA2iIWvM33k6Tj3OAM39kypFSuH+UEpkx603XtxratD+pBjUCUvWyj2DMxwnwclP/uACyh0ZVrI7rC5xJn4jSura+5J2/j6Z/I7lMBBGLESt7+pGn/3/kArDE/1RShOvm5eYKqrTMRfK4n3yd1U1DRsMzxkU2AdlCrv1swT4o+Cq8zMI97CF/xyqk8z2L98HKlzLjtvXIE6ogljyHc9YsfU9XhHwZ7SKXRNkmHswOgYIQCSa1RdLHtlVjN9UdUyUoQIIO2AWPzdKseKJJhXwqKJ7lzfAtStErRzDjmjr7ld/5tFd3TTQZ8yiq3D6aRLRUnOMTr7kFOycPOPhOeZQlTjJ6SH3PWFsdtMMQsGzb2vSukkXvJXFVUM0TcwRZlqT5MFHyKBBPprIt0wVN6MmSKc8m5kdk7ZBU2ICDs/9Cd/fyzAIRDu3Kzm7egbAVK9zc1kXwGzowUkGGy1XvZxyRS5jF1zu6KzVgaXOGcrOLH4z/OHzxvbyW22/UwahWGN7MD4j37iJ7gjZDrk=";
  
  private static final KConfig CONFIG = Core.getInstance().getConfig("utils");
  private static final Pattern REAL_PATTERN = Pattern.compile("(?i)kcorefakereal:\\w*"), NOT_CHANGE_PATTERN = Pattern.compile("(?i)kcorenotchange:\\w*");
  
  public static Map<String, String> fakeNames = new HashMap<>();
  public static Map<String, Role> fakeRoles = new HashMap<>();
  public static Map<String, String> fakeSkins = new HashMap<>();
  
  private static TextComponent FAKE_ROLES;
  private static TextComponent FAKE_SKINS;
  
  private static List<String> randoms;
  private static Boolean bungeeSide;
  
  public static void setupFake() {
    if (CONFIG.get("fake.role") instanceof String) {
      CONFIG.set("fake.role", Arrays.asList(CONFIG.getString("fake.role")));
    }
    FAKE_ROLES = new TextComponent("");
    for (BaseComponent component : TextComponent.fromLegacyText("§5§lALTERAR NICKNAME\n \n§0Escolha o cargo que gostaria de utilizar enquanto está disfarçado:\n ")) {
      FAKE_ROLES.addExtra(component);
    }
    for (String roleName : CONFIG.getStringList("fake.role")) {
      Role role = Role.getRoleByName(roleName);
      if (role != null) {
        TextComponent component = new TextComponent("\n §0▪ " + role.getName());
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Seu nickname será exibido como '" + role.getPrefix() + "Nickname'§7.")));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fake " + roleName));
        FAKE_ROLES.addExtra(component);
      }
    }
    FAKE_SKINS = new TextComponent("");
    for (BaseComponent component : TextComponent.fromLegacyText("§5§lALTERAR NICKNAME\n \n§0Enquanto disfarçado, sua skin será alterada para ajudar a te camuflar.\n \n§0Escolha sua skin:\n ")) {
      FAKE_SKINS.addExtra(component);
    }
    TextComponent STEVE = new TextComponent("\n §0▪ §7Steve");
    STEVE.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Você irá obter a aparência de Steve.")));
    STEVE.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fake {role} steve"));
    TextComponent ALEX = new TextComponent("\n §0▪ §7Alex");
    ALEX.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Você irá obter a aparência da Alex.")));
    ALEX.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fake {role} alex"));
    TextComponent YOU = new TextComponent("\n §0▪ §7Você");
    YOU.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Você irá obter a sua aparência.")));
    YOU.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fake {role} you"));
    FAKE_SKINS.addExtra(STEVE);
    FAKE_SKINS.addExtra(YOU);
    FAKE_SKINS.addExtra(ALEX);
  }
  
  public static void sendRole(Player player) {
    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta meta = (BookMeta) book.getItemMeta();
    meta.setAuthor("Nys");
    meta.setTitle("Escolher cargo");
    book.setItemMeta(meta);
    book = BukkitUtils.setNBTList(book, "pages", Collections.singletonList(ComponentSerializer.toString(FAKE_ROLES)));
    BukkitUtils.openBook(player, book);
  }
  
  public static void sendSkin(Player player, String role) {
    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta meta = (BookMeta) book.getItemMeta();
    meta.setAuthor("Nys");
    meta.setTitle("Escolher cargo");
    book.setItemMeta(meta);
    book = BukkitUtils.setNBTList(book, "pages", Collections.singletonList(ComponentSerializer.toString(FAKE_SKINS).replace("{role}", role)));
    BukkitUtils.openBook(player, book);
  }
  
  public static void applyFake(Player player, String fakeName, String role, String skin) {
    if (!isBungeeSide()) {
      player.kickPlayer(StringUtils.formatColors(CONFIG.getString("fake.kick-apply")).replace("\\n", "\n"));
    }
    fakeNames.put(player.getName(), fakeName);
    fakeRoles.put(player.getName(), Role.getRoleByName(role));
    fakeSkins.put(player.getName(), skin);
  }
  
  public static void removeFake(Player player) {
    if (!isBungeeSide()) {
      player.kickPlayer(StringUtils.formatColors(CONFIG.getString("fake.kick-remove")).replace("\\n", "\n"));
    }
    fakeNames.remove(player.getName());
    fakeRoles.remove(player.getName());
    fakeSkins.remove(player.getName());
  }
  
  public static String getCurrent(String playerName) {
    return isFake(playerName) ? getFake(playerName) : playerName;
  }
  
  public static String getFake(String playerName) {
    return fakeNames.get(playerName);
  }
  
  public static Role getRole(String playerName) {
    return fakeRoles.getOrDefault(playerName, Role.getLastRole());
  }
  
  public static String getSkin(String playerName) {
    return fakeSkins.getOrDefault(playerName, STEVE);
  }
  
  public static boolean isFake(String playerName) {
    return fakeNames.containsKey(playerName);
  }
  
  public static boolean isUsable(String name) {
    return !fakeNames.containsKey(name) && !fakeNames.containsValue(name) && Bukkit.getPlayer(name) == null;
  }
  
  public static List<String> listNicked() {
    return new ArrayList<>(fakeNames.keySet());
  }
  
  public static List<String> getRandomNicks() {
    if (randoms == null) {
      randoms = CONFIG.getStringList("fake.randoms");
    }
    
    return randoms;
  }
  
  public static boolean isFakeRole(String roleName) {
    return CONFIG.getStringList("fake.role").stream().anyMatch(role -> role.equalsIgnoreCase(roleName));
  }
  
  public static boolean isBungeeSide() {
    if (bungeeSide == null) {
      bungeeSide = CONFIG.getBoolean("bungeecord");
    }
    
    return bungeeSide;
  }
  
  public static String replaceNickedChanges(String original) {
    String replaced = original;
    for (String name : listNicked()) {
      Matcher matcher = Pattern.compile("(?i)" + name).matcher(replaced);
      
      while (matcher.find()) {
        replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement("kcorenotchange:" + name));
      }
    }
    
    return replaced;
  }
  
  public static String replaceNickedPlayers(String original, boolean toFake) {
    String replaced = original;
    List<String> backup = new ArrayList<>();
    for (String name : listNicked()) {
      Matcher matcher = NOT_CHANGE_PATTERN.matcher(replaced);
      while (matcher.find()) {
        String found = matcher.group();
        backup.add(found.replace("kcorenotchange:", ""));
        replaced = replaced.replaceFirst(Pattern.quote(found), Matcher.quoteReplacement("kcorenotchange:" + (backup.size() - 1)));
      }
      
      matcher = Pattern.compile("(?i)" + (toFake ? name : getFake(name))).matcher(replaced);
      while (matcher.find()) {
        replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(toFake ? getFake(name) : name));
      }
    }
    
    Matcher matcher = REAL_PATTERN.matcher(replaced);
    while (matcher.find()) {
      String found = matcher.group();
      replaced = replaced.replaceFirst(Pattern.quote(found), Matcher.quoteReplacement(
          fakeNames.entrySet().stream().filter(entry -> entry.getValue().equals(found.replace("kcorefakereal:", ""))).map(Map.Entry::getKey).findFirst().orElse("")));
    }
    
    matcher = NOT_CHANGE_PATTERN.matcher(replaced);
    while (matcher.find()) {
      String found = matcher.group();
      replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(backup.get(Integer.parseInt(found.replace("kcorenotchange:", "")))));
    }
    
    backup.clear();
    return replaced;
  }
  
  public static WrappedGameProfile cloneProfile(WrappedGameProfile profile) {
    WrappedGameProfile gameProfile = profile.withName(getFake(profile.getName()));
    gameProfile.getProperties().clear();
    
    try {
      String id = Mojang.getUUID(gameProfile.getName());
      if (id != null) {
        String textures = Mojang.getSkinProperty(id);
        if (textures != null) {
          gameProfile.getProperties().put("textures", new WrappedSignedProperty(textures.split(" : ")[0], textures.split(" : ")[1], textures.split(" : ")[2]));
        }
      }
    } catch (Exception ignore) {
    }
    
    return gameProfile;
  }
}