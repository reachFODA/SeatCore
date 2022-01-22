package dev.slickcollections.kiwizin.menus;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.data.container.SkinsContainer;
import dev.slickcollections.kiwizin.libraries.menu.PagedPlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuSkinsLibrary extends PagedPlayerMenu {

  public static Map<String, String> LIBRARY = new HashMap<>();


  static {
    LIBRARY.put("Nezuko", "Nezuko______Chan:ewogICJ0aW1lc3RhbXAiIDogMTYxOTU1MjY5MjEwNSwKICAicHJvZmlsZUlkIiA6ICI4OGU0YWNiYTQwOTc0YWZkYmE0ZDM1YjdlYzdmNmJmYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJKb2FvMDkxNSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81OTAzYjY1MGIwYTk1ZTAyMDEyYTUzYTU0N2YzMjc0YzM5MDVkNDE4OTJkZDc0YmY2YTRjNmJhOWI1NjE3YmIyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0");
    LIBRARY.put("Tanjiro", "Tanjxro_:ewogICJ0aW1lc3RhbXAiIDogMTYzMjg0MjUxNjQxOSwKICAicHJvZmlsZUlkIiA6ICJmMjU5MTFiOTZkZDU0MjJhYTcwNzNiOTBmOGI4MTUyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmYXJsb3VjaDEwMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xOTdkYjUxMjE2Y2U1ZDZjOTQ0N2M1OGUzZGNkZWNhYmJhZDQ0ODJkMzY2OWRmNDE0NDA3NjhjMTYwYmY4MGRhIgogICAgfQogIH0KfQ==");
    LIBRARY.put("Akame", "EgirlFxse:ewogICJ0aW1lc3RhbXAiIDogMTY0MjcyOTM2OTk3NSwKICAicHJvZmlsZUlkIiA6ICJhOGJhMGY1YTFmNjQ0MTgzODZkZGI3OWExZmY5ZWRlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmVlcGVyOTA3NSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iZDZmODBlYTJiNjc2ZGFjZGUyZTU3NmQxOTdmMmQwMTQyZTYwNTQ2NDQ1NDZjNzQ5NDdlZDQ3OGM1YTU0ZjIwIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
    LIBRARY.put("Tatsumi", "Tatsuumiii:ewogICJ0aW1lc3RhbXAiIDogMTY0MjcyOTU2MjQxMiwKICAicHJvZmlsZUlkIiA6ICJiYjdjY2E3MTA0MzQ0NDEyOGQzMDg5ZTEzYmRmYWI1OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXVyZW5jaW8zMDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcwYzNkOWRlMDdiNTUzYjcyMGVjNmFlYzdiYTc0NDFkYmRlNWUzZjI3MWI4YzIwY2UzODQzYjQ0ZTYxOTg4YSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Venom", "Venoms_GM:ewogICJ0aW1lc3RhbXAiIDogMTYyMDI0OTIyOTkxNCwKICAicHJvZmlsZUlkIiA6ICIzOTg5OGFiODFmMjU0NmQxOGIyY2ExMTE1MDRkZGU1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNeVV1aWRJcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zM2E3NTIxMzMxOTFhMjY3NDIzYzQ3NWJmOTg0NzBjMTE1ZDAxOTYwMWMwYTRmZGZmNjBhMmI0MDBmYjdlODc3IgogICAgfQogIH0KfQ==");
    LIBRARY.put("Goku", "Oyo_Boyo:ewogICJ0aW1lc3RhbXAiIDogMTYxNzM0MTQ0NjczMSwKICAicHJvZmlsZUlkIiA6ICI1NjY3NWIyMjMyZjA0ZWUwODkxNzllOWM5MjA2Y2ZlOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVJbmRyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NDAzOTdlYzQ1MmQ2NmFjOThhMGQyNGRkNDc0ZmY3ZThhODFjNGNlMzdhNGEwNDRlNjQ5YTFmNTFjYTdjYzI4IgogICAgfQogIH0KfQ==");
    LIBRARY.put("Naruto", "Narutc:ewogICJ0aW1lc3RhbXAiIDogMTYyNTc1NDgxMDc3NCwKICAicHJvZmlsZUlkIiA6ICIwNjNhMTc2Y2RkMTU0ODRiYjU1MjRhNjQyMGM1YjdhNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYXZpcGF0dXJ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRkNTM1NjgwZWJkN2M4OGMxNDgyMzU5YWFlZmVjMTg2OTVhNTlmMzcwYTZkN2Y3ZDJlYTZkY2I5MjQ5OWY1MzMiCiAgICB9CiAgfQp9");
    LIBRARY.put("Homem Aranha", "700c:ewogICJ0aW1lc3RhbXAiIDogMTY0MDI5NTExNDYzOSwKICAicHJvZmlsZUlkIiA6ICI2MzMyMDgwZTY3YTI0Y2MxYjE3ZGJhNzZmM2MwMGYxZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUZWFtSHlkcmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDVlMzIzODNhNGMyOGFhODFjYjIzYWUzYWNhY2JlNTc0N2EyOTNkN2VhOTRiZjhhNDQ2OTkyZmIzMGU5NTI4NyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Homem de Ferro", "6ixsauer:eyJ0aW1lc3RhbXAiOjE1NzcyNDMwODk4NjIsInByb2ZpbGVJZCI6IjIzZjFhNTlmNDY5YjQzZGRiZGI1MzdiZmVjMTA0NzFmIiwicHJvZmlsZU5hbWUiOiIyODA3Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMTI5ODg0NTkxM2VmNmZkN2JlYTc2NmUxY2YyYTEzMTczNDFhOWZlYTcxYjE2M2E0NGZkMTk3NTM4NmQ5ZDEyIn19fQ==");
    LIBRARY.put("Jiraya", "Zakharov:ewogICJ0aW1lc3RhbXAiIDogMTYzMTk3MjkyNTkxOCwKICAicHJvZmlsZUlkIiA6ICJjMGYzYjI3YTUwMDE0YzVhYjIxZDc5ZGRlMTAxZGZlMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDEzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZkM2JlNmNhMWI3MGRjOGQ5ZTE3ZWNkZjg3MjBmYWIzYWVhYTNkYTU1NmE2N2ZmNjk5ZjllYjEyMWExZmM4NjkiCiAgICB9CiAgfQp9");
    LIBRARY.put("Kakashi", "eZio789:ewogICJ0aW1lc3RhbXAiIDogMTYyNTM0OTI4OTgxMCwKICAicHJvZmlsZUlkIiA6ICI5ZDQyNWFiOGFmZjg0MGU1OWM3NzUzZjc5Mjg5YjMyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUb21wa2luNDIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiYzU3MDBkODBhMGQ1ZDcyYWE2MDE3MDk2ZmNhNmI5ZjA3MjI2YTVkNWFkYThmZTYxZDhhYWNhMzU2NjRjMiIKICAgIH0KICB9Cn0=");
    LIBRARY.put("Obito", "sharinganek:ewogICJ0aW1lc3RhbXAiIDogMTYyNjA3ODAxMDY2OCwKICAicHJvZmlsZUlkIiA6ICIxOTI1MjFiNGVmZGI0MjVjODkzMWYwMmE4NDk2ZTExYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZXJpYWxpemFibGUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc4MWRkYzNjMDBjM2VlMWYzMzBjZTVlZDlmODhlMTNhNWFhODMxNTk3Yjk1YjliMjBkYjc0NmY2M2I5ODJmNCIKICAgIH0KICB9Cn0=");
    LIBRARY.put("Mitsuki", "iXwin:ewogICJ0aW1lc3RhbXAiIDogMTYyNjI2NTY3MDk1NiwKICAicHJvZmlsZUlkIiA6ICIxOTI1MjFiNGVmZGI0MjVjODkzMWYwMmE4NDk2ZTExYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZXJpYWxpemFibGUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFmZDA0ZDZkMGVlODcyMzUwZDJhMmE3NmU2MGMzOTkzNWQxMTYxNWMyN2MxNjZkMTYwY2JkYjU3ZmI0MmJhZCIKICAgIH0KICB9Cn0=");
    LIBRARY.put("Todoroki ", "_Slm:ewogICJ0aW1lc3RhbXAiIDogMTYxMzk5NTMzNTk2MiwKICAicHJvZmlsZUlkIiA6ICIwYWFjMWRlZjUwZmI0N2RjODNmOGU2Njk3MTg1ODRkZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ1bnN0YWJsZWRkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YwNDk1YTAxNmEwY2MwN2Q4YmVhYzYxNWQ5ZTMwYTg4ODkxMWE2OTM2MWE2NjM4NzFmMGU1ZjM2YmJmZjQ0OGIiCiAgICB9CiAgfQp9");
    LIBRARY.put("Midoriya", "LHIP:ewogICJ0aW1lc3RhbXAiIDogMTYxODcwMzUwNDQ4OSwKICAicHJvZmlsZUlkIiA6ICJiNzVjZDRmMThkZjg0MmNlYjJhY2MxNTU5MTNiMjA0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJLcmlzdGlqb25hczEzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJlMjdmNTJiM2VhYmU1NjA1MzViNDBiOTU4ZDI4OTA2NzQ1YmNkNzIwNWEwZTYzN2U3MjhmZWZkZDkwMTNjN2IiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    LIBRARY.put("All Might", "xG18x:ewogICJ0aW1lc3RhbXAiIDogMTU5MTY0MzE2MjYwOSwKICAicHJvZmlsZUlkIiA6ICIzM2ViZDMyYmIzMzk0YWQ5YWM2NzBjOTZjNTQ5YmE3ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEYW5ub0JhbmFubm9YRCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZmNiYzM4OTVlNWZlM2UxZGZhZjRhNmYzOGI5ZTAyYjM2NWYyYTJhYTc2NTIzZTc0Mzk3YTllNzIzY2VhNmFjIgogICAgfQogIH0KfQ==");
    LIBRARY.put("Zero Two", "Zer0_2:ewogICJ0aW1lc3RhbXAiIDogMTYyMDU4MTc5NTY2NSwKICAicHJvZmlsZUlkIiA6ICIzYTNmNzhkZmExZjQ0OTllYjE5NjlmYzlkOTEwZGYwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb19jcmVyYXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM2MTIwZmRkMDMwZWQ3ODMyODAxZDFhNDlkMDk1ZjZiYWU5ODY0NWQ0MWRhN2I5NzAzZWI4MjBkZmE0YWNkOSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Luffy", "Tlaoof:ewogICJ0aW1lc3RhbXAiIDogMTYxODc5ODE3MzU1MCwKICAicHJvZmlsZUlkIiA6ICJmZDQ3Y2I4YjgzNjQ0YmY3YWIyYmUxODZkYjI1ZmMwZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDEyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I1OTE4NzM4ZWEzOTI2ZWUyYTM4MzUyOGY5MTkxMDM3YmIwZTBlMWYxYzc5ZDYxMzMyMTAyNDk1M2RkM2E0ZGYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    LIBRARY.put("Saitama", "BradyJonas:ewogICJ0aW1lc3RhbXAiIDogMTYwMzI2MjA5OTYwOSwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMTAwMDExMDAxMDAwMDExIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2IzNzAxZTBhMzYxYjUzMTJjOTM0MDAyZDMyNWIxNTc4NmExMGNkMmJjNmNjYTJhMzVkNTM1MmZhZDEwOGRmZjgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    LIBRARY.put("Sakura", "MysticalFire128:ewogICJ0aW1lc3RhbXAiIDogMTYzNTg5NzU2NTg2MywKICAicHJvZmlsZUlkIiA6ICJiYjdjY2E3MTA0MzQ0NDEyOGQzMDg5ZTEzYmRmYWI1OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXVyZW5jaW8zMDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdkZDA3OWRjNTI5MWE0ZGIxY2Q5MjA1ODVmOGZiNjU3MTgyY2IzZTY4NGMxNDc4MjdhNzI0Y2E3NzJiZDgyNSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Sasuke", "SasukeUchiha:ewogICJ0aW1lc3RhbXAiIDogMTYyMTg1NDc1OTk5NiwKICAicHJvZmlsZUlkIiA6ICI1ZWE0ODg2NTg2OWI0Y2ZhOWRjNTg5YmFlZWQwNzM5MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJfUllOMF8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU2ZjM2YWJhN2Y5MDcxY2MzYjRmODkzYWE1NDliZjc1NTE2YmU4ZWRiZGEzNTM4MjY5ZGRmNzFhMmJkYWU0MyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Ranger Preto", "Geero0:ewogICJ0aW1lc3RhbXAiIDogMTYwNTYzMTU0ODc0NSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ZTY5YzA1OTE4NmJjYjVkOWNiMDVlMjQ2OGMzNmIwOWNhMzdkY2RiNTRmOGZjMzBkNDNiZTBlNGEzNWEyNzIwIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
    LIBRARY.put("Ranger Amarelo", "Harryr_:ewogICJ0aW1lc3RhbXAiIDogMTYwMDM4MjIyNjkyNCwKICAicHJvZmlsZUlkIiA6ICJkZTU3MWExMDJjYjg0ODgwOGZlN2M5ZjQ0OTZlY2RhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfTWluZXNraW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2NiMmU3NjkxN2NmMTJlZDU3MGZiMzgyMzkzZWUyZTIyMGU5YTk4NjM2MDU1NjMyMjk1ZDYwOTVjYjBjNzMwMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Ranger Branco", "crimb:ewogICJ0aW1lc3RhbXAiIDogMTY0Mjc5MzY0MjQ1NCwKICAicHJvZmlsZUlkIiA6ICI3NDJkYmE3MzFjOTQ0MjZlODVlZTBjNzVhYWY2OTcyMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJjcmltYiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hYjZlM2VlNWYzNTNlNmU5ZDU0NTBlMDNkNDU4ZmIwYmVhYjI3ZTIwYmUxZjM0MDU2YWJhYzFmMDkzYTQ4YWE3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
    LIBRARY.put("Ranger Verde", "nickloading:ewogICJ0aW1lc3RhbXAiIDogMTYwODIwNjI1ODE2NiwKICAicHJvZmlsZUlkIiA6ICI2ZmU4OTUxZDVhY2M0NDc3OWI2ZmYxMmU3YzFlOTQ2MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJlcGhlbXJhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRjMjM0MTQ0MTc0NzM1YmNmMWI3MWMxZjRjY2I5Mjc5MDM3NjRhYTI1MmQ3YThkODdmNjhjYzBhNjY4YThhYTQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    LIBRARY.put("Ranger Azul", "TewPaq:ewogICJ0aW1lc3RhbXAiIDogMTYwMDcxNjMyNTk5NSwKICAicHJvZmlsZUlkIiA6ICIzYTNmNzhkZmExZjQ0OTllYjE5NjlmYzlkOTEwZGYwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVyb2Ryb2dvIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUxZmMwZTY1YWQwMjA2MWEyYjAyNzgzYTg3ZTkwYTQ3NDc4MDYwNGU2ZjFmYmQ2NTFkZDZkOGNjOWVjN2RkMjIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    LIBRARY.put("Ranger Roxa", "blmsikecuh345:ewogICJ0aW1lc3RhbXAiIDogMTYwMzI4MjE2OTAyNCwKICAicHJvZmlsZUlkIiA6ICJiZWNkZGIyOGEyYzg0OWI0YTliMDkyMmE1ODA1MTQyMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdFR2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJjOWE1NDU1NWFlODk4Yzc5OWVjMWE2MTVkYTNmZWQ5MGQxNDIxYjU4YTM5YjM2Y2NkZGFhNzFlODY5YWZiYzQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    LIBRARY.put("Ranger Rosa", "Zyluh:ewogICJ0aW1lc3RhbXAiIDogMTYwMDMxMDk3OTEzOSwKICAicHJvZmlsZUlkIiA6ICIwNjEzY2I1Y2QxYjg0M2JjYjI4OTk1NWU4N2QzMGEyYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJicmVhZGxvYWZzcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ZWE0ZGNiZWZiZmE3MjMwNTk5ZmU0ZmVkMjA1YTdjYzlmMmIwNTczMmQxZmMyZTcwYmY5MGYxNjU4OWQ5MmQ3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
    LIBRARY.put("Ranger Vermelho", "imcheatinglul:ewogICJ0aW1lc3RhbXAiIDogMTYwMDcxNjE2OTkzNiwKICAicHJvZmlsZUlkIiA6ICI3ZGEyYWIzYTkzY2E0OGVlODMwNDhhZmMzYjgwZTY4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb2xkYXBmZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAwM2E3OGYyMjY2MzU2NDljMzNlNGQ0NDZkNWE3YWIyNDE0MzMwMjdhMGEyOTNhNjFhMzRlMTRlYTViMDg1NiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
  }

  private Map<ItemStack, String> skins = new HashMap<>();

  public MenuSkinsLibrary(Profile profile) {
    super(profile.getPlayer(), "Biblioteca", (LIBRARY.size() / 7) + 4);
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

    SkinsContainer skinsContainer = profile.getSkinsContainer();

    String selectedSkin = skinsContainer == null ? "none" : skinsContainer.getSkin();

    List<ItemStack> items = new ArrayList<>();
    LIBRARY.forEach((key, value) -> {
      String skin = value.split(":")[1];
      ItemStack icon = BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : skin>" + skin + " : nome>&a" + key
          + " : desc>&7Altere sua skin para " + key + ".\n \n" + (selectedSkin.equals(value.split(":")[0]) ? "§eSelecionado." : "§eClique para alterar!"));
      items.add(icon);
      this.skins.put(icon, key);
    });

    this.removeSlotsWith(BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"), (this.rows * 9) - 5);

    this.setItems(items);
    items.clear();

    this.register(Core.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);

      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();

          if (item != null && item.getType() != Material.AIR) {
            String skin = this.skins.get(item);

            if (evt.getSlot() == (this.rows * 9) - 5) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuSkins(profile);
            } else if (skin != null) {
              SkinsContainer container = profile.getSkinsContainer();
              SkinsContainer listContainer = profile.getSkinListContainer();
              String skinName = LIBRARY.get(skin).split(":")[0].replace(" ", "");

              if (!container.getSkin().equals(skinName)) {
                player.chat("/skin library " + skin);
                container.setSkin(skinName);
                listContainer.addSkin(skinName);
                player.closeInventory();
              }
            }
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.skins.clear();
    this.skins = null;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}
