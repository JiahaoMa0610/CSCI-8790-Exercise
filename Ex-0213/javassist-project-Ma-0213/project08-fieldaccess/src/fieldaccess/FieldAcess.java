package fieldaccess;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FieldAcess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";
   static final String TARGET_MY_APP = "target.MyAppField";
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
      FieldAcess s = new FieldAcess();
      Class<?> c = s.loadClass(TARGET_MY_APP);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
   }

   private ClassPool pool;

   public FieldAcess() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(FieldAccess f) throws CannotCompileException {
               if (!f.getClassName().equals(TARGET_MY_APP) || !f.where().getMethodInfo().isMethod()) {
                  return;
               }
               String fieldName = f.getFieldName();
               System.out.println("[Edited by ClassLoader] Edit field " + fieldName + " (line: " + f.getLineNumber() + ")");
               String block1 = "{ " + _L_ //
                     + " $0." + fieldName + " = 0;" + _L_ + "  System.out.println(\"" //
                     + fieldName + " " + "value: \" + $0." + fieldName + " + " + "\", " //
                     + "type: \" + " + "$type); " + _L_ //
                     + "  $_ = $proceed($$); " + _L_ //
                     + " System.out.println(\"Resulting value:\" + $_);" + _L_ + "}";
               System.out.println("[DBG] BLOCK1: " + block1);
               f.replace(block1);
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