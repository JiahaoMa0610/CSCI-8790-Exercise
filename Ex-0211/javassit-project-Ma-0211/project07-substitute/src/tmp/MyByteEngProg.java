package tmp;

public class MyByteEngProg extends SuperClassLoader {
	public static void main(String[] args) {
		System.out.println("[DBG] Bgn...");
		MyByteEngProg prog = new MyByteEngProg();
		prog.loadClass("MyTarget");
		System.out.println("[Done]");
	}
	@Override
	void loadClass(String clazz) {
		super.loadClass(clazz);
		System.out.println("[DBG] Apply Bytecode Instrument");
	}
}

class SuperClassLoader{
	void loadClass(String clazz) {		
		System.out.println("[DBG] " + clazz);
		findClass(clazz);
	}
	void findClass(String clazz) {
		
	}
}
