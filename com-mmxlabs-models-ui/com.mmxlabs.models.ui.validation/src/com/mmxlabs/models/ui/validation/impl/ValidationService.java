/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.impl;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;

/**
 * A helper class for running validations which lets you pass extra data into the validation. Validation constraints can access the extra data using {@link IValidationInputService}, and so long as the
 * validation has been run through a helper class like this they will be able to access the right copy.
 * 
 * @author hinton
 * 
 */
public class ValidationService implements IValidationService {
	private static final Logger log = LoggerFactory.getLogger(ValidationService.class);

	private ValidationInputService validationInputService = new ValidationInputService();

	private ThreadPoolExecutor executor;

	public ValidationService() {

		// Previously we used Executors.newSingleThreadExecutor() to create our pool. However this tended to result in a memory leak with the ThreadLocals pool resulting out of EMF Validation. This
		// implementation instead is permitted to kill off the thread when inactive thus freeing up the thread local pool, but still maintain the maximum of one thread.
		executor = new ThreadPoolExecutor(1, 1, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()) {
			@Override
			protected void finalize() {
				shutdown();
			};
		};
		executor.allowCoreThreadTimeOut(true);
	}

	public void shutdown() {
		executor.shutdown();
	}

	@Override
	public IStatus runValidation(final IValidator<EObject> validator, final IExtraValidationContext extraContext, final Collection<? extends EObject> targets) {
		final FutureTask<IStatus> validationTask = new FutureTask<IStatus>(new Callable<IStatus>() {
			@Override
			public IStatus call() throws Exception {
				try {
					validationInputService.setExtraContext(extraContext);
					return validator.validate(targets);
				} catch (Throwable t) {
					t.printStackTrace();
					throw t;
				} finally {
					validationInputService.setExtraContext(null);
				}
			}
		});

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
			// executor.shutdownNow();
		}
	}

	@Override
	public IExtraValidationContext getExtraContext() {
		return validationInputService.getExtraContext();
	}
}
