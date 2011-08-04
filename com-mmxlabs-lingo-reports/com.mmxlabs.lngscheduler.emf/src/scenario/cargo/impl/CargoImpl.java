/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.CargoType;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.fleet.Vessel;
import scenario.impl.AnnotatedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoImpl#getCargoType <em>Cargo Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoImpl extends AnnotatedObjectImpl implements Cargo {
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
	 * The cached value of the '{@link #getAllowedVessels() <em>Allowed Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> allowedVessels;

	/**
	 * The cached value of the '{@link #getLoadSlot() <em>Load Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot;

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
	 * The default value of the '{@link #getCargoType() <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoType()
	 * @generated
	 * @ordered
	 */
	protected static final CargoType CARGO_TYPE_EDEFAULT = CargoType.FLEET;

	/**
	 * The cached value of the '{@link #getCargoType() <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoType()
	 * @generated
	 * @ordered
	 */
	protected CargoType cargoType = CARGO_TYPE_EDEFAULT;

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
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public LoadSlot getLoadSlot() {
		if (loadSlot != null && loadSlot.eIsProxy()) {
			InternalEObject oldLoadSlot = (InternalEObject)loadSlot;
			loadSlot = (LoadSlot)eResolveProxy(oldLoadSlot);
			if (loadSlot != oldLoadSlot) {
				InternalEObject newLoadSlot = (InternalEObject)loadSlot;
				NotificationChain msgs = oldLoadSlot.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__LOAD_SLOT, null, null);
				if (newLoadSlot.eInternalContainer() == null) {
					msgs = newLoadSlot.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__LOAD_SLOT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CARGO__LOAD_SLOT, oldLoadSlot, loadSlot));
			}
		}
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot() {
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLoadSlot(LoadSlot newLoadSlot, NotificationChain msgs) {
		LoadSlot oldLoadSlot = loadSlot;
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
	public void setLoadSlot(LoadSlot newLoadSlot) {
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
		if (dischargeSlot != null && dischargeSlot.eIsProxy()) {
			InternalEObject oldDischargeSlot = (InternalEObject)dischargeSlot;
			dischargeSlot = (Slot)eResolveProxy(oldDischargeSlot);
			if (dischargeSlot != oldDischargeSlot) {
				InternalEObject newDischargeSlot = (InternalEObject)dischargeSlot;
				NotificationChain msgs = oldDischargeSlot.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__DISCHARGE_SLOT, null, null);
				if (newDischargeSlot.eInternalContainer() == null) {
					msgs = newDischargeSlot.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO__DISCHARGE_SLOT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CARGO__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
			}
		}
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetDischargeSlot() {
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
	public EList<Vessel> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<Vessel>(Vessel.class, this, CargoPackage.CARGO__ALLOWED_VESSELS);
		}
		return allowedVessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoType getCargoType() {
		return cargoType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoType(CargoType newCargoType) {
		CargoType oldCargoType = cargoType;
		cargoType = newCargoType == null ? CARGO_TYPE_EDEFAULT : newCargoType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__CARGO_TYPE, oldCargoType, cargoType));
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
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				return getAllowedVessels();
			case CargoPackage.CARGO__LOAD_SLOT:
				if (resolve) return getLoadSlot();
				return basicGetLoadSlot();
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				if (resolve) return getDischargeSlot();
				return basicGetDischargeSlot();
			case CargoPackage.CARGO__CARGO_TYPE:
				return getCargoType();
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
			case CargoPackage.CARGO__ID:
				setId((String)newValue);
				return;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends Vessel>)newValue);
				return;
			case CargoPackage.CARGO__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				setDischargeSlot((Slot)newValue);
				return;
			case CargoPackage.CARGO__CARGO_TYPE:
				setCargoType((CargoType)newValue);
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
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				return;
			case CargoPackage.CARGO__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				setDischargeSlot((Slot)null);
				return;
			case CargoPackage.CARGO__CARGO_TYPE:
				setCargoType(CARGO_TYPE_EDEFAULT);
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
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
			case CargoPackage.CARGO__LOAD_SLOT:
				return loadSlot != null;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				return dischargeSlot != null;
			case CargoPackage.CARGO__CARGO_TYPE:
				return cargoType != CARGO_TYPE_EDEFAULT;
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
		result.append(", cargoType: ");
		result.append(cargoType);
		result.append(')');
		return result.toString();
	}

} //CargoImpl
