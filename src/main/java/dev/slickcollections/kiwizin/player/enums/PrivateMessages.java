package dev.slickcollections.kiwizin.player.enums;

public enum PrivateMessages {
  TODOS,
  NENHUM;
  
  private static final PrivateMessages[] VALUES = values();
  
  public static PrivateMessages getByOrdinal(long ordinal) {
    if (ordinal < 2 && ordinal > -1) {
      return VALUES[(int) ordinal];
    }
    
    return null;
  }
  
  public String getInkSack() {
    if (this == TODOS) {
      return "10";
    }
    
    return "8";
  }
  
  public String getName() {
    if (this == TODOS) {
      return "§aAtivado";
    }
    
    return "§cDesativado";
  }
  
  public PrivateMessages next() {
    if (this == NENHUM) {
      return TODOS;
    }
    
    return NENHUM;
  }
}
