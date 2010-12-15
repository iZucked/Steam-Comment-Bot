/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleetallocation.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.VesselClass;

import scenario.schedule.fleetallocation.FleetallocationPackage;
import scenario.schedule.fleetallocation.SpotVessel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Vessel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.fleetallocation.impl.SpotVesselImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link scenario.schedule.fleetallocation.impl.SpotVesselImpl#getVesselClass <em>Vessel Class</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpotVesselImpl extends AllocatedVesselImpl implements SpotVessel {
	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVesselClass() <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass vesselClass;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotVesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetallocationPackage.Literals.SPOT_VESSEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(int newIndex) {
		int oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetallocationPackage.SPOT_VESSEL__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass getVesselClass() {
		if (vesselClass != null && vesselClass.eIsProxy()) {
			InternalEObject oldVesselClass = (InternalEObject)vesselClass;
			vesselClass = (VesselClass)eResolveProxy(oldVesselClass);
			if (vesselClass != oldVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetallocationPackage.SPOT_VESSEL__VESSEL_CLASS, oldVesselClass, vesselClass));
			}
		}
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetVesselClass() {
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselClass(VesselClass newVesselClass) {
		VesselClass oldVesselClass = vesselClass;
		vesselClass = newVesselClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetallocationPackage.SPOT_VESSEL__VESSEL_CLASS, oldVesselClass, vesselClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetallocationPackage.SPOT_VESSEL__INDEX:
				return getIndex();
			case FleetallocationPackage.SPOT_VESSEL__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
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
			case FleetallocationPackage.SPOT_VESSEL__INDEX:
				setIndex((Integer)newValue);
				return;
			case FleetallocationPackage.SPOT_VESSEL__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
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
			case FleetallocationPackage.SPOT_VESSEL__INDEX:
				setIndex(INDEX_EDEFAULT);
				return;
			case FleetallocationPackage.SPOT_VESSEL__VESSEL_CLASS:
				setVesselClass((VesselClass)null);
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
			case FleetallocationPackage.SPOT_VESSEL__INDEX:
				return index != INDEX_EDEFAULT;
			case FleetallocationPackage.SPOT_VESSEL__VESSEL_CLASS:
				return vesselClass != null;
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
		result.append(" (index: ");
		result.append(index);
		result.append(')');
		return result.toString();
	}

	@Override
	public String getName() {
		return getVesselClass().getName() +" " + Integer.toString(getIndex());
	}

} //SpotVesselImpl
