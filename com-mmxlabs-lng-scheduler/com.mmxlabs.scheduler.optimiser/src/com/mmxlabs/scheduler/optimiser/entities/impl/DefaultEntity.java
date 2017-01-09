/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;


/**
 * Basic entity which has a tax curve and and does transfer pricing by adding a fixed offset per MMBTU
 * 
 * @author hinton
 * 
 */
public class DefaultEntity extends AbstractEntity {

	public DefaultEntity(final String name) {
		super(name);
	}
}
