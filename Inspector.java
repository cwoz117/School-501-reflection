
import java.lang.reflect.*;

public class Inspector {
	
	private Method[] methods;
	private Field[] fields;
	private Class inspected;
	private Class parent;
	
	public Inspector(){
		parent = null;
		methods = null;
		fields = null;
		inspected = null;
	}
	
	public void inspect(Object obj, boolean recursive){

		if (obj == null){
			throw new IllegalArgumentException("The object is null, nothing to inspect");
		}
		
		// Get declaring class
		inspected = obj.getClass();
		
		// Name of immediate superclass
		parent = inspected.getSuperclass();
		// Name of interface(s)
		
		// Declared Methods, including
		
			// Exceptions
		
			// parameters
		
			// return
		
			// modifiers
		
		// Constructors
		
			// Parameters
		
			// Modifiers
		
		// Fields
			
			// Type
		
			// modifiers
		
		// Field values (pointer values in unsigned int's for objects if recursive is false.
		
	}
	
	private void inspectHelper(){
		
	}
	
}
