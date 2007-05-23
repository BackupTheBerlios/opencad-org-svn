package org.opencad.export;

import java.lang.reflect.Method;

public class BeanField {

	final String name;

	final Method getter, setter;

	public BeanField(final String name, final Method getter, final Method setter) {
		this.name = name;
		this.getter = getter;
		this.setter = setter;
	}
}
