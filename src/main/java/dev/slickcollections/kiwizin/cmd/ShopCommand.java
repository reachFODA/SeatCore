package dev.slickcollections.kiwizin.cmd;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends Commands {

  public ShopCommand() {
    super("loja", "shop");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }
    Player player = (Player) sender;
    player.closeInventory();
    TextComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText("\n §eClique ")) {
      component.addExtra(components);
    }
    TextComponent click = new TextComponent("AQUI");
    click.setColor(ChatColor.YELLOW);
    click.setBold(true);
    click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://loja.redeseat.com"));
    click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique aqui para abrir a loja.")));
    component.addExtra(click);
    for (BaseComponent components : TextComponent.fromLegacyText(" §epara abrir a loja do servidor.\n ")) {
      component.addExtra(components);
    }

    player.spigot().sendMessage(component);

  }

}
