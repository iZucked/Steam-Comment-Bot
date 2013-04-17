package com.mmxlabs.rcp.logger.internal;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class ExtendedLogLoggerFactory implements ILoggerFactory {

	@Override
	public Logger getLogger(String name) {
		return new ExtendedLogLogger(name);
	}

}
