/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.types.TimePeriod;
import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pre Defined Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getDates <em>Dates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getWindowSizeUnits <em>Window Size Units</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPreDefinedDistributionModel()
 * @model
 * @generated
 */
public interface PreDefinedDistributionModel extends DistributionModel {
	/**
	 * Returns the value of the '<em><b>Dates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.PreDefinedDate}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dates</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPreDefinedDistributionModel_Dates()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<PreDefinedDate> getDates();

	/**
	 * Returns the value of the '<em><b>Window Size</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size</em>' attribute.
	 * @see #setWindowSize(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPreDefinedDistributionModel_WindowSize()
	 * @model default="1" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
	 * @generated
	 */
	int getWindowSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getWindowSize <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size</em>' attribute.
	 * @see #getWindowSize()
	 * @generated
	 */
	void setWindowSize(int value);

	/**
	 * Returns the value of the '<em><b>Window Size Units</b></em>' attribute.
	 * The default value is <code>"MONTHS"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setWindowSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPreDefinedDistributionModel_WindowSizeUnits()
	 * @model default="MONTHS" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
	 * @generated
	 */
	TimePeriod getWindowSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getWindowSizeUnits <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getWindowSizeUnits()
	 * @generated
	 */
	void setWindowSizeUnits(TimePeriod value);

} // PreDefinedDistributionModel
