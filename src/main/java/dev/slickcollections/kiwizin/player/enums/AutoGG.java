package dev.slickcollections.kiwizin.player.enums;

public enum AutoGG {
  ATIVADO,
  DESATIVADO;

  private static final AutoGG[] VALUES = values();

  public static AutoGG getByOrdinal(long ordinal) {
    if (ordinal < 2 && ordinal > -1) {
      return VALUES[(int) ordinal];
    }

    return null;
  }

  public String getInkSack() {
    if (this == ATIVADO) {
      return "10";
    }

    return "8";
  }

  public String getName() {
    if (this == ATIVADO) {
      return "§aAtivado";
    }

    return "§cDesativado";
  }

  public AutoGG next() {
    if (this == DESATIVADO) {
      return ATIVADO;
    }

    return DESATIVADO;
  }
}
