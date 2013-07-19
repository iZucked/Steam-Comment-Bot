package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class SlotDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	private static final CargoPackage CargoFeatures = CargoPackage.eINSTANCE;

	private ExpandableComposite ecWindow;
	private ExpandableComposite ecTerms;
	ScrolledComposite scrollComposite;
	Composite contentComposite;
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor;
	private final Map<EObject, EContentAdapter> eObject2Editor;
	Collection theExpandables;
	ExpandableSet esWindow;
	private ExpandableSet es2Restrictions;
	private final ExpandableSet esTerms;
	private ArrayList<EStructuralFeature> mainFeatures;
	private ArrayList<EStructuralFeature> termsFeatures;
	private ArrayList<EStructuralFeature> windowFeatures;
	private ArrayList<EStructuralFeature> missedFeatures;
	private ArrayList<EStructuralFeature> loadOnlyFeatures;
	private ArrayList<EStructuralFeature> dischargeOnlyFeatures;

	private Collection Features;

	{
		mainFeatures = (ArrayList<EStructuralFeature>) CollectionsUtil.makeArrayList(CargoFeatures.getSlot_Contract(), CargoFeatures.getSlot_PriceExpression(), CargoFeatures.getSlot_Port(),
				CargoFeatures.getSlot_MinQuantity(), CargoFeatures.getSlot_MaxQuantity(), CargoFeatures.getSlot_Optional());

		windowFeatures = new ArrayList<EStructuralFeature>();
		windowFeatures.add(CargoFeatures.getSlot_WindowStart());
		windowFeatures.add(CargoFeatures.getSlot_WindowStartTime());
		windowFeatures.add(CargoFeatures.getSlot_WindowSize());
		windowFeatures.add(CargoFeatures.getSlot_Duration());

		termsFeatures = new ArrayList<EStructuralFeature>();
		termsFeatures.add(CargoFeatures.getLoadSlot_ArriveCold());
		termsFeatures.add(CargoFeatures.getSlot_Notes());
		// termsFeatures.add(CargoFeatures.getSlot_WindowSize());
		missedFeatures = new ArrayList<EStructuralFeature>();

		loadOnlyFeatures = new ArrayList<EStructuralFeature>();
		loadOnlyFeatures.add(CargoFeatures.getLoadSlot_ArriveCold());
		loadOnlyFeatures.add(CargoFeatures.getLoadSlot_CargoCV());

		dischargeOnlyFeatures = new ArrayList<EStructuralFeature>();
	}

	private final class ExpandableSet {

		String title;
		ExpandableComposite ec;
		Composite client;
		EStructuralFeature headerFeature;
		Collection<EStructuralFeature> features;

		public ExpandableSet(final String t, final List<EStructuralFeature> f, final EStructuralFeature titleF) {
			title = t;
			features = f;
			headerFeature = titleF;
		}
	}

	void updateTitle(final ExpandableSet es) {

		final EStructuralFeature f = es.headerFeature;
		if (f != null) {
			final EObject eo = feature2Editor.get(f).getEditorTarget();
			final Date d = (Date) eo.eGet(f);
			es.ec.setText(es.title + ": " + d);
		}
	}

	public SlotDetailComposite(final Composite parent, final int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
		feature2Editor = new HashMap<EStructuralFeature, IInlineEditor>();
		eObject2Editor = new HashMap<EObject, EContentAdapter>();
		// setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		esWindow = new ExpandableSet("Window", windowFeatures, windowFeatures.get(0));
		esTerms = new ExpandableSet("Terms", termsFeatures, termsFeatures.get(0));
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new FillLayout();
			}
		};
	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {
		final EStructuralFeature f = editor.getFeature();
		// // Here the exceptions are listed for the elements which should go into the bottom
		// if (feature == CargoPackage.eINSTANCE.getCargo_AllowedVessels()) {
		// // topElement = false;
		// }
		// if (feature == AssignmentPackage.eINSTANCE.getElementAssignment_Assignment()) {
		// // topElement = false;
		// }
		// Do not add elements if they are for the wrong section.
		// if (top != topElement) {
		// return;
		// }

		final IInlineEditor ed = feature2Editor.put(f, editor);
		if (!mainFeatures.contains(f) && !windowFeatures.contains(f)) {
			missedFeatures.add(f);
		}

		// System.out.println(f);
		// if (feature2Editor.get(f) == null) {
		// System.out.println("NULL EDITOR!");
		// }
		// if (f == CargoFeatures.getLoadSlot_ArriveCold()) {
		// System.out.println("GOT IT!");
		// }
		//
		// // if ( (feature != CargoPackage.eINSTANCE.getSlot_Duration())
		// // && (feature != CargoPackage.eINSTANCE.getSlot_WindowSize())
		// // // && (feature != CargoPackage.eINSTANCE.getSlot_Port())
		// // && (feature != CargoPackage.eINSTANCE.getSlot_Contract()) )
		// // return;

		super.addInlineEditor(editor);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		for (final Map.Entry<EObject, EContentAdapter> e : eObject2Editor.entrySet()) {
			e.getKey().eAdapters().remove(e.getValue());
		}

		super.display(location, root, object, range, dbc);
		// ec.setExpanded(true);
		// ec.setExpanded(false);
		// final EClass eClass = object.eClass();
		// setLayout(layoutProvider.createDetailLayout(root, object));
		// if (eClass != displayedClass) {
		// clear();
		// initialize(eClass);
		// createControls(root, object);
		// }
		// for (final IInlineEditor editor : editors) {
		// editor.display(location, root, object, range);
		// }
		ecWindow.setSize(ecWindow.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		ecWindow.layout();
		scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		final EContentAdapter ad = new EContentAdapter() {
			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				final Object f = notification.getFeature();
				if (f == CargoFeatures.getSlot_WindowStart()) {
					final EObject eo = feature2Editor.get(f).getEditorTarget();
					final Date d = (Date) eo.eGet(CargoFeatures.getSlot_WindowStart());
					ecWindow.setText("Window: " + d);
				}
			}
		};
		final EObject eo = feature2Editor.get(CargoFeatures.getSlot_WindowStart()).getEditorTarget();
		final Date d = (Date) eo.eGet(CargoFeatures.getSlot_WindowStart());
		ecWindow.setText("Window: " + d);
		eo.eAdapters().add(ad);
		eObject2Editor.put(eo, ad);

		ecTerms.setText("Terms");

	}

	@Override
	public void createControls(final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc) {

		toolkit.adapt(this);

		LoadSlot loadSlot;
		DischargeSlot dischargeSlot;
		boolean isLoad;
		if (object instanceof LoadSlot) {
			loadSlot = (LoadSlot) object;
			isLoad = true;
		} else if (object instanceof DischargeSlot) {
			dischargeSlot = (DischargeSlot) object;
			isLoad = false;
		} else {
			// Say what?...
			isLoad = false;
		}

		scrollComposite = new ScrolledComposite(this, SWT.NONE | SWT.V_SCROLL);
		// scrollComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		toolkit.adapt(scrollComposite, true, true);
		scrollComposite.setLayout(new FillLayout());
		// scroller.setAlwaysShowScrollBars(true);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		// scroller.setSize(scroller.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// contentComposite = new Composite(scrollComposite, SWT.NONE);
		contentComposite = toolkit.createComposite(scrollComposite);
		// contentComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		contentComposite.setLayout(new GridLayout(2, false));
		scrollComposite.setContent(contentComposite);

		for (final EStructuralFeature f : mainFeatures) {
			makeControl(root, object, contentComposite, f, dbc);
		}

		// ecWindow = new ExpandableComposite(contentComposite, SWT.NONE, ExpandableComposite.`TIE);
		ecWindow = toolkit.createSection(contentComposite, ExpandableComposite.TWISTIE);
		ecWindow.setText("");
		final Composite windowC = createExpandable(ecWindow);
		for (final EStructuralFeature f : windowFeatures) {

			final Control control = makeControl(root, object, windowC, f, dbc);
		}

		ecTerms = toolkit.createSection(contentComposite, ExpandableComposite.TWISTIE);
		ecTerms.setText("");

		final Composite termC = createExpandable(ecTerms);
		for (final EStructuralFeature f : termsFeatures) {
			if (!isLoad && loadOnlyFeatures.contains(f))
				continue;
			final Control control = makeControl(root, object, termC, f, dbc);
		}
		for (final EStructuralFeature f : missedFeatures) {
			// Control control = makeControl(root, object, termC, f);
		}

		for (final IInlineEditor editor : editors) {
			final EStructuralFeature f = editor.getFeature();
			if (mainFeatures.contains(f))
				continue;
			if (windowFeatures.contains(f))
				continue;
			if (termsFeatures.contains(f))
				continue;
			final Control c = makeControl(root, object, windowC, f, dbc);
		}

		this.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
				scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
	}

	private Composite createExpandable(final ExpandableComposite ec) {
		// ec.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		ec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		ec.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				final Point p = ec.getSize();
				final Point p2 = ec.computeSize(p.x, SWT.DEFAULT);
				ec.setSize(p.x, p2.y);
				scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				// scrollComposite.layout();
				contentComposite.layout();
			}
		});
		final Composite inner = toolkit.createComposite(ec);
		// inner.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		inner.setLayout(new GridLayout(2, false));
		ec.setClient(inner);
		return inner;
	}

	protected final void expandedStateChanged(final ExpandableComposite expandable) {
		final SharedScrolledComposite parentScrolledComposite = getParentScrolledComposite(expandable);
		if (parentScrolledComposite != null) {
			parentScrolledComposite.reflow(true);
		}
	}

	protected SharedScrolledComposite getParentScrolledComposite(final Control control) {
		Control parent = control.getParent();
		while (!(parent instanceof SharedScrolledComposite) && parent != null) {
			parent = parent.getParent();
		}
		if (parent instanceof SharedScrolledComposite) {
			return (SharedScrolledComposite) parent;
		}
		return null;
	}

	private Control makeControl(final MMXRootObject root, final EObject object, final Composite c, final EStructuralFeature f, final EMFDataBindingContext dbc) {

		final IInlineEditor editor = feature2Editor.get(f);
		final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(c, SWT.NONE) : null;
		editor.setLabel(label);
		final Control control = editor.createControl(c, dbc, toolkit);
		control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
		control.setData(LABEL_CONTROL_KEY, label);
		// control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		if (label != null) {
			toolkit.adapt(label, true, false);
			// label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
		}
		return control;
	}

	public void dispose() {

		for (final Map.Entry<EObject, EContentAdapter> e : eObject2Editor.entrySet()) {
			e.getKey().eAdapters().remove(e.getValue());
		}
	};
}
