package dev.slickcollections.kiwizin.bungee.cmd;

import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class OnlineCommand extends Commands {

  public OnlineCommand() { super("online"); }

  @Override
  public void perform(CommandSender sender, String[] args) {
    sender.sendMessage(TextComponent.fromLegacyText(""));
    sender.sendMessage(TextComponent.fromLegacyText("§aOnlines:"));
    BungeeCord.getInstance().getServers().forEach((key, value) -> {
      sender.sendMessage(TextComponent.fromLegacyText("§f➟ " + StringUtils.capitalise(key.toLowerCase()) + ": §7" + value.getPlayers().size() +
          (sender instanceof ProxiedPlayer && ((ProxiedPlayer) sender).getServer().getInfo().equals(value) ? " §e(Você está aqui.)" : "")));
    });
    sender.sendMessage(TextComponent.fromLegacyText(""));
  }
}
