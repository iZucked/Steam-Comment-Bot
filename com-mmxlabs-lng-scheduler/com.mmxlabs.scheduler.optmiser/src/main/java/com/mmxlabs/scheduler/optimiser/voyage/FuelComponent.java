package com.mmxlabs.scheduler.optimiser.voyage;

/**
 * Enum describing the possible breakdowns of fuel components. Each component
 * represents a specific calculation required in a voyage calculation.
 * 
 * @author Simon Goodall
 * 
 */
public enum FuelComponent {

	/**
	 * Voyage used base fuel as main fuel source.
	 */
	Base, 
	
	/**
	 * Voyage used NBO as main fuel source
	 */
	NBO, 
	
	/**
	 * NBO Voyage is supplemented by FBO.
	 */
	FBO,
	
	/**
	 * NBO Voyage is supplemented by base fuel.
	 */
	Base_Supplemental, 
	
	/**
	 * Idle time NBO use
	 */
	IdleNBO, 
	
	/**
	 * Idle time base fuel use
	 */
	IdleBase
}
