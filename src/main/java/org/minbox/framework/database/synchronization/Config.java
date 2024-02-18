package org.minbox.framework.database.synchronization;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置信息
 *
 * @author 恒宇少年
 */
@Getter
public class Config {
    private static final String CONFIG_FILE_PATH = "sync_config.yml";
    private static final String CONFIG_KEY_CONNECTION = "connection";
    private static final String CONFIG_KEY_SOURCE = "source";
    private static final String CONFIG_KEY_TARGET = "target";
    private static final String CONFIG_KEY_TABLES = "tables";
    private static final String TABLE_SEPARATOR = ";";
    /**
     * 源库
     */
    private Connection source;
    /**
     * 目标库
     */
    private Connection target;
    /**
     * 同步表
     */
    private List<Table> tables;

    public static Config load() throws Exception {
        Config config = new Config();
        Yaml yaml = new Yaml();
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
        Map<String, Object> configMap = yaml.load(inputStream);

        Map<String, Object> connection = (Map<String, Object>) configMap.get(CONFIG_KEY_CONNECTION);
        Map<String, Object> source = (Map<String, Object>) connection.get(CONFIG_KEY_SOURCE);
        Map<String, Object> target = (Map<String, Object>) connection.get(CONFIG_KEY_TARGET);
        List<String> tables = (List<String>) configMap.get(CONFIG_KEY_TABLES);

        config.source = JSON.parseObject(JSON.toJSONString(source), Connection.class);
        config.target = JSON.parseObject(JSON.toJSONString(target), Connection.class);
        // @formatter:off
        config.tables = tables.stream()
                .map(tableString -> {
                    String[] tableArray = tableString.split(TABLE_SEPARATOR);
                    return new Table(tableArray[0], tableArray.length > 1 ? tableArray[1] : null);
                }).collect(Collectors.toList());
        // @formatter:on
        return config;
    }

    @Data
    public static class Connection {
        private String schema;
        private String ip;
        private int port;
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class Table {
        private String name;
        private String where;
    }
}
