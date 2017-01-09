/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IOrderedSequenceElementsDataComponentProviderEditor extends IOrderedSequenceElementsDataComponentProvider {

	/**
	 * Define a pair of elements which must be together. An element may have only one next element assigned to it. An element may have only one previous element assigned to it.
	 * 
	 * @param previousElement
	 * @param nextElement
	 */
	void setElementOrder(@NonNull ISequenceElement previousElement, @NonNull ISequenceElement nextElement);

}
