package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.IPostExportProcessor;

/**
 * @since 2.0
 */
public interface IPostExportProcessorFactory {

	IPostExportProcessor createInstance();
}
