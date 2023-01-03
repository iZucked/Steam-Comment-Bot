/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class CurveTreeTransformer {
	public enum DataType {
		Commodity("Commodity", false, PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES, true, PriceIndexType.COMMODITY), //
		// CommodityOverlay("CommodityOverlay", false, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES, false, PriceIndexType.COMMODITY) //
		;
		private final String name;
		private final EReference containerFeature;
		private final boolean expandedByDefault;
		private final PriceIndexType priceIndexType;
		
		private DataType(final String name, final boolean useIntegers, final EReference containerFeature, final boolean expandedByDefault, PriceIndexType priceIndexType) {
			this.name = name;
			this.containerFeature = containerFeature;
			this.expandedByDefault = expandedByDefault;
			this.priceIndexType = priceIndexType;
		}
		
		public EReference getContainerFeature() {
			return containerFeature;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isExpandedByDefault() {
			return expandedByDefault;
		}
		
		public PriceIndexType getPriceIndexType() {
			return priceIndexType;
		}
	}

	private final EPackage modelPackage;
	private final EClass nodeClass;
	private final EReference dataReference;
	private final EAttribute expandAttribute;
	private final EAttribute nameAttribute = MMXCorePackage.Literals.NAMED_OBJECT__NAME;
	private EObject root;
	
	private final Map<DataType, EObject> nodeMap = new EnumMap<>(DataType.class);
	
	public CurveTreeTransformer() {
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
		
		{
			root = modelPackage.getEFactoryInstance().create(nodeClass);
			root.eSet(nameAttribute, "root");
			
			final List<EObject> nodes = new LinkedList<>();
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
	
	public boolean isNode(Object obj) {
		return nodeClass.isInstance(obj);
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
			private SensitivityModel currentSensitivityModel = null;
			private PricingModel currentPricingModel = null;
			
			private final Map<Object, Object> parentMap = new HashMap<>();
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				parentMap.clear();
				currentSensitivityModel = null;
				currentPricingModel = null;
				if (newInput != null) {
					final Pair<SensitivityModel, PricingModel> pair = (Pair<SensitivityModel, PricingModel>) newInput;
					currentSensitivityModel = pair.getFirst();
					currentPricingModel = pair.getSecond();
				}
			}
			
			@Override
			public void dispose() {
				parentMap.clear();
				currentSensitivityModel = null;
				currentPricingModel = null;
			}

			@Override
			public boolean hasChildren(Object element) {
				return element instanceof final CommodityCurveOverlay cco && !cco.getAlternativeCurves().isEmpty();
			}

			public Object getParent(Object element) {
				return parentMap.get(element);
			};
			
			public Object[] getElements(Object inputElement) {
				final Map<CommodityCurve, CommodityCurveOverlay> existingCommodityCurveOverlays = ((Pair<SensitivityModel, ?>) inputElement).getFirst().getSensitivityModel().getCommodityCurves().stream() //
						.filter(CommodityCurveOverlay.class::isInstance) //
						.map(CommodityCurveOverlay.class::cast) //
						.collect(Collectors.toMap(c -> c.getReferenceCurve(), Function.identity()));
				return ((Pair<?, PricingModel>) inputElement).getSecond().getCommodityCurves().stream() //
						.map(cc -> {
							CommodityCurveOverlay overlay = existingCommodityCurveOverlays.get(cc);
							if (overlay == null) {
								overlay = AnalyticsFactory.eINSTANCE.createCommodityCurveOverlay();
								overlay.setReferenceCurve(cc);
							}
							return overlay;
						}) //
						.toArray();
				
//				return getChildren(inputElement);
			};
			
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof final CommodityCurveOverlay cco) {
					if (!cco.getAlternativeCurves().isEmpty()) {
						final Object[] array = cco.getAlternativeCurves().toArray();
						for (final Object obj : array) {
							parentMap.put(obj, parentElement);
						}
						return array;
					}
				}
				return null;
			};
		};
	}

	public EObject getRoot() {
		return root;
	}

	public void dispose() {
		root = null;
		nodeMap.clear();
	}
}
