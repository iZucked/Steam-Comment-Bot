/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

public interface IUpstreamServiceChangedListener {
	enum Service {
		BaseCaseWorkspace, TeamWorkspace,
	}

	void changed(Service service);

}
