package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

	static String TARGET_MY_APP = "";
	static String MOVE_METHOD = "";
	static String DRAW_METHOD = "draw";
	static String INDEX_PARAMETER;
	static String NEW_VALUE;

	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws Throwable {
		ArrayList<String> modified = new ArrayList<String>();
		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out.println("Enter the four inputs: (e.g, ComponentApp,move,1,0, or ServiceApp,fill,2,10)");
				String[] input = UtilMenu.getArguments();
				TARGET_MY_APP = "target." + input[0];
				if (input.length == 4) {
					if (!(modified.contains(input[1]))) {
						modified.add(input[1]);
						MOVE_METHOD = input[1];
						INDEX_PARAMETER = input[2];
						NEW_VALUE = input[3];
						SubstituteMethodBody s = new SubstituteMethodBody();
						Class<?> c = s.loadClass(TARGET_MY_APP);
						Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
						mainMethod.invoke(null, new Object[] { args });
					} else {
						System.out.println("[WRN] This Method '" + input[1] + "' has been modified!!");
					}
				} else {
					System.out.println("[WRN] Invalid input size!!");
				}
				break;
			default:
				break;
			}
		}

	}

	private ClassPool pool;

	public SubstituteMethodBody() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(INPUT_PATH); // "target" must be there.
		System.out.println("[DBG] Class Pathes: " + pool.toString());
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			if (!cc.getName().equals(TARGET_MY_APP)) {
				return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
			}

			cc.instrument(new ExprEditor() {
				public void edit(MethodCall call) throws CannotCompileException {
					String className = call.getClassName();
					String methodName = call.getMethodName();

					if (className.equals(TARGET_MY_APP) && methodName.equals(DRAW_METHOD)) {
						System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: "
								+ call.getLineNumber());
						String block1 = "{" + _L_ //
								+ "System.out.println(\"Before a call to " + methodName + ".\"); " + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "System.out.println(\"After a call to " + methodName + ".\"); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK1: " + block1);
						System.out.println("------------------------");
						call.replace(block1);
					} else if (className.equals(TARGET_MY_APP) && methodName.equals(MOVE_METHOD)) {
						System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: "
								+ call.getLineNumber());
						String block2 = "{" + _L_ //
								+ "System.out.println(\"\tReset param to "+NEW_VALUE+".\"); " + _L_ //
								+ "$" + INDEX_PARAMETER + " = " + NEW_VALUE + "; " + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK2: " + block2);
						System.out.println("------------------------");
						call.replace(block2);
					}
				}
			});
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
}
