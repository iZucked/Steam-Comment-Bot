/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.pricing.CooldownPriceEntry;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cooldown Price Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceEntryImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceEntryImpl#getLumpsumExpression <em>Lumpsum Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceEntryImpl#getVolumeExpression <em>Volume Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CooldownPriceEntryImpl extends MMXObjectImpl implements CooldownPriceEntry {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> ports;

	/**
	 * The default value of the '{@link #getLumpsumExpression() <em>Lumpsum Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLumpsumExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String LUMPSUM_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLumpsumExpression() <em>Lumpsum Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLumpsumExpression()
	 * @generated
	 * @ordered
	 */
	protected String lumpsumExpression = LUMPSUM_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeExpression() <em>Volume Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String VOLUME_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolumeExpression() <em>Volume Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeExpression()
	 * @generated
	 * @ordered
	 */
	protected String volumeExpression = VOLUME_EXPRESSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CooldownPriceEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.COOLDOWN_PRICE_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, PricingPackage.COOLDOWN_PRICE_ENTRY__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLumpsumExpression() {
		return lumpsumExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLumpsumExpression(String newLumpsumExpression) {
		String oldLumpsumExpression = lumpsumExpression;
		lumpsumExpression = newLumpsumExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.COOLDOWN_PRICE_ENTRY__LUMPSUM_EXPRESSION, oldLumpsumExpression, lumpsumExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVolumeExpression() {
		return volumeExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeExpression(String newVolumeExpression) {
		String oldVolumeExpression = volumeExpression;
		volumeExpression = newVolumeExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.COOLDOWN_PRICE_ENTRY__VOLUME_EXPRESSION, oldVolumeExpression, volumeExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.COOLDOWN_PRICE_ENTRY__PORTS:
				return getPorts();
			case PricingPackage.COOLDOWN_PRICE_ENTRY__LUMPSUM_EXPRESSION:
				return getLumpsumExpression();
			case PricingPackage.COOLDOWN_PRICE_ENTRY__VOLUME_EXPRESSION:
				return getVolumeExpression();
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
			case PricingPackage.COOLDOWN_PRICE_ENTRY__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case PricingPackage.COOLDOWN_PRICE_ENTRY__LUMPSUM_EXPRESSION:
				setLumpsumExpression((String)newValue);
				return;
			case PricingPackage.COOLDOWN_PRICE_ENTRY__VOLUME_EXPRESSION:
				setVolumeExpression((String)newValue);
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
			case PricingPackage.COOLDOWN_PRICE_ENTRY__PORTS:
				getPorts().clear();
				return;
			case PricingPackage.COOLDOWN_PRICE_ENTRY__LUMPSUM_EXPRESSION:
				setLumpsumExpression(LUMPSUM_EXPRESSION_EDEFAULT);
				return;
			case PricingPackage.COOLDOWN_PRICE_ENTRY__VOLUME_EXPRESSION:
				setVolumeExpression(VOLUME_EXPRESSION_EDEFAULT);
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
			case PricingPackage.COOLDOWN_PRICE_ENTRY__PORTS:
				return ports != null && !ports.isEmpty();
			case PricingPackage.COOLDOWN_PRICE_ENTRY__LUMPSUM_EXPRESSION:
				return LUMPSUM_EXPRESSION_EDEFAULT == null ? lumpsumExpression != null : !LUMPSUM_EXPRESSION_EDEFAULT.equals(lumpsumExpression);
			case PricingPackage.COOLDOWN_PRICE_ENTRY__VOLUME_EXPRESSION:
				return VOLUME_EXPRESSION_EDEFAULT == null ? volumeExpression != null : !VOLUME_EXPRESSION_EDEFAULT.equals(volumeExpression);
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
		result.append(" (lumpsumExpression: ");
		result.append(lumpsumExpression);
		result.append(", volumeExpression: ");
		result.append(volumeExpression);
		result.append(')');
		return result.toString();
	}

} //CooldownPriceEntryImpl
