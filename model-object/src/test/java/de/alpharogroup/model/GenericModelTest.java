package de.alpharogroup.model;


import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

public class GenericModelTest
{

	@Test
	public void testSetInnerProperty()
	{
		final Employee employee = Employee.builder().person(Person.builder().name("bar").build())
			.build();
		final PropertyModel<String> model = new PropertyModel<>(employee, "person.name");
		model.setObject("foo");
		String expected = employee.getPerson().getName();
		String actual = model.getObject();
		AssertJUnit.assertEquals(expected, actual);
	}

}
