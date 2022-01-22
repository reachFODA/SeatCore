package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.menus.MenuSkins;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.Validator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SkinCommand extends Commands {

  public static Map<String, String> LIBRARY = new HashMap<>();

  static {
    LIBRARY.put("Nezuko", "Nezuko______Chan");
    LIBRARY.put("Tanjiro", "Tanjxro_");
    LIBRARY.put("Akame", "EgirlFxse");
    LIBRARY.put("Tatsumi", "Tatsuumiii");
    LIBRARY.put("Venom", "Venoms_GM");
    LIBRARY.put("Goku", "Oyo_Boyo");
    LIBRARY.put("Naruto", "Narutc");
    LIBRARY.put("HomemAranha", "700c");
    LIBRARY.put("HomemdeFerro", "6ixsauer");
    LIBRARY.put("Jiraya", "Zakharov");
    LIBRARY.put("Kakashi", "eZio789");
    LIBRARY.put("Obito", "sharinganek");
    LIBRARY.put("Mitsuki", "iXwin");
    LIBRARY.put("Todoroki ", "_Slm");
    LIBRARY.put("Midoriya", "LHIP");
    LIBRARY.put("All Might", "xG18x");
    LIBRARY.put("Zero Two", "Zer0_2");
    LIBRARY.put("Luffy", "Tlaoof");
    LIBRARY.put("Saitama", "BradyJonas");
    LIBRARY.put("Sakura", "MysticalFire128");
    LIBRARY.put("Sasuke", "SasukeUchiha");
    LIBRARY.put("Ranger Preto", "Geero0");
    LIBRARY.put("Ranger Amarelo", "Harryr_");
    LIBRARY.put("Ranger Branco", "crimb");
    LIBRARY.put("Ranger Verde", "nickloading");
    LIBRARY.put("Ranger Azul", "TewPaq");
    LIBRARY.put("Ranger Roxa", "blmsikecuh345");
    LIBRARY.put("Ranger Rosa", "Zyluh");
    LIBRARY.put("Ranger Vermelho", "imcheatinglul");

  }

  public SkinCommand() {
    super("skin", "skins");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }

    Player player = (Player) sender;
    Profile profile = Profile.getProfile(player.getName());
    if (args.length == 0) {
      new MenuSkins(profile);
      return;
    }

    String name = args[0];
    if (name.equalsIgnoreCase("ajuda")) {
      player.sendMessage("");
      player.sendMessage("§eAjuda");
      player.sendMessage("");
      player.sendMessage("§6/skin [nome] §f- §7Atualizar sua skin para a de um outro jogador.");
      player.sendMessage("§6/skin atualizar §f- §7Retornar para a skin da sua conta do Minecraft.");
      player.sendMessage("§6/skin ajuda §f- §7Exibe essa mensagem.");
      player.sendMessage("");
      return;
    }
    if (name.equalsIgnoreCase("library")) {
      if (args.length < 2) {
        player.sendMessage("§cUtilize /skin library [nome]");
        return;
      }

      String librarySkin = args[1];
      if (LIBRARY.containsKey(librarySkin)) {
        player.sendMessage("§aAtualizando sua skin...");
        if (profile.getSkinListContainer().getSkins().size() > 20) {
          player.sendMessage("§cVocê atingiu o limite máximo de skins.");
          return;
        }
        profile.getSkinListContainer().addSkin(LIBRARY.get(librarySkin));
        player.sendMessage("§aSua skin foi atualizada, relogue para ela aparecer.");
        profile.getSkinsContainer().setSkin(LIBRARY.get(librarySkin));
      }
      return;
    }

    if (name.equalsIgnoreCase("atualizar")) {
      player.sendMessage("§aAtualizando sua skin...");
      if (profile.getSkinListContainer().getSkins().size() > 20) {
        player.sendMessage("§cVocê atingiu o limite máximo de skins.");
        return;
      }
      profile.getSkinListContainer().addSkin(player.getName());
      player.sendMessage("§aSua skin foi atualizada, relogue para ela aparecer.");
      profile.getSkinsContainer().setSkin(player.getName());
      return;
    }

    if (!player.hasPermission("kcore.skins.update")) {
      player.sendMessage("§cVocê precisa ter algum plano VIP para executar este comando.");
      return;
    }

    if (!Validator.isValidUsername(name)) {
      player.sendMessage("§cUsuário inválido.");
      return;
    }

    player.sendMessage("§aAtualizando sua skin...");
    if (profile.getSkinListContainer().getSkins().size() > 20) {
      player.sendMessage("§cVocê atingiu o limite máximo de skins.");
      return;
    }
    profile.getSkinListContainer().addSkin(name);
    player.sendMessage("§aSua skin foi atualizada, relogue para ela aparecer.");
    profile.getSkinsContainer().setSkin(name);
  }
}
