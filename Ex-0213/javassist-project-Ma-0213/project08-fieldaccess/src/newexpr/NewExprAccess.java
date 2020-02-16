package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static String TARGET_MY_APP2 = "";
	static int NumOfFields;
	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws Throwable {
		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out.println("Enter the two inputs: (e.g., target.ComponentApp,1 or target.ServiceApp,100)");
				String[] input = UtilMenu.getArguments();
				if (input.length == 2) {
					TARGET_MY_APP2 = input[0];
					NumOfFields = Integer.parseInt(input[1]);
					NewExprAccess s = new NewExprAccess();
					Class<?> c = s.loadClass(TARGET_MY_APP2);
					Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
					mainMethod.invoke(null, new Object[] { args });
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

	public NewExprAccess() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(CLASS_PATH); // TARGET must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(NewExpr newExpr) throws CannotCompileException {
					try {
						String longName = newExpr.getConstructor().getLongName();
						if (longName.startsWith("java.")) {
							return;
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					String log = String.format("[Edited by ClassLoader] new expr: %s, " //
							+ "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
							newExpr.getLineNumber(), newExpr.getSignature());
					System.out.println(log);

					CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
					String all_block = "{ " + _L_ + "  $_ = $proceed($$);";
					if (NumOfFields >= fields.length) {

						for (int i = 0; i < fields.length; i++) {
							String filedName = fields[i].getName();
							String fieldType = null;
							try {
								fieldType = fields[i].getType().getName();
							} catch (NotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							all_block += "  { " + _L_ //
									+ "    String cName = $_.getClass().getName();" + _L_ //
									+ "    String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_//
									+ "    String fieldFullName = cName + " + "\".\"" + " + fName;" + _L_ + "    "
									+ fieldType + " fieldValue = $_." + filedName + ";" + _L_
									+ "    System.out.println(\"  [Instrument] \" + fieldFullName + \": \" + fieldValue);"
									+ _L_ //
									+ "  }" + _L_;
							// System.out.println(all_block[i]);

						}
					} else {
						for (int i = 0; i < NumOfFields; i++) {
							String filedName = fields[i].getName();
							String fieldType = null;
							try {
								fieldType = fields[i].getType().getName();
							} catch (NotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							all_block += "  { " + _L_ //
									+ "    String cName = $_.getClass().getName();" + _L_ //
									+ "    String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_//
									+ "    String fieldFullName = cName + " + "\".\"" + " + fName;" + _L_ + "    "
									+ fieldType + " fieldValue = $_." + filedName + ";" + _L_
									+ "    System.out.println(\"  [Instrument] \" + fieldFullName + \": \" + fieldValue);"
									+ _L_ //
									+ "  }" + _L_;
						}
					}
					all_block += "}" + _L_;
					System.out.println(all_block);
					newExpr.replace(all_block);
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