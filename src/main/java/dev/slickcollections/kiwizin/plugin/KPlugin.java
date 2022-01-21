package dev.slickcollections.kiwizin.plugin;

import dev.slickcollections.kiwizin.plugin.config.FileUtils;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.plugin.config.KWriter;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.reflection.Accessors;
import dev.slickcollections.kiwizin.reflection.acessors.FieldAccessor;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class KPlugin extends JavaPlugin {
  
  private static final FieldAccessor<PluginLogger> LOGGER_ACCESSOR = Accessors.getField(JavaPlugin.class, "logger", PluginLogger.class);
  private final FileUtils fileUtils;
  
  public KPlugin() {
    this.fileUtils = new FileUtils(this);
    LOGGER_ACCESSOR.set(this, new KLogger(this));
    
    this.start();
  }
  
  public abstract void start();
  
  public abstract void load();
  
  public abstract void enable();
  
  public abstract void disable();
  
  @Override
  public void onLoad() {
    this.load();
  }
  
  @Override
  public void onEnable() {
    this.enable();
  }
  
  @Override
  public void onDisable() {
    this.disable();
  }
  
  public KConfig getConfig(String name) {
    return this.getConfig("", name);
  }
  
  public KConfig getConfig(String path, String name) {
    return KConfig.getConfig(this, "plugins/" + this.getName() + "/" + path, name);
  }
  
  public KWriter getWriter(File file) {
    return this.getWriter(file, "");
  }
  
  public KWriter getWriter(File file, String header) {
    return new KWriter((KLogger) this.getLogger(), file, header);
  }
  
  public FileUtils getFileUtils() {
    return this.fileUtils;
  }
}
