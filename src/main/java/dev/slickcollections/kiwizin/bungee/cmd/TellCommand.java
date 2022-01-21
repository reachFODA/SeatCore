package dev.slickcollections.kiwizin.bungee.cmd;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class TellCommand extends Commands {

  public TellCommand() {
    super("tell");
  }

  public static Map<String, String> TELL_CACHE = new HashMap<>();

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length < 2) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /tell [jogador] [mensagem]"));
      return;
    }
    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
    if (target == null || !target.isConnected()) {
      player.sendMessage(TextComponent.fromLegacyText("§cJogador não encontrado."));
      return;
    }
    if (target.equals(player)) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode mandar mensagens privadas para sí mesmo."));
      return;
    }
    boolean canReceiveTell = Database.getInstance().getPreference(player.getName(), "pm", true);
    boolean canReceiveTellT = Database.getInstance().getPreference(target.getName(), "pm", true);
    if (!canReceiveTell) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode enviar mensagens privadas com as mensagens privadas desativadas."));
      return;
    }
    if (!canReceiveTellT) {
      player.sendMessage(TextComponent.fromLegacyText("§cEste usuário desativou as mensagens privadas."));
      return;
    }
    String message = StringUtils.join(args, 1, " ");
    if (player.hasPermission("kcore.tell.color")) {
      message = StringUtils.formatColors(message);
    }
    TELL_CACHE.put(player.getName(), target.getName());
    TELL_CACHE.put(target.getName(), player.getName());
    target.sendMessage(TextComponent.fromLegacyText("§8Mensagem de: " +
        Role.getColored(player.getName()) + "§8: §6" + message));
    player.sendMessage(TextComponent.fromLegacyText("§8Mensagem para: " + Role.getColored(target.getName()) + "§8: §6" + message));
  }
}
