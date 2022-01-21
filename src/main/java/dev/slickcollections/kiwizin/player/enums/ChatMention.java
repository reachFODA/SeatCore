package dev.slickcollections.kiwizin.player.enums;

public enum ChatMention {
    ATIVADO,
    DESATIVADO;

    private static final ChatMention[] VALUES = values();

    public static ChatMention getByOrdinal(long ordinal) {
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

    public boolean isEnabled() {
        return this == ATIVADO;
    }

    public String getName() {
        if (this == ATIVADO) {
            return "§aAtivado";
        }

        return "§cDesativado";
    }

    public ChatMention next() {
        if (this == DESATIVADO) {
            return ATIVADO;
        }

        return DESATIVADO;
    }
}