package AdditionalClasses;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Timer {
    private static Timer instance = new Timer();
    private final long startTime;
    private final int secondPerHour = 3600;
    private final int millisPerSecond = 1000;
    private int speed;
    private ScheduledExecutorService ses;
    private List<Consumer<Integer>> listeners;

    private Timer() {
        startTime = System.currentTimeMillis();
        speed = 450;
        listeners = new CopyOnWriteArrayList<>();
        ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(
                () -> listeners.forEach(l -> l.accept(getCurHour())),
        0L, secondPerHour * millisPerSecond / 2 /speed, TimeUnit.MILLISECONDS);
    }

    public static Timer getInstance() {
        Timer localInstance = instance;
        if (localInstance == null) {
            synchronized (Timer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Timer();
                }
            }
        }
        return localInstance;
    }

    public int getCurHour() {
        int curHour = (int) (System.currentTimeMillis() - startTime) * speed / millisPerSecond / secondPerHour;
        return curHour % 24;
    }

    public long getMillisToNextHour() {
        long pastTime = (System.currentTimeMillis() - startTime) * speed;
        long currentHour = (System.currentTimeMillis() - startTime) * speed / millisPerSecond / secondPerHour;
        long hourFinishTime = (currentHour + 1) * secondPerHour * millisPerSecond;
        return (hourFinishTime - pastTime) / speed;
    }

    public void addListener(Consumer<Integer> listener) {
        listeners.add(listener);
    }
}