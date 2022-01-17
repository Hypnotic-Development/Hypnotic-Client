/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class EventManager {
	private Map<Class<? extends Event>, ArrayHelper<Data>> REGISTRY_MAP = new HashMap<Class<? extends Event>, ArrayHelper<Data>>();
	public static EventManager INSTANCE = new EventManager();

	public void register(Object o) {

		for (Method method : o.getClass().getDeclaredMethods()) {
			if (!isMethodBad(method)) {
				register(method, o);
			}
		}
	}

	public void register(Object o, Class<? extends Event> clazz) {
		for (Method method : o.getClass().getDeclaredMethods()) {
			if (!isMethodBad(method, clazz)) {
				register(method, o);
			}
		}
	}

	@SuppressWarnings({"unchecked" })
	private void register(Method method, Object o) {
		Class<?> clazz = method.getParameterTypes()[0];
		final Data methodData = new Data(o, method, method.getAnnotation(EventTarget.class).value());

		methodData.target.setAccessible(true);

		if (REGISTRY_MAP.containsKey(clazz)) {
			if (!REGISTRY_MAP.get(clazz).contains(methodData)) {
				REGISTRY_MAP.get(clazz).add(methodData);
				sortListValue((Class<? extends Event>) clazz);
			}
		} else {
			REGISTRY_MAP.put((Class<? extends Event>) clazz, new ArrayHelper<Data>() {
				{
					this.add(methodData);
				}
			});
		}
	}

	public void unregister(final Object o) {

		for (ArrayHelper<Data> flexibalArray : REGISTRY_MAP.values()) {
			for (Data methodData : flexibalArray) {
				if (methodData.source.equals(o)) {
					flexibalArray.remove(methodData);
				}
			}
		}

		cleanMap(true);
	}

	public void unregister(final Object o, final Class<? extends Event> clazz) {
		if (REGISTRY_MAP.containsKey(clazz)) {
			for (final Data methodData : REGISTRY_MAP.get(clazz)) {
				if (methodData.source.equals(o)) {
					REGISTRY_MAP.get(clazz).remove(methodData);
				}
			}

			cleanMap(true);
		}
	}

	public void cleanMap(boolean b) {

		Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();

		while (iterator.hasNext()) {
			if (!b || iterator.next().getValue().isEmpty()) {
				iterator.remove();
			}
		}
	}

	public void removeEnty(Class<? extends Event> clazz) {

		Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();

		while (iterator.hasNext()) {
			if (iterator.next().getKey().equals(clazz)) {
				iterator.remove();
				break;
			}
		}
	}

	private void sortListValue(Class<? extends Event> clazz) {

		ArrayHelper<Data> flexibleArray = new ArrayHelper<Data>();

		for (byte b : Priority.VALUE_ARRAY) {
			for (Data methodData : REGISTRY_MAP.get(clazz)) {
				if (methodData.priority == b) {
					flexibleArray.add(methodData);
				}
			}
		}

		REGISTRY_MAP.put(clazz, flexibleArray);
	}

	private boolean isMethodBad(final Method method) {

		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
	}

	private boolean isMethodBad(Method method, Class<? extends Event> clazz) {
		return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
	}

	public ArrayHelper<Data> get(final Class<? extends Event> clazz) {
		return REGISTRY_MAP.get(clazz);
	}

	public void shutdown() {
		REGISTRY_MAP.clear();
	}

}

