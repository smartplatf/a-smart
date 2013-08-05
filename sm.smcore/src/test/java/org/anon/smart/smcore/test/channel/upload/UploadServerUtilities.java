package org.anon.smart.smcore.test.channel.upload;


import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.test.ServerUtilities;
import org.anon.utilities.test.PathHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.anon.utilities.services.ServiceLocator.reflect;

public class UploadServerUtilities extends ServerUtilities {

    private int _uploadPort ;

    public UploadServerUtilities(int port ,int upPort)
    {
        super(port);
        _uploadPort = upPort;
    }

    @Override
    protected URL[] getURLs()
            throws Exception
    {
        URL[] urls = super.getURLs();
        List<URL> mod = new ArrayList<URL>();
        for (int i = 0; i < urls.length; i++)
            mod.add(urls[i]);
        mod.add(new URL(PathHelper.getJar(true, BASE)));
        return mod.toArray(new URL[0]);
    }

    @Override
    protected String[] getModules()
    {
        String[] comps = new String[] { "org.anon.smart.smcore.anatomy.SMCoreModule", "org.anon.smart.base.test.testanatomy.TestModule" };
        return comps;
    }

    @Override
    public void runServer(String runner)
            throws Exception
    {
        SmartLoader ldr = createLoader();
        _loader = ldr;
        _runner = runner;
        Class cls = ldr.loadClass(runner);
        Object run = cls.getDeclaredConstructor(Boolean.TYPE, Integer.TYPE,Integer.TYPE).newInstance(true, _port,_uploadPort);
        Thread thrd = new Thread((Runnable)run);
        thrd.setContextClassLoader(ldr);
        thrd.start();
        reflect().getAnyMethod(cls, "waitToStart").invoke(run);
        //for now, wait for server to start up
        //Thread.currentThread().sleep(10000);
    }

    @Override
    public void stopServer()
            throws Exception
    {
        Class cls = _loader.loadClass(_runner);
        Object run = cls.getDeclaredConstructor(Boolean.TYPE, Integer.TYPE,Integer.TYPE).newInstance(false, _port,_uploadPort);
        Thread thrd = new Thread((Runnable)run);
        thrd.setContextClassLoader(_loader);
        thrd.start();
        reflect().getAnyMethod(cls, "waitToStop").invoke(run);
        //for now, wait for server to stop
        //Thread.currentThread().sleep(10000);
        System.out.println("Stopped.");
    }
}
