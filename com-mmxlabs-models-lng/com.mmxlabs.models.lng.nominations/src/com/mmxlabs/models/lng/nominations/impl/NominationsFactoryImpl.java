/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class NominationsFactoryImpl extends EFactoryImpl implements NominationsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static NominationsFactory init() {
		try {
			NominationsFactory theNominationsFactory = (NominationsFactory)EPackage.Registry.INSTANCE.getEFactory(NominationsPackage.eNS_URI);
			if (theNominationsFactory != null) {
				return theNominationsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new NominationsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NominationsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case NominationsPackage.NOMINATIONS_MODEL: return createNominationsModel();
			case NominationsPackage.SLOT_NOMINATION_SPEC: return createSlotNominationSpec();
			case NominationsPackage.SLOT_NOMINATION: return createSlotNomination();
			case NominationsPackage.CONTRACT_NOMINATION: return createContractNomination();
			case NominationsPackage.CONTRACT_NOMINATION_SPEC: return createContractNominationSpec();
			case NominationsPackage.NOMINATIONS_PARAMETERS: return createNominationsParameters();
			case NominationsPackage.NOMINATION_AUDIT_ITEM: return createNominationAuditItem();
			case NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM: return createNominationSpecAuditItem();
			case NominationsPackage.ABSTRACT_AUDIT_ITEM: return createAbstractAuditItem();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case NominationsPackage.DATE_PERIOD_PRIOR:
				return createDatePeriodPriorFromString(eDataType, initialValue);
			case NominationsPackage.SIDE:
				return createSideFromString(eDataType, initialValue);
			case NominationsPackage.AUDIT_ITEM_TYPE:
				return createAuditItemTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case NominationsPackage.DATE_PERIOD_PRIOR:
				return convertDatePeriodPriorToString(eDataType, instanceValue);
			case NominationsPackage.SIDE:
				return convertSideToString(eDataType, instanceValue);
			case NominationsPackage.AUDIT_ITEM_TYPE:
				return convertAuditItemTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationsModel createNominationsModel() {
		NominationsModelImpl nominationsModel = new NominationsModelImpl();
		return nominationsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotNominationSpec createSlotNominationSpec() {
		SlotNominationSpecImpl slotNominationSpec = new SlotNominationSpecImpl();
		return slotNominationSpec;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotNomination createSlotNomination() {
		SlotNominationImpl slotNomination = new SlotNominationImpl();
		return slotNomination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractNomination createContractNomination() {
		ContractNominationImpl contractNomination = new ContractNominationImpl();
		return contractNomination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractNominationSpec createContractNominationSpec() {
		ContractNominationSpecImpl contractNominationSpec = new ContractNominationSpecImpl();
		return contractNominationSpec;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationsParameters createNominationsParameters() {
		NominationsParametersImpl nominationsParameters = new NominationsParametersImpl();
		return nominationsParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationAuditItem createNominationAuditItem() {
		NominationAuditItemImpl nominationAuditItem = new NominationAuditItemImpl();
		return nominationAuditItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationSpecAuditItem createNominationSpecAuditItem() {
		NominationSpecAuditItemImpl nominationSpecAuditItem = new NominationSpecAuditItemImpl();
		return nominationSpecAuditItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AbstractAuditItem createAbstractAuditItem() {
		AbstractAuditItemImpl abstractAuditItem = new AbstractAuditItemImpl();
		return abstractAuditItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatePeriodPrior createDatePeriodPriorFromString(EDataType eDataType, String initialValue) {
		DatePeriodPrior result = DatePeriodPrior.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDatePeriodPriorToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Side createSideFromString(EDataType eDataType, String initialValue) {
		Side result = Side.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSideToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AuditItemType createAuditItemTypeFromString(EDataType eDataType, String initialValue) {
		AuditItemType result = AuditItemType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAuditItemTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationsPackage getNominationsPackage() {
		return (NominationsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static NominationsPackage getPackage() {
		return NominationsPackage.eINSTANCE;
	}

} //NominationsFactoryImpl
