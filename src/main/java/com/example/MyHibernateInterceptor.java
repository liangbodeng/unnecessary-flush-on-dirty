package com.example;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("unused")
public class MyHibernateInterceptor extends EmptyInterceptor {

	@Override
	public boolean onFlushDirty(
			Object entity,
			Serializable id,
			Object[] currentState,
			Object[] previousState,
			String[] propertyNames,
			Type[] types
	) {
		System.out.println("onFlushDirty invoked on a " + entity.getClass().getSimpleName());

		if (entity instanceof User) {
			int versionIndex = Arrays.asList(propertyNames).indexOf("version");
			currentState[versionIndex] = (int) (currentState[versionIndex]) + 1;
			return true;
		}

		return false;
	}
}
