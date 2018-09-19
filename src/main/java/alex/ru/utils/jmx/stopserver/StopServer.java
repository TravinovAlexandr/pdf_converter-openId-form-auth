package alex.ru.utils.jmx.stopserver;

import java.util.concurrent.atomic.AtomicBoolean;

public class StopServer implements StopServerMBean {

    private AtomicBoolean isStopped = new AtomicBoolean(false);

    @Override
    public void stopServer() {
        isStopped.compareAndSet(isStopped.get(), !isStopped.get());
    }

    @Override
    public boolean isStopped() {
        return isStopped.get();
    }

}
