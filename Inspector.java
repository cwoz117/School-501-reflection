
import java.lang.reflect.*;
import java.util.*;

public class Inspector {
	private final static String CLASSSPACE = "============================================\n";
	private final static String LINEDIVIDE = "--------------------------------\n";
	private List<Class<?>> storedObj;
	
	public Inspector(){
		storedObj = new ArrayList<Class<?>>();
	}
	private Object[] getAllDeclaredFields(Object obj){
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
		
		storedObj.add(obj.getClass());
		Object[] scrapedFields = getAllDeclaredFields(obj);
		for (int i = 0; i < scrapedFields.length; i++) {
			Class<?> var = scrapedFields.getClass();
			if ((!var.isInstance(Field.class)) && recursive){
				if (!storedObj.contains(var)){
					storedObj.add(var);
				}
			}
		}
		for (Class<?> c: storedObj){
			System.out.println(formattedOutpuet(c));
		}
	}
	private String formattedOutpuet(Class<?> c){
		String s = "";
		
		s += CLASSSPACE;
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
			if (f[i].getType().isPrimitive()){
				s += "\ttype:\t" + f[i].getType() + "\n";
				try {
					Object val = f[i].get(c);
					s += "\tval:\t" + val.toString() + "\n";
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				s += "\tmod:\t" + "\n";
			} else if(f[i].getType().isArray()){
				s += "\ttype:\t" + f[i].getType() + "\n";
				s += "\tval:\t" + "\n";
				s += "\tmod:\t" + "\n";
			} else {
				s += "\ttype:\t" + f[i].getType() + "\n";
				s += "\tval:\t" + "\n";
				s += "\tmod:\t" + "\n";
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
			s += m[i].getModifiers() + "\n";

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
			s += con[i].getModifiers() + "\n";
		}
		return s;
	}

}
