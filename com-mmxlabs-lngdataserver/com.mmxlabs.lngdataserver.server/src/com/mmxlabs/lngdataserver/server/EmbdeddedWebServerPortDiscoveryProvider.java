package com.mmxlabs.lngdataserver.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Class to pull out the spring boot embedded web server port. This is not available when this bean in constructed - thus we fire off a thread to poll the container until a valid port is available.
 * 
 * @author Simon Goodall
 *
 */
@Component
public class EmbdeddedWebServerPortDiscoveryProvider {

	@Autowired
	EmbeddedWebApplicationContext server;

	@PostConstruct
	public void createDiscoveryThread() {

		final Thread thread = new Thread(() -> {
			final EmbeddedServletContainer embeddedServletContainer = server.getEmbeddedServletContainer();
			while (true) {
				final int port = embeddedServletContainer.getPort();
				if (port > 0) {
					System.err.println("Backend server port: " + port);
					BackEndUrlProvider.INSTANCE.setPort(port);
					BackEndUrlProvider.INSTANCE.setAvailable(true);
					return;
				}
				try {
					Thread.sleep(300);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		thread.setName("Dataserver port discoverer");
		thread.start();
	}
}
