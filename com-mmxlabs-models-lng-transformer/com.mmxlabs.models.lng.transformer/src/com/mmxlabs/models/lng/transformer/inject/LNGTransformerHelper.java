/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * @author Simon Goodall
 */
public class LNGTransformerHelper {

	private LNGTransformerHelper() {

	}

	/**
	 * Ignore cache override settings as we will re-override in a unit test module and need to avoid binding clashes
	 */
	public static final @NonNull String HINT_TESTING_IGNORE_CACHE_SETTINGS = "hint-ignore-cache-settings";

	/**
	 */
	public static final @NonNull String HINT_OPTIMISE_LSO = "hint-lngtransformer-optimise-lso";
	public static final @NonNull String HINT_OPTIMISE_INSERTION = "hint-lngtransformer-optimise-insertion";
	/**
	 * Hint to indicate this is for evaluation only
	 */
	public static final @NonNull String HINT_EVALUATION_ONLY = "hint-lngtransformer-evaluation";
	/**
	 */
	public static final @NonNull String HINT_OPTIMISE_BREAKDOWN = "hint-lngtransformer-optimise-breakdown";

	public static final @NonNull String HINT_GENERATE_CHARTER_OUTS = "hint-lngtransformer-generate-charter-outs";
	public static final @NonNull String HINT_CHARTER_LENGTH = "hint-lngtransformer-charter-length";
	public static final @NonNull String HINT_CLEAN_STATE_EVALUATOR = "hint-lngtransformer-clean-state-evaluator";

	public static final @NonNull String HINT_SHIPPING_ONLY = "hint-lngtransformer-shipping-only";
	public static final @NonNull String HINT_SPOT_CARGO_MARKETS = "hint-lngtransformer-spot-cargo-markets";
	public static final @NonNull String HINT_NO_NOMINALS_IN_PROMPT = "hint-lngtransformer-no-nominals-in-prompt";

	// Used to override the license feature linked HINT_NO_NOMINALS_IN_PROMPT
	public static final @NonNull String HINT_KEEP_NOMINALS_IN_PROMPT = "hint-lngtransformer-keep-nominals-in-prompt";

	public static final @NonNull String HINT_DISABLE_CACHES = "hint-lngtransformer-disable-caches";
	public static final @NonNull String HINT_PORTFOLIO_BREAKEVEN = "hint-portfolio-breakeven";
	public static final @NonNull String HINT_NOMINAL_ADP = "hint-nominal-adp";
	
	public static final @NonNull String HINT_PERIOD_SCENARIO = "hint-period-scenario";

	// Turn auto-heding using flat curve on/off
	public static final @NonNull String HINT_GENERATED_PAPERS_PNL = "hint-generated-papers-pnl";
	
