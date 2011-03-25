/**
 * 
 */
package scenario.presentation.cargoeditor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

import scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditor;
import scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditorFactory;
import scenario.presentation.cargoeditor.detailview.BooleanInlineEditor;
import scenario.presentation.cargoeditor.detailview.LocalDateInlineEditor;
import scenario.presentation.cargoeditor.detailview.NumberInlineEditor;
import scenario.presentation.cargoeditor.detailview.TextInlineEditor;

/**
 * @author Tom Hinton
 * 
 */
public class EObjectDetailPropertySheetPage implements IPropertySheetPage {
	private Composite control;//, top;
	private ScrolledComposite sc;
	
	private final Map<EClassifier, IInlineEditorFactory> editorFactories = new HashMap<EClassifier, IInlineEditorFactory>();
	private final Map<EStructuralFeature, IInlineEditorFactory> editorFactoriesByFeature = new HashMap<EStructuralFeature, IInlineEditorFactory>();
	
	private final HashMap<EClass, EObjectDetailView> detailViewsByClass = new HashMap<EClass, EObjectDetailView>();
	private final EditingDomain editingDomain;

	public void setEditorFactoryForClassifier(final EClassifier classifier,
			final IInlineEditorFactory factory) {
		editorFactories.put(classifier, factory);
	}

	public void setEditorFactoryForFeature(
			final EStructuralFeature feature,
			final IInlineEditorFactory iInlineEditorFactory) {
		editorFactoriesByFeature.put(feature, iInlineEditorFactory);
	}
	
	public EObjectDetailPropertySheetPage(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;

		editorFactories.put(EcorePackage.eINSTANCE.getEString(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(EMFPath path,
							EStructuralFeature feature) {
						return new TextInlineEditor(path, feature,
								editingDomain);
					}
				});

		final IInlineEditorFactory numberEditorFactory = new IInlineEditorFactory() {
			@Override
			public IInlineEditor createEditor(EMFPath path,
					EStructuralFeature feature) {
				return new NumberInlineEditor(path, feature, editingDomain);
			}
		};

		editorFactories.put(EcorePackage.eINSTANCE.getEInt(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getELong(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getEFloat(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getEDouble(),
				numberEditorFactory);
		editorFactories.put(EcorePackage.eINSTANCE.getEDate(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(EMFPath path,
							EStructuralFeature feature) {
						return new LocalDateInlineEditor(path, feature,
								editingDomain);
					}
				});
		
		editorFactories.put(EcorePackage.eINSTANCE.getEBoolean(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(EMFPath path, EStructuralFeature feature) {
						return new BooleanInlineEditor(path, feature, editingDomain);
					}
				});
	}

	private EObjectDetailView getDetailView(final EClass eClass) {
		if (detailViewsByClass.containsKey(eClass)) {
			return detailViewsByClass.get(eClass);
		} else {
			final EObjectDetailView eodv = new EObjectDetailView(control,
					SWT.NONE);

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
			
			for (final Map.Entry<EStructuralFeature, String> entry : nameByFeature.entrySet()) {
				eodv.setNameForFeature(entry.getKey(), entry.getValue());
			}
			
			eodv.initForEClass(eClass);
			eodv.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			detailViewsByClass.put(eClass, eodv);
			return eodv;
		}
	}

	@Override
	public void createControl(final Composite parent) {
		
		sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setLayout(new FillLayout());
		control = new Composite(sc, SWT.NONE);
		sc.setContent(control);
		
		sc.setExpandHorizontal(true);
//		sc.setExpandVertical(false);
		control.setLayout(new GridLayout(1, false));
	}

	@Override
	public void dispose() {
		control.dispose();
	}

	@Override
	public Control getControl() {
		return sc;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {

	}

	@Override
	public void setFocus() {
		control.setFocus(); // TODO set focus to child entry
	}

	private EObjectDetailView activeDetailView = null;
	private boolean selectionChanging = false;

	@Override
	public void selectionChanged(final IWorkbenchPart part,
			final ISelection selection) {
		// watch out of re-entry
		if (selectionChanging) {
			System.err.println("re-entrant selection?");
			return;
		}
		selectionChanging = true;
		try {
			if (selection instanceof IStructuredSelection) {
				final Object object = ((IStructuredSelection) selection)
						.getFirstElement();

				if (object instanceof EObject) {
					final EObjectDetailView eodv = getDetailView(((EObject) object)
							.eClass());
					if (eodv != activeDetailView) {
						// make view visible
						if (activeDetailView != null) {
							((GridData) activeDetailView.getLayoutData()).exclude = true;
							activeDetailView.setVisible(false);
							activeDetailView.setInput(null);
						}
						activeDetailView = eodv;
						((GridData) activeDetailView.getLayoutData()).exclude = false;
						activeDetailView.setVisible(true);
					}
					eodv.setInput((EObject) object);
				} else {
					if (activeDetailView != null) {
						((GridData) activeDetailView.getLayoutData()).exclude = true;
						activeDetailView.setVisible(false);
						activeDetailView.setInput(null);
					}
					activeDetailView = null;
				}
				control.layout(true);
				control.setSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//				sc.setContent(null);
//				sc.setContent(control);
			}
		} finally {
			selectionChanging = false;
		}
	}

	private Map<EStructuralFeature, String> nameByFeature = 
		new HashMap<EStructuralFeature,String>();
	public void setNameForFeature(final EStructuralFeature feature,
			final String string) {
		nameByFeature.put(feature, string);
	}

	
}
