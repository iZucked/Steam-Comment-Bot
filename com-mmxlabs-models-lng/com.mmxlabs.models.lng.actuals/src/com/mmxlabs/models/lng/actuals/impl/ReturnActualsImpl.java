/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Return Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getTitleTransferPoint <em>Title Transfer Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getOperationsStart <em>Operations Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getEndHeelM3 <em>End Heel M3</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReturnActualsImpl extends EObjectImpl implements ReturnActuals {
	/**
	 * The cached value of the '{@link #getTitleTransferPoint() <em>Title Transfer Point</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitleTransferPoint()
	 * @generated
	 * @ordered
	 */
	protected Port titleTransferPoint;

	/**
	 * The default value of the '{@link #getOperationsStart() <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime OPERATIONS_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationsStart() <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime operationsStart = OPERATIONS_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndHeelM3() <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelM3()
	 * @generated
	 * @ordered
	 */
	protected static final double END_HEEL_M3_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getEndHeelM3() <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelM3()
	 * @generated
	 * @ordered
	 */
	protected double endHeelM3 = END_HEEL_M3_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReturnActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.RETURN_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getTitleTransferPoint() {
		if (titleTransferPoint != null && titleTransferPoint.eIsProxy()) {
			InternalEObject oldTitleTransferPoint = (InternalEObject)titleTransferPoint;
			titleTransferPoint = (Port)eResolveProxy(oldTitleTransferPoint);
			if (titleTransferPoint != oldTitleTransferPoint) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.RETURN_ACTUALS__TITLE_TRANSFER_POINT, oldTitleTransferPoint, titleTransferPoint));
			}
		}
		return titleTransferPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetTitleTransferPoint() {
		return titleTransferPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTitleTransferPoint(Port newTitleTransferPoint) {
		Port oldTitleTransferPoint = titleTransferPoint;
		titleTransferPoint = newTitleTransferPoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.RETURN_ACTUALS__TITLE_TRANSFER_POINT, oldTitleTransferPoint, titleTransferPoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getOperationsStart() {
		return operationsStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationsStart(LocalDateTime newOperationsStart) {
		LocalDateTime oldOperationsStart = operationsStart;
		operationsStart = newOperationsStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.RETURN_ACTUALS__OPERATIONS_START, oldOperationsStart, operationsStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getEndHeelM3() {
		return endHeelM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndHeelM3(double newEndHeelM3) {
		double oldEndHeelM3 = endHeelM3;
		endHeelM3 = newEndHeelM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.RETURN_ACTUALS__END_HEEL_M3, oldEndHeelM3, endHeelM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ZonedDateTime getOperationsStartAsDateTime() {
		final LocalDateTime os = getOperationsStart();
		if (os != null) {
			return os.atZone(ZoneId.of(getTimeZone(ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START)));
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getTimeZone(EAttribute attribute) {
		if (getTitleTransferPoint() == null) return "UTC";
		if (getTitleTransferPoint().getTimeZone() == null) return "UTC";
		if (getTitleTransferPoint().getTimeZone().isEmpty()) return "UTC";
		return getTitleTransferPoint().getTimeZone();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActualsPackage.RETURN_ACTUALS__TITLE_TRANSFER_POINT:
				if (resolve) return getTitleTransferPoint();
				return basicGetTitleTransferPoint();
			case ActualsPackage.RETURN_ACTUALS__OPERATIONS_START:
				return getOperationsStart();
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_M3:
				return getEndHeelM3();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ActualsPackage.RETURN_ACTUALS__TITLE_TRANSFER_POINT:
				setTitleTransferPoint((Port)newValue);
				return;
			case ActualsPackage.RETURN_ACTUALS__OPERATIONS_START:
				setOperationsStart((LocalDateTime)newValue);
				return;
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_M3:
				setEndHeelM3((Double)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ActualsPackage.RETURN_ACTUALS__TITLE_TRANSFER_POINT:
				setTitleTransferPoint((Port)null);
				return;
			case ActualsPackage.RETURN_ACTUALS__OPERATIONS_START:
				setOperationsStart(OPERATIONS_START_EDEFAULT);
				return;
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_M3:
				setEndHeelM3(END_HEEL_M3_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ActualsPackage.RETURN_ACTUALS__TITLE_TRANSFER_POINT:
				return titleTransferPoint != null;
			case ActualsPackage.RETURN_ACTUALS__OPERATIONS_START:
				return OPERATIONS_START_EDEFAULT == null ? operationsStart != null : !OPERATIONS_START_EDEFAULT.equals(operationsStart);
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_M3:
				return endHeelM3 != END_HEEL_M3_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case ActualsPackage.RETURN_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME:
				return getOperationsStartAsDateTime();
			case ActualsPackage.RETURN_ACTUALS___GET_TIME_ZONE__EATTRIBUTE:
				return getTimeZone((EAttribute)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (operationsStart: ");
		result.append(operationsStart);
		result.append(", endHeelM3: ");
		result.append(endHeelM3);
		result.append(')');
		return result.toString();
	}

} //ReturnActualsImpl
