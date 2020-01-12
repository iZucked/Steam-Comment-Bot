/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.util.Locale;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferenceConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class AbstractSWTBotTest {

	private final CyclicBarrier swtBarrier = new CyclicBarrier(2);
	protected static Shell shell;
	protected static SWTBot bot;
	protected static Thread uiThread;
	protected static Display display;

	@BeforeClass
	public static void setKeyboard() {
		// For typeText, set US keyboard layout as GB does not seem to be supported
		System.setProperty(SWTBotPreferenceConstants.KEY_KEYBOARD_LAYOUT, "EN_US");
	}
	@BeforeClass
	public static void setLocale() {
		// Enforce UK Locale Needed for running tests on build server. Keeps date format consistent.
		Locale.setDefault(Locale.UK);
		DateTimeFormatsProvider.INSTANCE.setDefaultDayMonthFormats();
	}

	@Before
	public void createDisplay() throws InterruptedException, BrokenBarrierException {

		uiThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create or reuse existing display
				display = Display.getDefault();
				shell = new Shell(display);

				shell.setText("Testing: " + getClass().getName());
				shell.setLayout(new FillLayout());

				bot = new SWTBot(shell);
				shell.open();

				// Pause until we near the exit of createDisplay method
				try {
					swtBarrier.await();
				} catch (final Exception e) {
					e.printStackTrace();
				}
				try {
					while (!display.isDisposed() && !shell.isDisposed()) {
						try {
							if (!display.readAndDispatch()) {
								display.sleep();
							}
						} catch (Throwable t) {

						}
					}
				} finally {
					// Clean up display
					if (!display.isDisposed()) {
						display.dispose();
					}
				}
			}
		}, "UI Thead");
		// Make daemon so that JVM can exit while thread is still running.
		uiThread.setDaemon(true);
		uiThread.start();

		// Wait for display to be open before exiting this method
		swtBarrier.await();
	}

	@After
	public void disposeDisplay() throws InterruptedException {
		// Ensure display is disposed
		if (display != null && !display.isDisposed()) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (display != null && !display.isDisposed()) {
						display.dispose();
					}
				}
			});
		}
		// Wait for UI thread to finish
		uiThread.join();
		// Reset variables
		uiThread = null;
		shell = null;
		display = null;
	}
}
