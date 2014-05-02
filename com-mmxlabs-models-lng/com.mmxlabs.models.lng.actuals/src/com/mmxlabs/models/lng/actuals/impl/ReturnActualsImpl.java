/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.port.Port;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Return Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getTitleTransferPoint <em>Title Transfer Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getOperationsStart <em>Operations Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getEndHeelM3 <em>End Heel M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getEndHeelMMBTu <em>End Heel MMB Tu</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl#getCV <em>CV</em>}</li>
 * </ul>
 * </p>
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
	protected static final Date OPERATIONS_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationsStart() <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsStart()
	 * @generated
	 * @ordered
	 */
	protected Date operationsStart = OPERATIONS_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndHeelM3() <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelM3()
	 * @generated
	 * @ordered
	 */
	protected static final int END_HEEL_M3_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEndHeelM3() <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelM3()
	 * @generated
	 * @ordered
	 */
	protected int endHeelM3 = END_HEEL_M3_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndHeelMMBTu() <em>End Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelMMBTu()
	 * @generated
	 * @ordered
	 */
	protected static final int END_HEEL_MMB_TU_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEndHeelMMBTu() <em>End Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelMMBTu()
	 * @generated
	 * @ordered
	 */
	protected int endHeelMMBTu = END_HEEL_MMB_TU_EDEFAULT;

	/**
	 * The default value of the '{@link #getCV() <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCV()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCV() <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCV()
	 * @generated
	 * @ordered
	 */
	protected double cv = CV_EDEFAULT;

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
	public Date getOperationsStart() {
		return operationsStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationsStart(Date newOperationsStart) {
		Date oldOperationsStart = operationsStart;
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
	public int getEndHeelM3() {
		return endHeelM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndHeelM3(int newEndHeelM3) {
		int oldEndHeelM3 = endHeelM3;
		endHeelM3 = newEndHeelM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.RETURN_ACTUALS__END_HEEL_M3, oldEndHeelM3, endHeelM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getEndHeelMMBTu() {
		return endHeelMMBTu;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndHeelMMBTu(int newEndHeelMMBTu) {
		int oldEndHeelMMBTu = endHeelMMBTu;
		endHeelMMBTu = newEndHeelMMBTu;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.RETURN_ACTUALS__END_HEEL_MMB_TU, oldEndHeelMMBTu, endHeelMMBTu));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCV() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCV(double newCV) {
		double oldCV = cv;
		cv = newCV;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.RETURN_ACTUALS__CV, oldCV, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Calendar getLocalStart() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getOperationsStart());
		calendar.setTimeZone(TimeZone.getTimeZone(getTimeZone(ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START)));
		return calendar;
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
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_MMB_TU:
				return getEndHeelMMBTu();
			case ActualsPackage.RETURN_ACTUALS__CV:
				return getCV();
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
				setOperationsStart((Date)newValue);
				return;
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_M3:
				setEndHeelM3((Integer)newValue);
				return;
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_MMB_TU:
				setEndHeelMMBTu((Integer)newValue);
				return;
			case ActualsPackage.RETURN_ACTUALS__CV:
				setCV((Double)newValue);
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
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_MMB_TU:
				setEndHeelMMBTu(END_HEEL_MMB_TU_EDEFAULT);
				return;
			case ActualsPackage.RETURN_ACTUALS__CV:
				setCV(CV_EDEFAULT);
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
			case ActualsPackage.RETURN_ACTUALS__END_HEEL_MMB_TU:
				return endHeelMMBTu != END_HEEL_MMB_TU_EDEFAULT;
			case ActualsPackage.RETURN_ACTUALS__CV:
				return cv != CV_EDEFAULT;
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
			case ActualsPackage.RETURN_ACTUALS___GET_LOCAL_START:
				return getLocalStart();
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
		result.append(", endHeelMMBTu: ");
		result.append(endHeelMMBTu);
		result.append(", CV: ");
		result.append(cv);
		result.append(')');
		return result.toString();
	}

} //ReturnActualsImpl
