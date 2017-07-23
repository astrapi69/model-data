package de.alpharogroup.model.util;

import java.io.Serializable;

import de.alpharogroup.model.GenericModel;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The class {@link GenericCollectionModel} is the base class for wild card collections that contains
 * object that do not appear to implement the {@link Serializable} interface.
 *
 * @param <T>
 *            the generic type of the model object
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class GenericCollectionModel<T> extends GenericModel<T>
{
	private static final long serialVersionUID = 1L;

	@Override
	public void setObject(T object)
	{
		if (!(object instanceof Serializable))
		{
			object = newSerializableCollectionOf(object);
		}
		super.setObject(object);
	}

	/**
	 * Creates a serializable version of the object. The object is usually a collection.
	 *
	 * @param object
	 *            the object
	 * @return serializable version of <code>object</code>
	 */
	protected abstract T newSerializableCollectionOf(T object);

}