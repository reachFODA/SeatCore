package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends Commands {

  public FlyCommand() {
    super("fly", "voar");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }
    Player player = (Player) sender;
    Profile profile = Profile.getProfile(player.getName());
    if (!player.hasPermission("kcore.fly")) {
      player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
      return;
    }
    if (profile.playingGame()) {
      player.sendMessage("§cVocê não pode alterar o modo voar em jogo.");
      return;
    }

    player.setAllowFlight(!player.getAllowFlight());
    player.sendMessage("§aModo voar " + (player.getAllowFlight() ? "ativado." : "desativado."));
  }
}
