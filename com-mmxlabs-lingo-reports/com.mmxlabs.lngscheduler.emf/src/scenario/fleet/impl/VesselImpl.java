/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getClass_ <em>Class</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getStartRequirement <em>Start Requirement</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getEndRequirement <em>End Requirement</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#isTimeChartered <em>Time Chartered</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselImpl extends EObjectImpl implements Vessel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getClass_() <em>Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClass_()
	 * @generated
	 * @ordered
	 */
	protected VesselClass class_;

	/**
	 * The cached value of the '{@link #getStartRequirement() <em>Start Requirement</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartRequirement()
	 * @generated
	 * @ordered
	 */
	protected PortAndTime startRequirement;

	/**
	 * The cached value of the '{@link #getEndRequirement() <em>End Requirement</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndRequirement()
	 * @generated
	 * @ordered
	 */
	protected PortAndTime endRequirement;

	/**
	 * The default value of the '{@link #isTimeChartered() <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTimeChartered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TIME_CHARTERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTimeChartered() <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTimeChartered()
	 * @generated
	 * @ordered
	 */
	protected boolean timeChartered = TIME_CHARTERED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselClass getClass_() {
		if (class_ != null && class_.eIsProxy()) {
			InternalEObject oldClass = (InternalEObject)class_;
			class_ = (VesselClass)eResolveProxy(oldClass);
			if (class_ != oldClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__CLASS, oldClass, class_));
			}
		}
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetClass() {
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClass(VesselClass newClass) {
		VesselClass oldClass = class_;
		class_ = newClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__CLASS, oldClass, class_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortAndTime getStartRequirement() {
		return startRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartRequirement(PortAndTime newStartRequirement, NotificationChain msgs) {
		PortAndTime oldStartRequirement = startRequirement;
		startRequirement = newStartRequirement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_REQUIREMENT, oldStartRequirement, newStartRequirement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartRequirement(PortAndTime newStartRequirement) {
		if (newStartRequirement != startRequirement) {
			NotificationChain msgs = null;
			if (startRequirement != null)
				msgs = ((InternalEObject)startRequirement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_REQUIREMENT, null, msgs);
			if (newStartRequirement != null)
				msgs = ((InternalEObject)newStartRequirement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_REQUIREMENT, null, msgs);
			msgs = basicSetStartRequirement(newStartRequirement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_REQUIREMENT, newStartRequirement, newStartRequirement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortAndTime getEndRequirement() {
		return endRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEndRequirement(PortAndTime newEndRequirement, NotificationChain msgs) {
		PortAndTime oldEndRequirement = endRequirement;
		endRequirement = newEndRequirement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__END_REQUIREMENT, oldEndRequirement, newEndRequirement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndRequirement(PortAndTime newEndRequirement) {
		if (newEndRequirement != endRequirement) {
			NotificationChain msgs = null;
			if (endRequirement != null)
				msgs = ((InternalEObject)endRequirement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__END_REQUIREMENT, null, msgs);
			if (newEndRequirement != null)
				msgs = ((InternalEObject)newEndRequirement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__END_REQUIREMENT, null, msgs);
			msgs = basicSetEndRequirement(newEndRequirement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__END_REQUIREMENT, newEndRequirement, newEndRequirement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTimeChartered() {
		return timeChartered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeChartered(boolean newTimeChartered) {
		boolean oldTimeChartered = timeChartered;
		timeChartered = newTimeChartered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__TIME_CHARTERED, oldTimeChartered, timeChartered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL__START_REQUIREMENT:
				return basicSetStartRequirement(null, msgs);
			case FleetPackage.VESSEL__END_REQUIREMENT:
				return basicSetEndRequirement(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL__NAME:
				return getName();
			case FleetPackage.VESSEL__CLASS:
				if (resolve) return getClass_();
				return basicGetClass();
			case FleetPackage.VESSEL__START_REQUIREMENT:
				return getStartRequirement();
			case FleetPackage.VESSEL__END_REQUIREMENT:
				return getEndRequirement();
			case FleetPackage.VESSEL__TIME_CHARTERED:
				return isTimeChartered();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.VESSEL__NAME:
				setName((String)newValue);
				return;
			case FleetPackage.VESSEL__CLASS:
				setClass((VesselClass)newValue);
				return;
			case FleetPackage.VESSEL__START_REQUIREMENT:
				setStartRequirement((PortAndTime)newValue);
				return;
			case FleetPackage.VESSEL__END_REQUIREMENT:
				setEndRequirement((PortAndTime)newValue);
				return;
			case FleetPackage.VESSEL__TIME_CHARTERED:
				setTimeChartered((Boolean)newValue);
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
			case FleetPackage.VESSEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FleetPackage.VESSEL__CLASS:
				setClass((VesselClass)null);
				return;
			case FleetPackage.VESSEL__START_REQUIREMENT:
				setStartRequirement((PortAndTime)null);
				return;
			case FleetPackage.VESSEL__END_REQUIREMENT:
				setEndRequirement((PortAndTime)null);
				return;
			case FleetPackage.VESSEL__TIME_CHARTERED:
				setTimeChartered(TIME_CHARTERED_EDEFAULT);
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
			case FleetPackage.VESSEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FleetPackage.VESSEL__CLASS:
				return class_ != null;
			case FleetPackage.VESSEL__START_REQUIREMENT:
				return startRequirement != null;
			case FleetPackage.VESSEL__END_REQUIREMENT:
				return endRequirement != null;
			case FleetPackage.VESSEL__TIME_CHARTERED:
				return timeChartered != TIME_CHARTERED_EDEFAULT;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", timeChartered: ");
		result.append(timeChartered);
		result.append(')');
		return result.toString();
	}

} //VesselImpl
