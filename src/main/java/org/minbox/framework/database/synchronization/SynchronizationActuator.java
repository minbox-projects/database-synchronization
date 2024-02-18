package org.minbox.framework.database.synchronization;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步执行器
 *
 * @author 恒宇少年
 */
@Slf4j
public class SynchronizationActuator {

    public static void main(String[] args) throws Exception {
        Config config = Config.load();
        SynchronizationActuator actuator = new SynchronizationActuator();
        actuator.processing(config);
    }

    public void processing(Config config) throws Exception {
        List<String[]> cmdList = this.getFormattedCmd(config);
        cmdList.forEach(cmdArray -> {
            try {
                String cmdString = StringUtils.join(cmdArray, " ");
                System.out.println(">>> " + cmdString);
                Process process = new ProcessBuilder(Arrays.asList(cmdArray)).start();
                printResults(process);
                printErrors(process);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.equals("Exiting because there are no more rows.")) {
                System.out.println("同步完成.");
            }
        }
        reader.close();
    }

    public void printErrors(Process process) throws Exception {
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.err.println(line);
        }
        errorReader.close();
    }

    private List<String[]> getFormattedCmd(Config config) {
        Config.Connection source = config.getSource();
        Config.Connection target = config.getTarget();
        return config.getTables().stream().map(table -> {
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
        }).collect(Collectors.toList());
    }
}
