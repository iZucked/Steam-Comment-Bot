/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;

import com.mmxlabs.models.lng.port.Port;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Repositioning Fee Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RepositioningFeeTermImpl#getOriginPort <em>Origin Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RepositioningFeeTermImpl extends EObjectImpl implements RepositioningFeeTerm {
	/**
	 * The cached value of the '{@link #getOriginPort() <em>Origin Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginPort()
	 * @generated
	 * @ordered
	 */
	protected Port originPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RepositioningFeeTermImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.REPOSITIONING_FEE_TERM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getOriginPort() {
		if (originPort != null && originPort.eIsProxy()) {
			InternalEObject oldOriginPort = (InternalEObject)originPort;
			originPort = (Port)eResolveProxy(oldOriginPort);
			if (originPort != oldOriginPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.REPOSITIONING_FEE_TERM__ORIGIN_PORT, oldOriginPort, originPort));
			}
		}
		return originPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetOriginPort() {
		return originPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOriginPort(Port newOriginPort) {
		Port oldOriginPort = originPort;
		originPort = newOriginPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REPOSITIONING_FEE_TERM__ORIGIN_PORT, oldOriginPort, originPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				if (resolve) return getOriginPort();
				return basicGetOriginPort();
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
			case CommercialPackage.REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				setOriginPort((Port)newValue);
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
			case CommercialPackage.REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				setOriginPort((Port)null);
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
			case CommercialPackage.REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				return originPort != null;
		}
		return super.eIsSet(featureID);
	}

} //RepositioningFeeTermImpl
