/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Basic entity which has a tax curve and and does transfer pricing by adding a fixed offset per MMBTU
 * 
 * @author hinton
 * 
 */
@NonNullByDefault
public class DefaultEntity extends AbstractEntity {

	public DefaultEntity(final String name, boolean thirdparty) {
		super(name, thirdparty);
	}
}
