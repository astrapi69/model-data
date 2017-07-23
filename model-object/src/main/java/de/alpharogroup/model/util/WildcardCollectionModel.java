/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.alpharogroup.model.util;

import java.util.ArrayList;
import java.util.Collection;

import de.alpharogroup.model.api.Model;


/**
 * Based on <code>Model</code> but for any collections of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside collection
 */
public class WildcardCollectionModel<T> extends GenericCollectionModel<Collection<T>>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates empty model
	 */
	public WildcardCollectionModel()
	{
	}

	/**
	 * Creates model that will contain <code>collection</code>.
	 *
	 * @param collection the collection
	 */
	public WildcardCollectionModel(final Collection<T> collection)
	{
		setObject(collection);
	}

	/** {@inheritDoc} */
	@Override
	protected Collection<T> newSerializableCollectionOf(final Collection<T> object)
	{
		return new ArrayList<>(object);
	}

	/**
	 * Factory method for models that contain collections. This factory method will automatically
	 * rebuild a nonserializable <code>collection</code> into a serializable {@link ArrayList}.
	 *
	 * @param <C>
	 *            model type
	 * @param collection
	 *            The Collection, which may or may not be Serializable
	 * @return A Model object wrapping the Set
	 */
	public static <C> Model<Collection<C>> of(final Collection<C> collection)
	{
		return new WildcardCollectionModel<>(collection);
	}
}