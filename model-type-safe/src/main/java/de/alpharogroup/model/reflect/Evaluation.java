/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.alpharogroup.model.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import de.alpharogroup.model.reflect.IProxyFactory.Callback;
import de.alpharogroup.model.util.Objects;
import lombok.extern.java.Log;

/**
 * An evaluation of method invocations.
 *
 * @param R
 *            result type
 *
 * @author svenmeier
 */
@Log
@SuppressWarnings("rawtypes")
public class Evaluation<R> implements Callback
{

	/**
	 * If not null containing the last invocation result which couldn't be proxied (i.e. is was
	 * primitive or final).
	 *
	 * @see #proxy()
	 */
	private static final ThreadLocal<Evaluation<?>> lastNonProxyable = new ThreadLocal<>();

	/**
	 * The factory for proxies.
	 */
	public static IProxyFactory proxyFactory = new CachingProxyFactory(new DefaultProxyFactory());

	/**
	 * Reverse operation of {@link #proxy()}, i.e. get the evaluation from an evaluation result.
	 *
	 * @param <R>
	 *            the generic type
	 * @param result
	 *            invocation result
	 * @return evaluation
	 */
	@SuppressWarnings("unchecked")
	public static <R> Evaluation<R> eval(R result)
	{
		Evaluation<R> evaluation = (Evaluation<R>)proxyFactory.getCallback(result);
		if (evaluation == null)
		{
			evaluation = (Evaluation<R>)lastNonProxyable.get();
			lastNonProxyable.remove();
			if (evaluation == null)
			{
				throw new RuntimeException("no invocation result given");
			}
		}
		return evaluation;
	}

	/**
	 * Start evaluation from the give type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param type
	 *            starting type
	 * @return proxy
	 */
	@SuppressWarnings("unchecked")
	public static <T> T of(Class<T> type)
	{
		return (T)new Evaluation(type).proxy();
	}

	/**
	 * Each invoked method followed by its arguments.
	 */
	public final List<Object> stack = new ArrayList<>();

	private Type type;

	/**
	 * Evaluation of method invocations on the given type.
	 *
	 * @param type
	 *            starting type
	 */
	public Evaluation(Type type)
	{
		this.type = type;
	}

	/**
	 * Handle an invocation on a result proxy.
	 *
	 * @return proxy for the invocation result
	 *
	 * @see #proxy()
	 */
	@Override
	public Object on(Object obj, Method method, Object[] parameters) throws Throwable
	{
		if ("finalize".equals(method.getName()))
		{
			super.finalize();
			return null;
		}

		stack.add(method);

		for (Object param : parameters)
		{
			if (param == null)
			{
				// could be a non-proxyable nested evaluation
				Evaluation evaluation = lastNonProxyable.get();
				if (evaluation != null)
				{
					lastNonProxyable.remove();
					stack.add(evaluation);
					continue;
				}
			}

			stack.add(param);
		}

		type = Reflection.resultType(type, method.getGenericReturnType());
		if (type == null)
		{
			log.log(Level.FINE, "falling back to raw type for method {}", method);
			type = method.getReturnType();
		}

		return proxy();
	}

	/**
	 * Create a proxy for the current type.
	 * <p>
	 * If the result cannot be proxied, it is accessible via {@link #lastNonProxyable}.
	 *
	 * @return proxy or {@code null} if invocation result cannot be proxied
	 */
	@SuppressWarnings("unchecked")
	public Object proxy()
	{
		Class clazz = Reflection.getClass(type);

		if (clazz.isPrimitive())
		{
			lastNonProxyable.set(this);

			return Objects.convertValue(null, clazz);
		}

		Class proxyClass = proxyFactory.createClass(clazz);
		if (proxyClass == null)
		{
			lastNonProxyable.set(this);

			return null;
		}

		return proxyFactory.createInstance(proxyClass, this);
	}

}