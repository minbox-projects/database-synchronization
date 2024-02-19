package org.minbox.framework.database.synchronization.config;

import lombok.Data;

import java.util.List;

/**
 * 数据库配置
 *
 * @author 恒宇少年
 */
@Data
public class Database {
    private Connection source;
    private Connection target;
    private List<Table> tables;
}
