/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generated Charter Out</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterOut()
 * @model
 * @generated
 */
public interface GeneratedCharterOut extends Event, ExtraDataContainer {
	/**
	 * Returns the value of the '<em><b>Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revenue</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Revenue</em>' attribute.
	 * @see #setRevenue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterOut_Revenue()
	 * @model required="true"
	 * @generated
	 */
	int getRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue <em>Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Revenue</em>' attribute.
	 * @see #getRevenue()
	 * @generated
	 */
	void setRevenue(int value);

} // end of  GeneratedCharterOut

// finish type fixing
