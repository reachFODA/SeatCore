package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.cash.CashException;
import dev.slickcollections.kiwizin.cash.CashManager;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand extends Commands {

  public CashCommand() {
    super("cash");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (args.length == 0) {
      if (sender instanceof Player) {
        sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
        return;
      }

      sender.sendMessage(
          " \n§6/cash set [jogador] [quantia] §f- §7Setar o cash do jogador.\n§6/cash add [jogador] [quantia] §f- §7Dar cash para um jogador.\n§6/cash remove [jogador] [quantia] §f- §7Remover o cash de um jogador.\n ");
      return;
    }

    if (!sender.hasPermission("kcore.cmd.cash")) {
      sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
      return;
    }

    String action = args[0];
    if (!action.equalsIgnoreCase("set") && !action.equalsIgnoreCase("add") && !action.equalsIgnoreCase("remove")) {
      sender.sendMessage(
          " \n§6/cash set [jogador] [quantia] §f- §7Setar o cash do jogador.\n§6/cash add [jogador] [quantia] §f- §7Dar cash para um jogador.\n§6/cash remove [jogador] [quantia] §f- §7Remover o cash de um jogador.\n ");
      return;
    }

    if (args.length <= 2) {
      sender.sendMessage("§cUtilize /cash " + action + " [jogador] [quantia]");
      return;
    }

    long amount;
    try {
      if (args[2].startsWith("-")) {
        throw new NumberFormatException();
      }

      amount = Long.parseLong(args[2]);
    } catch (NumberFormatException ex) {
      sender.sendMessage("§cUtilize números válidos e positivos.");
      return;
    }

    try {
      switch (action.toLowerCase()) {
        case "set":
          CashManager.setCash(args[1], amount);
          sender.sendMessage("§aVocê setou o Cash do(a) " + Role.getColored(args[1]) + " §apara §b" + StringUtils.formatNumber(amount) + "§a.");
          break;
        case "add":
          CashManager.addCash(args[1], amount);
          sender.sendMessage("§aVocê adicionou §b" + StringUtils.formatNumber(amount) + " §ade Cash para o(a) " + Role.getColored(args[1]) + "§a.");
          break;
        case "remove":
          CashManager.removeCash(args[1], amount);
          sender.sendMessage("§aVocê removeu §b" + StringUtils.formatNumber(amount) + " §ade Cash do(a) " + Role.getColored(args[1]) + "§a.");
      }
    } catch (CashException ex) {
      sender.sendMessage("§cO usuário precisa estar conectado.");
    }
  }
}
