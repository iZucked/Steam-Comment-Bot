/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import java.time.YearMonth;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Period Distribution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getRange <em>Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMinCargoes <em>Min Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMaxCargoes <em>Max Cargoes</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPeriodDistribution()
 * @model
 * @generated
 */
public interface PeriodDistribution extends EObject {
	/**
	 * Returns the value of the '<em><b>Range</b></em>' attribute list.
	 * The list contents are of type {@link java.time.YearMonth}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Range</em>' attribute list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPeriodDistribution_Range()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	EList<YearMonth> getRange();

	/**
	 * Returns the value of the '<em><b>Min Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Cargoes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Cargoes</em>' attribute.
	 * @see #isSetMinCargoes()
	 * @see #unsetMinCargoes()
	 * @see #setMinCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPeriodDistribution_MinCargoes()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMinCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMinCargoes <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Cargoes</em>' attribute.
	 * @see #isSetMinCargoes()
	 * @see #unsetMinCargoes()
	 * @see #getMinCargoes()
	 * @generated
	 */
	void setMinCargoes(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMinCargoes <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinCargoes()
	 * @see #getMinCargoes()
	 * @see #setMinCargoes(int)
	 * @generated
	 */
	void unsetMinCargoes();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMinCargoes <em>Min Cargoes</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Cargoes</em>' attribute is set.
	 * @see #unsetMinCargoes()
	 * @see #getMinCargoes()
	 * @see #setMinCargoes(int)
	 * @generated
	 */
	boolean isSetMinCargoes();

	/**
	 * Returns the value of the '<em><b>Max Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Cargoes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Cargoes</em>' attribute.
	 * @see #isSetMaxCargoes()
	 * @see #unsetMaxCargoes()
	 * @see #setMaxCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPeriodDistribution_MaxCargoes()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMaxCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMaxCargoes <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Cargoes</em>' attribute.
	 * @see #isSetMaxCargoes()
	 * @see #unsetMaxCargoes()
	 * @see #getMaxCargoes()
	 * @generated
	 */
	void setMaxCargoes(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMaxCargoes <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxCargoes()
	 * @see #getMaxCargoes()
	 * @see #setMaxCargoes(int)
	 * @generated
	 */
	void unsetMaxCargoes();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMaxCargoes <em>Max Cargoes</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Cargoes</em>' attribute is set.
	 * @see #unsetMaxCargoes()
	 * @see #getMaxCargoes()
	 * @see #setMaxCargoes(int)
	 * @generated
	 */
	boolean isSetMaxCargoes();

} // PeriodDistribution
