package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLoggingSequenceScheduler {

	private static final Logger log = LoggerFactory.getLogger(AbstractLoggingSequenceScheduler.class);

	private BufferedWriter logWriter;
	private static boolean loggingEnabled = false;

	public static void setLoggingEnabled(final boolean loggingEnabled) {
		AbstractLoggingSequenceScheduler.loggingEnabled = loggingEnabled;
	}

	private static int tag = 0;

	private static synchronized int getTag() {
		return tag++;
	}

	protected final void createLog() {
		if (!loggingEnabled) {
			return;
		}
		try {
			final String name = getClass().getSimpleName() + "_log_" + getTag();
			final File f = new File("./" + name + ".py");
			logWriter = new BufferedWriter(new FileWriter(f));

			log.debug("Created scheduler log " + f.getAbsolutePath());
		} catch (final IOException ex) {
		}
	}

	protected final void startLogEntry(final int sequenceSize) {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write("Schedule(" + sequenceSize + ",[");
		} catch (final IOException e) {

		}
	}

	protected final void logValue(final long fitness) {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write(fitness + ", ");
		} catch (final IOException e) {
		}
	}

	protected final void endLogEntry() {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write("])\n");
			logWriter.flush();
		} catch (final IOException e) {
		}
	}

	protected final void closeLog() {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.flush();
			logWriter.close();
		} catch (final IOException e) {
		}
		logWriter = null;
	}
}
