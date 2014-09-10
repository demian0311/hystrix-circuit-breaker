package hystrix.circuit.breaker;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.netflix.turbine.init.TurbineInit;

public class TurbineContextListener implements ServletContextListener {
	private static AtomicBoolean turbineInited = new AtomicBoolean(false);
	private static final Logger log = Logger.getLogger(TurbineContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (!turbineInited.getAndSet(true)) {
			try {
				TurbineInit.init();
			} catch (Exception e) {
				if (!e.getMessage().contains("already started")) {
					log.error("Calling TurbineInit.init()", e);
				}
			}
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// nothing to do
	}
}
