/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.InventorySubprofile;
import com.mmxlabs.models.lng.adp.MultipleInventoryProfile;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiple Inventory Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MultipleInventoryProfileImpl#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MultipleInventoryProfileImpl#getVolumeFlex <em>Volume Flex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MultipleInventoryProfileImpl#getInventories <em>Inventories</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MultipleInventoryProfileImpl#getFullCargoLotValue <em>Full Cargo Lot Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultipleInventoryProfileImpl extends EObjectImpl implements MultipleInventoryProfile {
	/**
	 * The default value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected int windowSize = WINDOW_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeFlex() <em>Volume Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeFlex()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_FLEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeFlex() <em>Volume Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeFlex()
	 * @generated
	 * @ordered
	 */
	protected int volumeFlex = VOLUME_FLEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInventories() <em>Inventories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInventories()
	 * @generated
	 * @ordered
	 */
	protected EList<InventorySubprofile> inventories;

	/**
	 * The default value of the '{@link #getFullCargoLotValue() <em>Full Cargo Lot Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFullCargoLotValue()
	 * @generated
	 * @ordered
	 */
	protected static final int FULL_CARGO_LOT_VALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFullCargoLotValue() <em>Full Cargo Lot Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFullCargoLotValue()
	 * @generated
	 * @ordered
	 */
	protected int fullCargoLotValue = FULL_CARGO_LOT_VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleInventoryProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.MULTIPLE_INVENTORY_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSize(int newWindowSize) {
		int oldWindowSize = windowSize;
		windowSize = newWindowSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULTIPLE_INVENTORY_PROFILE__WINDOW_SIZE, oldWindowSize, windowSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVolumeFlex() {
		return volumeFlex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeFlex(int newVolumeFlex) {
		int oldVolumeFlex = volumeFlex;
		volumeFlex = newVolumeFlex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULTIPLE_INVENTORY_PROFILE__VOLUME_FLEX, oldVolumeFlex, volumeFlex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventorySubprofile> getInventories() {
		if (inventories == null) {
			inventories = new EObjectContainmentEList.Resolving<InventorySubprofile>(InventorySubprofile.class, this, ADPPackage.MULTIPLE_INVENTORY_PROFILE__INVENTORIES);
		}
		return inventories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFullCargoLotValue() {
		return fullCargoLotValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFullCargoLotValue(int newFullCargoLotValue) {
		int oldFullCargoLotValue = fullCargoLotValue;
		fullCargoLotValue = newFullCargoLotValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULTIPLE_INVENTORY_PROFILE__FULL_CARGO_LOT_VALUE, oldFullCargoLotValue, fullCargoLotValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__INVENTORIES:
				return ((InternalEList<?>)getInventories()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__WINDOW_SIZE:
				return getWindowSize();
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__VOLUME_FLEX:
				return getVolumeFlex();
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__INVENTORIES:
				return getInventories();
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__FULL_CARGO_LOT_VALUE:
				return getFullCargoLotValue();
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
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__WINDOW_SIZE:
				setWindowSize((Integer)newValue);
				return;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__VOLUME_FLEX:
				setVolumeFlex((Integer)newValue);
				return;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__INVENTORIES:
				getInventories().clear();
				getInventories().addAll((Collection<? extends InventorySubprofile>)newValue);
				return;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__FULL_CARGO_LOT_VALUE:
				setFullCargoLotValue((Integer)newValue);
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
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__WINDOW_SIZE:
				setWindowSize(WINDOW_SIZE_EDEFAULT);
				return;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__VOLUME_FLEX:
				setVolumeFlex(VOLUME_FLEX_EDEFAULT);
				return;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__INVENTORIES:
				getInventories().clear();
				return;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__FULL_CARGO_LOT_VALUE:
				setFullCargoLotValue(FULL_CARGO_LOT_VALUE_EDEFAULT);
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
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__WINDOW_SIZE:
				return windowSize != WINDOW_SIZE_EDEFAULT;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__VOLUME_FLEX:
				return volumeFlex != VOLUME_FLEX_EDEFAULT;
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__INVENTORIES:
				return inventories != null && !inventories.isEmpty();
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE__FULL_CARGO_LOT_VALUE:
				return fullCargoLotValue != FULL_CARGO_LOT_VALUE_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (windowSize: ");
		result.append(windowSize);
		result.append(", volumeFlex: ");
		result.append(volumeFlex);
		result.append(", fullCargoLotValue: ");
		result.append(fullCargoLotValue);
		result.append(')');
		return result.toString();
	}

} //MultipleInventoryProfileImpl
