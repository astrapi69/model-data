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


/**
 * Based on <code>Model</code> but for any collections of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside collection
 */
public class CollectionModel<T> extends GenericCollectionModel<Collection<T>>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates empty model
	 */
	public CollectionModel()
	{
	}

	/**
	 * Creates model that will contain <code>collection</code>.
	 *
	 * @param collection the collection
	 */
	public CollectionModel(final Collection<T> collection)
	{
		setObject(collection);
	}


	/** {@inheritDoc} */
	@Override
	protected Collection<T> newSerializableCollectionOf(final Collection<T> object)
	{
		return new ArrayList<>(object);
	}
}