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

public class IndexTreeTransformer implements ITransformer<PricingModel, EObject> {

	private final EPackage modelPackage;
	private final EClass node;
	private final EReference data;
	private final EAttribute name = MMXCorePackage.Literals.NAMED_OBJECT__NAME;

	public IndexTreeTransformer() {
		modelPackage = EcoreFactory.eINSTANCE.createEPackage();

		node = EcoreFactory.eINSTANCE.createEClass();
		node.getESuperTypes().add(MMXCorePackage.Literals.NAMED_OBJECT);

		data = EcoreFactory.eINSTANCE.createEReference();
		data.setName("data");
		data.setContainment(false);
		data.setUpperBound(-1);
		data.setLowerBound(0);
		data.setEType(EcorePackage.Literals.EOBJECT);

		node.getEStructuralFeatures().add(data);

		modelPackage.getEClassifiers().add(node);
	}

	@Override
	public EObject transform(final PricingModel pricingModel) {

		final EObject root = modelPackage.getEFactoryInstance().create(node);
		root.eSet(name, "root");

		final List<EObject> nodes = new LinkedList<EObject>();
		// Commodity
		{
			final EObject n = modelPackage.getEFactoryInstance().create(node);
			n.eSet(name, "Commodity Curves");
			n.eSet(data, pricingModel.getCommodityIndices());
			nodes.add(n);
		}

		// Base Fuels
		{
			final EObject n = modelPackage.getEFactoryInstance().create(node);
			n.eSet(name, "Base FuelCurves");
			n.eSet(data, pricingModel.getBaseFuelPrices());
			nodes.add(n);
		}

		// Charter Curves
		{
			final EObject n = modelPackage.getEFactoryInstance().create(node);
			n.eSet(name, "Charter Curves");
			n.eSet(data, pricingModel.getCharterIndices());
			nodes.add(n);
		}

		root.eSet(data, nodes);

		return root;
	}

	public EPackage getModelPackage() {
		return modelPackage;
	}

	public EClass getNodeClass() {
		return node;
	}

	public EReference getDataReference() {
		return data;
	}

	public EAttribute getNameAttribute() {
		return name;
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

				if (node.isInstance(element)) {
					final List<?> children = (List<?>) ((EObject) element).eGet(data);
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

				if (node.isInstance(parentElement)) {
					final List<?> children = (List<?>) ((EObject) parentElement).eGet(data);
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
}
