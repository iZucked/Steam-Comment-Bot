/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;
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
 * An implementation of the model object '<em><b>Cargo Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getActuals <em>Actuals</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getContractYear <em>Contract Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getOperationNumber <em>Operation Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getSubOperationNumber <em>Sub Operation Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getSellerID <em>Seller ID</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getCargoReference <em>Cargo Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getCargoReferenceSeller <em>Cargo Reference Seller</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getInsurancePremium <em>Insurance Premium</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getCrewBonus <em>Crew Bonus</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoActualsImpl extends EObjectImpl implements CargoActuals {
	/**
	 * The cached value of the '{@link #getActuals() <em>Actuals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActuals()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotActuals> actuals;

	/**
	 * The default value of the '{@link #getContractYear() <em>Contract Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractYear()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTRACT_YEAR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getContractYear() <em>Contract Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractYear()
	 * @generated
	 * @ordered
	 */
	protected int contractYear = CONTRACT_YEAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationNumber() <em>Operation Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int OPERATION_NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOperationNumber() <em>Operation Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationNumber()
	 * @generated
	 * @ordered
	 */
	protected int operationNumber = OPERATION_NUMBER_EDEFAULT;

	/**
	 * The default value of the '{@link #getSubOperationNumber() <em>Sub Operation Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubOperationNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int SUB_OPERATION_NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSubOperationNumber() <em>Sub Operation Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubOperationNumber()
	 * @generated
	 * @ordered
	 */
	protected int subOperationNumber = SUB_OPERATION_NUMBER_EDEFAULT;

	/**
	 * The default value of the '{@link #getSellerID() <em>Seller ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellerID()
	 * @generated
	 * @ordered
	 */
	protected static final String SELLER_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSellerID() <em>Seller ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellerID()
	 * @generated
	 * @ordered
	 */
	protected String sellerID = SELLER_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getCargoReference() <em>Cargo Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoReference()
	 * @generated
	 * @ordered
	 */
	protected static final String CARGO_REFERENCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCargoReference() <em>Cargo Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoReference()
	 * @generated
	 * @ordered
	 */
	protected String cargoReference = CARGO_REFERENCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCargoReferenceSeller() <em>Cargo Reference Seller</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoReferenceSeller()
	 * @generated
	 * @ordered
	 */
	protected static final String CARGO_REFERENCE_SELLER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCargoReferenceSeller() <em>Cargo Reference Seller</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoReferenceSeller()
	 * @generated
	 * @ordered
	 */
	protected String cargoReferenceSeller = CARGO_REFERENCE_SELLER_EDEFAULT;

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
	 * The cached value of the '{@link #getCargo() <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo cargo;

	/**
	 * The default value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_FUEL_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected double baseFuelPrice = BASE_FUEL_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getInsurancePremium() <em>Insurance Premium</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsurancePremium()
	 * @generated
	 * @ordered
	 */
	protected static final int INSURANCE_PREMIUM_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getInsurancePremium() <em>Insurance Premium</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsurancePremium()
	 * @generated
	 * @ordered
	 */
	protected int insurancePremium = INSURANCE_PREMIUM_EDEFAULT;

	/**
	 * The default value of the '{@link #getCrewBonus() <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrewBonus()
	 * @generated
	 * @ordered
	 */
	protected static final int CREW_BONUS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCrewBonus() <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrewBonus()
	 * @generated
	 * @ordered
	 */
	protected int crewBonus = CREW_BONUS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.CARGO_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getBaseFuelPrice() {
		return baseFuelPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseFuelPrice(double newBaseFuelPrice) {
		double oldBaseFuelPrice = baseFuelPrice;
		baseFuelPrice = newBaseFuelPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE, oldBaseFuelPrice, baseFuelPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getInsurancePremium() {
		return insurancePremium;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInsurancePremium(int newInsurancePremium) {
		int oldInsurancePremium = insurancePremium;
		insurancePremium = newInsurancePremium;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM, oldInsurancePremium, insurancePremium));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCrewBonus() {
		return crewBonus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCrewBonus(int newCrewBonus) {
		int oldCrewBonus = crewBonus;
		crewBonus = newCrewBonus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__CREW_BONUS, oldCrewBonus, crewBonus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SlotActuals> getActuals() {
		if (actuals == null) {
			actuals = new EObjectContainmentEList.Resolving<SlotActuals>(SlotActuals.class, this, ActualsPackage.CARGO_ACTUALS__ACTUALS);
		}
		return actuals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getContractYear() {
		return contractYear;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContractYear(int newContractYear) {
		int oldContractYear = contractYear;
		contractYear = newContractYear;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__CONTRACT_YEAR, oldContractYear, contractYear));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getOperationNumber() {
		return operationNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationNumber(int newOperationNumber) {
		int oldOperationNumber = operationNumber;
		operationNumber = newOperationNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__OPERATION_NUMBER, oldOperationNumber, operationNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSubOperationNumber() {
		return subOperationNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSubOperationNumber(int newSubOperationNumber) {
		int oldSubOperationNumber = subOperationNumber;
		subOperationNumber = newSubOperationNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__SUB_OPERATION_NUMBER, oldSubOperationNumber, subOperationNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSellerID() {
		return sellerID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSellerID(String newSellerID) {
		String oldSellerID = sellerID;
		sellerID = newSellerID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__SELLER_ID, oldSellerID, sellerID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCargoReference() {
		return cargoReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoReference(String newCargoReference) {
		String oldCargoReference = cargoReference;
		cargoReference = newCargoReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE, oldCargoReference, cargoReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCargoReferenceSeller() {
		return cargoReferenceSeller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoReferenceSeller(String newCargoReferenceSeller) {
		String oldCargoReferenceSeller = cargoReferenceSeller;
		cargoReferenceSeller = newCargoReferenceSeller;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE_SELLER, oldCargoReferenceSeller, cargoReferenceSeller));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.CARGO_ACTUALS__VESSEL, oldVessel, vessel));
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
	@Override
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cargo getCargo() {
		if (cargo != null && cargo.eIsProxy()) {
			InternalEObject oldCargo = (InternalEObject)cargo;
			cargo = (Cargo)eResolveProxy(oldCargo);
			if (cargo != oldCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.CARGO_ACTUALS__CARGO, oldCargo, cargo));
			}
		}
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetCargo() {
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargo(Cargo newCargo) {
		Cargo oldCargo = cargo;
		cargo = newCargo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__CARGO, oldCargo, cargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				return ((InternalEList<?>)getActuals()).basicRemove(otherEnd, msgs);
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
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				return getActuals();
			case ActualsPackage.CARGO_ACTUALS__CONTRACT_YEAR:
				return getContractYear();
			case ActualsPackage.CARGO_ACTUALS__OPERATION_NUMBER:
				return getOperationNumber();
			case ActualsPackage.CARGO_ACTUALS__SUB_OPERATION_NUMBER:
				return getSubOperationNumber();
			case ActualsPackage.CARGO_ACTUALS__SELLER_ID:
				return getSellerID();
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE:
				return getCargoReference();
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE_SELLER:
				return getCargoReferenceSeller();
			case ActualsPackage.CARGO_ACTUALS__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case ActualsPackage.CARGO_ACTUALS__CARGO:
				if (resolve) return getCargo();
				return basicGetCargo();
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				return getBaseFuelPrice();
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				return getInsurancePremium();
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				return getCrewBonus();
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
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				getActuals().clear();
				getActuals().addAll((Collection<? extends SlotActuals>)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__CONTRACT_YEAR:
				setContractYear((Integer)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__OPERATION_NUMBER:
				setOperationNumber((Integer)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__SUB_OPERATION_NUMBER:
				setSubOperationNumber((Integer)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__SELLER_ID:
				setSellerID((String)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE:
				setCargoReference((String)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE_SELLER:
				setCargoReferenceSeller((String)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__CARGO:
				setCargo((Cargo)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				setBaseFuelPrice((Double)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				setInsurancePremium((Integer)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				setCrewBonus((Integer)newValue);
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
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				getActuals().clear();
				return;
			case ActualsPackage.CARGO_ACTUALS__CONTRACT_YEAR:
				setContractYear(CONTRACT_YEAR_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__OPERATION_NUMBER:
				setOperationNumber(OPERATION_NUMBER_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__SUB_OPERATION_NUMBER:
				setSubOperationNumber(SUB_OPERATION_NUMBER_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__SELLER_ID:
				setSellerID(SELLER_ID_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE:
				setCargoReference(CARGO_REFERENCE_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE_SELLER:
				setCargoReferenceSeller(CARGO_REFERENCE_SELLER_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__VESSEL:
				setVessel((Vessel)null);
				return;
			case ActualsPackage.CARGO_ACTUALS__CARGO:
				setCargo((Cargo)null);
				return;
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				setBaseFuelPrice(BASE_FUEL_PRICE_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				setInsurancePremium(INSURANCE_PREMIUM_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				setCrewBonus(CREW_BONUS_EDEFAULT);
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
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				return actuals != null && !actuals.isEmpty();
			case ActualsPackage.CARGO_ACTUALS__CONTRACT_YEAR:
				return contractYear != CONTRACT_YEAR_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__OPERATION_NUMBER:
				return operationNumber != OPERATION_NUMBER_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__SUB_OPERATION_NUMBER:
				return subOperationNumber != SUB_OPERATION_NUMBER_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__SELLER_ID:
				return SELLER_ID_EDEFAULT == null ? sellerID != null : !SELLER_ID_EDEFAULT.equals(sellerID);
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE:
				return CARGO_REFERENCE_EDEFAULT == null ? cargoReference != null : !CARGO_REFERENCE_EDEFAULT.equals(cargoReference);
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE_SELLER:
				return CARGO_REFERENCE_SELLER_EDEFAULT == null ? cargoReferenceSeller != null : !CARGO_REFERENCE_SELLER_EDEFAULT.equals(cargoReferenceSeller);
			case ActualsPackage.CARGO_ACTUALS__VESSEL:
				return vessel != null;
			case ActualsPackage.CARGO_ACTUALS__CARGO:
				return cargo != null;
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				return baseFuelPrice != BASE_FUEL_PRICE_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				return insurancePremium != INSURANCE_PREMIUM_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				return crewBonus != CREW_BONUS_EDEFAULT;
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
		result.append(" (contractYear: ");
		result.append(contractYear);
		result.append(", operationNumber: ");
		result.append(operationNumber);
		result.append(", subOperationNumber: ");
		result.append(subOperationNumber);
		result.append(", sellerID: ");
		result.append(sellerID);
		result.append(", cargoReference: ");
		result.append(cargoReference);
		result.append(", cargoReferenceSeller: ");
		result.append(cargoReferenceSeller);
		result.append(", baseFuelPrice: ");
		result.append(baseFuelPrice);
		result.append(", insurancePremium: ");
		result.append(insurancePremium);
		result.append(", crewBonus: ");
		result.append(crewBonus);
		result.append(')');
		return result.toString();
	}

} //CargoActualsImpl
