

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class InspectorTest {

	@Test
	public void testNullDeclaredFields() {
		Inspector i = new Inspector();
		Class<?> inspectMeta = i.getClass();
		try {
			Method getDeclaredField = inspectMeta.getDeclaredMethod("getAllDeclaredFields", Object.class);
			getDeclaredField.setAccessible(true);
			Object out = null;
			Object o = getDeclaredField.invoke(i, out);
			if (o != null){
				fail("Object is not null");
			}
		} catch (NoSuchMethodException e) {
			fail("could not find an existing method");
		} catch (SecurityException e) {
			fail("Security the existing method");
		} catch (IllegalAccessException e) {
			fail("Illegal access the existing method");
		} catch (IllegalArgumentException e) {
			fail("illegal argument");
		} catch (InvocationTargetException e) {
			fail("Could not invoke known good method");
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPassedNullValue(){
		Inspector i = new Inspector();
		i.inspect(null, true);
	}
}