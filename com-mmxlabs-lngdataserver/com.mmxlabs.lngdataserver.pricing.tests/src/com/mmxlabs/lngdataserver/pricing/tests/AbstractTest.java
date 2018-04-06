/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.pricing.tests;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class AbstractTest {

	// potential race condition with other services
    protected int getPort() {
        try (
                ServerSocket socket = new ServerSocket(0);
        ) {
            return socket.getLocalPort();

        } catch (IOException e) {
;			throw new RuntimeException("Error trying to find free port...");
		}
    }
}
