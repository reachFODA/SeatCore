package dev.slickcollections.kiwizin.player.gamemode;

import org.bukkit.entity.Player;

public class GameMode {

  public static void changeMode(Player player, Player target, String mode) {
    GameModeType type = GameModeType.fromName(mode);
    if (type == null) {
      player.sendMessage("§cModo não encontrado.");
      return;
    }

    if (player != target) {
      player.sendMessage("§aVocê alterou o modo de jogo de " + target.getName() + " para " + type.name().toUpperCase() + ".");
      return;
    }
    target.setGameMode(org.bukkit.GameMode.valueOf(type.name()));
    target.sendMessage("§aSeu modo de jogo foi alterado para " + type.name().toUpperCase() + ".");
  }

}
