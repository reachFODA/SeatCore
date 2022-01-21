package dev.slickcollections.kiwizin.database.tables;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "kCoreBuildBattle",
    create = "CREATE TABLE IF NOT EXISTS `kCoreBuildBattle` (`name` VARCHAR(32), `wins` LONG, `games` LONG, `points` LONG, " +
        "`monthlywins` LONG, `monthlygames` LONG, `monthlypoints` LONG, `month` TEXT, `coins` DOUBLE, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `kCoreBuildBattle` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `kCoreBuildBattle` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `kCoreBuildBattle` SET `wins` = ?, `games` = ?, `points` = ?, `monthlywins` = ?, `monthlygames` = ?, `monthlypoints` = ?, `month` = ?, `coins` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?"
)
public class BuildBattleTable extends DataTable {
  
  @Override
  public void init(Database database) {}
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("wins", new DataContainer(0L));
    defaultValues.put("games", new DataContainer(0L));
    defaultValues.put("points", new DataContainer(0L));
    for (String key : new String[]{"wins", "games", "points"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
        Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    return defaultValues;
  }
}
