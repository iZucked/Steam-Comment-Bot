package com.mmxlabs.models.lng.pricing.ui.editorpart;

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

import com.mmxlabs.common.ITransformer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class IndexTreeTransformer {

	private final EPackage modelPackage;
	private final EClass nodeClass;
	private final EReference dataReference;
	private final EAttribute expandAttribute;
	private final EAttribute nameAttribute = MMXCorePackage.Literals.NAMED_OBJECT__NAME;
	private EObject root;
	private EObject nodeCharterCurves;
	private EObject nodeBaseFuels;
	private EObject nodeCommodity;

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
			// Commodity
			{
				nodeCommodity = modelPackage.getEFactoryInstance().create(nodeClass);
				nodeCommodity.eSet(nameAttribute, "Commodity Curves");
				nodeCommodity.eSet(expandAttribute, Boolean.TRUE);
				nodes.add(nodeCommodity);
			}

			// Base Fuels
			{
				nodeBaseFuels = modelPackage.getEFactoryInstance().create(nodeClass);
				nodeBaseFuels.eSet(nameAttribute, "Base Fuel Curves");
				nodes.add(nodeBaseFuels);
			}

			// Charter Curves
			{
				nodeCharterCurves = modelPackage.getEFactoryInstance().create(nodeClass);
				nodeCharterCurves.eSet(nameAttribute, "Charter Curves");
				nodes.add(nodeCharterCurves);
			}

			root.eSet(dataReference, nodes);
		}
	}

	public void update(final PricingModel pricingModel) {

		nodeCommodity.eSet(dataReference, pricingModel.getCommodityIndices());
		nodeBaseFuels.eSet(dataReference, pricingModel.getBaseFuelPrices());
		nodeCharterCurves.eSet(dataReference, pricingModel.getCharterIndices());
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

			private Map<Object, Object> parentMap = new HashMap<>();

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
						Object[] array = children.toArray();
						for (Object obj : array) {
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
}
