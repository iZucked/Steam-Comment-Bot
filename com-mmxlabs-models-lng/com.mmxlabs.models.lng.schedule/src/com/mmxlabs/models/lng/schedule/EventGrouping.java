/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The {@link EventGrouping} is an object containing a list of events in a {@link Sequence} that form a P&L unit. For example this could be a {@link CargoAllocation} containing
 * the first Load up to, but excluding the next cargo load. This would include all {@link Journey} and {@link Idle} events and discharges.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EventGrouping#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEventGrouping()
 * @model
 * @generated
 */
public interface EventGrouping extends EObject {
	/**
	 * Returns the value of the '<em><b>Events</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.Event}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEventGrouping_Events()
	 * @model required="true"
	 * @generated
	 */
	EList<Event> getEvents();

} // EventGrouping
