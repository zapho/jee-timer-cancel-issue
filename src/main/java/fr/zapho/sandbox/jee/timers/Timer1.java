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
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Startup
@Singleton
public class Timer1 {

    public static final Logger LOGGER = Logger.getLogger(Timer1.class.getSimpleName());

    @Resource
    TimerService ts;

    private AtomicReference<Timer> timer = new AtomicReference<>();

    @PostConstruct
    public void init() {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo("timer=" + this.getClass().getSimpleName());

        timer.set(ts.createIntervalTimer(0, Duration.ofSeconds(5).toMillis(), timerConfig));
        System.out.println(timer.get().getInfo() + " started");
    }

    @Timeout
    public void tick() {
        System.out.println(getClass().getSimpleName() + " says: yolo");
    }

    @PreDestroy
    public void teardown() {
        cancelTimerDirect();
    }

    public Collection<Timer> getAllTimers() {
        return ts.getAllTimers();
    }

    public Collection<Timer> getTimers() {
        return ts.getTimers();
    }

    public void cancelTimerDirect() {
        Timer timer1 = timer.get();
        System.out.println("Trying to cancel (via direct ref) " + timer1.getInfo());
        timer1.cancel();
        System.out.println(timer1.getInfo() + " stopped");
    }

    public void cancelTimerLoop() {
        if (ts != null && ts.getTimers() != null) {
            for (Timer timer : ts.getAllTimers()) {
                System.out.println("Trying to cancel (via getTimers()) " + timer.getInfo());
                timer.cancel();
                System.out.println(timer.getInfo() + " stopped");
            }
        }
    }

    public Timer getTimer() {
        return timer.get();
    }
}
