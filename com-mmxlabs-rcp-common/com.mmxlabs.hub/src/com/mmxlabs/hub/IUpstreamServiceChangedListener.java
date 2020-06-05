/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub;

public interface IUpstreamServiceChangedListener {
	enum Service {
		BaseCaseWorkspace, TeamWorkspace,
	}

	void changed(Service service);

}
