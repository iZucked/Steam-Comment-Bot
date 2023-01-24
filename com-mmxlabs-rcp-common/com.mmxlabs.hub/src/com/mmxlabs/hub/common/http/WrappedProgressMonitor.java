/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public final class WrappedProgressMonitor implements IProgressListener {

	public static @Nullable IProgressListener wrapMonitor(final @Nullable IProgressMonitor monitor) {
		if (monitor == null) {
			return null;
		}
		return new WrappedProgressMonitor(monitor);
	}

	private boolean firstCall = true;
	private final @NonNull IProgressMonitor monitor;

	private WrappedProgressMonitor(final @NonNull IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void update(final long bytesRead, final long contentLength, final boolean done) {
		if (firstCall) {
			int total = (int) (contentLength / 1000L);
			if (total == 0) {
				total = 1;
			}
			monitor.beginTask("Transfer", total);
			firstCall = false;
		}
		final int worked = (int) (bytesRead / 1000L);
		monitor.worked(worked);
	}
}
