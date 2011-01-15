/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

public interface IOrderedSequenceElementsDataComponentProviderEditor<T> extends
		IOrderedSequenceElementsDataComponentProvider<T> {

	/**
	 * Define a pair of elements which must be together. An element may have
	 * only one next element assigned to it. An element may have only one
	 * previous element assigned to it.
	 * 
	 * @param previousElement
	 * @param nextElement
	 */
	void setElementOrder(T previousElement, T nextElement);

}
