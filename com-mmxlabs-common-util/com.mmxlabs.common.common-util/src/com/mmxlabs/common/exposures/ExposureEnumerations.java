/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.exposures;

public class ExposureEnumerations{
	public enum Mode {
		VALUE, VOLUME
	}

	public enum ValueMode {
		VOLUME_MMBTU, VOLUME_TBTU, VOLUME_NATIVE, NATIVE_VALUE
	}

	public enum AggregationMode {
		BY_MONTH_NO_TOTAL, BY_MONTH, BY_DEALSET
	}
}
