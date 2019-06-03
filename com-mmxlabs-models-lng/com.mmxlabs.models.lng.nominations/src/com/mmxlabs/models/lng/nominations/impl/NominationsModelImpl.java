/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.ContractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.SlotNominationSpec;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getSlotNominationSpecs <em>Slot Nomination Specs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getSlotNominations <em>Slot Nominations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getContractNominationSpecs <em>Contract Nomination Specs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getContractNominations <em>Contract Nominations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NominationsModelImpl extends UUIDObjectImpl implements NominationsModel {
	/**
	 * The cached value of the '{@link #getSlotNominationSpecs() <em>Slot Nomination Specs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotNominationSpecs()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotNominationSpec> slotNominationSpecs;

	/**
	 * The cached value of the '{@link #getSlotNominations() <em>Slot Nominations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotNominations()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotNomination> slotNominations;

	/**
	 * The cached value of the '{@link #getContractNominationSpecs() <em>Contract Nomination Specs</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractNominationSpecs()
	 * @generated
	 * @ordered
	 */
	protected EList<ContractNominationSpec> contractNominationSpecs;

	/**
	 * The cached value of the '{@link #getContractNominations() <em>Contract Nominations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractNominations()
	 * @generated
	 * @ordered
	 */
	protected EList<ContractNomination> contractNominations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NominationsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.NOMINATIONS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SlotNominationSpec> getSlotNominationSpecs() {
		if (slotNominationSpecs == null) {
			slotNominationSpecs = new EObjectContainmentEList<SlotNominationSpec>(SlotNominationSpec.class, this, NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS);
		}
		return slotNominationSpecs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SlotNomination> getSlotNominations() {
		if (slotNominations == null) {
			slotNominations = new EObjectContainmentEList<SlotNomination>(SlotNomination.class, this, NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATIONS);
		}
		return slotNominations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ContractNominationSpec> getContractNominationSpecs() {
		if (contractNominationSpecs == null) {
			contractNominationSpecs = new EObjectResolvingEList<ContractNominationSpec>(ContractNominationSpec.class, this, NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATION_SPECS);
		}
		return contractNominationSpecs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ContractNomination> getContractNominations() {
		if (contractNominations == null) {
			contractNominations = new EObjectResolvingEList<ContractNomination>(ContractNomination.class, this, NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATIONS);
		}
		return contractNominations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS:
				return ((InternalEList<?>)getSlotNominationSpecs()).basicRemove(otherEnd, msgs);
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATIONS:
				return ((InternalEList<?>)getSlotNominations()).basicRemove(otherEnd, msgs);
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
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS:
				return getSlotNominationSpecs();
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATIONS:
				return getSlotNominations();
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATION_SPECS:
				return getContractNominationSpecs();
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATIONS:
				return getContractNominations();
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
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS:
				getSlotNominationSpecs().clear();
				getSlotNominationSpecs().addAll((Collection<? extends SlotNominationSpec>)newValue);
				return;
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATIONS:
				getSlotNominations().clear();
				getSlotNominations().addAll((Collection<? extends SlotNomination>)newValue);
				return;
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATION_SPECS:
				getContractNominationSpecs().clear();
				getContractNominationSpecs().addAll((Collection<? extends ContractNominationSpec>)newValue);
				return;
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATIONS:
				getContractNominations().clear();
				getContractNominations().addAll((Collection<? extends ContractNomination>)newValue);
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
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS:
				getSlotNominationSpecs().clear();
				return;
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATIONS:
				getSlotNominations().clear();
				return;
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATION_SPECS:
				getContractNominationSpecs().clear();
				return;
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATIONS:
				getContractNominations().clear();
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
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS:
				return slotNominationSpecs != null && !slotNominationSpecs.isEmpty();
			case NominationsPackage.NOMINATIONS_MODEL__SLOT_NOMINATIONS:
				return slotNominations != null && !slotNominations.isEmpty();
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATION_SPECS:
				return contractNominationSpecs != null && !contractNominationSpecs.isEmpty();
			case NominationsPackage.NOMINATIONS_MODEL__CONTRACT_NOMINATIONS:
				return contractNominations != null && !contractNominations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NominationsModelImpl
