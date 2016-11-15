/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parameters;

import java.time.YearMonth;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferenceConstants;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ParameterModesDialogTest {

	private final CyclicBarrier swtBarrier = new CyclicBarrier(2);
	protected static Shell shell;
	protected static SWTBot bot;
	protected static Thread uiThread;
	protected static Display display;
	protected ParameterModesDialog dialog;

	@BeforeClass
	public static void init() {
		// Enable these features for the dialog test
		LicenseFeatures.initialiseFeatureEnablements("optimisation-charter-out-generation", "optimisation-period", "optimisation-similarity", "optimisation-actionset");
	}

	@BeforeClass
	public static void setKeyboard() {
		// For typeText, set US keyboard layout as GB does not seem to be supported
		System.setProperty(SWTBotPreferenceConstants.KEY_KEYBOARD_LAYOUT, "EN_US");
	}

	@Test
	public void testSetShippingOnly_On() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setShippingOnly(false);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_SHIPPING_ONLY_ON).click(), u -> Assert.assertTrue(u.isShippingOnly()));
	}

	@Test
	public void testSetShippingOnly_Off() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setShippingOnly(true);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_SHIPPING_ONLY_OFF).click(), u -> Assert.assertFalse(u.isShippingOnly()));
	}

	@Test
	public void testSetGenerateCharterOuts_On() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setGenerateCharterOuts(false);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_CHARTEROUTGENERATION_ON).click(), u -> Assert.assertTrue(u.isGenerateCharterOuts()));
	}

	@Test
	public void testSetGenerateCharterOuts_Off() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setGenerateCharterOuts(true);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_CHARTEROUTGENERATION_OFF).click(), u -> Assert.assertFalse(u.isGenerateCharterOuts()));
	}

	@Test
	public void testSetSimilarityMode_Low() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setSimilarityMode(SimilarityMode.OFF);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_LOW).click(), u -> Assert.assertEquals(SimilarityMode.LOW, u.getSimilarityMode()));
	}

	@Test
	public void testSetSimilarityMode_Medium() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setSimilarityMode(SimilarityMode.OFF);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_MEDIUM).click(), u -> Assert.assertEquals(SimilarityMode.MEDIUM, u.getSimilarityMode()));
	}

	@Test
	public void testSetSimilarityMode_High() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.setSimilarityMode(SimilarityMode.OFF);

		executeValidTest(settings, b -> b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_HIGH).click(), u -> Assert.assertEquals(SimilarityMode.HIGH, u.getSimilarityMode()));
	}

	@Test
	public void testSetPeriodStart() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();

		executeValidTest(settings, b -> b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("02/2015"), u -> {
			Assert.assertEquals(YearMonth.of(2015, 2), u.getPeriodStart()); //
			Assert.assertFalse(u.isSetPeriodEnd()); //
		});
	}

	@Test
	public void testSetPeriodEnd() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();

		executeValidTest(settings, b -> {
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("04/2015");//
		}, u -> {
			Assert.assertFalse(u.isSetPeriodStart()); //
			Assert.assertEquals(YearMonth.of(2015, 4), u.getPeriodEnd()); //
		});
	}

	@Test
	public void testSetPeriodStartEnd() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();

		executeValidTest(settings, b -> {
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("02/2015");//
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("04/2015");//
		}, u -> {
			Assert.assertEquals(YearMonth.of(2015, 2), u.getPeriodStart()); //
			Assert.assertEquals(YearMonth.of(2015, 4), u.getPeriodEnd()); //
		});
	}

	@Test
	public void testActionSet_Off_NoPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeInvalidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
		}, b -> {
			Assert.assertFalse(b.button("OK").isEnabled());
		});
	}

	@Test
	public void testActionSet_Low_1mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeValidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_LOW).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("02/2015"); //
		}, u -> {
			Assert.assertTrue(u.isBuildActionSets());//
			Assert.assertEquals(SimilarityMode.LOW, u.getSimilarityMode()); //
			Assert.assertEquals(YearMonth.of(2015, 1), u.getPeriodStart()); //
			Assert.assertEquals(YearMonth.of(2015, 2), u.getPeriodEnd()); //
		});
	}

	@Test
	public void testActionSet_Low_3mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeValidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_LOW).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("04/2015"); //
		}, u -> {
			Assert.assertTrue(u.isBuildActionSets());//
			Assert.assertEquals(SimilarityMode.LOW, u.getSimilarityMode()); //
			Assert.assertEquals(YearMonth.of(2015, 1), u.getPeriodStart()); //
			Assert.assertEquals(YearMonth.of(2015, 4), u.getPeriodEnd()); //
		});
	}

	@Test
	public void testActionSet_Medium_6mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeValidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_MEDIUM).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("07/2015"); //
		}, u -> {
			Assert.assertTrue(u.isBuildActionSets());//
			Assert.assertEquals(SimilarityMode.MEDIUM, u.getSimilarityMode()); //
			Assert.assertEquals(YearMonth.of(2015, 1), u.getPeriodStart()); //
			Assert.assertEquals(YearMonth.of(2015, 7), u.getPeriodEnd()); //
		});
	}

	@Test
	public void testActionSet_High_6mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeValidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_HIGH).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("07/2015"); //
		}, u -> {
			Assert.assertTrue(u.isBuildActionSets());//
			Assert.assertEquals(SimilarityMode.HIGH, u.getSimilarityMode()); //
			Assert.assertEquals(YearMonth.of(2015, 1), u.getPeriodStart()); //
			Assert.assertEquals(YearMonth.of(2015, 7), u.getPeriodEnd()); //
		});
	}

	@Test
	public void testActionSet_Low_4mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeInvalidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_LOW).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("04/2015"); //
		}, b -> {
			Assert.assertFalse(b.button("OK").isEnabled());
		});
	}

	@Test
	public void testActionSet_Med_7mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeInvalidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_MEDIUM).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("08/2015"); //
		}, b -> {
			Assert.assertFalse(b.button("OK").isEnabled());
		});
	}

	@Test
	public void testActionSet_High_7mPeriod() throws Exception {
		final UserSettings settings = ScenarioUtils.createDefaultUserSettings();
		settings.unsetPeriodStart();
		settings.unsetPeriodEnd();
		settings.setSimilarityMode(SimilarityMode.OFF);
		settings.setBuildActionSets(false);

		executeInvalidTest(settings, b -> {
			b.radioWithId(OptimisationHelper.SWTBOT_ACTION_SET_ON).click(); //
			b.radioWithId(OptimisationHelper.SWTBOT_SIMILARITY_PREFIX_HIGH).click(); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_START).setText("01/2015"); //
			b.textWithId(OptimisationHelper.SWTBOT_PERIOD_END).setText("08/2015"); //
		}, b -> {
			Assert.assertFalse(b.button("OK").isEnabled());
		});
	}

	@FunctionalInterface
	interface IManipulatorFunction {
		void manipulate(SWTBot b);
	}

	@FunctionalInterface
	interface IStateCheckerFunction {
		void check(SWTBot b);
	}

	@FunctionalInterface
	interface IValidationFunction {
		void validate(UserSettings u);
	}

	public void executeInvalidTest(final UserSettings initialSettings, final IManipulatorFunction manipulator, final IStateCheckerFunction checker) throws Exception {
		final Future<UserSettings> future = executeTest(initialSettings, manipulator);
		final SWTBot b = new SWTBot(bot.shell("Optimisation Settings").widget);

		manipulator.manipulate(b);

		checker.check(b);

		b.button("Cancel").click();
	}

	public void executeValidTest(final UserSettings initialSettings, final IManipulatorFunction manipulator, final IValidationFunction validator) throws Exception {
		final Future<UserSettings> future = executeTest(initialSettings, manipulator);
		final SWTBot b = new SWTBot(bot.shell("Optimisation Settings").widget);

		// Buttons react quickly to changes. Text fields take some time to propagate to the model so we try to control with syncExec and sleep calls, otherwise the returned settings object may not yet
		// have the settings! This could be an issue in the application should the user be quick fingered....
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				manipulator.manipulate(b);
			}
		});

		bot.sleep(700);

		display.syncExec(new Runnable() {
			@Override
			public void run() {
				b.button("OK").click();
			}
		});

		validator.validate(future.get());
	}

	public Future<UserSettings> executeTest(final UserSettings initialSettings, final IManipulatorFunction manipulator) throws Exception {
		Assert.assertSame(display, bot.getDisplay());

		final ExecutorService executor = Executors.newSingleThreadExecutor();

		// final CyclicBarrier barrier = new CyclicBarrier(2);
		final Future<UserSettings> future = executor.submit(new Callable<UserSettings>() {

			@Override
			public UserSettings call() throws Exception {
				try {
					// Create a dummy model with minimal data need to get tests working correctly.
					final LNGScenarioModel dummyModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
					final CargoModel dummyCargoModel = CargoFactory.eINSTANCE.createCargoModel();
					dummyModel.setCargoModel(dummyCargoModel);
					return OptimisationHelper.openUserDialog(dummyModel, display, shell, false, initialSettings, initialSettings, false);
				} catch (final Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});

		bot.waitUntil(Conditions.shellIsActive("Optimisation Settings"));

		return future;
	}

	@Before
	public void createDisplay() throws InterruptedException, BrokenBarrierException {

		uiThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create or reuse existing display
				display = Display.getDefault();
				shell = new Shell(display);
				Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
					public void run() {
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
								} catch (final Throwable t) {

								}
							}
						} finally {
							// Clean up display
							if (!display.isDisposed()) {
								display.dispose();
							}
						}
					}
				});
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
