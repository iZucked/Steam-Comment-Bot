/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.nominations.NominationsFactory
 * @model kind="package"
 * @generated
 */
public interface NominationsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "nominations";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/nominations/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.nominations";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NominationsPackage eINSTANCE = com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationsModel()
	 * @generated
	 */
	int NOMINATIONS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Nomination Specs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL__NOMINATION_SPECS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Nominations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL__NOMINATIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Nomination Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL__NOMINATION_PARAMETERS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Audit Log</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL__AUDIT_LOG = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl <em>Abstract Nomination Spec</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAbstractNominationSpec()
	 * @generated
	 */
	int ABSTRACT_NOMINATION_SPEC = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__TYPE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__COUNTERPARTY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__REMARK = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__SIZE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__SIZE_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__ALERT_SIZE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Alert Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__SIDE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC__REFERER_ID = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Abstract Nomination Spec</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.SlotNominationSpecImpl <em>Slot Nomination Spec</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.SlotNominationSpecImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getSlotNominationSpec()
	 * @generated
	 */
	int SLOT_NOMINATION_SPEC = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__EXTENSIONS = ABSTRACT_NOMINATION_SPEC__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__UUID = ABSTRACT_NOMINATION_SPEC__UUID;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__TYPE = ABSTRACT_NOMINATION_SPEC__TYPE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__COUNTERPARTY = ABSTRACT_NOMINATION_SPEC__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__REMARK = ABSTRACT_NOMINATION_SPEC__REMARK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__SIZE = ABSTRACT_NOMINATION_SPEC__SIZE;

	/**
	 * The feature id for the '<em><b>Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__SIZE_UNITS = ABSTRACT_NOMINATION_SPEC__SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__DAY_OF_MONTH = ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH;

	/**
	 * The feature id for the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__ALERT_SIZE = ABSTRACT_NOMINATION_SPEC__ALERT_SIZE;

	/**
	 * The feature id for the '<em><b>Alert Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__ALERT_SIZE_UNITS = ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__SIDE = ABSTRACT_NOMINATION_SPEC__SIDE;

	/**
	 * The feature id for the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC__REFERER_ID = ABSTRACT_NOMINATION_SPEC__REFERER_ID;

	/**
	 * The number of structural features of the '<em>Slot Nomination Spec</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_SPEC_FEATURE_COUNT = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl <em>Abstract Nomination</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAbstractNomination()
	 * @generated
	 */
	int ABSTRACT_NOMINATION = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__EXTENSIONS = ABSTRACT_NOMINATION_SPEC__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__UUID = ABSTRACT_NOMINATION_SPEC__UUID;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__TYPE = ABSTRACT_NOMINATION_SPEC__TYPE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__COUNTERPARTY = ABSTRACT_NOMINATION_SPEC__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__REMARK = ABSTRACT_NOMINATION_SPEC__REMARK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__SIZE = ABSTRACT_NOMINATION_SPEC__SIZE;

	/**
	 * The feature id for the '<em><b>Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__SIZE_UNITS = ABSTRACT_NOMINATION_SPEC__SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__DAY_OF_MONTH = ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH;

	/**
	 * The feature id for the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__ALERT_SIZE = ABSTRACT_NOMINATION_SPEC__ALERT_SIZE;

	/**
	 * The feature id for the '<em><b>Alert Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__ALERT_SIZE_UNITS = ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__SIDE = ABSTRACT_NOMINATION_SPEC__SIDE;

	/**
	 * The feature id for the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__REFERER_ID = ABSTRACT_NOMINATION_SPEC__REFERER_ID;

	/**
	 * The feature id for the '<em><b>Nominee Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__NOMINEE_ID = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Due Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__DUE_DATE = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__DONE = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Alert Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__ALERT_DATE = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Spec Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION__SPEC_UUID = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Abstract Nomination</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_NOMINATION_FEATURE_COUNT = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.SlotNominationImpl <em>Slot Nomination</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.SlotNominationImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getSlotNomination()
	 * @generated
	 */
	int SLOT_NOMINATION = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.ContractNominationSpecImpl <em>Contract Nomination Spec</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.ContractNominationSpecImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getContractNominationSpec()
	 * @generated
	 */
	int CONTRACT_NOMINATION_SPEC = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.ContractNominationImpl <em>Contract Nomination</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.ContractNominationImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getContractNomination()
	 * @generated
	 */
	int CONTRACT_NOMINATION = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__EXTENSIONS = ABSTRACT_NOMINATION__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__UUID = ABSTRACT_NOMINATION__UUID;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__TYPE = ABSTRACT_NOMINATION__TYPE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__COUNTERPARTY = ABSTRACT_NOMINATION__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__REMARK = ABSTRACT_NOMINATION__REMARK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__SIZE = ABSTRACT_NOMINATION__SIZE;

	/**
	 * The feature id for the '<em><b>Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__SIZE_UNITS = ABSTRACT_NOMINATION__SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__DAY_OF_MONTH = ABSTRACT_NOMINATION__DAY_OF_MONTH;

	/**
	 * The feature id for the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__ALERT_SIZE = ABSTRACT_NOMINATION__ALERT_SIZE;

	/**
	 * The feature id for the '<em><b>Alert Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__ALERT_SIZE_UNITS = ABSTRACT_NOMINATION__ALERT_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__SIDE = ABSTRACT_NOMINATION__SIDE;

	/**
	 * The feature id for the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__REFERER_ID = ABSTRACT_NOMINATION__REFERER_ID;

	/**
	 * The feature id for the '<em><b>Nominee Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__NOMINEE_ID = ABSTRACT_NOMINATION__NOMINEE_ID;

	/**
	 * The feature id for the '<em><b>Due Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__DUE_DATE = ABSTRACT_NOMINATION__DUE_DATE;

	/**
	 * The feature id for the '<em><b>Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__DONE = ABSTRACT_NOMINATION__DONE;

	/**
	 * The feature id for the '<em><b>Alert Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__ALERT_DATE = ABSTRACT_NOMINATION__ALERT_DATE;

	/**
	 * The feature id for the '<em><b>Spec Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION__SPEC_UUID = ABSTRACT_NOMINATION__SPEC_UUID;

	/**
	 * The number of structural features of the '<em>Slot Nomination</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_NOMINATION_FEATURE_COUNT = ABSTRACT_NOMINATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__EXTENSIONS = ABSTRACT_NOMINATION__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__UUID = ABSTRACT_NOMINATION__UUID;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__TYPE = ABSTRACT_NOMINATION__TYPE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__COUNTERPARTY = ABSTRACT_NOMINATION__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__REMARK = ABSTRACT_NOMINATION__REMARK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__SIZE = ABSTRACT_NOMINATION__SIZE;

	/**
	 * The feature id for the '<em><b>Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__SIZE_UNITS = ABSTRACT_NOMINATION__SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__DAY_OF_MONTH = ABSTRACT_NOMINATION__DAY_OF_MONTH;

	/**
	 * The feature id for the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__ALERT_SIZE = ABSTRACT_NOMINATION__ALERT_SIZE;

	/**
	 * The feature id for the '<em><b>Alert Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__ALERT_SIZE_UNITS = ABSTRACT_NOMINATION__ALERT_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__SIDE = ABSTRACT_NOMINATION__SIDE;

	/**
	 * The feature id for the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__REFERER_ID = ABSTRACT_NOMINATION__REFERER_ID;

	/**
	 * The feature id for the '<em><b>Nominee Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__NOMINEE_ID = ABSTRACT_NOMINATION__NOMINEE_ID;

	/**
	 * The feature id for the '<em><b>Due Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__DUE_DATE = ABSTRACT_NOMINATION__DUE_DATE;

	/**
	 * The feature id for the '<em><b>Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__DONE = ABSTRACT_NOMINATION__DONE;

	/**
	 * The feature id for the '<em><b>Alert Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__ALERT_DATE = ABSTRACT_NOMINATION__ALERT_DATE;

	/**
	 * The feature id for the '<em><b>Spec Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION__SPEC_UUID = ABSTRACT_NOMINATION__SPEC_UUID;

	/**
	 * The number of structural features of the '<em>Contract Nomination</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_FEATURE_COUNT = ABSTRACT_NOMINATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__EXTENSIONS = ABSTRACT_NOMINATION_SPEC__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__UUID = ABSTRACT_NOMINATION_SPEC__UUID;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__TYPE = ABSTRACT_NOMINATION_SPEC__TYPE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__COUNTERPARTY = ABSTRACT_NOMINATION_SPEC__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__REMARK = ABSTRACT_NOMINATION_SPEC__REMARK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__SIZE = ABSTRACT_NOMINATION_SPEC__SIZE;

	/**
	 * The feature id for the '<em><b>Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__SIZE_UNITS = ABSTRACT_NOMINATION_SPEC__SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__DAY_OF_MONTH = ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH;

	/**
	 * The feature id for the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__ALERT_SIZE = ABSTRACT_NOMINATION_SPEC__ALERT_SIZE;

	/**
	 * The feature id for the '<em><b>Alert Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS = ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__SIDE = ABSTRACT_NOMINATION_SPEC__SIDE;

	/**
	 * The feature id for the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC__REFERER_ID = ABSTRACT_NOMINATION_SPEC__REFERER_ID;

	/**
	 * The number of structural features of the '<em>Contract Nomination Spec</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_NOMINATION_SPEC_FEATURE_COUNT = ABSTRACT_NOMINATION_SPEC_FEATURE_COUNT + 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationsParametersImpl <em>Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsParametersImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationsParameters()
	 * @generated
	 */
	int NOMINATIONS_PARAMETERS = 7;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_PARAMETERS__START_DATE = 0;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_PARAMETERS__END_DATE = 1;

	/**
	 * The number of structural features of the '<em>Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATIONS_PARAMETERS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.AbstractAuditItemImpl <em>Abstract Audit Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.AbstractAuditItemImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAbstractAuditItem()
	 * @generated
	 */
	int ABSTRACT_AUDIT_ITEM = 10;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_AUDIT_ITEM__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_AUDIT_ITEM__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Audit Item Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Abstract Audit Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_AUDIT_ITEM_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationAuditItemImpl <em>Nomination Audit Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationAuditItemImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationAuditItem()
	 * @generated
	 */
	int NOMINATION_AUDIT_ITEM = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_AUDIT_ITEM__EXTENSIONS = ABSTRACT_AUDIT_ITEM__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_AUDIT_ITEM__UUID = ABSTRACT_AUDIT_ITEM__UUID;

	/**
	 * The feature id for the '<em><b>Audit Item Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_AUDIT_ITEM__AUDIT_ITEM_TYPE = ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE;

	/**
	 * The feature id for the '<em><b>Nomination</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_AUDIT_ITEM__NOMINATION = ABSTRACT_AUDIT_ITEM_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Nomination Audit Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_AUDIT_ITEM_FEATURE_COUNT = ABSTRACT_AUDIT_ITEM_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationSpecAuditItemImpl <em>Nomination Spec Audit Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationSpecAuditItemImpl
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationSpecAuditItem()
	 * @generated
	 */
	int NOMINATION_SPEC_AUDIT_ITEM = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_SPEC_AUDIT_ITEM__EXTENSIONS = ABSTRACT_AUDIT_ITEM__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_SPEC_AUDIT_ITEM__UUID = ABSTRACT_AUDIT_ITEM__UUID;

	/**
	 * The feature id for the '<em><b>Audit Item Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_SPEC_AUDIT_ITEM__AUDIT_ITEM_TYPE = ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE;

	/**
	 * The feature id for the '<em><b>Nomination Spec</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC = ABSTRACT_AUDIT_ITEM_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Nomination Spec Audit Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATION_SPEC_AUDIT_ITEM_FEATURE_COUNT = ABSTRACT_AUDIT_ITEM_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.DatePeriodPrior <em>Date Period Prior</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getDatePeriodPrior()
	 * @generated
	 */
	int DATE_PERIOD_PRIOR = 11;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.Side <em>Side</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.Side
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getSide()
	 * @generated
	 */
	int SIDE = 12;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.nominations.AuditItemType <em>Audit Item Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.nominations.AuditItemType
	 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAuditItemType()
	 * @generated
	 */
	int AUDIT_ITEM_TYPE = 13;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.NominationsModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsModel
	 * @generated
	 */
	EClass getNominationsModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.nominations.NominationsModel#getNominationSpecs <em>Nomination Specs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nomination Specs</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsModel#getNominationSpecs()
	 * @see #getNominationsModel()
	 * @generated
	 */
	EReference getNominationsModel_NominationSpecs();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.nominations.NominationsModel#getNominations <em>Nominations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nominations</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsModel#getNominations()
	 * @see #getNominationsModel()
	 * @generated
	 */
	EReference getNominationsModel_Nominations();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.nominations.NominationsModel#getNominationParameters <em>Nomination Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Nomination Parameters</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsModel#getNominationParameters()
	 * @see #getNominationsModel()
	 * @generated
	 */
	EReference getNominationsModel_NominationParameters();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.nominations.NominationsModel#getAuditLog <em>Audit Log</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Audit Log</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsModel#getAuditLog()
	 * @see #getNominationsModel()
	 * @generated
	 */
	EReference getNominationsModel_AuditLog();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec <em>Abstract Nomination Spec</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Nomination Spec</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec
	 * @generated
	 */
	EClass getAbstractNominationSpec();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getType()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_Type();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#isCounterparty <em>Counterparty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Counterparty</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#isCounterparty()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_Counterparty();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRemark <em>Remark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remark</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRemark()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_Remark();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSize()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_Size();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSizeUnits <em>Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size Units</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSizeUnits()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_SizeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getDayOfMonth <em>Day Of Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Day Of Month</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getDayOfMonth()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_DayOfMonth();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSize <em>Alert Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alert Size</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSize()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_AlertSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSizeUnits <em>Alert Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alert Size Units</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSizeUnits()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_AlertSizeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSide <em>Side</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Side</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSide()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_Side();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRefererId <em>Referer Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Referer Id</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRefererId()
	 * @see #getAbstractNominationSpec()
	 * @generated
	 */
	EAttribute getAbstractNominationSpec_RefererId();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.SlotNominationSpec <em>Slot Nomination Spec</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Nomination Spec</em>'.
	 * @see com.mmxlabs.models.lng.nominations.SlotNominationSpec
	 * @generated
	 */
	EClass getSlotNominationSpec();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.SlotNomination <em>Slot Nomination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Nomination</em>'.
	 * @see com.mmxlabs.models.lng.nominations.SlotNomination
	 * @generated
	 */
	EClass getSlotNomination();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.ContractNomination <em>Contract Nomination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contract Nomination</em>'.
	 * @see com.mmxlabs.models.lng.nominations.ContractNomination
	 * @generated
	 */
	EClass getContractNomination();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.ContractNominationSpec <em>Contract Nomination Spec</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contract Nomination Spec</em>'.
	 * @see com.mmxlabs.models.lng.nominations.ContractNominationSpec
	 * @generated
	 */
	EClass getContractNominationSpec();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.AbstractNomination <em>Abstract Nomination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Nomination</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNomination
	 * @generated
	 */
	EClass getAbstractNomination();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getNomineeId <em>Nominee Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nominee Id</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNomination#getNomineeId()
	 * @see #getAbstractNomination()
	 * @generated
	 */
	EAttribute getAbstractNomination_NomineeId();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getDueDate <em>Due Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Due Date</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNomination#getDueDate()
	 * @see #getAbstractNomination()
	 * @generated
	 */
	EAttribute getAbstractNomination_DueDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#isDone <em>Done</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Done</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNomination#isDone()
	 * @see #getAbstractNomination()
	 * @generated
	 */
	EAttribute getAbstractNomination_Done();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getAlertDate <em>Alert Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alert Date</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNomination#getAlertDate()
	 * @see #getAbstractNomination()
	 * @generated
	 */
	EAttribute getAbstractNomination_AlertDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getSpecUuid <em>Spec Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spec Uuid</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractNomination#getSpecUuid()
	 * @see #getAbstractNomination()
	 * @generated
	 */
	EAttribute getAbstractNomination_SpecUuid();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.NominationsParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameters</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsParameters
	 * @generated
	 */
	EClass getNominationsParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.NominationsParameters#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsParameters#getStartDate()
	 * @see #getNominationsParameters()
	 * @generated
	 */
	EAttribute getNominationsParameters_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.NominationsParameters#getEndDate <em>End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationsParameters#getEndDate()
	 * @see #getNominationsParameters()
	 * @generated
	 */
	EAttribute getNominationsParameters_EndDate();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.NominationAuditItem <em>Nomination Audit Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Nomination Audit Item</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationAuditItem
	 * @generated
	 */
	EClass getNominationAuditItem();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.nominations.NominationAuditItem#getNomination <em>Nomination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Nomination</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationAuditItem#getNomination()
	 * @see #getNominationAuditItem()
	 * @generated
	 */
	EReference getNominationAuditItem_Nomination();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.NominationSpecAuditItem <em>Nomination Spec Audit Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Nomination Spec Audit Item</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationSpecAuditItem
	 * @generated
	 */
	EClass getNominationSpecAuditItem();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.nominations.NominationSpecAuditItem#getNominationSpec <em>Nomination Spec</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Nomination Spec</em>'.
	 * @see com.mmxlabs.models.lng.nominations.NominationSpecAuditItem#getNominationSpec()
	 * @see #getNominationSpecAuditItem()
	 * @generated
	 */
	EReference getNominationSpecAuditItem_NominationSpec();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.nominations.AbstractAuditItem <em>Abstract Audit Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Audit Item</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractAuditItem
	 * @generated
	 */
	EClass getAbstractAuditItem();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.nominations.AbstractAuditItem#getAuditItemType <em>Audit Item Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Audit Item Type</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AbstractAuditItem#getAuditItemType()
	 * @see #getAbstractAuditItem()
	 * @generated
	 */
	EAttribute getAbstractAuditItem_AuditItemType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.nominations.DatePeriodPrior <em>Date Period Prior</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Date Period Prior</em>'.
	 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
	 * @generated
	 */
	EEnum getDatePeriodPrior();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.nominations.Side <em>Side</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Side</em>'.
	 * @see com.mmxlabs.models.lng.nominations.Side
	 * @generated
	 */
	EEnum getSide();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.nominations.AuditItemType <em>Audit Item Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Audit Item Type</em>'.
	 * @see com.mmxlabs.models.lng.nominations.AuditItemType
	 * @generated
	 */
	EEnum getAuditItemType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	NominationsFactory getNominationsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationsModel()
		 * @generated
		 */
		EClass NOMINATIONS_MODEL = eINSTANCE.getNominationsModel();

		/**
		 * The meta object literal for the '<em><b>Nomination Specs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATIONS_MODEL__NOMINATION_SPECS = eINSTANCE.getNominationsModel_NominationSpecs();

		/**
		 * The meta object literal for the '<em><b>Nominations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATIONS_MODEL__NOMINATIONS = eINSTANCE.getNominationsModel_Nominations();

		/**
		 * The meta object literal for the '<em><b>Nomination Parameters</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATIONS_MODEL__NOMINATION_PARAMETERS = eINSTANCE.getNominationsModel_NominationParameters();

		/**
		 * The meta object literal for the '<em><b>Audit Log</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATIONS_MODEL__AUDIT_LOG = eINSTANCE.getNominationsModel_AuditLog();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl <em>Abstract Nomination Spec</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAbstractNominationSpec()
		 * @generated
		 */
		EClass ABSTRACT_NOMINATION_SPEC = eINSTANCE.getAbstractNominationSpec();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__TYPE = eINSTANCE.getAbstractNominationSpec_Type();

		/**
		 * The meta object literal for the '<em><b>Counterparty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__COUNTERPARTY = eINSTANCE.getAbstractNominationSpec_Counterparty();

		/**
		 * The meta object literal for the '<em><b>Remark</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__REMARK = eINSTANCE.getAbstractNominationSpec_Remark();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__SIZE = eINSTANCE.getAbstractNominationSpec_Size();

		/**
		 * The meta object literal for the '<em><b>Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__SIZE_UNITS = eINSTANCE.getAbstractNominationSpec_SizeUnits();

		/**
		 * The meta object literal for the '<em><b>Day Of Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH = eINSTANCE.getAbstractNominationSpec_DayOfMonth();

		/**
		 * The meta object literal for the '<em><b>Alert Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__ALERT_SIZE = eINSTANCE.getAbstractNominationSpec_AlertSize();

		/**
		 * The meta object literal for the '<em><b>Alert Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS = eINSTANCE.getAbstractNominationSpec_AlertSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Side</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__SIDE = eINSTANCE.getAbstractNominationSpec_Side();

		/**
		 * The meta object literal for the '<em><b>Referer Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION_SPEC__REFERER_ID = eINSTANCE.getAbstractNominationSpec_RefererId();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.SlotNominationSpecImpl <em>Slot Nomination Spec</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.SlotNominationSpecImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getSlotNominationSpec()
		 * @generated
		 */
		EClass SLOT_NOMINATION_SPEC = eINSTANCE.getSlotNominationSpec();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.SlotNominationImpl <em>Slot Nomination</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.SlotNominationImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getSlotNomination()
		 * @generated
		 */
		EClass SLOT_NOMINATION = eINSTANCE.getSlotNomination();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.ContractNominationImpl <em>Contract Nomination</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.ContractNominationImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getContractNomination()
		 * @generated
		 */
		EClass CONTRACT_NOMINATION = eINSTANCE.getContractNomination();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.ContractNominationSpecImpl <em>Contract Nomination Spec</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.ContractNominationSpecImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getContractNominationSpec()
		 * @generated
		 */
		EClass CONTRACT_NOMINATION_SPEC = eINSTANCE.getContractNominationSpec();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl <em>Abstract Nomination</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAbstractNomination()
		 * @generated
		 */
		EClass ABSTRACT_NOMINATION = eINSTANCE.getAbstractNomination();

		/**
		 * The meta object literal for the '<em><b>Nominee Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION__NOMINEE_ID = eINSTANCE.getAbstractNomination_NomineeId();

		/**
		 * The meta object literal for the '<em><b>Due Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION__DUE_DATE = eINSTANCE.getAbstractNomination_DueDate();

		/**
		 * The meta object literal for the '<em><b>Done</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION__DONE = eINSTANCE.getAbstractNomination_Done();

		/**
		 * The meta object literal for the '<em><b>Alert Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION__ALERT_DATE = eINSTANCE.getAbstractNomination_AlertDate();

		/**
		 * The meta object literal for the '<em><b>Spec Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_NOMINATION__SPEC_UUID = eINSTANCE.getAbstractNomination_SpecUuid();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationsParametersImpl <em>Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsParametersImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationsParameters()
		 * @generated
		 */
		EClass NOMINATIONS_PARAMETERS = eINSTANCE.getNominationsParameters();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOMINATIONS_PARAMETERS__START_DATE = eINSTANCE.getNominationsParameters_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOMINATIONS_PARAMETERS__END_DATE = eINSTANCE.getNominationsParameters_EndDate();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationAuditItemImpl <em>Nomination Audit Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationAuditItemImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationAuditItem()
		 * @generated
		 */
		EClass NOMINATION_AUDIT_ITEM = eINSTANCE.getNominationAuditItem();

		/**
		 * The meta object literal for the '<em><b>Nomination</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATION_AUDIT_ITEM__NOMINATION = eINSTANCE.getNominationAuditItem_Nomination();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.NominationSpecAuditItemImpl <em>Nomination Spec Audit Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationSpecAuditItemImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getNominationSpecAuditItem()
		 * @generated
		 */
		EClass NOMINATION_SPEC_AUDIT_ITEM = eINSTANCE.getNominationSpecAuditItem();

		/**
		 * The meta object literal for the '<em><b>Nomination Spec</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC = eINSTANCE.getNominationSpecAuditItem_NominationSpec();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.impl.AbstractAuditItemImpl <em>Abstract Audit Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.impl.AbstractAuditItemImpl
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAbstractAuditItem()
		 * @generated
		 */
		EClass ABSTRACT_AUDIT_ITEM = eINSTANCE.getAbstractAuditItem();

		/**
		 * The meta object literal for the '<em><b>Audit Item Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE = eINSTANCE.getAbstractAuditItem_AuditItemType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.DatePeriodPrior <em>Date Period Prior</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getDatePeriodPrior()
		 * @generated
		 */
		EEnum DATE_PERIOD_PRIOR = eINSTANCE.getDatePeriodPrior();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.Side <em>Side</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.Side
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getSide()
		 * @generated
		 */
		EEnum SIDE = eINSTANCE.getSide();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.nominations.AuditItemType <em>Audit Item Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.nominations.AuditItemType
		 * @see com.mmxlabs.models.lng.nominations.impl.NominationsPackageImpl#getAuditItemType()
		 * @generated
		 */
		EEnum AUDIT_ITEM_TYPE = eINSTANCE.getAuditItemType();

	}

} //NominationsPackage
