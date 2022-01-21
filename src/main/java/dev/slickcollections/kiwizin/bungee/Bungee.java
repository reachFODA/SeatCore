package dev.slickcollections.kiwizin.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slickcollections.kiwizin.bungee.cmd.Commands;
import dev.slickcollections.kiwizin.bungee.listener.Listeners;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

public class Bungee extends Plugin {
  
  public static final String STEVE =
      "ewogICJ0aW1lc3RhbXAiIDogMTU4Nzc0NTY0NTA2NCwKICAicHJvZmlsZUlkIiA6ICJlNzkzYjJjYTdhMmY0MTI2YTA5ODA5MmQ3Yzk5NDE3YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVfSG9zdGVyX01hbiIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ZDNiMDZjMzg1MDRmZmMwMjI5Yjk0OTIxNDdjNjlmY2Y1OWZkMmVkNzg4NWY3ODUwMjE1MmY3N2I0ZDUwZGUxIgogICAgfQogIH0KfQ==:m4AHOr3btZjX3Rlxkwb5GMf69ZUo60XgFtwpADk92DgX1zz+ZOns+KejAKNpfVZOxRAVpSWwU8+ZNgiEvOdgyTFEW4yVXthQSdBYsKGtpifxOTb8YEXznmq+yVfA1iWZx2P72TbTmbZgG/TyOViMvyqUQsVmaZDCSW/M+ImDTmzrB3KrRW25XY9vaWshNvsaVH8SfrIOm3twtiLc7jRf+sipyxWcbFsw/Kh+6GyCKgID4tgTsydu5nhthm9A5Sa1ZI8LeySSFLzU5VirZeT3LvybHkikART/28sDaTs66N2cjFDNcdtjpWb4y0G9aLdwcWdx8zoYlVXcSWGW5aAFIDLKngtadHxRWnhryydz6YrlrBMflj4s6Qf9meIPI18J6eGWnBC8fhSwsfsJCEq6SKtkeQIHZ9g0sFfqt2YLG3CM6ZOHz2pWedCFUlokqr824XRB/h9FCJIRPIR6kpOK8barZTWwbL9/1lcjwspQ+7+rVHrZD+sgFavQvKyucQqE+IXL7Md5qyC5CYb2WMkXAhjzHp5EUyRq5FiaO6iok93gi6reh5N3ojuvWb1o1cOAwSf4IEaAbc7ej5aCDW5hteZDuVgLvBjPlbSfW9OmA8lbvxxgXR2fUwyfycUVFZUZbtgWzRIjKMOyfgRq5YFY9hhAb3BEAMHeEPqXoSPF5/A=";
  public static final String ALEX =
      "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==:W60UUuAYlWfLFt5Ay3Lvd/CGUbKuuU8+HTtN/cZLhc0BC22XNgbY1btTite7ZtBUGiZyFOhYqQi+LxVWrdjKEAdHCSYWpCRMFhB1m0zEfu78yg4XMcFmd1v7y9ZfS45b3pLAJ463YyjDaT64kkeUkP6BUmgsTA2iIWvM33k6Tj3OAM39kypFSuH+UEpkx603XtxratD+pBjUCUvWyj2DMxwnwclP/uACyh0ZVrI7rC5xJn4jSura+5J2/j6Z/I7lMBBGLESt7+pGn/3/kArDE/1RShOvm5eYKqrTMRfK4n3yd1U1DRsMzxkU2AdlCrv1swT4o+Cq8zMI97CF/xyqk8z2L98HKlzLjtvXIE6ogljyHc9YsfU9XhHwZ7SKXRNkmHswOgYIQCSa1RdLHtlVjN9UdUyUoQIIO2AWPzdKseKJJhXwqKJ7lzfAtStErRzDjmjr7ld/5tFd3TTQZ8yiq3D6aRLRUnOMTr7kFOycPOPhOeZQlTjJ6SH3PWFsdtMMQsGzb2vSukkXvJXFVUM0TcwRZlqT5MFHyKBBPprIt0wVN6MmSKc8m5kdk7ZBU2ICDs/9Cd/fyzAIRDu3Kzm7egbAVK9zc1kXwGzowUkGGy1XvZxyRS5jF1zu6KzVgaXOGcrOLH4z/OHzxvbyW22/UwahWGN7MD4j37iJ7gjZDrk=";
  private static Bungee instance;
  private static final Map<String, String> fakeNames = new HashMap<>();
  private static final Map<String, Role> fakeRoles = new HashMap<>();
  private static final Map<String, String> fakeSkins = new HashMap<>();
  private static List<String> randoms;
  private Configuration config;
  private Configuration utils;
  private Configuration roles;
  
  public Bungee() {
    instance = this;
  }
  
  public static Bungee getInstance() {
    return instance;
  }

