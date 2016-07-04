/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.properties;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.ui.properties.PropertiesFactory
 * @model kind="package"
 * @generated
 */
public interface PropertiesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "properties";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/properties/1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "models.properties";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PropertiesPackage eINSTANCE = com.mmxlabs.models.ui.properties.impl.PropertiesPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl <em>Detail Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl
	 * @see com.mmxlabs.models.ui.properties.impl.PropertiesPackageImpl#getDetailProperty()
	 * @generated
	 */
	int DETAIL_PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__PARENT = 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__CHILDREN = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__ID = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__NAME = 3;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__DESCRIPTION = 4;

	/**
	 * The feature id for the '<em><b>Units Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__UNITS_PREFIX = 5;

	/**
	 * The feature id for the '<em><b>Units Suffix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__UNITS_SUFFIX = 6;

	/**
	 * The feature id for the '<em><b>Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__OBJECT = 7;

	/**
	 * The feature id for the '<em><b>Label Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY__LABEL_PROVIDER = 8;

	/**
	 * The number of structural features of the '<em>Detail Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY_FEATURE_COUNT = 9;

	/**
	 * The operation id for the '<em>Format</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY___FORMAT = 0;

	/**
	 * The number of operations of the '<em>Detail Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_PROPERTY_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '<em>ILabel Provider</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jface.viewers.ILabelProvider
	 * @see com.mmxlabs.models.ui.properties.impl.PropertiesPackageImpl#getILabelProvider()
	 * @generated
	 */
	int ILABEL_PROVIDER = 1;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.properties.DetailProperty <em>Detail Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Detail Property</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty
	 * @generated
	 */
	EClass getDetailProperty();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.models.ui.properties.DetailProperty#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getParent()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EReference getDetailProperty_Parent();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.properties.DetailProperty#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getChildren()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EReference getDetailProperty_Children();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getId()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_Id();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getName()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getDescription()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_Description();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getUnitsPrefix <em>Units Prefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units Prefix</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getUnitsPrefix()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_UnitsPrefix();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getUnitsSuffix <em>Units Suffix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units Suffix</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getUnitsSuffix()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_UnitsSuffix();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Object</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getObject()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_Object();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.properties.DetailProperty#getLabelProvider <em>Label Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label Provider</em>'.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getLabelProvider()
	 * @see #getDetailProperty()
	 * @generated
	 */
	EAttribute getDetailProperty_LabelProvider();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.ui.properties.DetailProperty#format() <em>Format</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Format</em>' operation.
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#format()
	 * @generated
	 */
	EOperation getDetailProperty__Format();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.jface.viewers.ILabelProvider <em>ILabel Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>ILabel Provider</em>'.
	 * @see org.eclipse.jface.viewers.ILabelProvider
	 * @model instanceClass="org.eclipse.jface.viewers.ILabelProvider"
	 * @generated
	 */
	EDataType getILabelProvider();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PropertiesFactory getPropertiesFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl <em>Detail Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl
		 * @see com.mmxlabs.models.ui.properties.impl.PropertiesPackageImpl#getDetailProperty()
		 * @generated
		 */
		EClass DETAIL_PROPERTY = eINSTANCE.getDetailProperty();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DETAIL_PROPERTY__PARENT = eINSTANCE.getDetailProperty_Parent();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DETAIL_PROPERTY__CHILDREN = eINSTANCE.getDetailProperty_Children();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__ID = eINSTANCE.getDetailProperty_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__NAME = eINSTANCE.getDetailProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__DESCRIPTION = eINSTANCE.getDetailProperty_Description();

		/**
		 * The meta object literal for the '<em><b>Units Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__UNITS_PREFIX = eINSTANCE.getDetailProperty_UnitsPrefix();

		/**
		 * The meta object literal for the '<em><b>Units Suffix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__UNITS_SUFFIX = eINSTANCE.getDetailProperty_UnitsSuffix();

		/**
		 * The meta object literal for the '<em><b>Object</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__OBJECT = eINSTANCE.getDetailProperty_Object();

		/**
		 * The meta object literal for the '<em><b>Label Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DETAIL_PROPERTY__LABEL_PROVIDER = eINSTANCE.getDetailProperty_LabelProvider();

		/**
		 * The meta object literal for the '<em><b>Format</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DETAIL_PROPERTY___FORMAT = eINSTANCE.getDetailProperty__Format();

		/**
		 * The meta object literal for the '<em>ILabel Provider</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jface.viewers.ILabelProvider
		 * @see com.mmxlabs.models.ui.properties.impl.PropertiesPackageImpl#getILabelProvider()
		 * @generated
		 */
		EDataType ILABEL_PROVIDER = eINSTANCE.getILabelProvider();

	}

} //PropertiesPackage
