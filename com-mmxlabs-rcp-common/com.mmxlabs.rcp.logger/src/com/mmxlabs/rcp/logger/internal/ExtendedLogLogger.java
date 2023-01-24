/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.logger.internal;

import java.util.Arrays;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.slf4j.helpers.MarkerIgnoringBase;

public class ExtendedLogLogger extends MarkerIgnoringBase {

	private static final long serialVersionUID = 1L;

	private boolean infoEnabled = false;
	private boolean debugEnabled = false;

	private boolean debugFlagEnabled = Arrays.stream(System.getProperty("eclipse.commands").split("\n")).anyMatch("-debug"::equalsIgnoreCase);

	public ExtendedLogLogger(final String name) {
		this.name = name;
		// TODO: Hack to disable excess log messages from shiro. Build some better API
		infoEnabled = !("org.apache.shiro.session.mgt.AbstractValidatingSessionManager".equals(name)
				|| "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping".equals(name) //
				|| "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping".equals(name) //
				|| "org.springframework.core.annotation.AnnotationUtils".equals(name) //
				|| "org.mongodb.driver.cluster".equals(name) //
				|| "org.mongodb.morphia.logging.MorphiaLoggerFactory".equals(name) //
				|| "org.springframework.boot.web.servlet.FilterRegistrationBean".equals(name) //
				|| "com.mmxlabs.lngdataservice.central.EmbeddedMongoMorphiaAutoConfiguration".equals(name) //
		);
		debugEnabled = !(name.startsWith("org.springframework")//
				|| name.startsWith("org.eclipse.jetty")//
				|| name.startsWith("org.asynchttpclient.netty")//
		);
	}

	@Override
	public boolean isTraceEnabled() {
		return Platform.inDebugMode();
	}

	@Override
	public void trace(final String msg) {
		if (isTraceEnabled()) {
			doLog(IStatus.INFO, msg, null);
		}
	}

	@Override
	public void trace(final String format, final Object arg) {
		if (isTraceEnabled()) {
			doLog(IStatus.INFO, String.format(format, arg), null);
		}
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		if (isTraceEnabled()) {
			doLog(IStatus.INFO, String.format(format, arg1, arg2), null);
		}
	}

	@Override
	public void trace(final String format, final Object... argArray) {
		if (isTraceEnabled()) {
			doLog(IStatus.INFO, String.format(format, argArray), null);
		}
	}

	@Override
	public void trace(final String msg, final Throwable t) {

		if (isTraceEnabled()) {
			doLog(IStatus.INFO, msg, t);
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return debugFlagEnabled && debugEnabled;
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
	public void debug(final String format, final Object... argArray) {

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
		return infoEnabled;
	}

	@Override
	public void info(final String msg) {
		if (isInfoEnabled()) {
			doLog(IStatus.INFO, msg, null);
		}
	}

	@Override
	public void info(final String format, final Object arg) {
		if (isInfoEnabled()) {
			doLog(IStatus.INFO, String.format(format, arg), null);
		}
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		if (isInfoEnabled()) {
			doLog(IStatus.INFO, String.format(format, arg1, arg2), null);
		}

	}

	@Override
	public void info(final String format, final Object... argArray) {
		if (isInfoEnabled()) {
			doLog(IStatus.INFO, String.format(format, argArray), null);
		}
	}

	@Override
	public void info(final String msg, final Throwable t) {
		if (isInfoEnabled()) {
			doLog(IStatus.INFO, msg, t);
		}
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
	public void warn(final String format, final Object... argArray) {
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
	public void error(final String format, final Object... argArray) {
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
		try {
			return Platform.getLog(Platform.getBundle("org.slf4j.api"));
		} catch (NullPointerException e) {
			// This can happen during workbench shutdown.
			return new ILog() {

				@Override
				public void removeLogListener(ILogListener listener) {

				}

				@Override
				public void log(IStatus status) {
					System.err.printf("[Fallback logger]%s", status.getMessage());
				}

				@Override
				public Bundle getBundle() {
					return null;
				}

				@Override
				public void addLogListener(ILogListener listener) {

				}
			};
		}
	}
}
