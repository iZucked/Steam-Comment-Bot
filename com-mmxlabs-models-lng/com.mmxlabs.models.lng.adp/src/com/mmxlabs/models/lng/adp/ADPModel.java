/**
 */
package com.mmxlabs.models.lng.adp;

import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getYearStart <em>Year Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getPurchaseContractProfiles <em>Purchase Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getSalesContractProfiles <em>Sales Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getBindingRules <em>Binding Rules</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel()
 * @model
 * @generated
 */
public interface ADPModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Year Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year Start</em>' attribute.
	 * @see #setYearStart(YearMonth)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_YearStart()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getYearStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ADPModel#getYearStart <em>Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year Start</em>' attribute.
	 * @see #getYearStart()
	 * @generated
	 */
	void setYearStart(YearMonth value);

	/**
	 * Returns the value of the '<em><b>Purchase Contract Profiles</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.PurchaseContractProfile}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purchase Contract Profiles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purchase Contract Profiles</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_PurchaseContractProfiles()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<PurchaseContractProfile> getPurchaseContractProfiles();

	/**
	 * Returns the value of the '<em><b>Sales Contract Profiles</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.SalesContractProfile}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sales Contract Profiles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sales Contract Profiles</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_SalesContractProfiles()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SalesContractProfile> getSalesContractProfiles();

	/**
	 * Returns the value of the '<em><b>Binding Rules</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.BindingRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Binding Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Binding Rules</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_BindingRules()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<BindingRule> getBindingRules();

} // ADPModel
