package com.mmxlabs.models.lng.transformer.extensions.exposures;

import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.transformer.inject.IOutputScheduleProcessorFactory;

public class ExposuresOutputScheduleProcessorFactory implements IOutputScheduleProcessorFactory {

	@Override
	public IOutputScheduleProcessor createInstance() {
		return new ExposuresOutputScheduleProcessor();
	}

}
