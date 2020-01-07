package fr.zapho.sandbox.jee.timers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import java.time.Duration;

@Singleton
@Startup
public class Timer2 {

    @Resource
    TimerService ts;


    @PostConstruct
    public void init() {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo("timer=" + this.getClass().getSimpleName());
        Timer timer = ts.createIntervalTimer(0, Duration.ofSeconds(5).toMillis(), timerConfig);
        System.out.println(timer.getInfo() + " started");
    }

    @Timeout
    public void tick() {
        System.out.println(getClass().getSimpleName() + " says: yolo");
    }

    @PreDestroy
    public void teardown() {
        if (ts != null && ts.getTimers() != null) {
            for (Timer timer : ts.getAllTimers()) {
                timer.cancel();
                System.out.println(timer.getInfo() + " stopped");
            }
        }
    }

}
