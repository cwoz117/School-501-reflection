
import java.lang.reflect.*;
import java.util.*;

public class Inspector {
	private final static String LINEDIVIDE = "------------------------------------\n";
	private List<Class<?>> storedObj;

	private Object[] getAllDeclaredFields(Object obj){

		if (obj == null){
			return null;
		}
		List<Object> ary = new ArrayList<Object>();
		Class<?> cls = obj.getClass();

		Field[] f;
		while (cls != null){
			f = cls.getDeclaredFields();
			for (int i = 0; i < f.length; i++){
				ary.add(f[i]);
			}
			cls = cls.getSuperclass();
		}
		return ary.toArray();
	}
	
	public void inspect(Object obj, boolean recursive){

		if (obj == null){
			throw new IllegalArgumentException("The object is null, nothing to inspect");
		}
		storedObj = new ArrayList<Class<?>>();
		storedObj.add(obj.getClass());

		if (recursive){
			Object[] scrapedFields = getAllDeclaredFields(obj);
			for (int i = 0; i < scrapedFields.length; i++) {
				Class<?> var = scrapedFields.getClass();
				if ((!var.isInstance(Field.class)) && recursive){
					if (!storedObj.contains(var)){
						storedObj.add(var);
					}
				}
			}
		}
		for (Class<?> c: storedObj){
			System.out.println(formattedOutput(c));
		}
	}
	private String formattedOutput(Class<?> c){
		String s = "";
		
		// Class header info
		s += "Class: " + c.getSimpleName() + "\n";
		s += "Super: " + c.getSuperclass().getName() + "\n";
		Class<?>[] iface = c.getInterfaces();
		for (int i = 0; i < iface.length; i++){
			s += "Iface: " + iface[i].getName() + "\n";
		}
		
		// Field Info
		s += formattedFields(c);
		s += formattedConstructor(c);
		s += formattedMethod(c);

		return s;
	}
	
	private String formattedFields(Class<?> c){
		String s = "";
		Field[] f = c.getDeclaredFields();
		Field.setAccessible(f, true);
		
		for (int i = 0; i < f.length; i++){

			s += LINEDIVIDE;
			s += "Field: " + "\tname:\t" + f[i].getName() + "\n";
			try {
				if (f[i].getType().isPrimitive()){
					Object classInstance = f[i].get(c.newInstance());
					s += "\ttype:\t" + classInstance.getClass().getTypeName() + "\n";
					s += "\tval:\t" + classInstance.toString() + "\n";
					s += "\tmod:\t" + Modifier.toString(classInstance.getClass().getModifiers()) + "\n";
				} else if(f[i].getType().isArray()){
					int aryIndex = Array.getLength(f[i].get(c.newInstance()));
					Object[] classInstance = (Object[]) Array.newInstance(f[i].getType(), aryIndex);
					s += "\ttype:\t" + classInstance.getClass().getName() + "\n\tval:\t";
					for (int j = 0; j < aryIndex; j++){
						if (j%4 == 0 && j != 0){
							s += "\n\t\t";
						}
						if (classInstance[i] == null){
							s += "null ";
						}else {
							s += classInstance[i].toString() +" ";
						}
					}
					s += "\n\tmod:\t" + Modifier.toString(classInstance.getClass().getModifiers());
				} else {
					Object classInstance = f[i].get(c.newInstance());
					s += "\ttype:\t" + f[i].getType() + "\n";
					if (classInstance != null){
						s += "\tval:\t" + classInstance.toString() + "\n";
					} else {
						s += "\tval:\t" + "null" + "\n";
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		return s + "\n";
	}
	
	private String formattedMethod(Class<?> c){
		Method[] m = c.getDeclaredMethods();
		Class<?>[] exceptions;
		Class<?>[] parameters;
		String s = "";
		int i, j;
		
		for (i = 0; i < m.length; i++){
			s += LINEDIVIDE;
			s += "method: " + m[i].getName() + "\n";
			
			s += "\tExceptions:\n";
			exceptions = m[i].getExceptionTypes();
			for(j = 0; j < exceptions.length; j++){
				s += "\t\t" + exceptions[j].getName() + "\n";
			}
			
			s += "\tParameters:\n";
			parameters = m[i].getParameterTypes();
			for(j = 0; j < parameters.length; j++){
				s += "\t\t" + parameters[j].getName() + "\n";
			}
			
			s += "\tModifier: ";
			s += Modifier.toString(m[i].getModifiers()) + "\n";

		}

		return s;
	}
	private String formattedConstructor(Class<?> c){
		Class<?>[] parameters;
		String s = "";
		int i, j;

		// Constructor info
		Constructor<?>[] con = c.getConstructors();
		for (i = 0; i < con.length; i++){
			s += LINEDIVIDE;
			s += "Constructor: " + con[i].getName() + "\n";
			
			s += "             Parameters: ";
			parameters = con[i].getParameterTypes();
			if (parameters.length == 0){s+= "N/A\n";}
			for(j = 0; j < parameters.length; j++){
				if (j == 0){
					s += parameters[j].getName() + "\n";
				}
				s += "                         " + parameters[j].getName() + "\n";
			}
			
			s += "             Modifier: ";
			s += Modifier.toString(con[i].getModifiers()) + "\n";
		}
		return s;
	}

}
