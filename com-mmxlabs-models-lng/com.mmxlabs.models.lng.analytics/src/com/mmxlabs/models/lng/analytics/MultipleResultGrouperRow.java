/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiple Result Grouper Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow#getGroupResults <em>Group Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow#getObject <em>Object</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouperRow()
 * @model
 * @generated
 */
public interface MultipleResultGrouperRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Group Results</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ResultSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Results</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Results</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouperRow_GroupResults()
	 * @model
	 * @generated
	 */
	EList<ResultSet> getGroupResults();

	/**
	 * Returns the value of the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object</em>' reference.
	 * @see #setObject(EObject)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouperRow_Object()
	 * @model
	 * @generated
	 */
	EObject getObject();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow#getObject <em>Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object</em>' reference.
	 * @see #getObject()
	 * @generated
	 */
	void setObject(EObject value);

} // MultipleResultGrouperRow
