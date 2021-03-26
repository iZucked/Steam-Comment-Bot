/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Collection;
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
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.EditObjectMouseListener;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;

public abstract class AbstractModellerComponent<T, U> {
	protected final Map<Object, IStatus> validationErrors;
	protected final @NonNull IScenarioEditingLocation scenarioEditingLocation;
	protected final @NonNull Supplier<U> modelProvider;
	protected final @NonNull List<Consumer<U>> inputWants = new LinkedList<>();

	protected boolean locked;
	protected Collection<Consumer<Boolean>> lockedListeners = Sets.newConcurrentHashSet();

	protected SandboxUIHelper sandboxUIHelper;

	protected AbstractModellerComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull final Supplier<U> modelProvider) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.validationErrors = validationErrors;
		this.modelProvider = modelProvider;
	}

	protected ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s) {
		return wrapInExpandable(composite, name, s, null, false);
	}

	protected ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s, @Nullable final Consumer<ExpandableComposite> customiser,
			final boolean expand) {

		sandboxUIHelper = new SandboxUIHelper(composite);

		final ExpandableComposite expandableCompo;
		if (!expand) {
			expandableCompo = new ExpandableComposite(composite, SWT.NONE, 0);
		} else {
			expandableCompo = new ExpandableComposite(composite, SWT.NONE, ExpandableComposite.TWISTIE);
		}

		expandableCompo.setText(name);
		expandableCompo.setFont(sandboxUIHelper.largeFont);
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

		final DragSource source = new DragSource(viewer.getGrid(), DND.DROP_LINK);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer) {
			@Override
			public void dragSetData(final DragSourceEvent event) {
				super.dragSetData(event);

				if (locked) {
					event.doit = false;
					event.detail = DND.DROP_NONE;
				} else {
					event.detail = DND.DROP_LINK;
				}
			}
		});
		;
	}

	protected void hookOpenEditor(final @NonNull GridTreeViewer viewer) {
		viewer.getGrid().addMouseListener(new EditObjectMouseListener(viewer, scenarioEditingLocation));
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {
		return createColumn(viewer, createLabelProvider(name, renderer, pathObjects), name, renderer, isTree, pathObjects);
	}

	protected GridViewerColumn createColumn(final GridTreeViewer viewer, final CellFormatterLabelProvider labelProvider, final String name, final ICellRenderer renderer, final boolean isTree,
			final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);
		return gvc;
	}

	private CellFormatterLabelProvider createLabelProvider(final String name, final ICellRenderer renderer, final ETypedElement... pathObjects) {
		return new CellFormatterLabelProvider(renderer, pathObjects) {

			@Override
			protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

				if (validationErrors.containsKey(element)) {
					final IStatus status = validationErrors.get(element);
					final Image img = sandboxUIHelper.getValidationImageForStatus(status);
					if (img != null) {
						return img;
					}
				}
				if (element instanceof RoundTripShippingOption) {
					return sandboxUIHelper.imgShippingRoundTrip;
				} else if (element instanceof SimpleVesselCharterOption) {
					return sandboxUIHelper.imgShippingFleet;
				}
				return null;
			}

			@Override
			public String getToolTipText(final Object element) {

				final Set<Object> targetElements = getTargetElementsForLabelProvider(name, element);

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

			@Override
			public void update(final ViewerCell cell) {
				super.update(cell);

				final GridItem item = (GridItem) cell.getItem();
				item.setHeaderText("");
				item.setHeaderImage(null);
				cell.setBackground(null);
				final Object element = cell.getElement();

				final Set<Object> targetElements = getTargetElementsForLabelProvider(null, element);
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
						cell.setBackground(sandboxUIHelper.colourError);
						item.setBackground(sandboxUIHelper.colourError);
					} else if (s.matches(IStatus.WARNING)) {
						cell.setBackground(sandboxUIHelper.colourWarn);
						item.setBackground(sandboxUIHelper.colourWarn);
					} else if (s.matches(IStatus.INFO)) {
						cell.setBackground(sandboxUIHelper.colourInfo);
						item.setBackground(sandboxUIHelper.colourInfo);
					}
				}

				if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
					if (validationErrors.containsKey(element)) {
						final IStatus status = validationErrors.get(element);
						final Image img = sandboxUIHelper.getValidationImageForStatus(status);
						item.setHeaderImage(img);
					}
				}
			}
		};
	}

	protected CellLabelProvider createWiringColumnLabelProvider() {

		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {

				final Set<Object> targetElements = getTargetElementsForWiringProvider(element);

				final StringBuilder sb = new StringBuilder();
				sandboxUIHelper.extractValidationMessages(sb, targetElements, validationErrors);

				if (sb.length() > 0) {
					return sb.toString();
				}
				return super.getToolTipText(element);
			}

			@Override
			public void update(final ViewerCell cell) {

				final GridItem item = (GridItem) cell.getItem();
				item.setHeaderText("");
				item.setHeaderImage(null);
				cell.setBackground(null);

				final Object element = cell.getElement();

				final Set<Object> targetElements = getTargetElementsForWiringProvider(element);
				final IStatus s = sandboxUIHelper.getWorstStatus(targetElements, validationErrors);
				sandboxUIHelper.updateGridItem(cell, s);

				if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
					if (validationErrors.containsKey(element)) {
						final IStatus status = validationErrors.get(element);
						sandboxUIHelper.updateGridHeaderItem(cell, status);
					}
				}
			}

		};
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

	public @NonNull List<Consumer<U>> getInputWants() {
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

	public abstract void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, T modellerView);

	protected abstract Set<Object> getTargetElementsForLabelProvider(final String name, final Object element);

	protected abstract Set<Object> getTargetElementsForWiringProvider(final Object element);

	protected MouseListener hookMaximiseListener(final Control source) {
		return new MouseAdapter() {

			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				if (e.getSource() != source) {
					return;
				}
				Control child = source;
				for (Control pparent = source.getParent(); pparent != null; pparent = pparent.getParent()) {
					if (pparent instanceof CTabFolder) {
						final CTabFolder cTabFolder = (CTabFolder) pparent;
						cTabFolder.setMaximized(!cTabFolder.getMaximized());
					} else if (pparent instanceof SashForm) {
						final SashForm sashForm = (SashForm) pparent;
						if (sashForm.getMaximizedControl() == null) {
							sashForm.setMaximizedControl(child);
						} else {
							sashForm.setMaximizedControl(null);
						}
						break;
					}
					child = pparent;
				}
			}
		};
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
		lockedListeners.forEach(e -> e.accept(locked));
		doSetLocked(locked);
	}

	protected void doSetLocked(final boolean locked) {
		// Subclasses should override this method to react to new locked state (or use the locked listeners).
	}

	protected void addLockedListener(final Consumer<Boolean> l) {
		lockedListeners.add(l);
	}

	protected void removeLockedListener(final Consumer<Boolean> l) {
		lockedListeners.remove(l);
	}

	public void dispose() {
		// Sub-classes can override this method
	}
}
