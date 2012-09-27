/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * Container for cargoes on virtual vessels
 * 
 * @author hinton
 * 
 */
@Deprecated
public class VirtualCargo {
	final ILoadOption loadOption;
	final IDischargeOption dischargeOption;
	final int loadTime;
	final int dischargeTime;

	public VirtualCargo(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime) {
		this.loadOption = loadOption;
		this.dischargeOption = dischargeOption;
		this.loadTime = loadTime;
		this.dischargeTime = dischargeTime;
	}

	public ILoadOption getLoadOption() {
		return loadOption;
	}

	public IDischargeOption getDischargeOption() {
		return dischargeOption;
	}

	public int getLoadTime() {
		return loadTime;
	}

	public int getDischargeTime() {
		return dischargeTime;
	}
}
