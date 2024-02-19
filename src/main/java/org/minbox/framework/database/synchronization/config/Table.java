package org.minbox.framework.database.synchronization.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 同步的表配置
 *
 * @author 恒宇少年
 */
@Data
@AllArgsConstructor
public class Table {
    private String name;
    private String where;
}
