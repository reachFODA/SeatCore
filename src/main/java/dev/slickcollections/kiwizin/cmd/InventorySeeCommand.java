package dev.slickcollections.kiwizin.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventorySeeCommand extends Commands {

  public InventorySeeCommand() {
    super("invsee", "inventorysee");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }
    Player player = (Player) sender;
    if (!player.hasPermission("kcore.cmd.invsee")) {
      player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
      return;
    }
    if (args.length == 0) {
      player.sendMessage("§cUtilize /invsee [jogador]");
      return;
    }
    Player target = Bukkit.getPlayerExact(args[0]);
    if (target == null) {
      player.sendMessage("§cJogador não encontrado.");
      return;
    }
    player.openInventory(target.getInventory());
  }

}
