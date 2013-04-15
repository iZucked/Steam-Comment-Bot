

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.common.util.EMap;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capacity Violations Holder</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CapacityViolationsHolder#getViolations <em>Violations</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCapacityViolationsHolder()
 * @model
 * @generated
 */
public interface CapacityViolationsHolder extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Violations</b></em>' map.
	 * The key is of type {@link com.mmxlabs.models.lng.schedule.CapacityViolationType},
	 * and the value is of type {@link java.lang.Long},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Violations</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Violations</em>' map.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCapacityViolationsHolder_Violations()
	 * @model mapType="com.mmxlabs.models.lng.schedule.CapacityMapEntry<com.mmxlabs.models.lng.schedule.CapacityViolationType, org.eclipse.emf.ecore.ELongObject>"
	 * @generated
	 */
	EMap<CapacityViolationType, Long> getViolations();

} // end of  CapacityViolationsHolder

// finish type fixing
