package dev.slickcollections.kiwizin.bungee.cmd;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand extends Commands {

  public StaffChatCommand() { super("sc", "staffchat"); }

  public static List<String> IGNORE = new ArrayList<>();

  @Override
  public void perform(CommandSender sender, String[] args)  {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("kcore.cmd.staffchat")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /sc [mensagem] ou /sc [ativar/desativar]"));
      return;
    }
    String message = args[0];
    if (message.equalsIgnoreCase("ativar")) {
      player.sendMessage(TextComponent.fromLegacyText("§aStaffChat ativado."));
      IGNORE.remove(player.getName());
      return;
    } else if (message.equalsIgnoreCase("desativar")) {
      player.sendMessage(TextComponent.fromLegacyText("§cStaffChat desativado."));
      IGNORE.add(player.getName());
      return;
    }
    String format = StringUtils.formatColors(StringUtils.join(args, " "));
    BungeeCord.getInstance().getPlayers().stream()
        .filter(pplayer -> pplayer.hasPermission("kcore.cmd.staffchat") && !IGNORE.contains(pplayer.getName()))
        .forEach(pplayer -> {
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF("STAFF_BAR");
          out.writeUTF(pplayer.getName());
          pplayer.getServer().sendData("kCore", out.toByteArray());

          pplayer.sendMessage(TextComponent.fromLegacyText("§3[SC] §7[" + StringUtils.capitalise(player.getServer().getInfo().getName().toLowerCase())
              + "] §7" + Role.getPrefixed(player.getName()) + "§f: " + format));
        });
  }

}
