/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager;

public interface IJobManagerDescriptor {

	String getName();

	String getDescription();

	Object getCapabilities();
}
