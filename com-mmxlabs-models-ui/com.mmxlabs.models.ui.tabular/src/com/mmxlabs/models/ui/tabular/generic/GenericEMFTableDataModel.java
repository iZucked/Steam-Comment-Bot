/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.generic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * The {@link GenericEMFTableDataModel} constructs a dynamic EMF model aimed for use in tables etc where we wish to use the EMF reflective API to show table data, but where the real data model has no
 * suitable data structure. The dynamic model create a "Root" node with a number of "Row"s. Each row has a configurable number of named references (prefixed with {@value #FEATURE_VALUE_PREFIX}).
 * {@link EStructuralFeature}s for the references can be returned and passed into reflective EMF code. Additionally, each row can belong to a "Group". A group is a linked set of "Row"s which should be
 * displayed together in consecutive rows in the table. A {@link ViewerComparator} wrapper can be constructed using {@link #createGroupComparator(ViewerComparator, EPackage)} to wrap an existing
 * {@link ViewerComparator} for the table to provide the group binding. The {@link EPackage} created by this class is not "frozen" and can be modified or extended as required.
 * 
 * @author Simon Goodall
 * 
 */

public class GenericEMFTableDataModel {

	private static final String CLASS_NAME_GROUP = "Group";
	private static final String CLASS_NAME_ROW = "Row";
	private static final String CLASS_NAME_ROOT = "Root";

	private static final String FEATURE_GROUPS = "groups";
	private static final String FEATURE_GROUP = "group";
	private static final String FEATURE_ROWS = "rows";

	private static final String FEATURE_VALUE_PREFIX = "value_";

	/**
	 * Create the initial data model passing in the reference names for the "Row" objects.
	 * 
	 * @param values
	 * @return
	 */
	public static EPackage createEPackage(final String... values) {
		final EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		final EClass rootClass = EcoreFactory.eINSTANCE.createEClass();
		rootClass.setName(CLASS_NAME_ROOT);
		ePackage.getEClassifiers().add(rootClass);

		final EClass rowClass = EcoreFactory.eINSTANCE.createEClass();
		rowClass.setName(CLASS_NAME_ROW);
		ePackage.getEClassifiers().add(rowClass);

		final EClass groupClass = EcoreFactory.eINSTANCE.createEClass();
		groupClass.setName(CLASS_NAME_GROUP);
		ePackage.getEClassifiers().add(groupClass);

		final EReference row_group_ref = EcoreFactory.eINSTANCE.createEReference();
		row_group_ref.setContainment(false);
		row_group_ref.setLowerBound(0);
		row_group_ref.setUpperBound(1);
		row_group_ref.setEType(groupClass);
		row_group_ref.setName(FEATURE_GROUP);
		rowClass.getEStructuralFeatures().add(row_group_ref);

		final EReference group_rows_ref = EcoreFactory.eINSTANCE.createEReference();
		group_rows_ref.setContainment(false);
		group_rows_ref.setLowerBound(0);
		group_rows_ref.setUpperBound(-1);
		group_rows_ref.setEType(rowClass);
		group_rows_ref.setName(FEATURE_ROWS);
		groupClass.getEStructuralFeatures().add(group_rows_ref);

		// Automatically maintain group <-> row bindings
		group_rows_ref.setEOpposite(row_group_ref);
		row_group_ref.setEOpposite(group_rows_ref);

		final EReference root_rows_ref = EcoreFactory.eINSTANCE.createEReference();
		root_rows_ref.setContainment(true);
		root_rows_ref.setLowerBound(0);
		root_rows_ref.setUpperBound(-1);
		root_rows_ref.setEType(rowClass);
		root_rows_ref.setName(FEATURE_ROWS);

		rootClass.getEStructuralFeatures().add(root_rows_ref);

		final EReference root_groups_ref = EcoreFactory.eINSTANCE.createEReference();
		root_groups_ref.setContainment(true);
		root_groups_ref.setLowerBound(0);
		root_groups_ref.setUpperBound(-1);
		root_groups_ref.setEType(groupClass);
		root_groups_ref.setName(FEATURE_GROUPS);
		rootClass.getEStructuralFeatures().add(root_groups_ref);

		// Create all the row references
		for (final String value : values) {
			rowClass.getEStructuralFeatures().add(createRowReference(value));
		}

		return ePackage;
	}

	/**
	 * Create an {@link EReference} for use with a "Row" object.
	 * 
	 * @param value
	 * @return
	 */
	@NonNull
	public static EReference createRowReference(final String value) {
		return createRowPlainReference(FEATURE_VALUE_PREFIX + value);
	}

	/**
	 * Create an {@link EReference} for use with a "Row" object.
	 * 
	 * @param value
	 * @return
	 */
	@NonNull
	public static EReference createRowPlainReference(final String value) {
		final EReference valueRef = EcoreFactory.eINSTANCE.createEReference();
		valueRef.setContainment(false);
		valueRef.setLowerBound(0);
		valueRef.setUpperBound(1);
		valueRef.setEType(EcorePackage.Literals.EJAVA_OBJECT);
		valueRef.setName(value);
		return valueRef;
	}

	/**
	 * Create an {@link EReference} for use with a "Row" object.
	 * 
	 * @param value
	 * @return
	 */
	@NonNull
	public static EAttribute createRowAttribute(final EClass owner, final EClassifier type, final String value) {
		final EAttribute valueRef = EcoreFactory.eINSTANCE.createEAttribute();
		valueRef.setLowerBound(0);
		valueRef.setUpperBound(1);
		valueRef.setEType(type);
		valueRef.setName(FEATURE_VALUE_PREFIX + value);

		owner.getEStructuralFeatures().add(valueRef);

		return valueRef;
	}

	/**
	 * Return the {@link EStructuralFeature} for a "Row" object with the given name.
	 * 
	 * @param ePackage
	 * @param value
	 * @return
	 */
	public static EStructuralFeature getRowFeature(final EPackage ePackage, final String value) {
		final EClass eClassifier = (EClass) ePackage.getEClassifier(CLASS_NAME_ROW);
		return eClassifier.getEStructuralFeature(FEATURE_VALUE_PREFIX + value);
	}

	/**
	 * Instantiate a new root data item for the table.
	 * 
	 * @param dataModel
	 * @return
	 */
	public static EObject createRootInstance(final EPackage dataModel) {
		final EFactory factory = dataModel.getEFactoryInstance();
		final EClass rootClass = (EClass) dataModel.getEClassifier(CLASS_NAME_ROOT);
		return factory.create(rootClass);
	}

	/**
	 * Create a new "Group" object and add it to the data model
	 * 
	 * @param dataModel
	 * @param dataModelInstance
	 * @return
	 */
	public static EObject createGroup(final EPackage dataModel, final EObject dataModelInstance) {
		final EFactory factory = dataModel.getEFactoryInstance();
		final EClass groupClass = (EClass) dataModel.getEClassifier(CLASS_NAME_GROUP);
		final EClass rootClass = (EClass) dataModel.getEClassifier(CLASS_NAME_ROOT);
		final EStructuralFeature rootGroups = rootClass.getEStructuralFeature(FEATURE_GROUPS);

		final List<EObject> newList = new ArrayList<EObject>();
		if (dataModelInstance.eIsSet(rootGroups)) {
			final List<EObject> oldList = (List<EObject>) dataModelInstance.eGet(rootGroups);
			newList.addAll(oldList);
		}

		final EObject group = factory.create(groupClass);
		newList.add(group);

		dataModelInstance.eSet(rootGroups, newList);

		return group;
	}

	/**
	 * Create a new "Row" object and add it to the data model. If the "Group" object is not null, then add this "Row" to the "Group"
	 * 
	 * @param dataModel
	 * @param dataModelInstance
	 * @param group
	 * @return
	 */
	@NonNull
	public static EObject createRow(final EPackage dataModel, final EObject dataModelInstance, final EObject group) {
		final EFactory factory = dataModel.getEFactoryInstance();
		final EClass nodeClass = (EClass) dataModel.getEClassifier(CLASS_NAME_ROW);
		final EClass rootClass = (EClass) dataModel.getEClassifier(CLASS_NAME_ROOT);
		final EStructuralFeature rootNodes = rootClass.getEStructuralFeature(FEATURE_ROWS);
		final EStructuralFeature nodeGroup = nodeClass.getEStructuralFeature(FEATURE_GROUP);

		final List<EObject> newList = new ArrayList<EObject>();
		if (dataModelInstance.eIsSet(rootNodes)) {
			@SuppressWarnings("unchecked")
			final List<EObject> oldList = (List<EObject>) dataModelInstance.eGet(rootNodes);
			newList.addAll(oldList);
		}

		final EObject node = factory.create(nodeClass);
		newList.add(node);

		if (group != null) {
			node.eSet(nodeGroup, group);
		}

		dataModelInstance.eSet(rootNodes, newList);

		return node;
	}

	/**
	 * Set the reference value for the given "Row" object.
	 * 
	 * @param dataModel
	 * @param row
	 * @param value
	 * @param obj
	 */
	public static void setRowValue(final EPackage dataModel, final EObject row, final String value, final Object obj) {
		row.eSet(getRowFeature(dataModel, value), obj);
	}

	public static void setRowValue(final EPackage dataModel, final EObject row, final EStructuralFeature feature, final Object obj) {
		row.eSet(feature, obj);
	}

	/**
	 * Creates a wrapper {@link ViewerComparator} to keep "Row" objects with the same "Group" together.
	 * 
	 * @param vc
	 * @param dataModel
	 * @return
	 */
	public static ViewerComparator createGroupComparator(final ViewerComparator vc, final EPackage dataModel) {

		final EClass rowClass = (EClass) dataModel.getEClassifier(CLASS_NAME_ROW);
		final EClass groupClass = (EClass) dataModel.getEClassifier(CLASS_NAME_GROUP);
		final EStructuralFeature rowGroupFeature = rowClass.getEStructuralFeature(FEATURE_GROUP);
		final EStructuralFeature groupRowsFeature = groupClass.getEStructuralFeature(FEATURE_ROWS);
		return new ViewerComparator() {

			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				EObject g1 = null;
				EObject g2 = null;
				if (rowClass.isInstance(e1)) {
					g1 = (EObject) ((EObject) e1).eGet(rowGroupFeature);
				}
				if (rowClass.isInstance(e2)) {
					g2 = (EObject) ((EObject) e2).eGet(rowGroupFeature);
				}
				if (g1 == g2) {
					return vc.compare(viewer, e1, e2);
				} else {
					final Object rd1 = getObjectFromGroup(groupRowsFeature, e1, g1);
					final Object rd2 = getObjectFromGroup(groupRowsFeature, e2, g2);

					return vc.compare(viewer, rd1, rd2);
				}
			}

			private Object getObjectFromGroup(final EStructuralFeature groupNodesFeature, final Object e1, final EObject g1) {
				final Object rd1;
				if (g1 != null) {
					final List<?> nodes = (List<?>) g1.eGet(groupNodesFeature);
					rd1 = nodes.get(0);
				} else {
					rd1 = e1;
				}
				return rd1;
			}
		};
	}

	/**
	 * Return the EClass instance representing the row
	 * 
	 */
	@NonNull
	public static EClass getRowClass(final EPackage dataModel) {
		return (EClass) dataModel.getEClassifier(CLASS_NAME_ROW);
	}
}
