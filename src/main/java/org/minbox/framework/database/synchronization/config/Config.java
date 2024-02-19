package org.minbox.framework.database.synchronization.config;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
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

    private final List<Database> databases = new ArrayList<>();

    public static Config load() throws Exception {
        Config config = new Config();
        Yaml yaml = new Yaml();
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
        Map<String, Object> configMap = yaml.load(inputStream);

        List<Object> connections = (List<Object>) configMap.get(CONFIG_KEY_CONNECTION);
        // 加载每个数据库
        connections.forEach(connectionObject -> {
            Database database = new Database();
            Map<String, Object> connection = (Map<String, Object>) connectionObject;
            Map<String, Object> source = (Map<String, Object>) connection.get(CONFIG_KEY_SOURCE);
            Map<String, Object> target = (Map<String, Object>) connection.get(CONFIG_KEY_TARGET);
            List<String> tables = (List<String>) connection.get(CONFIG_KEY_TABLES);

            database.setSource(JSON.parseObject(JSON.toJSONString(source), Connection.class));
            database.setTarget(JSON.parseObject(JSON.toJSONString(target), Connection.class));
            // @formatter:off
            database.setTables(tables.stream()
                    .map(tableString -> {
                        String[] tableArray = tableString.split(TABLE_SEPARATOR);
                        return new Table(tableArray[0], tableArray.length > 1 ? tableArray[1] : null);
                    }).collect(Collectors.toList()));
            // @formatter:on
            config.databases.add(database);
        });
        return config;
    }
}
