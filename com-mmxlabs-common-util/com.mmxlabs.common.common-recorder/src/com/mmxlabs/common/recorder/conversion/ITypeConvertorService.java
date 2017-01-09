/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion;

/**
 * Interface definining a service to convert between Objects and String representations e.g. during an XML I/O operation.
 * 
 * @author Simon Goodall
 * 
 */
public interface ITypeConvertorService {

	/**
	 * Register a {@link ITypeConvertor} with this service
	 * 
	 * @param convertor
	 */
	public void registerTypeConvertor(final ITypeConvertor<?> convertor);

	/**
	 * Unregister a {@link ITypeConvertor} with this service
	 * 
	 * @param convertor
	 */
	public void unregisterTypeConvertor(final String className);

	/**
	 * Converts the {@link String} representation of a {@link Class} (fully-qualified class name specified) to an Object using an appropriate {@link ITypeConvertor} previously registered.
	 * 
	 * @param className
	 * @param value
	 * @return
	 */
	public Object toObject(final String className, final String value);

	/**
	 * Converts the Object into a {@link String} representation of a {@link Class} (fully-qualified class name specified) using an appropriate {@link ITypeConvertor} previously registered.
	 * 
	 * @param className
	 * @param value
	 * @return
	 */
	public String toString(final String className, final Object object);

}