/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DesSpacingAllocation;
import com.mmxlabs.models.lng.adp.DesSpacingRow;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import com.mmxlabs.models.lng.commercial.SalesContract;

import com.mmxlabs.models.lng.port.Port;
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
 * An implementation of the model object '<em><b>Des Spacing Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DesSpacingAllocationImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DesSpacingAllocationImpl#getDesSpacingRows <em>Des Spacing Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DesSpacingAllocationImpl#getPort <em>Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DesSpacingAllocationImpl extends SpacingAllocationImpl implements DesSpacingAllocation {
	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected VesselAvailability vessel;

	/**
	 * The cached value of the '{@link #getDesSpacingRows() <em>Des Spacing Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesSpacingRows()
	 * @generated
	 * @ordered
	 */
	protected EList<DesSpacingRow> desSpacingRows;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DesSpacingAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.DES_SPACING_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselAvailability getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (VesselAvailability)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.DES_SPACING_ALLOCATION__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVessel(VesselAvailability newVessel) {
		VesselAvailability oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DES_SPACING_ALLOCATION__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DesSpacingRow> getDesSpacingRows() {
		if (desSpacingRows == null) {
			desSpacingRows = new EObjectContainmentEList.Resolving<DesSpacingRow>(DesSpacingRow.class, this, ADPPackage.DES_SPACING_ALLOCATION__DES_SPACING_ROWS);
		}
		return desSpacingRows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.DES_SPACING_ALLOCATION__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DES_SPACING_ALLOCATION__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.DES_SPACING_ALLOCATION__DES_SPACING_ROWS:
				return ((InternalEList<?>)getDesSpacingRows()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.DES_SPACING_ALLOCATION__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case ADPPackage.DES_SPACING_ALLOCATION__DES_SPACING_ROWS:
				return getDesSpacingRows();
			case ADPPackage.DES_SPACING_ALLOCATION__PORT:
				if (resolve) return getPort();
				return basicGetPort();
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
			case ADPPackage.DES_SPACING_ALLOCATION__VESSEL:
				setVessel((VesselAvailability)newValue);
				return;
			case ADPPackage.DES_SPACING_ALLOCATION__DES_SPACING_ROWS:
				getDesSpacingRows().clear();
				getDesSpacingRows().addAll((Collection<? extends DesSpacingRow>)newValue);
				return;
			case ADPPackage.DES_SPACING_ALLOCATION__PORT:
				setPort((Port)newValue);
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
			case ADPPackage.DES_SPACING_ALLOCATION__VESSEL:
				setVessel((VesselAvailability)null);
				return;
			case ADPPackage.DES_SPACING_ALLOCATION__DES_SPACING_ROWS:
				getDesSpacingRows().clear();
				return;
			case ADPPackage.DES_SPACING_ALLOCATION__PORT:
				setPort((Port)null);
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
			case ADPPackage.DES_SPACING_ALLOCATION__VESSEL:
				return vessel != null;
			case ADPPackage.DES_SPACING_ALLOCATION__DES_SPACING_ROWS:
				return desSpacingRows != null && !desSpacingRows.isEmpty();
			case ADPPackage.DES_SPACING_ALLOCATION__PORT:
				return port != null;
		}
		return super.eIsSet(featureID);
	}

} //DesSpacingAllocationImpl
