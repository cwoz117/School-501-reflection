
public class MyTest {

	public static void main(String[] args){
		ClassB a;
		try {
			a = new ClassB();
			
			Inspector insp = new Inspector();
			insp.inspect(a, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
