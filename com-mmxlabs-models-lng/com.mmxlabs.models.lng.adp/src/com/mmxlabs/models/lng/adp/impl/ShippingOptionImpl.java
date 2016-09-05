/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ShippingOption;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.types.VesselAssignmentType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl#getVesselAssignmentType <em>Vessel Assignment Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl#getMaxLadenIdleDays <em>Max Laden Idle Days</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ShippingOptionImpl extends EObjectImpl implements ShippingOption {
	/**
	 * The cached value of the '{@link #getVesselAssignmentType() <em>Vessel Assignment Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAssignmentType()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType vesselAssignmentType;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getMaxLadenIdleDays() <em>Max Laden Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLadenIdleDays()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_LADEN_IDLE_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxLadenIdleDays() <em>Max Laden Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLadenIdleDays()
	 * @generated
	 * @ordered
	 */
	protected int maxLadenIdleDays = MAX_LADEN_IDLE_DAYS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShippingOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.SHIPPING_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getVesselAssignmentType() {
		if (vesselAssignmentType != null && vesselAssignmentType.eIsProxy()) {
			InternalEObject oldVesselAssignmentType = (InternalEObject)vesselAssignmentType;
			vesselAssignmentType = (VesselAssignmentType)eResolveProxy(oldVesselAssignmentType);
			if (vesselAssignmentType != oldVesselAssignmentType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE, oldVesselAssignmentType, vesselAssignmentType));
			}
		}
		return vesselAssignmentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetVesselAssignmentType() {
		return vesselAssignmentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselAssignmentType(VesselAssignmentType newVesselAssignmentType) {
		VesselAssignmentType oldVesselAssignmentType = vesselAssignmentType;
		vesselAssignmentType = newVesselAssignmentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE, oldVesselAssignmentType, vesselAssignmentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SHIPPING_OPTION__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SHIPPING_OPTION__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SHIPPING_OPTION__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxLadenIdleDays() {
		return maxLadenIdleDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxLadenIdleDays(int newMaxLadenIdleDays) {
		int oldMaxLadenIdleDays = maxLadenIdleDays;
		maxLadenIdleDays = newMaxLadenIdleDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS, oldMaxLadenIdleDays, maxLadenIdleDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE:
				if (resolve) return getVesselAssignmentType();
				return basicGetVesselAssignmentType();
			case ADPPackage.SHIPPING_OPTION__SPOT_INDEX:
				return getSpotIndex();
			case ADPPackage.SHIPPING_OPTION__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case ADPPackage.SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS:
				return getMaxLadenIdleDays();
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
			case ADPPackage.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE:
				setVesselAssignmentType((VesselAssignmentType)newValue);
				return;
			case ADPPackage.SHIPPING_OPTION__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case ADPPackage.SHIPPING_OPTION__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case ADPPackage.SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS:
				setMaxLadenIdleDays((Integer)newValue);
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
			case ADPPackage.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE:
				setVesselAssignmentType((VesselAssignmentType)null);
				return;
			case ADPPackage.SHIPPING_OPTION__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
				return;
			case ADPPackage.SHIPPING_OPTION__VESSEL:
				setVessel((Vessel)null);
				return;
			case ADPPackage.SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS:
				setMaxLadenIdleDays(MAX_LADEN_IDLE_DAYS_EDEFAULT);
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
			case ADPPackage.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE:
				return vesselAssignmentType != null;
			case ADPPackage.SHIPPING_OPTION__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
			case ADPPackage.SHIPPING_OPTION__VESSEL:
				return vessel != null;
			case ADPPackage.SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS:
				return maxLadenIdleDays != MAX_LADEN_IDLE_DAYS_EDEFAULT;
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
		result.append(" (spotIndex: ");
		result.append(spotIndex);
		result.append(", maxLadenIdleDays: ");
		result.append(maxLadenIdleDays);
		result.append(')');
		return result.toString();
	}

} //ShippingOptionImpl
