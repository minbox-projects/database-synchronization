package org.minbox.framework.database.synchronization.config;

import lombok.Data;

/**
 * 连接配置
 *
 * @author 恒宇少年
 */
@Data
public class Connection {
    private String schema;
    private String ip;
    private int port;
    private String username;
    private String password;
}
