package com.mmxlabs.rcp.common.debug;

import java.util.EnumSet;

import org.eclipse.osgi.service.debug.DebugOptions;

public interface IDebugOptionsProvider {

	/**
	 * Returns the display name for this logging item
	 * 
	 * @return
	 */
	String getName();

	/**
	 * A short description used for e.g. tooltips about what this options provider
	 * covers and the meanings for the log level
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * Returns true if we can use this logging item. E.g. may be false for optional
	 * features.
	 * 
	 * @return
	 */
	boolean isAvailable();

	/**
	 * Returns the set of logging options supported by this provider. Typically
	 * should include {@link DebugOptionsLevel}.NONE and exclude
	 * {@link DebugOptionsLevel}.PARTIAL (as this is used when a .options file is
	 * provided.)
	 * 
	 * @return
	 */
	EnumSet<DebugOptionsLevel> getSupportedLevels();

	/**
	 * Configure the {@link DebugOptions} class to enable/disable logging based on
	 * the given log level
	 * 
	 * @param debugOptions
	 * @param debugOptionsLevel
	 */
	void apply(DebugOptions debugOptions, DebugOptionsLevel debugOptionsLevel);

	/**
	 * Derive the current log level from the runtime {@link DebugOptions} set. This
	 * may return {@link DebugOptionsLevel}.PARTIAL is there is not an exact match
	 * (i.e. because logging has been configured outside of this object e.g. by
	 * .options file)
	 * 
	 * @param debugOptions
	 * @param debugOptionsLevel
	 */
	DebugOptionsLevel getCurrentLevel(DebugOptions debugOptions);

}
