# Issue reproducer: JEE Timer NoSuchObjectLocalException: WFLYEJB0331: Timer was canceled

# Environment

* Wildfly 10.1.0.Final or 15.0.0.Final
* Windows 10
* java -version
  * java version "1.8.0_221"
   Java(TM) SE Runtime Environment (build 1.8.0_221-b11)
   Java HotSpot(TM) 64-Bit Server VM (build 25.221-b11, mixed mode) 

# Steps

1. `mvn package`
1. deploy jeetimers-1.0-SNAPSHOT.war to Wildfly
11. Wildfly console should print `[stdout] (EJB default - 10) Timer1 says: yolo` and `[stdout] (EJB default - 10) Timer2 says: yolo`
1. GET http://localhost:8080/jeetimers-1.0-SNAPSHOT/rest/timers?op=stopdirect
11. `NoSuchObjectLocalException` is thrown (see below)
1. Different method, same result GET http://localhost:8080/jeetimers-1.0-SNAPSHOT/rest/timers?op=stoploop
11. `NoSuchObjectLocalException` is thrown (see below)
```
Caused by: javax.ejb.NoSuchObjectLocalException: WFLYEJB0331: Timer was canceled
 	at org.jboss.as.ejb3.timerservice.TimerImpl.assertTimerState(TimerImpl.java:463)
 	at org.jboss.as.ejb3.timerservice.TimerImpl.getInfo(TimerImpl.java:235)
 	at fr.zapho.sandbox.jee.timers.Timer1.cancelTimer(Timer1.java:69)
```

Contrary to what the error message says, timer1 has never been cancelled, `[stdout] (EJB default - 10) Timer1 says: yolo` is still printed in Console log.
