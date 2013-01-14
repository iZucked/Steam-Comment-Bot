/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import java.lang.Iterable;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Data Holder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.AdditionalDataHolder#getAdditionalData <em>Additional Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalDataHolder()
 * @model
 * @generated
 */
public interface AdditionalDataHolder extends EObject {
	/**
	 * Returns the value of the '<em><b>Additional Data</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.AdditionalData}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Additional Data</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Additional Data</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalDataHolder_AdditionalData()
	 * @model containment="true"
	 * @generated
	 */
	EList<AdditionalData> getAdditionalData();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" keyRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='for (final AdditionalData ad : getAdditionalData()) {\n  if (ad.getKey().equals(key)) return ad;\n}\n\nreturn null;'"
	 * @generated
	 */
	AdditionalData getAdditionalDataWithKey(String key);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" keysDataType="com.mmxlabs.models.lng.schedule.Iterable<org.eclipse.emf.ecore.EString>" keysRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='AdditionalDataHolder adh = this;\nfor (final String key : keys) {\n\tif (adh == null) return null;\n\tadh = adh.getAdditionalDataWithKey(key);\n}\nif (adh instanceof AdditionalData) return (AdditionalData) adh;\nreturn null;'"
	 * @generated
	 */
	AdditionalData getAdditionalDataWithPath(Iterable<String> keys);

} // end of  AdditionalDataHolder

// finish type fixing
