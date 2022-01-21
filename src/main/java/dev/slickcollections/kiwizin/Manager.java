package dev.slickcollections.kiwizin;

import dev.slickcollections.kiwizin.bungee.Bungee;
import dev.slickcollections.kiwizin.libraries.profile.Mojang;
import dev.slickcollections.kiwizin.player.fake.FakeManager;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.reflection.Accessors;
import dev.slickcollections.kiwizin.reflection.acessors.MethodAccessor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import com.mojang.authlib.properties.Property;

public class Manager {
  
  public static boolean BUNGEE;
  
  private static Object PROXY_SERVER;
  
  private static MethodAccessor GET_NAME;
  private static MethodAccessor GET_PLAYER;
  private static MethodAccessor GET_SPIGOT;
  private static MethodAccessor HAS_PERMISSION;
  private static MethodAccessor SEND_MESSAGE;
  private static MethodAccessor SEND_MESSAGE_COMPONENTS;
  
  private static MethodAccessor IS_FAKE;
  private static MethodAccessor GET_CURRENT;
  private static MethodAccessor GET_FAKE;
  private static MethodAccessor GET_FAKE_ROLE;
  
  static {
    try {
      Class<?> proxyServer = Class.forName("net.md_5.bungee.api.ProxyServer");
      Class<?> proxiedPlayer = Class.forName("net.md_5.bungee.api.connection.ProxiedPlayer");
      Class<?> bungeeMain = Class.forName("dev.slickcollections.kiwizin.bungee.Bungee");
      PROXY_SERVER = Accessors.getMethod(proxyServer, "getInstance").invoke(null);
      GET_NAME = Accessors.getMethod(proxiedPlayer, "getName");
      GET_PLAYER = Accessors.getMethod(proxyServer, "getPlayer", String.class);
      HAS_PERMISSION = Accessors.getMethod(proxiedPlayer, "hasPermission", String.class);
      SEND_MESSAGE_COMPONENTS = Accessors.getMethod(proxiedPlayer, "sendMessage", BaseComponent[].class);
      IS_FAKE = Accessors.getMethod(bungeeMain, "isFake", String.class);
      GET_CURRENT = Accessors.getMethod(bungeeMain, "getCurrent", String.class);
      GET_FAKE = Accessors.getMethod(bungeeMain, "getFake", String.class);
      GET_FAKE_ROLE = Accessors.getMethod(bungeeMain, "getRole", String.class);
      BUNGEE = true;
    } catch (ClassNotFoundException ignore) {
      try {
        Class<?> player = Class.forName("org.bukkit.entity.Player");
        Class<?> spigot = Class.forName("org.bukkit.entity.Player$Spigot");
        Class<?> fakeManager = Class.forName("dev.slickcollections.kiwizin.player.fake.FakeManager");
        GET_NAME = Accessors.getMethod(player, "getName");
        GET_PLAYER = Accessors.getMethod(Class.forName("dev.slickcollections.kiwizin.player.Profile"), "findCached", String.class);
        HAS_PERMISSION = Accessors.getMethod(player, "hasPermission", String.class);
        SEND_MESSAGE = Accessors.getMethod(player, "sendMessage", String.class);
        GET_SPIGOT = Accessors.getMethod(player, "spigot");
        SEND_MESSAGE_COMPONENTS = Accessors.getMethod(spigot, "sendMessage", BaseComponent[].class);
        IS_FAKE = Accessors.getMethod(fakeManager, "isFake", String.class);
        GET_CURRENT = Accessors.getMethod(fakeManager, "getCurrent", String.class);
        GET_FAKE = Accessors.getMethod(fakeManager, "getFake", String.class);
        GET_FAKE_ROLE = Accessors.getMethod(fakeManager, "getRole", String.class);
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
      }
    }
  }
  
  
  public static String getSkin(String player, String type) {
    try {
      String id = Mojang.getUUID(player);
      if (id != null) {
        String textures = Mojang.getSkinProperty(id);
        if (textures != null) {
          return type.equalsIgnoreCase("value") ? textures.split(" : ")[1] : textures.split(" : ")[2];
        }
      }
    } catch (Exception ignored) {}

    String defaultSkin = BUNGEE ? Bungee.ALEX : FakeManager.ALEX;
    return (type.equalsIgnoreCase("value") ? defaultSkin.split(":")[0] : defaultSkin.split(":")[1]);
  }

  public static String getSkin(String player) {
    try {
      String id = Mojang.getUUID(player);
      if (id != null) {
        String textures = Mojang.getSkinProperty(id);
        if (textures != null) {
          return textures.split(" : ")[1] + ":" + textures.split(" : ")[2];
        }
      }
    } catch (Exception ignore) {}

    return Bungee.ALEX;
  }

  public static Property getSkinProperty(String player) {
    try {
      String id = Mojang.getUUID(player);
      if (id != null) {
        String textures = Mojang.getSkinProperty(id);
        if (textures != null) {
          return new Property(textures.split(" : ")[0], textures.split(" : ")[1], textures.split(" : ")[2]);
        }
      }
    } catch (Exception ignore) {}

    return null;
  }
  
  public static void sendMessage(Object player, String message) {
    if (BUNGEE) {
      sendMessage(player, TextComponent.fromLegacyText(message));
      return;
    }
    
    SEND_MESSAGE.invoke(player, message);
  }
  
  public static void sendMessage(Object player, BaseComponent... components) {
    SEND_MESSAGE_COMPONENTS.invoke(BUNGEE ? player : GET_SPIGOT.invoke(player), new Object[]{components});
  }
  
  public static String getName(Object player) {
    return (String) GET_NAME.invoke(player);
  }
  
  public static Object getPlayer(String name) {
    return GET_PLAYER.invoke(BUNGEE ? PROXY_SERVER : null, name);
  }
  
  public static String getCurrent(String playerName) {
    return (String) GET_CURRENT.invoke(null, playerName);
  }
  
  public static String getFake(String playerName) {
    return (String) GET_FAKE.invoke(null, playerName);
  }
  
  public static Role getFakeRole(String playerName) {
    return (Role) GET_FAKE_ROLE.invoke(null, playerName);
  }
  
  public static boolean hasPermission(Object player, String permission) {
    return (boolean) HAS_PERMISSION.invoke(player, permission);
  }
  
  public static boolean isFake(String playerName) {
    return (boolean) IS_FAKE.invoke(null, playerName);
  }
}
