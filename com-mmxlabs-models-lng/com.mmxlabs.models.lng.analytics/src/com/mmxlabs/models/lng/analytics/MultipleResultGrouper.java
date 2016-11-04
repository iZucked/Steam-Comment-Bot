/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiple Result Grouper</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getGroupResults <em>Group Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getReferenceRow <em>Reference Row</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getFeatureName <em>Feature Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouper()
 * @model
 * @generated
 */
public interface MultipleResultGrouper extends EObject {
	/**
	 * Returns the value of the '<em><b>Group Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Results</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouper_GroupResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<MultipleResultGrouperRow> getGroupResults();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouper_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Reference Row</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Row</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Row</em>' reference.
	 * @see #setReferenceRow(PartialCaseRow)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouper_ReferenceRow()
	 * @model
	 * @generated
	 */
	PartialCaseRow getReferenceRow();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getReferenceRow <em>Reference Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Row</em>' reference.
	 * @see #getReferenceRow()
	 * @generated
	 */
	void setReferenceRow(PartialCaseRow value);

	/**
	 * Returns the value of the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Name</em>' attribute.
	 * @see #setFeatureName(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMultipleResultGrouper_FeatureName()
	 * @model
	 * @generated
	 */
	String getFeatureName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper#getFeatureName <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Name</em>' attribute.
	 * @see #getFeatureName()
	 * @generated
	 */
	void setFeatureName(String value);

} // MultipleResultGrouper
