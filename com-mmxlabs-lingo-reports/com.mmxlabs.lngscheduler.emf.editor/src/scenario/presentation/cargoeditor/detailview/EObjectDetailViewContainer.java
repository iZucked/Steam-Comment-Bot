/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditorFactory;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * @author Tom Hinton
 * 
 */
public abstract class EObjectDetailViewContainer implements
		IDetailViewContainer {
	protected final Map<EClassifier, IInlineEditorFactory> editorFactories = new HashMap<EClassifier, IInlineEditorFactory>();
	protected final Map<EStructuralFeature, IInlineEditorFactory> editorFactoriesByFeature = new HashMap<EStructuralFeature, IInlineEditorFactory>();
	protected final HashMap<EClass, EObjectDetailView> detailViewsByClass = new HashMap<EClass, EObjectDetailView>();
	protected Map<EStructuralFeature, String> nameByFeature = new HashMap<EStructuralFeature, String>();

	protected abstract EditingDomain getEditingDomain();

	protected abstract ICommandProcessor getProcessor();

	@Override
	public void addDefaultEditorFactories() {
		editorFactories.put(EcorePackage.eINSTANCE.getEString(),
				wrapEditorFactory(new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(EMFPath path,
							EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new TextInlineEditor(path, feature,
								getEditingDomain(), processor);
					}
				}));

		final IInlineEditorFactory numberEditorFactory = wrapEditorFactory(new IInlineEditorFactory() {
			@Override
			public IInlineEditor createEditor(EMFPath path,
					EStructuralFeature feature,
					final ICommandProcessor processor) {
				return new NumberInlineEditor(path, feature,
						getEditingDomain(), processor);
			}
		});

		editorFactories.put(EcorePackage.eINSTANCE.getEInt(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getELong(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getEFloat(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getEDouble(),
				numberEditorFactory);

		final IInlineEditorFactory dateEditorFactory = wrapEditorFactory(new IInlineEditorFactory() {
			@Override
			public IInlineEditor createEditor(EMFPath path,
					EStructuralFeature feature,
					final ICommandProcessor processor) {
				return new LocalDateInlineEditor(path, feature,
						getEditingDomain(), processor);
			}
		});

		editorFactories.put(EcorePackage.eINSTANCE.getEDate(),
				dateEditorFactory);

		editorFactories.put(ScenarioPackage.eINSTANCE.getDateAndOptionalTime(),
				dateEditorFactory);
		
		editorFactories.put(EcorePackage.eINSTANCE.getEBoolean(),
				wrapEditorFactory(new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(EMFPath path,
							EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new BooleanInlineEditor(path, feature,
								getEditingDomain(), processor);
					}
				}));
	}

	protected IInlineEditorFactory wrapEditorFactory(
			final IInlineEditorFactory factory) {
		return factory;
	}

	protected EObjectDetailView getDetailView(final EClass eClass,
			final Composite control) {
		if (detailViewsByClass.containsKey(eClass)) {
			return detailViewsByClass.get(eClass);
		} else {
			final EObjectDetailView eodv = new EObjectDetailView(control,
					SWT.NONE, getProcessor(), getEditingDomain());

			for (final Map.Entry<EClassifier, IInlineEditorFactory> entry : editorFactories
					.entrySet()) {
				eodv.setEditorFactoryForClassifier(entry.getKey(),
						entry.getValue());
			}

			for (final Map.Entry<EStructuralFeature, IInlineEditorFactory> entry : editorFactoriesByFeature
					.entrySet()) {
				eodv.setEditorFactoryForFeature(entry.getKey(),
						entry.getValue());
			}

			for (final Map.Entry<EStructuralFeature, String> entry : nameByFeature
					.entrySet()) {
				eodv.setNameForFeature(entry.getKey(), entry.getValue());
			}

			eodv.initForEClass(eClass);
			eodv.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			detailViewsByClass.put(eClass, eodv);
			return eodv;
		}
	}

	@Override
	public void setNameForFeature(final EStructuralFeature feature,
			final String string) {
		nameByFeature.put(feature, string);
	}

	@Override
	public void setEditorFactoryForClassifier(final EClassifier classifier,
			final IInlineEditorFactory factory) {
		editorFactories.put(classifier, wrapEditorFactory(factory));
	}

	@Override
	public void setEditorFactoryForFeature(final EStructuralFeature feature,
			final IInlineEditorFactory factory) {
		editorFactoriesByFeature.put(feature, wrapEditorFactory(factory));
	}
}
