package dev.slickcollections.kiwizin.bungee.cmd;

import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.bungee.party.BungeeParty;
import dev.slickcollections.kiwizin.bungee.party.BungeePartyManager;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

import static dev.slickcollections.kiwizin.party.PartyRole.LEADER;

public class PartyCommand extends Commands {
  
  public PartyCommand() {
    super("party");
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }
    
    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(
          " \n§6/p [mensagem] §f- §7Comunicar-se com os membros.\n§6/party abrir §f- §7Tornar a party pública.\n§6/party fechar §f- §7Tornar a party privada.\n§6/party entrar [jogador] §f- §7Entrar em uma party pública.\n§6/party aceitar [jogador] §f- §7Aceitar uma solicitação.\n§6/party ajuda §f- §7Mostrar essa mensagem de ajuda.\n§6/party convidar [jogador] §f- §7Convidar um jogador.\n§6/party deletar §f- §7Deletar a party.\n§6/party expulsar [jogador] §f- §7Expulsar um membro.\n§6/party info §f- §7Informações da sua Party.\n§6/party negar [jogador] §f- §7Negar uma solicitação.\n§6/party sair §f- §7Sair da Party.\n§6/party transferir [jogador] §f- §7Transferir a Party para outro membro.\n "));
      return;
    }
    
    String action = args[0];
    if (action.equalsIgnoreCase("abrir")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return;
      }
      
      if (party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cSua party já é pública."));
        return;
      }
      
      party.setIsOpen(true);
      player.sendMessage(TextComponent.fromLegacyText("§aVocê abriu a party para qualquer jogador."));
    } else if (action.equalsIgnoreCase("fechar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return;
      }
      
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cSua party já é privada."));
        return;
      }
      
      party.setIsOpen(false);
      player.sendMessage(TextComponent.fromLegacyText("§cVocê fechou a party para apenas convidados."));
    } else if (action.equalsIgnoreCase("entrar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party entrar [jogador]"));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode entrar na party de você mesmo."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
        return;
      }
      
      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
        return;
      }
      
      target = party.getName(target);
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está fechada apenas para convidados."));
        return;
      }
      
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está lotada."));
        return;
      }
      
      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Role.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("aceitar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party aceitar [jogador]"));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode aceitar convites de você mesmo."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
        return;
      }
      
      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
        return;
      }
      
      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para Party."));
        return;
      }
      
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está lotada."));
        return;
      }
      
      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Role.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("ajuda")) {
      player.sendMessage(TextComponent.fromLegacyText(
          " \n§6/p [mensagem] §f- §7Comunicar-se com os membros.\n§6/party abrir §f- §7Tornar a party pública.\n§6/party fechar §f- §7Tornar a party privada.\n§6/party entrar [jogador] §f- §7Entrar em uma party pública.\n§6/party aceitar [jogador] §f- §7Aceitar uma solicitação.\n§6/party ajuda §f- §7Mostrar essa mensagem de ajuda.\n§6/party convidar [jogador] §f- §7Convidar um jogador.\n§6/party deletar §f- §7Deletar a party.\n§6/party expulsar [jogador] §f- §7Expulsar um membro.\n§6/party info §f- §7Informações da sua Party.\n§6/party negar [jogador] §f- §7Negar uma solicitação.\n§6/party sair §f- §7Sair da Party.\n§6/party transferir [jogador] §f- §7Transferir a Party para outro membro.\n "));
    } else if (action.equalsIgnoreCase("puxar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return;
      }
      
      party.summonMembers(player.getServer().getInfo());
      player.sendMessage(TextComponent.fromLegacyText("§aVocê puxou todos os jogadores para o seu servidor."));
    } else if (action.equalsIgnoreCase("deletar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return;
      }
      
      party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §adeletou a Party!\n ", true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText("§aVocê deletou a Party."));
    } else if (action.equalsIgnoreCase("expulsar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party expulsar [jogador]"));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é um Líder de Party."));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode se expulsar."));
        return;
      }
      
      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
        return;
      }
      
      target = party.getName(target);
      party.kick(target);
      party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §aexpulsou " + Role.getPrefixed(target) + " §ada Party!\n ");
    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }
      
      List<String> members = party.listMembers().stream().filter(pp -> pp.getRole() != LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
      player.sendMessage(TextComponent.fromLegacyText(
          " \n§6Líder: " + Role.getPrefixed(party.getLeader()) + "\n§6Pública: " + (party.isOpen() ? "§aSim" : "§cNão") + "\n§6Limite de Membros: §f" + party.listMembers()
              .size() + "/" + party.getSlots() + "\n§6Membros: " + StringUtils.join(members, "§7, ") + "\n "));
    } else if (action.equalsIgnoreCase("negar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party negar [jogador]"));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode negar convites de você mesmo."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
        return;
      }
      
      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
        return;
      }
      
      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para Party."));
        return;
      }
      
      party.reject(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê negou o convite de Party de " + Role.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("sair")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }
      
      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§aVocê saiu da Party!"));
    } else if (action.equalsIgnoreCase("transferir")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party transferir [jogador]"));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é um Líder de Party."));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode transferir a Party para você mesmo."));
        return;
      }
      
      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
        return;
      }
      
      target = party.getName(target);
      party.transfer(target);
      party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §atransferiu a liderança da Party para " + Role.getPrefixed(target) + "§a!\n ");
    } else {
      if (action.equalsIgnoreCase("convidar")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party convidar [jogador]"));
          return;
        }
        
        action = args[1];
      }
      
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
        return;
      }
      
      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode enviar convites para você mesmo."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cApenas o Líder da Party pode enviar convites!"));
        return;
      }
      
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA sua Party está lotada."));
        return;
      }
      
      if (party.isInvited(action)) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já enviou um convite para " + Manager.getCurrent(action) + "."));
        return;
      }
      
      if (BungeePartyManager.getMemberParty(action) != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(action) + " já pertence a uma Party."));
        return;
      }
      
      party.invite(target);
      player.sendMessage(
          TextComponent.fromLegacyText(" \n" + Role.getPrefixed(action) + " §afoi convidado para a Party. Ele tem 60 segundos para aceitar ou negar esta solicitação.\n "));
    }
  }
}
