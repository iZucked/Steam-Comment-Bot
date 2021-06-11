/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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

	public static final String FEATURE_REENCRYPT = "features:reencrypt";

	
	public static final String FEATURE_MODULE_DIFF_TOOLS = "features:difftools";
	
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
	public static final String FEATURE_OPTIONISER_EVENTS = "features:options-suggester-events";

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
	 * Enable the contingency idle time matrix
	 */
	public static final String FEATURE_CONTINGENCY_IDLE_TIME = "features:contingency-idle-time";

	/**
	 * Enable Exposures calculations
	 */
	public static final String FEATURE_EXPOSURES = "features:exposures";
	public static final String FEATURE_EXPOSURES_IGNORE_ENERGY_CONVERSION = "features:exposures-ignore-energy-conversion";
	public static final String FEATURE_EXPOSURES_CUTOFF_AT_PROMPT_START = "features:exposures-cutoff-at-prompt-start";

	public static final String FEATURE_INVENTORY_MODEL = "features:inventory-model";
	
	public static final String FEATURE_MULL_SLOT_GENERATION = "features:mull-slot-generation";

	public static final String FEATURE_PAPER_DEALS = "features:paperdeals";

	public static final String FEATURE_GENERATED_PAPER_DEALS = "features:generated-papers";
	public static final String FEATURE_RE_HEDGE_CUTOFF_AT_PROMPT_START = "features:re-hedge-cutoff-at-prompt-start";

	public static final String FEATURE_DEAL_SETS_GENERATE_FROM_CARGOES = "features:deal-sets-generate-from-cargoes";

	public static final String FEATURE_DEAL_SETS_GENERATE_FROM_CONTRACTS = "features:deal-sets-generate-from-contracts";

	public static final String FEATURE_DEAL_SETS_GENERATE_FROM_CURVES = "features:deal-sets-generate-from-curves";

	public static final String FEATURE_DEAL_SETS_GENERATE_FROM_INDICES = "features:deal-sets-generate-from-indices";

	/**
	 * Enable the charter length evaluation mode
	 */
	public static final String FEATURE_CHARTER_LENGTH = "features:charter-length";

	public static final String FEATURE_ADP = "features:adp";
	public static final String FEATURE_STRATEGIC = "features:optimisation-strategic";
	public static final String FEATURE_LONG_TERM = "features:longterm";

	// Experimental
	public static final String FEATURE_TRADER_BASED_INSERIONS = "features:trader-based-insertions";

	public static final String FEATURE_RE_EVALUATE_SOLUTIONS = "features:re-evaluate-solutions";

	public static final String FEATURE_SANDBOX = "features:sandbox";
	
	public static final String FEATURE_SANDBOX_CHARTER_INS = "features:sandbox-charter-ins";

	public static final String FEATURE_BREAK_EVEN_TABLE = "features:break-even-table";

	public static final String FEATURE_NOMINATIONS = "features:nominations";

	public static final String FEATURE_OPEN_SLOT_EXPOSURE = "features:open-slot-exposure";

	public static final String FEATURE_PURGE = "features:purge";

	public static final String FEATURE_DATAHUB_BASECASE_ARCHIVE = "features:datahub-basecase-archive";

	public static final String FEATURE_DATAHUB_BASECASE_NOTES = "features:datahub-basecase-notes";

	public static final String FEATURE_DATAHUB_STARTUP_CHECK = "features:datahub-lingo-startup-check";

	public static final String FEATURE_SHOW_TRADING_SHIPPING_SPLIT = "features:headline-trading-shipping-split";

	public static final String FEATURE_ADP_PROFILE_CONSTRAINTS_SUMMARY = "features:adp-profile-constraints-summary";

	/**
	 * Scenario repair features
	 */
	
	public static final String FEATURE_REPAIR_DELETE_ALL_EMPTY_CARGOES = "features:repair-delete-all-empty-cargoes";
	
	/** 
	 * Headline report entries
	 */
	
	public static final String FEATURE_HEADLINE_CHARTER_LENGTH = "features:headline-charter-length";
	public static final String FEATURE_HEADLINE_NOMINALS = "features:headline-nominals";
	public static final String FEATURE_HEADLINE_UPSIDE = "features:report-headline-upside";
	public static final String FEATURE_HEADLINE_SALES_REVENUE = "features:headline-sales-revenue";
	public static final String FEATURE_HEADLINE_EQUITY_BOOK = "features:report-equity-book";
	public static final String FEATURE_HEADLINE_IDLE_DAYS = "features:headline-idle-days";
	public static final String FEATURE_HEADLINE_PURCHASE_COST = "features:headline-purchase-cost";
	
}
