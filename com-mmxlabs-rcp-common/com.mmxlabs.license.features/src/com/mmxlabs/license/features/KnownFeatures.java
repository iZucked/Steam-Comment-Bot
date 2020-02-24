/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.license.features;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * This really should not be here.... but a shared place to store feature constants.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public final class KnownFeatures {
	private KnownFeatures() {

	}

	/**
	 * Enable parallelisation in optimiser
	 */
	public static final String FEATURE_MODULE_PARALLELISATION = "features:module-parallelisation";

	public static final String FEATURE_OPTIMISATION_ACTIONSET = "features:optimisation-actionset";
	public static final String FEATURE_OPTIMISATION_HILLCLIMB = "features:optimisation-hillclimb";
	public static final String FEATURE_OPTIMISATION_PERIOD = "features:optimisation-period";
	public static final String FEATURE_OPTIMISATION_CHARTER_OUT_GENERATION = "features:optimisation-charter-out-generation";
	public static final String FEATURE_OPTIMISATION_SIMILARITY = "features:optimisation-similarity";
	public static final String FEATURE_OPTIMISATION_NO_NOMINALS_IN_PROMPT = "features:no-nominal-in-prompt";

	public static final String FEATURE_OPTIONISER = "features:options-suggester";

	public static final String FEATURE_SHIP_TO_SHIP = "features:shiptoship";

	/**
	 * Feature to disable optimisation during PNL testing phase of a deployment
	 */
	public static final String FEATURE_PHASE_PNL_TESTING = "features:phase-pnl-testing";

	/**
	 * Feature to limit optimisation during PNL testing phase of a deployment
	 */
	public static final String FEATURE_PHASE_LIMITED_TESTING = "features:phase-limited-testing";

	/**
	 * Enable Panama Canal - distances and tariff calculation
	 */
	public static final String FEATURE_PANAMA_CANAL = "features:panama-canal";

	/**
	 * Enable Suez Canal tariff calculation
	 */
	public static final String FEATURE_SUEZ_CANAL = "features:suez-canal";

	/**
	 * Enable the contingency idle time matrix
	 */
	public static final String FEATURE_CONTINGENCY_IDLE_TIME = "features:contingency-idle-time";

	/**
	 * Enable Exposures calculations
	 */
	public static final String FEATURE_EXPOSURES = "features:exposures";

	public static final String FEATURE_INVENTORY_MODEL = "features:inventory-model";

	public static final String FEATURE_PAPER_DEALS = "features:paperdeals";
	
	public static final String FEATURE_GENERATED_PAPER_DEALS = "features:generated-papers";
	/**
	 * Enable counterparty volume calculations
	 */
	public static final String FEATURE_COUNTER_PARTY_VOLUME = "features:counter-party-volume";

	/**
	 * Enable the charter length evaluation mode
	 */
	public static final String FEATURE_CHARTER_LENGTH = "features:charter-length";

	public static final String FEATURE_ADP = "features:adp";
	public static final String FEATURE_STRATEGIC = "features:optimisation-strategic";

	// Experimental
	public static final String FEATURE_TRADER_BASED_INSERIONS = "features:trader-based-insertions";

	/**
	 * Enable break-even evaluations
	 */
	public static final String FEATURE_BREAK_EVENS = "features:break-evens";

	public static final String FEATURE_RE_EVALUATE_SOLUTIONS = "features:re-evaluate-solutions";

	public static final String FEATURE_SANDBOX = "features:sandbox";

	public static final String FEATURE_BREAK_EVEN_TABLE = "features:break-even-table";

	public static final String FEATURE_NOMINATIONS = "features:nominations";

	public static final String FEATURE_OPEN_SLOT_EXPOSURE = "features:open-slot-exposure";

	public static final String FEATURE_PURGE = "features:purge";

	public static final String FEATURE_DATAHUB_BASECASE_ARCHIVE = "features:datahub-basecase-archive";

	public static final String FEATURE_DATAHUB_BASECASE_NOTES = "features:datahub-basecase-notes";

	public static final String FEATURE_DATAHUB_STARTUP_CHECK = "features:datahub-lingo-startup-check";

	public static final String FEATURE_SHOW_TRADING_SHIPPING_SPLIT = "features:headline-trading-shipping-split";
}
