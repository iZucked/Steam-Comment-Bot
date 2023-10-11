package com.mmxlabs.optimiser.common.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Maps optimiser objects to an external name for user facing messages. Simple version supporting optimiser objects.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public interface InternalElementNameMapper {

	default String generateString(ISequenceElement e) {
		return e.getName();
	}

	default String generateString(IResource resouce) {
		return resouce.getName();
	}

}
