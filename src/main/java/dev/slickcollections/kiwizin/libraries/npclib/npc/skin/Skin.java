package dev.slickcollections.kiwizin.libraries.npclib.npc.skin;

import dev.slickcollections.kiwizin.libraries.profile.InvalidMojangException;
import dev.slickcollections.kiwizin.libraries.profile.Mojang;
import dev.slickcollections.kiwizin.nms.NMS;

public class Skin {
  
  private String value, signature;
  
  private Skin(String value, String signature) {
    this.value = value;
    this.signature = signature;
  }
  
  public static Skin fromName(String name) {
    return new Skin(null, null).fetch(name);
  }
  
  public static Skin fromData(String value, String signature) {
    return new Skin(value, signature);
  }
  
  private Skin fetch(String name) {
    try {
      String id = Mojang.getUUID(name);
      if (id != null) {
        // premium valid username
        String property = Mojang.getSkinProperty(id);
        if (property != null) {
          // valid skin property
          this.value = property.split(" : ")[1];
          this.signature = property.split(" : ")[2];
        }
      }
    } catch (InvalidMojangException e) {
      System.out.println("Cannot fetch skin from name " + name + ": " + e.getMessage());
    }
    
    return this;
  }
  
  public void apply(SkinnableEntity entity) {
    NMS.setValueAndSignature(entity.getEntity(), value, signature);
  }
}
