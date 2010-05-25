package com.mmxlabs.common.recorder;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;

/**
 * Class to generate proxy implementations of an object to read/write method
 * invocations to a file. A number of constraints exist. 1) All methods must be
 * defined within an interface. 2) All objects used must be created by the
 * interface. I.e. all constructor and setters should be interface methods.
 * Otherwise the recorder will not see them. **** TODO: We could proxy the
 * object? ** Primitive types are the only exclusions
 * 
 * @author Simon Goodall
 * 
 */
public final class InterfaceRecorder {

	@SuppressWarnings("unchecked")
	public <T> T createRecorder(Class<T> clz, T object, OutputStream os,
			String lastMethod) throws Exception {

		return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
				new Class[] { clz }, new RecorderInvocationHandler(clz, object,
						os, lastMethod));
	}

	public <T> void replay(T object, InputStream is, String lastMethod)
			throws Exception {

		XmlStreamReader<T> reader = new XmlStreamReader<T>(object, is);
		reader.parse();
	}
}
