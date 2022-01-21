package dev.slickcollections.kiwizin.player.vanish;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.nms.NMS;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Vanish {

  public static List<String> isVanish = new ArrayList<>();

  public static void setup() {
    Bukkit.getScheduler().runTaskTimer(Core.getInstance(), () -> {
      Bukkit.getOnlinePlayers().stream().filter(player -> isVanish.contains(player.getName()))
          .forEach(player -> {
            NMS.sendActionBar(player, "§aVocê está invisível para os outros jogadores!");
            Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasPermission("kcore.cmd.vanish"))
                .forEach(p -> p.hidePlayer(player));
          });
    }, 1L, 1L);
  }

}
