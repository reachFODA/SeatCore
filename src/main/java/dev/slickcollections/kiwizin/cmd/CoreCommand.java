package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.utils.SlickUpdater;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreCommand extends Commands {

  public CoreCommand() {
    super("kcore", "kc");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!player.hasPermission("kcore.admin")) {
        sender.sendMessage("§cVocê não possui permissão para utilizar este comando.");
        return;
      }

      if (args.length == 0) {
        player.sendMessage(" \n§a/kc converter §f- §7Converter seu Banco de Dados.\n ");
        return;
      }

      String action = args[0];
      if (action.equalsIgnoreCase("converter")) {
        player.sendMessage("§fBanco de Dados: " + Database.getInstance().getClass().getSimpleName().replace("Database", ""));
        Database.getInstance().convertDatabase(player);
      } else {
        player.sendMessage(" \n§a/kc converter §f- §7Converter seu Banco de Dados.\n ");
      }
    } else {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
    }
  }
}
