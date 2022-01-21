package dev.slickcollections.kiwizin.libraries.npclib.api.metadata;

public interface MetadataStore {
  
  <T> T get(String key);
  
  <T> T get(String key, T def);
  
  boolean has(String key);
  
  void remove(String key);
  
  void set(String key, Object data);
}
