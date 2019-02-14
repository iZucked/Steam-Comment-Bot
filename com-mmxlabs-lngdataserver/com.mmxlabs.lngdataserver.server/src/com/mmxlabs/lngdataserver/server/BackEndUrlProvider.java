/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum BackEndUrlProvider {
	INSTANCE;
	private int port;
	private boolean available = false;

	private final Queue<IBackEndAvailableListener> listeners = new ConcurrentLinkedQueue<>();

	public void setPort(final int port) {
		this.port = port;
	}

	public void setAvailable(final boolean available) {
		this.available = available;
		if (available) {
			while (!listeners.isEmpty()) {
				final IBackEndAvailableListener l = listeners.poll();
				if (l != null) {
					try {
						l.backendAvailable();
					} catch (final Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}

	public int getPort() {
		return this.port;
	}

	public String getUrl() {
		if (port == 0) {
			throw new IllegalStateException("No port set yet.");
		}
		return "http://localhost:" + port;
	}

	public boolean isAvailable() {
		return available;
	}

	public void addAvailableListener(final IBackEndAvailableListener l) {
		if (available) {
			l.backendAvailable();
		} else {
			listeners.add(l);
		}
	}

	public void removeAvailableListener(final IBackEndAvailableListener l) {
		listeners.remove(l);
	}
}
