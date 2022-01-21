package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.player.gamemode.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand extends Commands {

  public GameModeCommand() {
    super("gamemode", "gm");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }
    Player player = (Player) sender;
    if (!player.hasPermission("kcore.cmd.gamemode")) {
      player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
      return;
    }
    if (args.length == 0) {
      player.sendMessage("§cUtilize /gamemode [player] <modo>");
      return;
    }
    if (args.length > 1) {
      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
        player.sendMessage("§cJogador não encontrado.");
        return;
      }
      GameMode.changeMode(player, target, args[1]);
      return;
    }
    GameMode.changeMode(player, player, args[0]);
  }
}
