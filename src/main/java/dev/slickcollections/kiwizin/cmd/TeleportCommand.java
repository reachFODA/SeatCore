package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends Commands {

  public TeleportCommand() {
    super("tp", "teleport");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }
    Player player = (Player) sender;
    if (!player.hasPermission("kcore.cmd.teleport")) {
      player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
      return;
    }
    if (args.length == 0) {
      player.sendMessage("§cUtilize /tp <jogador> [jogador] ou /tp [jogador] <x> <y> <z>");
      return;
    }
    if (args.length == 1) {
      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
        player.sendMessage("§cJogador não encontrado.");
        return;
      }
      player.teleport(target);
      player.sendMessage("§aVocê foi teleportado para a localização do jogador " + Role.getColored(target.getName()) + "§a.");
    } else if (args.length == 2) {
      Player target = Bukkit.getPlayer(args[0]);
      Player targetTo = Bukkit.getPlayer(args[1]);
      if (targetTo == null || target == null) {
        player.sendMessage("§cJogador não encontrado.");
        return;
      }
      target.teleport(targetTo);
      target.sendMessage("§aVocê foi teleportado para a localização do jogador " + Role.getColored(targetTo.getName()) + "§a.");
    } else {
      if (args.length > 3) {
        try {
          double x = Double.parseDouble(args[1]);
          double y = Double.parseDouble(args[2]);
          double z = Double.parseDouble(args[3]);
          Player target = Bukkit.getPlayer(args[0]);
          if (target == null) {
            player.sendMessage("§cJogador não encontrado.");
            return;
          }
          target.teleport(new Location(player.getWorld(), x, y, z));
          target.sendMessage("§aVocê foi teleportado até: §6x: " + x + " y: " + y + " x: " + z + "§a.");
        } catch (NumberFormatException ex) {
          player.sendMessage("§cUtilize apenas números.");
        }
      } else {
        try {
          double x = Double.parseDouble(args[0]);
          double y = Double.parseDouble(args[1]);
          double z = Double.parseDouble(args[2]);
          player.teleport(new Location(player.getWorld(), x, y, z));
          player.sendMessage("§aVocê foi teleportado até: §6x: " + x + " y: " + y + " x: " + z + "§a.");
        } catch (NumberFormatException ex) {
          player.sendMessage("§cUtilize apenas números.");
        }
      }
    }
  }

}
