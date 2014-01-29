/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.SlotActuals;

import com.mmxlabs.models.lng.cargo.Slot;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getCV <em>CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getMmBtu <em>Mm Btu</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getPortCharges <em>Port Charges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getBaseFuelConsumption <em>Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getSlot <em>Slot</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class SlotActualsImpl extends EObjectImpl implements SlotActuals {
	/**
	 * The default value of the '{@link #getCV() <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCV()
	 * @generated
	 * @ordered
	 */
	protected static final float CV_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getCV() <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCV()
	 * @generated
	 * @ordered
	 */
	protected float cv = CV_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected static final float VOLUME_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected float volume = VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMmBtu() <em>Mm Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmBtu()
	 * @generated
	 * @ordered
	 */
	protected static final int MM_BTU_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMmBtu() <em>Mm Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmBtu()
	 * @generated
	 * @ordered
	 */
	protected int mmBtu = MM_BTU_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortCharges() <em>Port Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCharges()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_CHARGES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortCharges() <em>Port Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCharges()
	 * @generated
	 * @ordered
	 */
	protected int portCharges = PORT_CHARGES_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFuelConsumption() <em>Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_FUEL_CONSUMPTION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseFuelConsumption() <em>Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected int baseFuelConsumption = BASE_FUEL_CONSUMPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot slot;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.SLOT_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getCV() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCV(float newCV) {
		float oldCV = cv;
		cv = newCV;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__CV, oldCV, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getVolume() {
		return volume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolume(float newVolume) {
		float oldVolume = volume;
		volume = newVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__VOLUME, oldVolume, volume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMmBtu() {
		return mmBtu;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMmBtu(int newMmBtu) {
		int oldMmBtu = mmBtu;
		mmBtu = newMmBtu;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__MM_BTU, oldMmBtu, mmBtu));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortCharges() {
		return portCharges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortCharges(int newPortCharges) {
		int oldPortCharges = portCharges;
		portCharges = newPortCharges;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__PORT_CHARGES, oldPortCharges, portCharges));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseFuelConsumption() {
		return baseFuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseFuelConsumption(int newBaseFuelConsumption) {
		int oldBaseFuelConsumption = baseFuelConsumption;
		baseFuelConsumption = newBaseFuelConsumption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION, oldBaseFuelConsumption, baseFuelConsumption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.SLOT_ACTUALS__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActualsPackage.SLOT_ACTUALS__CV:
				return getCV();
			case ActualsPackage.SLOT_ACTUALS__VOLUME:
				return getVolume();
			case ActualsPackage.SLOT_ACTUALS__MM_BTU:
				return getMmBtu();
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				return getPortCharges();
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				return getBaseFuelConsumption();
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
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
			case ActualsPackage.SLOT_ACTUALS__CV:
				setCV((Float)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__VOLUME:
				setVolume((Float)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__MM_BTU:
				setMmBtu((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				setPortCharges((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				setBaseFuelConsumption((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				setSlot((Slot)newValue);
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
			case ActualsPackage.SLOT_ACTUALS__CV:
				setCV(CV_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__VOLUME:
				setVolume(VOLUME_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__MM_BTU:
				setMmBtu(MM_BTU_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				setPortCharges(PORT_CHARGES_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				setBaseFuelConsumption(BASE_FUEL_CONSUMPTION_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				setSlot((Slot)null);
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
			case ActualsPackage.SLOT_ACTUALS__CV:
				return cv != CV_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__VOLUME:
				return volume != VOLUME_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__MM_BTU:
				return mmBtu != MM_BTU_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				return portCharges != PORT_CHARGES_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				return baseFuelConsumption != BASE_FUEL_CONSUMPTION_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				return slot != null;
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
		result.append(" (CV: ");
		result.append(cv);
		result.append(", volume: ");
		result.append(volume);
		result.append(", mmBtu: ");
		result.append(mmBtu);
		result.append(", portCharges: ");
		result.append(portCharges);
		result.append(", baseFuelConsumption: ");
		result.append(baseFuelConsumption);
		result.append(')');
		return result.toString();
	}

} //SlotActualsImpl