	@NonNull
	public static Set<@NonNull String> getHints(@NonNull final UserSettings userSettings, @NonNull final String @Nullable... initialHints) {

		final Set<@NonNull String> hints = new HashSet<>();
		// Check hints
		if (initialHints != null) {
			for (final String hint : initialHints) {
				if (hint != null) {
					hints.add(hint);
				}
			}
		}
		if (userSettings.isGenerateCharterOuts()) {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_CHARTER_OUT_GENERATION)) {
				hints.add(HINT_GENERATE_CHARTER_OUTS);
			}
		}
		if (userSettings.isWithCharterLength()) {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CHARTER_LENGTH)) {
				hints.add(HINT_CHARTER_LENGTH);
			}
		}
		if (userSettings.isShippingOnly()) {
			hints.add(HINT_SHIPPING_ONLY);
		} else if (userSettings.isWithSpotCargoMarkets()) {
			hints.add(HINT_SPOT_CARGO_MARKETS);
		}
		if (userSettings.getMode() != OptimisationMode.ADP && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_NO_NOMINALS_IN_PROMPT)) {
			hints.add(HINT_NO_NOMINALS_IN_PROMPT);
		}
		// If HINT_KEEP_NOMINALS_IN_PROMPT is set, override the HINT_NO_NOMINALS_IN_PROMPT hint
		if (hints.contains(HINT_KEEP_NOMINALS_IN_PROMPT)) {
			hints.remove(HINT_NO_NOMINALS_IN_PROMPT);
		}

		// Too late for LNGScenarioRunner, but add to hints for modules in case it is needed in the future.
		if (userSettings.isBuildActionSets()) {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_ACTIONSET)) {
				hints.add(HINT_OPTIMISE_BREAKDOWN);
			}
		}

		if (userSettings.isNominalOnly()) {
			hints.add(HINT_NOMINAL_ADP);
		}
		if (userSettings.isGeneratedPapersInPNL()) {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
				hints.add(HINT_GENERATED_PAPERS_PNL);
			}
		}

		return hints;
	}

	@NonNull
	public static Collection<@NonNull Module> getModulesWithOverrides(@NonNull final Module mainModule, @NonNull final Collection<@NonNull IOptimiserInjectorService> services,
			final IOptimiserInjectorService.@NonNull ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
		final List<@NonNull Module> overrides = new LinkedList<>();
		collectModuleOverrides(moduleType, services, overrides, hints);
		final LinkedList<@NonNull Module> modules = new LinkedList<>();
		if (overrides.isEmpty()) {
			modules.add(mainModule);
		} else {
			modules.add(Modules.override(mainModule).with(overrides));
		}
		modules.addAll(collectModules(moduleType, services, hints));
		return modules;
	}

	@NonNull
	public static Collection<@NonNull Module> getModulesWithOverrides(@NonNull final List<@NonNull Module> mainModules, @NonNull final Collection<@NonNull IOptimiserInjectorService> services,
			final IOptimiserInjectorService.@NonNull ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
		final List<@NonNull Module> overrides = new LinkedList<>();
		collectModuleOverrides(moduleType, services, overrides, hints);
		final LinkedList<@NonNull Module> modules = new LinkedList<>();
		if (overrides.isEmpty()) {
			modules.addAll(mainModules);
		} else {
			modules.add(Modules.override(mainModules).with(overrides));
		}
		modules.addAll(collectModules(moduleType, services, hints));
		return modules;
	}

	@NonNull
	public static Collection<@NonNull Module> collectModules(final IOptimiserInjectorService.@NonNull ModuleType moduleType, @NonNull final Collection<@NonNull IOptimiserInjectorService> services,
			@NonNull final Collection<@NonNull String> hints) {
		final List<@NonNull Module> modules = new LinkedList<>();
		for (final IOptimiserInjectorService s : services) {
			if (s != null) {
				final Module m = s.requestModule(moduleType, hints);
				if (m != null) {
					modules.add(m);
				}
			}
		}
		return modules;
	}

	public static void collectModuleOverrides(final IOptimiserInjectorService.@NonNull ModuleType moduleType, @NonNull final Collection<@NonNull IOptimiserInjectorService> moduleOverrides,
			@NonNull final List<@NonNull Module> overrides, @NonNull final Collection<@NonNull String> hints) {

		for (final IOptimiserInjectorService s : moduleOverrides) {
			if (s != null) {
				final List<Module> l = s.requestModuleOverrides(moduleType, hints);
				if (l != null) {
					overrides.addAll(l);
				}
			}
		}
	}

	/**
	 * Query the OSGi service registry for {@link IOptimiserInjectorService}s. Use the bootstrapModule parameter to specify an additional module to use to help with this. I.e. a JUnit test will not
	 * start OSGi and we need to bind the services directly in the module. Also permit a caller specified {@link IOptimiserInjectorService} implementation to add to the list. Note: the bootstrapModule
	 * will only be used if the OSGi platform is not running.
	 * 
	 * @param bootstrapModule
	 * @param extraService
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@NonNull
	public static Collection<@NonNull IOptimiserInjectorService> getOptimiserInjectorServices(@Nullable final Module bootstrapModule, @Nullable final IOptimiserInjectorService extraService) {

		final List<@NonNull IOptimiserInjectorService> services = new LinkedList<>();
		final Injector tmpInjector;
		if (Platform.isRunning()) {
			// Create temp injector to grab extraModules from OSGi services
			final AbstractModule optimiserInjectorServiceModule = new AbstractModule() {

				@Override
				protected void configure() {
					bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toProvider(Peaberry.service(IOptimiserInjectorService.class).multiple());
				}
			};
			final List<@NonNull Module> m = new ArrayList<>(3);
			m.add(Peaberry.osgiModule(FrameworkUtil.getBundle(LNGTransformerHelper.class).getBundleContext()));
			m.add(optimiserInjectorServiceModule);
			if (bootstrapModule != null) {
				m.add(bootstrapModule);
			}
			tmpInjector = Guice.createInjector(m);
		} else if (bootstrapModule != null) {
			tmpInjector = Guice.createInjector(bootstrapModule);
		} else {
			tmpInjector = null;
		}

		if (tmpInjector != null) {
			final Key<Iterable<? extends IOptimiserInjectorService>> key = Key.<Iterable<? extends IOptimiserInjectorService>> get(TypeLiterals.iterable(IOptimiserInjectorService.class));
			for (final IOptimiserInjectorService service : (Iterable<@NonNull IOptimiserInjectorService>) tmpInjector.getInstance(key)) {
				services.add(service);
			}
		}

		if (extraService != null) {
			services.add(extraService);
		}

		return services;
	}

	@NonNullByDefault
	public static void updateHintsFromUserSettings(UserSettings userSettings, Collection<String> hints) {

		if (userSettings.isGenerateCharterOuts()) {
			hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
		} else {
			hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
		}
		if (userSettings.isWithCharterLength()) {
			hints.add(LNGTransformerHelper.HINT_CHARTER_LENGTH);
		} else {
			hints.remove(LNGTransformerHelper.HINT_CHARTER_LENGTH);
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			if (userSettings.isGeneratedPapersInPNL()) {
				hints.add(LNGTransformerHelper.HINT_GENERATED_PAPERS_PNL);
			} else {
				hints.remove(LNGTransformerHelper.HINT_GENERATED_PAPERS_PNL);
			}
		} else {
			hints.remove(LNGTransformerHelper.HINT_GENERATED_PAPERS_PNL);
		}
	}

}
