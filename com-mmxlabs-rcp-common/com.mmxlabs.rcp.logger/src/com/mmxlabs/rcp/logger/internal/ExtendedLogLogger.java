/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.rcp.logger.internal;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.slf4j.helpers.MarkerIgnoringBase;

public class ExtendedLogLogger extends MarkerIgnoringBase {

	private static final long serialVersionUID = 1L;

	public ExtendedLogLogger(final String name) {
		this.name = name;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(final String msg) {

	}

	@Override
	public void trace(final String format, final Object arg) {

	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {

	}

	@Override
	public void trace(final String format, final Object[] argArray) {

	}

	@Override
	public void trace(final String msg, final Throwable t) {

	}

	@Override
	public boolean isDebugEnabled() {
		return Platform.inDebugMode();
	}

	@Override
	public void debug(final String msg) {
		if (isDebugEnabled()) {
			doLog(IStatus.INFO, msg, null);
		}
	}

	@Override
	public void debug(final String format, final Object arg) {
		if (isDebugEnabled()) {
			doLog(IStatus.INFO, String.format(format, arg), null);
		}
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		if (isDebugEnabled()) {
			doLog(IStatus.INFO, String.format(format, arg1, arg2), null);
		}
	}

	@Override
	public void debug(final String format, final Object[] argArray) {

		if (isDebugEnabled()) {
			doLog(IStatus.INFO, String.format(format, argArray), null);
		}
	}

	@Override
	public void debug(final String msg, final Throwable t) {

		if (isDebugEnabled()) {
			doLog(IStatus.INFO, msg, t);
		}
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public void info(final String msg) {
		doLog(IStatus.INFO, msg, null);
	}

	@Override
	public void info(final String format, final Object arg) {
		doLog(IStatus.INFO, String.format(format, arg), null);
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		doLog(IStatus.INFO, String.format(format, arg1, arg2), null);

	}

	@Override
	public void info(final String format, final Object[] argArray) {
		doLog(IStatus.INFO, String.format(format, argArray), null);
	}

	@Override
	public void info(final String msg, final Throwable t) {
		doLog(IStatus.INFO, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public void warn(final String msg) {
		doLog(IStatus.WARNING, msg, null);

	}

	@Override
	public void warn(final String format, final Object arg) {
		doLog(IStatus.WARNING, String.format(format, arg), null);

	}

	@Override
	public void warn(final String format, final Object[] argArray) {
		doLog(IStatus.WARNING, String.format(format, argArray), null);

	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		doLog(IStatus.WARNING, String.format(format, arg1, arg2), null);
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		doLog(IStatus.WARNING, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public void error(final String msg) {
		doLog(IStatus.ERROR, msg, null);
	}

	@Override
	public void error(final String format, final Object arg) {
		doLog(IStatus.ERROR, String.format(format, arg), null);
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		doLog(IStatus.ERROR, String.format(format, arg1, arg2), null);
	}

	@Override
	public void error(final String format, final Object[] argArray) {
		doLog(IStatus.ERROR, String.format(format, argArray), null);
	}

	@Override
	public void error(final String msg, final Throwable t) {
		doLog(IStatus.ERROR, msg, t);
	}

	private void doLog(final int severity, final String msg, final Throwable t) {
		getLogger().log(new Status(severity, getName(), msg, t));
	}

	private ILog getLogger() {
		return Platform.getLog(Platform.getBundle("org.slf4j.api"));
	}
}
