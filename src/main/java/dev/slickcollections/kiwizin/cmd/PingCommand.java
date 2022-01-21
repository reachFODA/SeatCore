package dev.slickcollections.kiwizin.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand extends Commands {

  public PingCommand() {
    super("ping");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }
    Player player = (Player) sender;
    player.sendMessage("§aSeu ping é " + ((CraftPlayer) sender).getHandle().ping + "ms.");
  }

}
