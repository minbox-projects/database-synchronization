/*
 *   Copyright (C) 2023  恒宇少年
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.minbox.framework.database.synchronization;

import org.minbox.framework.database.synchronization.config.Config;

/**
 * 同步shell脚本构建测试类
 *
 * @author 恒宇少年
 */
public class SynchronizationShellBuildTest {
    public static void main(String[] args) throws Exception {
        Config config = Config.load();
        SynchronizationShellBuilder shellBuilder = new SynchronizationShellBuilder(config);
        shellBuilder.generator();
    }
}
