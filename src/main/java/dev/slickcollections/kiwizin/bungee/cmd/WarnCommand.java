package dev.slickcollections.kiwizin.bungee.cmd;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WarnCommand extends Commands {

  public WarnCommand() { super("aviso", "avisar", "anunciar", "warn"); }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("kcore.cmd.warn")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /aviso [mensagem]"));
      return;
    }
    String format = StringUtils.formatColors(StringUtils.join(args, " "));
    BungeeCord.getInstance().getPlayers().forEach(pplayer -> {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("WARN_SOUND");
      out.writeUTF(pplayer.getName());
      pplayer.getServer().sendData("kCore", out.toByteArray());
      pplayer.sendMessage(TextComponent.fromLegacyText(""));
      pplayer.sendMessage(TextComponent.fromLegacyText("§7" + Role.getPrefixed(player.getName()) + "§f: " + format));
      pplayer.sendMessage(TextComponent.fromLegacyText(""));
    });
  }

}
