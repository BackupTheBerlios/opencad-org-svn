package org.opencad.export;

import java.util.HashMap;

class BeanDescriptorFactory {
	private final static HashMap<Class<?>, BeanDescriptor> descriptors = new HashMap<Class<?>, BeanDescriptor>();

	public static BeanDescriptor getDescriptor(Class<?> type) {
		if (!descriptors.containsKey(type)) {
			descriptors.put(type, BeanDescriptor.forClass(type));
		}
		return descriptors.get(type);
	}
}