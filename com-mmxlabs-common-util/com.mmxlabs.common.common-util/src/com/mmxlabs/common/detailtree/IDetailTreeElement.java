/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree;

import com.mmxlabs.common.detailtree.impl.CurrencyDetailElement;

/**
 * An interface wrapping an object with the intention that implementing classes infer some kind of formatting information or hints. For example the {@link CurrencyDetailElement} infers the contained
 * object is related to currency and should be formatted as such. This is intended for use with {@link IDetailTree} although there is no explicit link between this and the {@link IDetailTree}
 * 
 * @author Simon Goodall
 * 
 */
public interface IDetailTreeElement {

	/**
	 * Return the wrapped object.
	 * 
	 * @return
	 */
	Object getObject();
}
