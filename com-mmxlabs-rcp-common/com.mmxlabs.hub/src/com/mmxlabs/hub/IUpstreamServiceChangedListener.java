/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub;

public interface IUpstreamServiceChangedListener {
	enum Service {
		BaseCaseWorkspace, TeamWorkspace, CloudOptimisation,
	}

	void changed(Service service);

}
