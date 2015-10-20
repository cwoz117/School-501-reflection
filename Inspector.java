
import java.lang.reflect.*;
import java.util.*;

public class Inspector {
	private List<Class<?>> storedObj;
	
	public Inspector(){
		storedObj = new ArrayList<Class<?>>();
	}
	
	public void inspect(Object obj, boolean recursive){

		if (obj == null){
			throw new IllegalArgumentException("The object is null, nothing to inspect");
		}
		
		storedObj.add(obj.getClass());
		Field[] f = getAllDeclaredFields(obj);
		for (int i = 0; i < f.length; i++) {
			if ((recursive) && (!f[i].getType().isPrimitive()) && (!f[i].getType().isArray())){
				if (storedObj.contains(f[i].getDeclaringClass())) {
					storedObj.add(f[i].getType());
				}
			}
		}
		
		for (Class<?> c: storedObj){
			System.out.println(outString(c));
		}
	}
	private String outString(Class<?> c){
		String s = "";
		Class[] exceptions;
		Class[] parameters;
		Method[] m = c.getDeclaredMethods();
		int i, j;
		
		// Class header info
		s += "Class: " + c.getName() + "\n";
		s += "Super: " + c.getSuperclass() + "\n";
		Class<?>[] iface = c.getInterfaces();
		for (i = 0; i < iface.length; i++){
			s += "Iface: " + iface[i].getName() + "\n";
		}
		
		// Constructor info
		Constructor<?>[] con = c.getConstructors();
		for (i = 0; i < con.length; i++){
			s += "Constructor: " + con[i].getName() + "\n";
			
			s += "\tParameters:\n";
			parameters = con[i].getParameterTypes();
			for(j = 0; j < parameters.length; j++){
				s += "\t\t" + parameters[j].getName() + "\n";
			}
			
			s += "\tModifiers\n";
			s += "\t\t" + con[i].getModifiers() + "\n";
		}
		
		// Field Info
		
		
		
		// Method info
		for (i = 0; i < m.length; i++){
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
			
			s += "\tModifiers\n";
			s += "\t\t" + m[i].getModifiers() + "\n";
		}

		return s;
	}
	
	private Field[] getAllDeclaredFields(Object obj){
		List<Field> ary = new ArrayList<Field>();
		Class<?> cls = obj.getClass();
		Field[] f;
		while (cls != null){
			f = cls.getDeclaredFields();
			for (int i = 0; i < f.length; i++){
				ary.add(f[i]);
			}
			cls = cls.getSuperclass();
		}
		return ((Field[]) ary.toArray());
	}
}
