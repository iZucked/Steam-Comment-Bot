/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption;

import com.mmxlabs.models.lng.port.Port;
import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Optional Availability Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl#getBallastBonus <em>Ballast Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl#getStart <em>Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl#getEnd <em>End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl#getStartPort <em>Start Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl#getEndPort <em>End Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OptionalAvailabilityShippingOptionImpl extends FleetShippingOptionImpl implements OptionalAvailabilityShippingOption {
	/**
	 * The default value of the '{@link #getBallastBonus() <em>Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonus()
	 * @generated
	 * @ordered
	 */
	protected static final String BALLAST_BONUS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBallastBonus() <em>Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonus()
	 * @generated
	 * @ordered
	 */
	protected String ballastBonus = BALLAST_BONUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected static final String REPOSITIONING_FEE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected String repositioningFee = REPOSITIONING_FEE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDate start = START_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnd() <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnd()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnd() <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnd()
	 * @generated
	 * @ordered
	 */
	protected LocalDate end = END_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStartPort() <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartPort()
	 * @generated
	 * @ordered
	 */
	protected Port startPort;

	/**
	 * The cached value of the '{@link #getEndPort() <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPort()
	 * @generated
	 * @ordered
	 */
	protected Port endPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptionalAvailabilityShippingOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.OPTIONAL_AVAILABILITY_SHIPPING_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBallastBonus() {
		return ballastBonus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastBonus(String newBallastBonus) {
		String oldBallastBonus = ballastBonus;
		ballastBonus = newBallastBonus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS, oldBallastBonus, ballastBonus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRepositioningFee() {
		return repositioningFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepositioningFee(String newRepositioningFee) {
		String oldRepositioningFee = repositioningFee;
		repositioningFee = newRepositioningFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE, oldRepositioningFee, repositioningFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getStart() {
		return start;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStart(LocalDate newStart) {
		LocalDate oldStart = start;
		start = newStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START, oldStart, start));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnd(LocalDate newEnd) {
		LocalDate oldEnd = end;
		end = newEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END, oldEnd, end));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getStartPort() {
		if (startPort != null && startPort.eIsProxy()) {
			InternalEObject oldStartPort = (InternalEObject)startPort;
			startPort = (Port)eResolveProxy(oldStartPort);
			if (startPort != oldStartPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT, oldStartPort, startPort));
			}
		}
		return startPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetStartPort() {
		return startPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartPort(Port newStartPort) {
		Port oldStartPort = startPort;
		startPort = newStartPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT, oldStartPort, startPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getEndPort() {
		if (endPort != null && endPort.eIsProxy()) {
			InternalEObject oldEndPort = (InternalEObject)endPort;
			endPort = (Port)eResolveProxy(oldEndPort);
			if (endPort != oldEndPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT, oldEndPort, endPort));
			}
		}
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetEndPort() {
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndPort(Port newEndPort) {
		Port oldEndPort = endPort;
		endPort = newEndPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT, oldEndPort, endPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS:
				return getBallastBonus();
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE:
				return getRepositioningFee();
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START:
				return getStart();
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END:
				return getEnd();
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT:
				if (resolve) return getStartPort();
				return basicGetStartPort();
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT:
				if (resolve) return getEndPort();
				return basicGetEndPort();
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
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS:
				setBallastBonus((String)newValue);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE:
				setRepositioningFee((String)newValue);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START:
				setStart((LocalDate)newValue);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END:
				setEnd((LocalDate)newValue);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT:
				setStartPort((Port)newValue);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT:
				setEndPort((Port)newValue);
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
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS:
				setBallastBonus(BALLAST_BONUS_EDEFAULT);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE:
				setRepositioningFee(REPOSITIONING_FEE_EDEFAULT);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START:
				setStart(START_EDEFAULT);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END:
				setEnd(END_EDEFAULT);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT:
				setStartPort((Port)null);
				return;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT:
				setEndPort((Port)null);
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
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS:
				return BALLAST_BONUS_EDEFAULT == null ? ballastBonus != null : !BALLAST_BONUS_EDEFAULT.equals(ballastBonus);
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE:
				return REPOSITIONING_FEE_EDEFAULT == null ? repositioningFee != null : !REPOSITIONING_FEE_EDEFAULT.equals(repositioningFee);
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START:
				return START_EDEFAULT == null ? start != null : !START_EDEFAULT.equals(start);
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END:
				return END_EDEFAULT == null ? end != null : !END_EDEFAULT.equals(end);
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT:
				return startPort != null;
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT:
				return endPort != null;
		}
		return super.eIsSet(featureID);
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
		result.append(" (ballastBonus: ");
		result.append(ballastBonus);
		result.append(", repositioningFee: ");
		result.append(repositioningFee);
		result.append(", start: ");
		result.append(start);
		result.append(", end: ");
		result.append(end);
		result.append(')');
		return result.toString();
	}

} //OptionalAvailabilityShippingOptionImpl
