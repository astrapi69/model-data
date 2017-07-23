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
import java.util.List;

import de.alpharogroup.model.api.Model;

/**
 * Based on <code>Model</code> but for lists of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside list
 */
public class WildcardListModel<T> extends GenericCollectionModel<List<T>>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates empty model
	 */
	public WildcardListModel()
	{
	}

	/**
	 * Creates model that will contain <code>list</code>.
	 *
	 * @param list the list
	 */
	public WildcardListModel(final List<T> list)
	{
		setObject(list);
	}

	/** {@inheritDoc} */
	@Override
	protected List<T> newSerializableCollectionOf(final List<T> object)
	{
		if (object == null)
		{
			return null;
		}
		return new ArrayList<>(object);
	}

	/**
	 * Factory method for models that contain lists. This factory method will automatically rebuild
	 * a nonserializable <code>list</code> into a serializable one.
	 *
	 * @param <C>
	 *            model type
	 * @param list
	 *            The List, which may or may not be Serializable
	 * @return A Model object wrapping the List
	 */
	public static <C> Model<List<C>> ofList(final List<C> list)
	{
		return new WildcardListModel<>(list);
	}
}