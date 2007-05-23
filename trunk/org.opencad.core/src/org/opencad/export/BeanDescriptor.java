package org.opencad.export;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

class BeanDescriptor {
	HashSet<BeanField> fields = new HashSet<BeanField>();

	static boolean isGetter(Method method) {
		return isEtter(method, "get");
	}

	static boolean isSetter(Method method) {
		return isEtter(method, "set");
	}

	static String etterName(Method method) {
		return String.format("%s%s", method.getName().substring(3, 4)
				.toLowerCase(), method.getName().substring(4));
	}

	static boolean isEtter(Method method, String type) {
		if (method.getName().length() < 4) {
			return false;
		}
		String capital = method.getName().substring(3, 4);
		return method.getName().startsWith(type)
				&& capital.equals(capital.toUpperCase());
	}

	public boolean add(BeanField field) {
		return fields.add(field);
	}

	public boolean add(String name, Method getter, Method setter) {
		return fields.add(new BeanField(name, getter, setter));
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (BeanField beanField : fields) {
			sb.append(beanField.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public static BeanDescriptor forClass(Class<?> type) {
		BeanDescriptor beanDescriptor = new BeanDescriptor();
		HashMap<String, Method> getters = new HashMap<String, Method>();
		HashMap<String, Method> setters = new HashMap<String, Method>();
		for (Method method : type.getMethods()) {
			if (isGetter(method)) {
				getters.put(etterName(method), method);
			} else if (isSetter(method)) {
				setters.put(etterName(method), method);
			}
		}
		for (String field : getters.keySet()) {
			if (setters.containsKey(field)) {
				beanDescriptor.add(field, getters.get(field), setters
						.get(field));
			}
		}
		return beanDescriptor;
	}
}