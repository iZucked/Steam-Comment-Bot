/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
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
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * @author Simon Goodall
 */
public class LNGTransformerHelper {

	/**
	 */
	public static final String HINT_OPTIMISE_LSO = "hint-lngtransformer-optimise-lso";
	/**
	 */
	public static final String HINT_OPTIMISE_BREAKDOWN = "hint-lngtransformer-optimise-breakdown";

	public static final String HINT_GENERATE_CHARTER_OUTS = "hint-lngtransformer-generate-charter-outs";

	@NonNull
	public static Set<String> getHints(@NonNull final OptimiserSettings settings, @Nullable final String... initialHints) {

		final Set<String> hints = new HashSet<String>();
		// Check hints
		if (initialHints != null) {
			for (final String hint : initialHints) {
				hints.add(hint);
			}
		}
		if (settings.isGenerateCharterOuts()) {
			if (SecurityUtils.getSubject().isPermitted("features:optimisation-charter-out-generation")) {
				hints.add(HINT_GENERATE_CHARTER_OUTS);
			}
		}

		// Too late for LNGScenarioRunner, but add to hints for modules in case it is needed in the future.
		if (settings.isBuildActionSets()) {
			if (SecurityUtils.getSubject().isPermitted("features:optimisation-actionset")) {
				hints.add(HINT_OPTIMISE_BREAKDOWN);
			}
		}

		return hints;
	}
}
