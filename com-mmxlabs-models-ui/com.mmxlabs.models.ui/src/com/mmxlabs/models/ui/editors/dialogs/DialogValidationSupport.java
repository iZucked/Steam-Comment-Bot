/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.Dialog;

import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;

/**
 * A small utility class to manage validation in a {@link Dialog}. Once created,
 * a call
 * {@link IScenarioEditingLocation#pushExtraValidationContext(IExtraValidationContext)}
 * with the output from {@link #getValidationContext()} should be invoked, and a
 * corresponding call to
 * {@link IScenarioEditingLocation#popExtraValidationContext()} should occur in
 * a try/finally block. The objects to be validated should be set via a call to
 * {@link #setValidationTargets(Collection)}. Validation can then be performed
 * with {@link #validate()}. This will return an {@link IStatus} object which
 * can be queried for further information.
 * 
 * @author Simon Goodall
 * 
 */
public class DialogValidationSupport {

	private final IValidationService validationService = Activator.getDefault().getValidationService();

	private final DefaultExtraValidationContext validationContext;
	/**
	 * A validator used to check whether the OK button should be enabled.
	 */
	private final IValidator<EObject> validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);

	private Collection<? extends EObject> validationTargets;

	public DialogValidationSupport(final IExtraValidationContext extraValidationContext) {
		validationContext = new DefaultExtraValidationContext(extraValidationContext, true);

		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
	}

	/**
	 * Helper method to extract messages from an {@link IStatus}.
	 * 
	 * @param status
	 */
	public static String processMessages(final IStatus status) {
		final StringBuilder sb = new StringBuilder();
		processMessages(sb, status);
		return sb.toString();

	}

	private static void processMessages(final @NonNull StringBuilder sb, final @NonNull IStatus status) {
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				if (s != null) {
					processMessages(sb, s);
				}
			}
		} else {
			if (status instanceof DetailConstraintStatusDecorator) {
				final DetailConstraintStatusDecorator decorator = (DetailConstraintStatusDecorator) status;
				sb.append(decorator.getBaseMessage());
			} else {
				sb.append(status.getMessage());
			}
			sb.append("\n");
		}
	}

	public IStatus validate() {
		final Collection<? extends EObject> pVvalidationTargets = validationTargets;
		if (pVvalidationTargets == null) {
			// Should this be OK or CANCEL/ERROR?
			return Status.OK_STATUS;
		}
		return validationService.runValidation(validator, validationContext, pVvalidationTargets, null);
	}

	public DefaultExtraValidationContext getValidationContext() {
		return validationContext;
	}

	public Collection<? extends EObject> getValidationTargets() {
		return validationTargets;
	}

	public void setValidationTargets(final Collection<? extends EObject> validationTargets) {
		this.validationTargets = validationTargets;
	}

	public static void processStatus(@NonNull final Map<Object, Collection<IStatus>> validationErrors, @Nullable final IStatus status) {
		processStatus(status, new DefaultValidationTargetTransformer(), validationErrors);
	}

	public static void processStatus(@Nullable final IStatus status, @NonNull final Map<Object, IStatus> validationErrors) {
		processStatus(status, validationErrors, new DefaultValidationTargetTransformer());
	}

	public static void processStatus(@Nullable final IStatus status, @NonNull final IValiationTargetTransformer vtt, @NonNull final Map<Object, Collection<IStatus>> validationErrors) {
		if (status == null) {
			return;
		}
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				processStatus(s, vtt, validationErrors);
			}
		}
		if (status instanceof IDetailConstraintStatus detailConstraintStatus && !status.isOK()) {
			setStatus(vtt.transform(detailConstraintStatus.getTarget()), validationErrors, status);
			for (final EObject e : detailConstraintStatus.getObjects()) {
				setStatus(vtt.transform(e), validationErrors, status);
			}

		}
	}

	public static void processStatus(@Nullable final IStatus status, @NonNull final Map<Object, IStatus> validationErrors, @NonNull final IValiationTargetTransformer vtt) {
		if (status == null) {
			return;
		}
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				processStatus(s, validationErrors, vtt);
			}
		}
		if ((status instanceof IDetailConstraintStatus detailConstraintStatus) && !status.isOK()) {
			setStatus(vtt.transform(detailConstraintStatus.getTarget()), status, validationErrors);

			for (final EObject e : detailConstraintStatus.getObjects()) {
				setStatus(vtt.transform(e), status, validationErrors);
			}
		}
	}

	private static void setStatus(@Nullable final Object e, @NonNull final Map<Object, Collection<IStatus>> validationErrors, @NonNull final IStatus s) {
		if (e == null) {
			return;
		}
		validationErrors.computeIfAbsent(e, obj -> new HashSet<>()).add(s);
	}

	private static void setStatus(@Nullable final Object e, @NonNull final IStatus s, @NonNull final Map<Object, IStatus> validationErrors) {
		if (e == null) {
			return;
		}
		final IStatus existing = validationErrors.get(e);
		if (existing == null || s.getSeverity() > existing.getSeverity()) {
			validationErrors.put(e, s);
		}
	}

	public static interface IValiationTargetTransformer {
		EObject transform(EObject target);
	}

	public static class DefaultValidationTargetTransformer implements IValiationTargetTransformer {

		@Override
		public EObject transform(final EObject target) {
			return target;
		}

	}
}
