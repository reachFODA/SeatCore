package dev.slickcollections.kiwizin.database.tables;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "kCoreProfile",
    create = "CREATE TABLE IF NOT EXISTS `kCoreProfile` (`name` VARCHAR(32), `cash` LONG, `role` TEXT, `deliveries` TEXT, `preferences` TEXT, `titles` TEXT, `boosters` TEXT, `achievements` TEXT, `selected` TEXT, `created` LONG, `clan` TEXT, `lastlogin` LONG, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `kCoreProfile` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `kCoreProfile` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `kCoreProfile` SET `cash` = ?, `role` = ?, `deliveries` = ?, `preferences` = ?, `titles` = ?, `boosters` = ?, `achievements` = ?, `selected` = ?, `created` = ?, `clan` = ?, `lastlogin` = ? WHERE LOWER(`name`) = ?"
)
public class CoreTable extends DataTable {
  
  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreProfile` LIKE 'clan'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `kCoreProfile` ADD `clan` TEXT AFTER `created`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreProfile` LIKE 'clan'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `kCoreProfile` ADD `clan` TEXT AFTER `created`");
      }
    }
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
    defaultValues.put("role", new DataContainer("Membro"));
    defaultValues.put("deliveries", new DataContainer("{}"));
    defaultValues.put("preferences", new DataContainer("{\"pv\": 0, \"pm\": 0, \"bg\": 0, \"pl\": 0, \"cm\": 0, \"ag\": 0}"));
    defaultValues.put("titles", new DataContainer("[]"));
    defaultValues.put("boosters", new DataContainer("{}"));
    defaultValues.put("achievements", new DataContainer("[]"));
    defaultValues.put("selected", new DataContainer("{\"title\": \"0\", \"icon\": \"0\"}"));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("clan", new DataContainer(""));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));
    return defaultValues;
  }
}
