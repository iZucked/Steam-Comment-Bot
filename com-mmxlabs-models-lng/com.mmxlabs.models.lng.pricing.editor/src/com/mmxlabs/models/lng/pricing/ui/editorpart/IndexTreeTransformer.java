/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class IndexTreeTransformer {

	/**
	 * Enum defining the sub-nodes of the {@link PricingModel} representing indices to show in the same viewer
	 * 
	 */
	public enum DataType {
		Commodity("Commodity", false, PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA, true),

		BaseFuel("Base Fuel", false, PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA, false),

		Charter("Chartering", true, PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA, false);

		private final String name;
		private final boolean useIntegers;
		private final EReference containerFeature;
		private final EReference indexFeature;
		private final boolean expandedByDefault;

		private DataType(final String name, final boolean useIntegers, final EReference containerFeature, final EReference indexFeature, final boolean expandedByDefault) {
			this.name = name;
			this.useIntegers = useIntegers;
			this.containerFeature = containerFeature;
			this.indexFeature = indexFeature;
			this.expandedByDefault = expandedByDefault;
		}

		/**
		 * Integer based or double based
		 * 
		 * @return
		 */
		public boolean useIntegers() {
			return useIntegers;
		}

		public EReference getContainerFeature() {
			return containerFeature;
		}

		public EReference getIndexFeature() {
			return indexFeature;
		}

		public String getName() {
			return name;
		}

		public boolean isExpandedByDefault() {
			return expandedByDefault;
		}
	}

	private final EPackage modelPackage;
	private final EClass nodeClass;
	private final EReference dataReference;
	private final EAttribute expandAttribute;
	private final EAttribute nameAttribute = MMXCorePackage.Literals.NAMED_OBJECT__NAME;
	private EObject root;

	private final Map<DataType, EObject> nodeMap = new EnumMap<>(DataType.class);

	public IndexTreeTransformer() {

		// Create EPackage
		{
			modelPackage = EcoreFactory.eINSTANCE.createEPackage();

			nodeClass = EcoreFactory.eINSTANCE.createEClass();
			nodeClass.getESuperTypes().add(MMXCorePackage.Literals.NAMED_OBJECT);

			expandAttribute = EcoreFactory.eINSTANCE.createEAttribute();
			expandAttribute.setName("expand");
			expandAttribute.setUpperBound(1);
			expandAttribute.setLowerBound(0);
			expandAttribute.setEType(EcorePackage.Literals.EBOOLEAN);

			nodeClass.getEStructuralFeatures().add(expandAttribute);

			dataReference = EcoreFactory.eINSTANCE.createEReference();
			dataReference.setName("data");
			dataReference.setContainment(false);
			dataReference.setUpperBound(-1);
			dataReference.setLowerBound(0);
			dataReference.setEType(EcorePackage.Literals.EOBJECT);

			nodeClass.getEStructuralFeatures().add(dataReference);

			modelPackage.getEClassifiers().add(nodeClass);
		}
		// Create Data model instance
		{

			root = modelPackage.getEFactoryInstance().create(nodeClass);
			root.eSet(nameAttribute, "root");

			final List<EObject> nodes = new LinkedList<EObject>();
			for (final DataType dt : DataType.values()) {
				final EObject n = modelPackage.getEFactoryInstance().create(nodeClass);
				n.eSet(nameAttribute, dt.getName());
				n.eSet(expandAttribute, dt.isExpandedByDefault());
				nodes.add(n);
				nodeMap.put(dt, n);
			}

			root.eSet(dataReference, nodes);
		}
	}

	public void update(final PricingModel pricingModel) {

		for (final DataType dt : DataType.values()) {
			final EObject n = nodeMap.get(dt);
			if (n != null) {
				n.eSet(dataReference, new LinkedList<>((List<?>) pricingModel.eGet(dt.getContainerFeature())));
			}
		}
	}

	public EPackage getModelPackage() {
		return modelPackage;
	}

	public EClass getNodeClass() {
		return nodeClass;
	}

	public EReference getDataReference() {
		return dataReference;
	}

	public EAttribute getNameAttribute() {
		return nameAttribute;
	}

	public EAttribute getExpandAttribute() {
		return expandAttribute;
	}

	public ITreeContentProvider createContentProvider() {
		return new ITreeContentProvider() {

			private final Map<Object, Object> parentMap = new HashMap<>();

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				parentMap.clear();
			}

			@Override
			public void dispose() {
				parentMap.clear();
			}

			@Override
			public boolean hasChildren(final Object element) {

				if (nodeClass.isInstance(element)) {
					final List<?> children = (List<?>) ((EObject) element).eGet(dataReference);
					if (children != null) {
						return !children.isEmpty();
					}
				}

				return false;
			}

			@Override
			public Object getParent(final Object element) {

				return parentMap.get(element);
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				return getChildren(inputElement);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {

				if (nodeClass.isInstance(parentElement)) {
					final List<?> children = (List<?>) ((EObject) parentElement).eGet(dataReference);
					if (children != null) {
						final Object[] array = children.toArray();
						for (final Object obj : array) {
							parentMap.put(obj, parentElement);
						}
						return array;
					}
				}

				return null;
			}
		};
	}

	public EObject getRootObject() {
		return root;
	}

	public void dispose() {
		root = null;
		nodeMap.clear();
	}
}