  public static void sendRole(ProxiedPlayer player, String sound) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("FAKE_BOOK");
    out.writeUTF(player.getName());
    if (sound != null) {
      out.writeUTF(sound);
    }
    player.getServer().sendData("kCore", out.toByteArray());
  }

  public static void sendSkin(ProxiedPlayer player, String roleName, String sound) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("FAKE_BOOK2");
    out.writeUTF(player.getName());
    out.writeUTF(roleName);
    out.writeUTF(sound);
    player.getServer().sendData("kCore", out.toByteArray());
  }

  public static void applyFake(ProxiedPlayer player, String fakeName, String role, String skin) {
    player.disconnect(TextComponent.fromLegacyText(StringUtils.formatColors(getInstance().getConfig().getString("fake.kick-apply")).replace("\\n", "\n")));
    fakeNames.put(player.getName(), fakeName);
    fakeRoles.put(player.getName(), Role.getRoleByName(role));
    fakeSkins.put(player.getName(), skin);
  }

  public static void removeFake(ProxiedPlayer player) {
    player.disconnect(TextComponent.fromLegacyText(StringUtils.formatColors(getInstance().getConfig().getString("fake.kick-remove")).replace("\\n", "\n")));
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
    return !fakeNames.containsKey(name) && !fakeNames.containsValue(name) && getInstance().getProxy().getPlayer(name) == null;
  }
  
  public static List<String> listNicked() {
    return new ArrayList<>(fakeNames.keySet());
  }
  
  public static List<String> getRandomNicks() {
    if (randoms == null) {
      randoms = getInstance().getConfig().getStringList("fake.randoms");
    }
    
    return randoms;
  }
  
  public static boolean isFakeRole(String roleName) {
    return getInstance().getConfig().getStringList("fake.role").stream().anyMatch(role -> role.equalsIgnoreCase(roleName));
  }
  
  /**
   * Copia um arquivo a partir de um InputStream.
   *
   * @param input O input para ser copiado.
   * @param out   O arquivo destinario.
   */
  public static void copyFile(InputStream input, File out) {
    FileOutputStream ou = null;
    try {
      ou = new FileOutputStream(out);
      byte[] buff = new byte[1024];
      int len;
      while ((len = input.read(buff)) > 0) {
        ou.write(buff, 0, len);
      }
    } catch (IOException ex) {
      getInstance().getLogger().log(Level.WARNING, "Failed at copy file " + out.getName() + "!", ex);
    } finally {
      try {
        if (ou != null) {
          ou.close();
        }
        if (input != null) {
          input.close();
        }
      } catch (IOException ignore) {
      }
    }
  }
  
  @Override
  public void onEnable() {
    saveDefaultConfig();
    
    Database.setupDatabase(
        config.getString("database.tipo"),
        config.getString("database.mysql.host"),
        config.getString("database.mysql.porta"),
        config.getString("database.mysql.nome"),
        config.getString("database.mysql.usuario"),
        config.getString("database.mysql.senha"),
        config.getBoolean("database.mysql.hikari", false),
        config.getBoolean("database.mysql.mariadb", false),
        config.getString("database.mongodb.url", "")
    );
    
    setupRoles();
    Commands.setupCommands();
    getProxy().getPluginManager().registerListener(this, new Listeners());
    getProxy().registerChannel("kCore");
    
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void onDisable() {
    this.getLogger().info("O plugin foi desativado.");
  }
  
  public void saveDefaultConfig() {
    for (String fileName : new String[]{"config", "roles", "utils"}) {
      File file = new File("plugins/kCore/" + fileName + ".yml");
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        copyFile(Bungee.getInstance().getResourceAsStream(fileName + ".yml"), file);
      }
      
      try {
        if (fileName.equals("config")) {
          this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } else if (fileName.equals("utils")) {
          this.utils = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } else {
          this.roles = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        }
      } catch (IOException ex) {
        this.getLogger().log(Level.WARNING, "Cannot load " + fileName + ".yml: ", ex);
      }
    }
  }
  
  public Configuration getConfig() {
    return utils;
  }
  
  private void setupRoles() {
    try {
      if (utils.get("fake.role") instanceof String) {
        utils.set("fake.role", Arrays.asList(utils.getString("fake.role")));
        YamlConfiguration.getProvider(YamlConfiguration.class).save(utils, new File("plugins/kCore/utils.yml"));
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    
    for (String key : roles.getSection("roles").getKeys()) {
      String name = roles.getString("roles." + key + ".name");
      String prefix = roles.getString("roles." + key + ".prefix");
      String permission = roles.getString("roles." + key + ".permission");
      boolean broadcast = roles.getBoolean("roles." + key + ".broadcast", true);
      boolean alwaysVisible = roles.getBoolean("roles." + key + ".alwaysvisible", false);
      
      Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast));
    }
    
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Membro", "&7", "", false, false));
    }
  }
}
