/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.validation.internal.Activator;

/**
 * A helper class for running validations which lets you pass extra data into the validation. Validation constraints can access the extra data using {@link IValidationInputService}, and so long as the
 * validation has been run through a helper class like this they will be able to access the right copy.
 * 
 * @author hinton
 * 
 */
public class ValidationHelper {
	private static final Logger log = LoggerFactory.getLogger(ValidationHelper.class);

	public IStatus runValidation(final IValidator<EObject> validator, final IExtraValidationContext extraContext, final Collection<? extends EObject> targets) {
		final FutureTask<IStatus> validationTask = new FutureTask<IStatus>(new Callable<IStatus>() {
			@Override
			public IStatus call() throws Exception {
				try {
					Activator.getDefault().getInputService().setExtraContext(extraContext);
					return validator.validate(targets);
				} finally {
					Activator.getDefault().getInputService().setExtraContext(null);
				}
			}
		});

		// Validation should run in it's own thread - separate to any other validation task, so create an executor to manage the thread.
		final ExecutorService executor = Executors.newSingleThreadExecutor();

		try {
			// Submit a task to the executor
			executor.execute(validationTask);

			// Block until there is a result to return
			return validationTask.get();
		} catch (final InterruptedException e) {
			log.error("Interrupted validating " + targets, e);
			return null;
		} catch (final ExecutionException e) {
			log.error("Error excuting validation on " + targets, e);
			return null;
		} finally {
			// Force executor shutdown
			executor.shutdownNow();
		}
	}
}
