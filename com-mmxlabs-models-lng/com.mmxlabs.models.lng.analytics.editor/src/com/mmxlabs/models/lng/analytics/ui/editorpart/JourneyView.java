package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.Journey;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.transformer.IAnalyticsTransformer;
import com.mmxlabs.models.lng.analytics.transformer.impl.AnalyticsTransformer;
import com.mmxlabs.models.lng.analytics.ui.properties.UnitCostLinePropertySource;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class JourneyView extends ScenarioInstanceView {
	private final IAnalyticsTransformer transformer = new AnalyticsTransformer();
	private Composite top;
	private Composite inner;
	private UnitCostMatrixViewerPane pane;
	private Journey journey;
	private IDisplayComposite journeyComposite;
	private PropertySheetPage propertySheetPage;
	
	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			disposeContents();
			getSite().setSelectionProvider(null);
			super.displayScenarioInstance(instance);
			if (instance != null) {
				createContents();
			}
		}
	}
	
	private void disposeContents() {
		if (pane != null) {			
			pane.dispose();
			pane = null;
		}
		if (journeyComposite != null) {
			journeyComposite.getComposite().dispose();
			journeyComposite = null;
		}
		
		if (inner != null) {
			inner.dispose();
			inner = null;
		}
		journey = null;
	}
	
	private void createContents() {
		final Composite inner = new Composite(top, SWT.NONE);
		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		inner.setLayout(gridLayout);
		final Composite upper = new Composite(inner, SWT.NONE);
		upper.setLayout(new GridLayout());
		upper.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		journey = AnalyticsFactory.eINSTANCE.createJourney();
		
		journey.eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (!msg.isTouch()) {
					if (pane != null && pane.getControl() != null && !pane.getControl().isDisposed()) {
						pane.getViewer().refresh();
						if (propertySheetPage != null) {
							propertySheetPage.refresh();
						}
					}
				}
			}
		});
		
		IDisplayCompositeFactory factory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(journey.eClass());
		journeyComposite = factory.createToplevelComposite(upper, journey.eClass(), this);
		journeyComposite.setCommandHandler(getDefaultCommandHandler());
		journeyComposite.display(this, getRootObject(), journey, (Collection) Collections.emptyList());
		
		pane = new UnitCostMatrixViewerPane(getSite().getPage(), this, this, this.getViewSite().getActionBars()) {
			@Override
			protected boolean showEvaluateAction() {
				return false;
			}

			@Override
			public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
				super.init(path, adapterFactory);
				addTypicalColumn("Unit Cost", new NonEditableColumn() {
					@Override
					public String render(final Object object) {
						if (propertySheetPage != null) propertySheetPage.refresh();
						if (journey.getFrom() == null || journey.getTo() == null) return "";
						try {
							final UnitCostLine line = transformer.createCostLine(getRootObject(), (UnitCostMatrix) object, journey.getFrom(), journey.getTo());
							if (line != null) {
								return String.format("$%.2f", line.getUnitCost());
							} else {
								return "";
							}
						} catch (Throwable th) {
							return "";
						}
						
					}
				});
			}
		};
		
		pane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
		pane.createControl(inner);
		pane.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		pane.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_RoundTripMatrices()), getAdapterFactory());
		pane.getViewer().setInput(getRootObject().getSubModel(AnalyticsModel.class));
		
		getViewSite().setSelectionProvider(pane.getViewer());
	}

	@Override
	public void createPartControl(final Composite parent) {
		top = parent;
		GridLayout gridLayout = new GridLayout(1,false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		top.setLayout(gridLayout);
		listenToScenarioSelection();
	}

	@Override
	public void setFocus() {
		
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			if (propertySheetPage == null) {
				propertySheetPage = new PropertySheetPage();
				propertySheetPage.setPropertySourceProvider(new IPropertySourceProvider() {
					@Override
					public IPropertySource getPropertySource(Object object) {
						if (object instanceof UnitCostMatrix) {
							if (journey.getFrom() == null || journey.getTo() == null) return null;
							try {
								final UnitCostLine line = transformer.createCostLine(getRootObject(), (UnitCostMatrix) object, journey.getFrom(), journey.getTo());
								return new UnitCostLinePropertySource(line);
							} catch (Throwable th) {
								return null;
							}
						} else if (object instanceof IPropertySource) {
							return (IPropertySource) object;
						}
						return null;
					}
				});
			}
			return propertySheetPage;
		}
		return null;
	}
}
