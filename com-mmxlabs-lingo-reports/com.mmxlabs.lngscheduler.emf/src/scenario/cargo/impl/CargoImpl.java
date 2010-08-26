/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoImpl extends EObjectImpl implements Cargo {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLoadSlot() <em>Load Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot loadSlot;

	/**
	 * The cached value of the '{@link #getDischargeSlot() <em>Discharge Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot dischargeSlot;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CARGO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getLoadSlot() {
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLoadSlot(Slot newLoadSlot, NotificationChain msgs) {
		Slot oldLoadSlot = loadSlot;
		loadSlot = newLoadSlot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__LOAD_SLOT, oldLoadSlot, newLoadSlot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadSlot(Slot newLoadSlot) {
		if (newLoadSlot != loadSlot) {
			NotificationChain msgs = null;
			if (loadSlot != null)
				msgs = ((InternalEObject)loadSlot).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__LOAD_SLOT, null, msgs);
			if (newLoadSlot != null)
				msgs = ((InternalEObject)newLoadSlot).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__LOAD_SLOT, null, msgs);
			msgs = basicSetLoadSlot(newLoadSlot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__LOAD_SLOT, newLoadSlot, newLoadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getDischargeSlot() {
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDischargeSlot(Slot newDischargeSlot, NotificationChain msgs) {
		Slot oldDischargeSlot = dischargeSlot;
		dischargeSlot = newDischargeSlot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__DISCHARGE_SLOT, oldDischargeSlot, newDischargeSlot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeSlot(Slot newDischargeSlot) {
		if (newDischargeSlot != dischargeSlot) {
			NotificationChain msgs = null;
			if (dischargeSlot != null)
				msgs = ((InternalEObject)dischargeSlot).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__DISCHARGE_SLOT, null, msgs);
			if (newDischargeSlot != null)
				msgs = ((InternalEObject)newDischargeSlot).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__DISCHARGE_SLOT, null, msgs);
			msgs = basicSetDischargeSlot(newDischargeSlot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__DISCHARGE_SLOT, newDischargeSlot, newDischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO__LOAD_SLOT:
				return basicSetLoadSlot(null, msgs);
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				return basicSetDischargeSlot(null, msgs);
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
			case CargoPackage.CARGO__ID:
				return getId();
			case CargoPackage.CARGO__LOAD_SLOT:
				return getLoadSlot();
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				return getDischargeSlot();
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
			case CargoPackage.CARGO__ID:
				setId((String)newValue);
				return;
			case CargoPackage.CARGO__LOAD_SLOT:
				setLoadSlot((Slot)newValue);
				return;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				setDischargeSlot((Slot)newValue);
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
			case CargoPackage.CARGO__ID:
				setId(ID_EDEFAULT);
				return;
			case CargoPackage.CARGO__LOAD_SLOT:
				setLoadSlot((Slot)null);
				return;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				setDischargeSlot((Slot)null);
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
			case CargoPackage.CARGO__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case CargoPackage.CARGO__LOAD_SLOT:
				return loadSlot != null;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				return dischargeSlot != null;
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
		result.append(" (id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

} //CargoImpl
