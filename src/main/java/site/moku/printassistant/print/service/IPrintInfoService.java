/**
 * @author louisliu
 */

package site.moku.printassistant.print.service;

import java.util.Set;

public interface IPrintInfoService {

    void saveToRedis(String printKey, String content);

    String getFromRedis(String printKey);

    Set<String> getHistoryFromRedis();

    int testCache();
}
