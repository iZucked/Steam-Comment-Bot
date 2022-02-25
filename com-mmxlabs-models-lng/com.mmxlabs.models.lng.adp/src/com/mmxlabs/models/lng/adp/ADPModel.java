/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.mmxcore.UUIDObject;
import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getYearEnd <em>Year End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getPurchaseContractProfiles <em>Purchase Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getSalesContractProfiles <em>Sales Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getFleetProfile <em>Fleet Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getMullProfile <em>Mull Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModel#getSpacingProfile <em>Spacing Profile</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel()
 * @model
 * @generated
 */
public interface ADPModel extends UUIDObject {
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
	 * Returns the value of the '<em><b>Year End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Year End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year End</em>' attribute.
	 * @see #setYearEnd(YearMonth)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_YearEnd()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getYearEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ADPModel#getYearEnd <em>Year End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year End</em>' attribute.
	 * @see #getYearEnd()
	 * @generated
	 */
	void setYearEnd(YearMonth value);

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
	 * Returns the value of the '<em><b>Fleet Profile</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet Profile</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet Profile</em>' containment reference.
	 * @see #setFleetProfile(FleetProfile)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_FleetProfile()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	FleetProfile getFleetProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ADPModel#getFleetProfile <em>Fleet Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet Profile</em>' containment reference.
	 * @see #getFleetProfile()
	 * @generated
	 */
	void setFleetProfile(FleetProfile value);

	/**
	 * Returns the value of the '<em><b>Mull Profile</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mull Profile</em>' containment reference.
	 * @see #setMullProfile(MullProfile)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_MullProfile()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	MullProfile getMullProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ADPModel#getMullProfile <em>Mull Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mull Profile</em>' containment reference.
	 * @see #getMullProfile()
	 * @generated
	 */
	void setMullProfile(MullProfile value);

	/**
	 * Returns the value of the '<em><b>Spacing Profile</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spacing Profile</em>' containment reference.
	 * @see #setSpacingProfile(SpacingProfile)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModel_SpacingProfile()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	SpacingProfile getSpacingProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ADPModel#getSpacingProfile <em>Spacing Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spacing Profile</em>' containment reference.
	 * @see #getSpacingProfile()
	 * @generated
	 */
	void setSpacingProfile(SpacingProfile value);

} // ADPModel
