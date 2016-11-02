package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;

public abstract class AbstractSandboxComponent {
	protected final Map<Object, IStatus> validationErrors;
	protected final @NonNull IScenarioEditingLocation scenarioEditingLocation;
	protected final @NonNull Supplier<OptionAnalysisModel> modelProvider;
	protected final @NonNull List<Consumer<OptionAnalysisModel>> inputWants = new LinkedList<>();

	protected final Image image_grey_add;

	protected AbstractSandboxComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull Supplier<OptionAnalysisModel> modelProvider) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.validationErrors = validationErrors;
		this.modelProvider = modelProvider;

		final ImageDescriptor baseAdd = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD);
		image_grey_add = ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY).createImage();
	}

	public void dispose() {
		image_grey_add.dispose();
	}

	protected ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s) {
		return wrapInExpandable(composite, name, s, null);
	}

	protected ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s, @Nullable final Consumer<ExpandableComposite> customiser) {
		final ExpandableComposite expandableCompo = new ExpandableComposite(composite, SWT.NONE);
		expandableCompo.setText(name);
		expandableCompo.setLayout(new GridLayout(1, false));

		final Control client = s.apply(expandableCompo);
		GridDataFactory.generate(expandableCompo, 2, 2);

		expandableCompo.setClient(client);

		if (customiser != null) {
			customiser.accept(expandableCompo);
		}
		return expandableCompo;
	}

	protected void hookDragSource(final GridTreeViewer viewer) {

		final DragSource source = new DragSource(viewer.getGrid(), DND.DROP_MOVE);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer));
	}

	protected void hookOpenEditor(final @NonNull GridTreeViewer viewer) {
		viewer.getGrid().addMouseListener(new EditObjectMouseListener(viewer, scenarioEditingLocation));
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(new CellFormatterLabelProvider(renderer, pathObjects) {

			Image imgError = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/error.gif").createImage();
			Image imgWarn = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/warning.gif").createImage();
			Image imgInfo = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/information.gif").createImage();
			Image imgShippingRoundTrip = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/roundtrip.png").createImage();
			Image imgShippingFleet = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/fleet.png").createImage();

			Color colour_error = new Color(Display.getDefault(), new RGB(255, 100, 100));
			Color colour_warn = new Color(Display.getDefault(), new RGB(255, 255, 200));
			Color colour_info = new Color(Display.getDefault(), new RGB(200, 240, 240));

			@Override
			protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

				if (validationErrors.containsKey(element)) {
					final IStatus status = validationErrors.get(element);
					if (!status.isOK()) {
						if (status.matches(IStatus.ERROR)) {
							return imgError;
						}
						if (status.matches(IStatus.WARNING)) {
							return imgWarn;
						}
						if (status.matches(IStatus.INFO)) {
							return imgWarn;
						}
					}
				} else {
					if (element instanceof RoundTripShippingOption) {
						return imgShippingRoundTrip;
					} else if (element instanceof FleetShippingOption) {
						return imgShippingFleet;
					}
				}
				return null;
			}

			@Override
			public String getToolTipText(final Object element) {

				final Set<Object> targetElements = getTargetElements(name, element);

				final StringBuilder sb = new StringBuilder();
				for (final Object target : targetElements) {
					if (validationErrors.containsKey(target)) {
						final IStatus status = validationErrors.get(target);
						if (!status.isOK()) {
							if (sb.length() > 0) {
								sb.append("\n");
							}
							sb.append(status.getMessage());
						}
					}
				}
				if (sb.length() > 0) {
					return sb.toString();
				}
				return super.getToolTipText(element);
			}

			private Set<Object> getTargetElements(final String name, final Object element) {
				final Set<Object> targetElements = new HashSet<>();
				targetElements.add(element);
				// FIXME: Hacky!
				if (element instanceof BaseCaseRow) {
					final BaseCaseRow baseCaseRow = (BaseCaseRow) element;
					if (name == null || "Buy".equals(name)) {
						targetElements.add(baseCaseRow.getBuyOption());
					}
					if (name == null || "Sell".equals(name)) {
						targetElements.add(baseCaseRow.getSellOption());
					}
					if (name == null || "Shipping".equals(name)) {
						targetElements.add(baseCaseRow.getShipping());
					}
				}
				if (element instanceof PartialCaseRow) {
					final PartialCaseRow row = (PartialCaseRow) element;
					if (name == null || "Buy".equals(name)) {
						targetElements.addAll(row.getBuyOptions());
					}
					if (name == null || "Sell".equals(name)) {
						targetElements.addAll(row.getSellOptions());
					}
					if (name == null || "Shipping".equals(name)) {
						targetElements.add(row.getShipping());
					}
				}
				targetElements.remove(null);
				return targetElements;
			}

			@Override
			public void update(final ViewerCell cell) {
				super.update(cell);

				final GridItem item = (GridItem) cell.getItem();
				item.setHeaderText("");
				item.setHeaderImage(null);
				cell.setBackground(null);
				final Object element = cell.getElement();

				final Set<Object> targetElements = getTargetElements(null, element);
				IStatus s = org.eclipse.core.runtime.Status.OK_STATUS;
				for (final Object e : targetElements) {
					if (validationErrors.containsKey(e)) {
						final IStatus status = validationErrors.get(e);
						if (!status.isOK()) {
							if (status.getSeverity() > s.getSeverity()) {
								s = status;
							}
						}
					}
				}
				if (!s.isOK()) {
					if (s.matches(IStatus.ERROR)) {
						cell.setBackground(colour_error);
						item.setBackground(colour_error);
					} else if (s.matches(IStatus.WARNING)) {
						cell.setBackground(colour_warn);
						item.setBackground(colour_warn);
					} else if (s.matches(IStatus.INFO)) {
						cell.setBackground(colour_info);
						item.setBackground(colour_info);
					}
				}

				if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
					if (validationErrors.containsKey(element)) {
						final IStatus status = validationErrors.get(element);
						if (!status.isOK()) {
							if (status.matches(IStatus.ERROR)) {
								item.setHeaderImage(imgError);
							} else if (status.matches(IStatus.WARNING)) {
								item.setHeaderImage(imgWarn);
							} else if (status.matches(IStatus.INFO)) {
								item.setHeaderImage(imgInfo);
							}
						}
					}
				}
			}

			@Override
			public void dispose() {
				imgError.dispose();
				imgWarn.dispose();
				imgInfo.dispose();
				imgShippingRoundTrip.dispose();

				colour_error.dispose();
				colour_info.dispose();
				colour_warn.dispose();
				super.dispose();
			}

		});
		return gvc;
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final CellLabelProvider labelProvider, final boolean isTree, final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(200);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);
		return gvc;
	}

	public @NonNull List<Consumer<OptionAnalysisModel>> getInputWants() {
		return inputWants;
	}

	public abstract void refresh();

	protected final IDialogEditingContext dialogContext = new IDialogEditingContext() {

		@Override
		public void registerEditorControl(final EObject target, final EStructuralFeature feature, final Control control) {

		}

		@Override
		public boolean isNewEdit() {
			return false;
		}

		@Override
		public boolean isMultiEdit() {
			return false;
		}

		@Override
		public IScenarioEditingLocation getScenarioEditingLocation() {
			return scenarioEditingLocation;
		}

		@Override
		public List<Control> getEditorControls(final EObject target, final EStructuralFeature feature) {
			return null;
		}

		@Override
		public IDialogController getDialogController() {
			return null;
		}
	};

	public abstract void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, OptionModellerView optionModellerView);
}
