package org.minbox.framework.database.synchronization;

import org.apache.commons.lang3.StringUtils;
import org.minbox.framework.database.synchronization.config.Config;
import org.minbox.framework.database.synchronization.config.Connection;
import org.minbox.framework.database.synchronization.config.Database;
import org.minbox.framework.database.synchronization.config.Table;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 数据库表数据同步shell脚本文件生成器
 *
 * @author 恒宇少年
 */
public class SynchronizationShellBuilder {
    private static final String SHELL_FILE_NAME = System.getProperty("user.dir") + "/target/synchronization.sh";
    private static final String SHELL_HEADER = "#!/bin/bash";
    private final Config config;

    public SynchronizationShellBuilder(Config config) {
        this.config = config;
    }

    public void generator() throws Exception {
        FileWriter writer = new FileWriter(SHELL_FILE_NAME);
        writer.write(SHELL_HEADER);
        writer.write("\n\n");
        config.getDatabases().forEach(database -> {
            try {
                writer.write("# Execute synchronization source " + database.getSource().getSchema() + " database.");
                writer.write("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            database.getTables().forEach(table -> {
                try {
                    writer.write("# Execute synchronization " + table.getName() + " table");
                    writer.write("\n");
                    writer.write(StringUtils.join(this.getFormattedCmd(database, table), " "));
                    writer.write("\n\n");
                    System.out.println("[Database][" + database.getSource().getSchema() + "], [Table][" + table.getName() + "], [Where][" + (table.getWhere() != null && !table.getWhere().isEmpty() ? table.getWhere() : "") + "]同步命令写入成功.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        writer.close();
        System.out.println("[synchronization.sh]脚本生成完成.");
    }

    private String[] getFormattedCmd(Database database, Table table) {
        Connection source = database.getSource();
        Connection target = database.getTarget();
        // @formatter:off
        return new String[] {
                "pt-archiver",
                "--source",
                "h=" + source.getIp() + ",P=" + source.getPort() + ",u=" + source.getUsername() + ",p=" + source.getPassword() + ",D=" + source.getSchema() + ",t=" + table.getName(),
                "--dest",
                "h=" + target.getIp() + ",P=" + target.getPort() + ",u=" + target.getUsername() + ",p=" + target.getPassword() + ",D=" + target.getSchema() + ",t=" + table.getName(),
                "--where",
                table.getWhere() != null && !table.getWhere().isEmpty() ? "'" + table.getWhere() + "'" : "'1=1'",
                "--charset=UTF8",
                "--progress", "1000",
                "--limit=1000",
                "--txn-size", "1000",
                "--statistics",
                "--no-delete",
                "--why-quit",
                "--no-version-check"
        };
        // @formatter:on
    }
}
