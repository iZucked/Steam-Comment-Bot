/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IOrderedSequenceElementsDataComponentProviderEditor extends IOrderedSequenceElementsDataComponentProvider {

	/**
	 * Define a pair of elements which must be together. An element may have only one next element assigned to it. An element may have only one previous element assigned to it.
	 * 
	 * @param previousElement
	 * @param nextElement
	 */
	void setElementOrder(ISequenceElement previousElement, ISequenceElement nextElement);

}
