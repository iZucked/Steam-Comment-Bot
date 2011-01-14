/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.CharterOut;

import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.EventsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.events.impl.CharterOutVisitImpl#getCharterOut <em>Charter Out</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterOutVisitImpl extends PortVisitImpl implements CharterOutVisit {
	/**
	 * The cached value of the '{@link #getCharterOut() <em>Charter Out</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOut()
	 * @generated
	 * @ordered
	 */
	protected CharterOut charterOut;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.CHARTER_OUT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOut getCharterOut() {
		if (charterOut != null && charterOut.eIsProxy()) {
			InternalEObject oldCharterOut = (InternalEObject)charterOut;
			charterOut = (CharterOut)eResolveProxy(oldCharterOut);
			if (charterOut != oldCharterOut) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.CHARTER_OUT_VISIT__CHARTER_OUT, oldCharterOut, charterOut));
			}
		}
		return charterOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOut basicGetCharterOut() {
		return charterOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOut(CharterOut newCharterOut) {
		CharterOut oldCharterOut = charterOut;
		charterOut = newCharterOut;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.CHARTER_OUT_VISIT__CHARTER_OUT, oldCharterOut, charterOut));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EventsPackage.CHARTER_OUT_VISIT__CHARTER_OUT:
				if (resolve) return getCharterOut();
				return basicGetCharterOut();
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
			case EventsPackage.CHARTER_OUT_VISIT__CHARTER_OUT:
				setCharterOut((CharterOut)newValue);
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
			case EventsPackage.CHARTER_OUT_VISIT__CHARTER_OUT:
				setCharterOut((CharterOut)null);
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
			case EventsPackage.CHARTER_OUT_VISIT__CHARTER_OUT:
				return charterOut != null;
		}
		return super.eIsSet(featureID);
	}

	@Override
	public String getDisplayTypeName() {
		return "Charter Out";
	}

	@Override
	public String getId() {
		if (getCharterOut() == null) return "unknown";
		return getCharterOut().getId();
	}
} //CharterOutVisitImpl
