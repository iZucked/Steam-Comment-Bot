/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.AVesselSetImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getShortName <em>Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getFillCapacity <em>Fill Capacity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselImpl extends AVesselSetImpl<Vessel> implements Vessel {
	/**
	 * The default value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected String shortName = SHORT_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVesselClass() <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass vesselClass;

	/**
	 * The cached value of the '{@link #getInaccessiblePorts() <em>Inaccessible Ports</em>}' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getInaccessiblePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> inaccessiblePorts;

	/**
	 * The default value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final int CAPACITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected int capacity = CAPACITY_EDEFAULT;

	/**
	 * This is true if the Capacity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean capacityESet;

	/**
	 * The default value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final double FILL_CAPACITY_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected double fillCapacity = FILL_CAPACITY_EDEFAULT;

	/**
	 * This is true if the Fill Capacity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fillCapacityESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
	public String getShortName() {
		return shortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShortName(String newShortName) {
		String oldShortName = shortName;
		shortName = newShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__SHORT_NAME, oldShortName, shortName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass getVesselClass() {
		if (vesselClass != null && vesselClass.eIsProxy()) {
			InternalEObject oldVesselClass = (InternalEObject)vesselClass;
			vesselClass = (VesselClass)eResolveProxy(oldVesselClass);
			if (vesselClass != oldVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__VESSEL_CLASS, oldVesselClass, vesselClass));
			}
		}
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetVesselClass() {
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselClass(VesselClass newVesselClass) {
		VesselClass oldVesselClass = vesselClass;
		vesselClass = newVesselClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__VESSEL_CLASS, oldVesselClass, vesselClass));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getInaccessiblePorts() {
		if (inaccessiblePorts == null) {
			inaccessiblePorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, FleetPackage.VESSEL__INACCESSIBLE_PORTS);
		}
		return inaccessiblePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCapacity(int newCapacity) {
		int oldCapacity = capacity;
		capacity = newCapacity;
		boolean oldCapacityESet = capacityESet;
		capacityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__CAPACITY, oldCapacity, capacity, !oldCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCapacity() {
		int oldCapacity = capacity;
		boolean oldCapacityESet = capacityESet;
		capacity = CAPACITY_EDEFAULT;
		capacityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__CAPACITY, oldCapacity, CAPACITY_EDEFAULT, oldCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCapacity() {
		return capacityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getFillCapacity() {
		return fillCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFillCapacity(double newFillCapacity) {
		double oldFillCapacity = fillCapacity;
		fillCapacity = newFillCapacity;
		boolean oldFillCapacityESet = fillCapacityESet;
		fillCapacityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__FILL_CAPACITY, oldFillCapacity, fillCapacity, !oldFillCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFillCapacity() {
		double oldFillCapacity = fillCapacity;
		boolean oldFillCapacityESet = fillCapacityESet;
		fillCapacity = FILL_CAPACITY_EDEFAULT;
		fillCapacityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__FILL_CAPACITY, oldFillCapacity, FILL_CAPACITY_EDEFAULT, oldFillCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFillCapacity() {
		return fillCapacityESet;
	}

	/**
	 * <!-- begin-user-doc --> 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Vessel> collect(EList<AVesselSet<Vessel>> marked) {
		if (marked.contains(this)) {
			return ECollections.emptyEList();
		}
		final UniqueEList<Vessel> result = new UniqueEList<Vessel>();
		marked.add(this);
		result.add(this);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrVesselClassCapacity() {
		
		return (Integer) eGetWithDefault(FleetPackage.eINSTANCE.getVessel_Capacity());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrVesselClassFillCapacity() {
		return (Double) eGetWithDefault(FleetPackage.eINSTANCE.getVessel_FillCapacity());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getShortenedName() {
		
		final String shortName = getShortName();
		if (shortName == null || shortName.isEmpty()) {
			return getName();
		}
		return shortName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				return getShortName();
			case FleetPackage.VESSEL__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				return getInaccessiblePorts();
			case FleetPackage.VESSEL__CAPACITY:
				return getCapacity();
			case FleetPackage.VESSEL__FILL_CAPACITY:
				return getFillCapacity();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				setShortName((String)newValue);
				return;
			case FleetPackage.VESSEL__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				getInaccessiblePorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case FleetPackage.VESSEL__CAPACITY:
				setCapacity((Integer)newValue);
				return;
			case FleetPackage.VESSEL__FILL_CAPACITY:
				setFillCapacity((Double)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				setShortName(SHORT_NAME_EDEFAULT);
				return;
			case FleetPackage.VESSEL__VESSEL_CLASS:
				setVesselClass((VesselClass)null);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				return;
			case FleetPackage.VESSEL__CAPACITY:
				unsetCapacity();
				return;
			case FleetPackage.VESSEL__FILL_CAPACITY:
				unsetFillCapacity();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				return SHORT_NAME_EDEFAULT == null ? shortName != null : !SHORT_NAME_EDEFAULT.equals(shortName);
			case FleetPackage.VESSEL__VESSEL_CLASS:
				return vesselClass != null;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				return inaccessiblePorts != null && !inaccessiblePorts.isEmpty();
			case FleetPackage.VESSEL__CAPACITY:
				return isSetCapacity();
			case FleetPackage.VESSEL__FILL_CAPACITY:
				return isSetFillCapacity();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (shortName: ");
		result.append(shortName);
		result.append(", capacity: ");
		if (capacityESet) result.append(capacity); else result.append("<unset>");
		result.append(", fillCapacity: ");
		if (fillCapacityESet) result.append(fillCapacity); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		if (FleetPackage.eINSTANCE.getVessel_Capacity() == feature) {
			return new DelegateInformation(FleetPackage.eINSTANCE.getVessel_VesselClass(), FleetPackage.eINSTANCE.getVesselClass_Capacity(), (Integer) 0);
		} else if (FleetPackage.eINSTANCE.getVessel_FillCapacity() == feature) {
			return new DelegateInformation(FleetPackage.eINSTANCE.getVessel_VesselClass(), FleetPackage.eINSTANCE.getVesselClass_FillCapacity(), (Double) 1.0);
		}
		return super.getUnsetValueOrDelegate(feature);
	}

} // end of VesselImpl

// finish type fixing
