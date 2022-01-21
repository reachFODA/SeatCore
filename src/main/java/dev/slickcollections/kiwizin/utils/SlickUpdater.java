package dev.slickcollections.kiwizin.utils;

import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlickUpdater {
  
  public static SlickUpdater UPDATER;
  public boolean canDownload;
  private final KPlugin plugin;
  private final KLogger logger;
  private final int resourceId;
  
  public SlickUpdater(KPlugin plugin, int resourceId) {
    this.plugin = plugin;
    this.logger = ((KLogger) this.plugin.getLogger()).getModule("UPDATER");
    this.resourceId = resourceId;
  }
  
  public static String getVersion(int resourceId) {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.slickcollections.com.br/api/v1/plugin/" + resourceId).openConnection();
      connection.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
      return ((JSONObject) object.get("version")).get("id").toString();
    } catch (Exception ex) {
      return null;
    }
  }
  
  public void run() {
    this.logger.info("Procurando updates...");
    String latest = getVersion(this.resourceId);
    String current = this.plugin.getDescription().getVersion();
    if (latest == null) {
      logger.severe("Nao foi possivel buscar updates.");
    } else {
      int siteVersion = Integer.parseInt(latest.replace(".", ""));
      int pluginVersion = Integer.parseInt(current.replace(".", ""));
      
      if (pluginVersion >= siteVersion) {
        this.logger.info("Plugin se encontra em sua ultima versao.");
      } else {
        this.logger.warning("Encontramos um update. Utilize /kc atualizar para prosseguir.");
        UPDATER = this;
        this.canDownload = true;
      }
    }
  }
  
  public void downloadUpdate(Player player) {
    player.sendMessage("§aTentando baixar atualização...");
    try {
      File file = new File("plugins/kCore/update", "kCore.jar");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.slickcollections.com.br/download/kCore.jar").openConnection();
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      int max = connection.getContentLength();
      BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
      FileOutputStream fos = new FileOutputStream(file);
      BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
      int oneChar;
      int progress = 0;
      player.sendMessage("§aTamanho do arquivo: " + (max / 1024) + "kb");
      while ((oneChar = in.read()) != -1) {
        bout.write(oneChar);
        progress += 1;
        int percentage = (progress * 100) / max;
        if (max > 0 && progress % 1024 == 0) {
          NMS.sendActionBar(player, "§fBaixando " + file.getName() + " §7[§a" + StringUtils.repeat("█", percentage / 4) + "§8" + StringUtils.repeat("█", 25 - (percentage / 4)) + "§7]");
        }
      }
      
      NMS.sendActionBar(player, "§aAtualização baixada, pare o servidor para progredir.");
      in.close();
      bout.close();
    } catch (Exception ex) {
      NMS.sendActionBar(player, "§aNão foi possível baixar a atualização: " + ex.getMessage());
    }
  }
}
