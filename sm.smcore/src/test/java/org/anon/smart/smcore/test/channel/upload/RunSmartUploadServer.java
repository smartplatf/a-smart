package org.anon.smart.smcore.test.channel.upload;


import org.anon.smart.base.test.testanatomy.BaseStartConfig;
import org.anon.smart.smcore.test.StartCoreServerRunner;
import java.util.HashMap;
import java.util.Map;

public class RunSmartUploadServer extends StartCoreServerRunner{

    private int uploadPort ;

    public RunSmartUploadServer(boolean b, int port, int upPort)
    {
        super(b, port);
        uploadPort = upPort;
        _tenants.add("newtenant");
        _tenants.add("errortenant");
    }


    protected BaseStartConfig getConfig()
    {
        String[] tens = tenants();
        Map<String, String[]> enable = new HashMap<String, String[]>();
        enable.put("ReviewFlow.soa", tens); //by default enable for all. If any custom override and change
        TestStartUploadConfig cfg = new TestStartUploadConfig(new String[] { "ReviewFlow.soa" }, tens, enable, _port,uploadPort);
        addTenantConfigs(tens, cfg);
        return cfg;
    }

    protected void addTenantConfigs(String[] tenants, BaseStartConfig cfg)
    {
        cfg.addConfig(tenants[0], "org.anon.smart.smcore.test.channel.FirstTestConfig");
    }
}
