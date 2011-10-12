package com.mmxlabs.jobmanager.ipc.runner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.mmxlabs.jobmanager.ipc.runner.impl.CommandLoop;

public class Activator implements BundleActivator {
	private CommandLoop loop;
	private Thread loopThread;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		final String portString = System.getProperty("com.mmxlabs.jobmanager.ipc.runner.port");
		if (portString != null) {
			final int port = Integer.parseInt(portString);
			System.out.println("IPC connect " + port);
			loop = new CommandLoop();
			loop.setPort(port);
			loopThread = new Thread(loop);
			loopThread.start();
		} else {
			loop = null;
			loopThread = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		System.out.println("Stopped IPC runner");
		if (loop != null) {
			loop.stop();
			loopThread.join();
		}
		loop = null;
		loopThread = null;
	}

}
