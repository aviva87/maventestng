package listener.extentReports;

import com.google.common.collect.Maps;
import com.vimalselvam.testng.SystemInfo;

import java.util.Map;

public class MySystemInfo implements SystemInfo{
    @Override
    public Map<String, String> getSystemInfo() {
        Map<String, String> systemInfo = Maps.newHashMap();
        systemInfo.put("Test Env", "QA");
        systemInfo.put("测试人员","王雪莲");
        return systemInfo;
    }
}
