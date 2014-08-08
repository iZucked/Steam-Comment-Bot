/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.ITimezoneProvider;

import java.util.Calendar;
import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Return Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getTitleTransferPoint <em>Title Transfer Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getOperationsStart <em>Operations Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getEndHeelM3 <em>End Heel M3</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getReturnActuals()
 * @model
 * @generated
 */
public interface ReturnActuals extends ITimezoneProvider {
	/**
	 * Returns the value of the '<em><b>Title Transfer Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title Transfer Point</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title Transfer Point</em>' reference.
	 * @see #setTitleTransferPoint(Port)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getReturnActuals_TitleTransferPoint()
	 * @model
	 * @generated
	 */
	Port getTitleTransferPoint();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getTitleTransferPoint <em>Title Transfer Point</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title Transfer Point</em>' reference.
	 * @see #getTitleTransferPoint()
	 * @generated
	 */
	void setTitleTransferPoint(Port value);

	/**
	 * Returns the value of the '<em><b>Operations Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations Start</em>' attribute.
	 * @see #setOperationsStart(Date)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getReturnActuals_OperationsStart()
	 * @model
	 * @generated
	 */
	Date getOperationsStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getOperationsStart <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operations Start</em>' attribute.
	 * @see #getOperationsStart()
	 * @generated
	 */
	void setOperationsStart(Date value);

	/**
	 * Returns the value of the '<em><b>End Heel M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Heel M3</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Heel M3</em>' attribute.
	 * @see #setEndHeelM3(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getReturnActuals_EndHeelM3()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='#,###,###.###'"
	 * @generated
	 */
	double getEndHeelM3();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getEndHeelM3 <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Heel M3</em>' attribute.
	 * @see #getEndHeelM3()
	 * @generated
	 */
	void setEndHeelM3(double value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.lng.actuals.Calendar" required="true"
	 * @generated
	 */
	Calendar getLocalStart();

} // ReturnActuals
