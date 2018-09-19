package alex.ru.utils.jmx.stopserver;

import alex.ru.utils.jmx.JmxInitializer;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class StopServerInitializer implements JmxInitializer {

    private final StopServer mBean = new StopServer();
    private final String mBeanName = mBean.getClass().getSimpleName();

    public void init() {
        try {

            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            final ObjectName objectName = new ObjectName("alex.ru.utils.jmx.stopserver:type=" + mBeanName);
            mBeanServer.registerMBean(mBean, objectName);

        } catch (InstanceAlreadyExistsException |
                MBeanRegistrationException |
                NotCompliantMBeanException |
                MalformedObjectNameException e) {

            e.printStackTrace();
        }
    }

    public Object getMBean() {
        return  mBean;
    }

    public String getMBeanName() {
        return mBeanName;
    }

    @Override
    public int hashCode() {
        return mBeanName.hashCode();
    }
}
