package com.example.demo.main;

import com.example.demo.service.NettyServer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractIdleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class Service extends AbstractIdleService {
    public static final Log log = LogFactory.getLog(Service.class);
    @Override
    protected void startUp() throws Exception {
        log.info(this.state());

    }

    @Override
    protected void shutDown() throws Exception {
        log.info(this.state());
    }

    public void test() throws InterruptedException {
        System.err.println("laji");
        Thread.sleep(5000);
    }
    public void  test2() throws  InterruptedException{
        System.err.println("laji2");
        Thread.sleep(5000);
    }
}

public class GuavaTest {
    public static final Log log = LogFactory.getLog(GuavaTest.class);
    public static void main(String args[]) throws Exception {

        Service s = new Service();

        s.startAsync();

        s.awaitRunning();
        if(s.isRunning()){
            log.info(s.getClass().getSimpleName()+" - "+s.state());
            s.test();
            s.startAsync();
            s.test2();
        }




    }
}
