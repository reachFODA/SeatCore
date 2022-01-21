package dev.slickcollections.kiwizin.database.tables;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(name = "kCoreSkins",
        create = "CREATE TABLE IF NOT EXISTS `kCoreSkins` (`name` VARCHAR(32), `info` TEXT, `skins` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `kCoreSkins` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `kCoreSkins` VALUES (?, ?, ?)",
        update = "UPDATE `kCoreSkins` SET `info` = ?, `skins` = ? WHERE LOWER(`name`) = ?")
public class SkinsTable extends DataTable {
    @Override
    public void init(Database database) {
        if (database instanceof MySQLDatabase) {
            if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreSkins` LIKE 'skins'") == null) {
                ((MySQLDatabase) database).execute("ALTER TABLE `kCoreSkins` ADD `skins` TEXT AFTER `info`");
            }
        } else if (database instanceof HikariDatabase) {
            if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreSkins` LIKE 'skins'") == null) {
                ((HikariDatabase) database).execute("ALTER TABLE `kCoreSkins` ADD `skins` TEXT AFTER `info`");
            }
        }
    }

    public Map<String, DataContainer> getDefaultValues() {
        Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
        defaultValues.put("info", new DataContainer("{\"name\": \"none\", \"value\": \"none\", \"signature\": \"none\"}"));
        defaultValues.put("skins", new DataContainer("{}"));
        return defaultValues;
    }
}
