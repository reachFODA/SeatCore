package dev.slickcollections.kiwizin.utils.enums;

import dev.slickcollections.kiwizin.utils.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

public enum EnumRarity {
  DIVINO("§bDivino", 10),
  EPICO("§6Épico", 25),
  RARO("§dRaro", 50),
  COMUM("§9Comum", 100);
  
  private static final EnumRarity[] VALUES = values();
  private final String name;
  private final int percentage;
  
  EnumRarity(String name, int percentage) {
    this.name = name;
    this.percentage = percentage;
  }
  
  public static EnumRarity getRandomRarity() {
    int random = ThreadLocalRandom.current().nextInt(100);
    for (EnumRarity rarity : VALUES) {
      if (random <= rarity.percentage) {
        return rarity;
      }
    }
    
    return COMUM;
  }
  
  public static EnumRarity fromName(String name) {
    for (EnumRarity rarity : VALUES) {
      if (rarity.name().equalsIgnoreCase(name)) {
        return rarity;
      }
    }
    
    return COMUM;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getColor() {
    return StringUtils.getFirstColor(this.getName());
  }
  
  public String getTagged() {
    return this.getColor() + "[" + StringUtils.stripColors(this.getName()) + "]";
  }
}
