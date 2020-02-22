package newmethod;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class NewMethodExample4MutualRecursiveMethods {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(INPUT_DIR);
         CtClass cc = pool.get("target.Point");

         String block1 = "public abstract int m(int i);";
         System.out.println(block1);
         CtMethod m = CtNewMethod.make(block1, cc);

         String block2 = "public abstract int n(int i);";
         System.out.println(block2);
         CtMethod n = CtNewMethod.make(block2, cc);

         cc.addMethod(m);
         cc.addMethod(n);

         block1 = "{ " + _L_ //
               + "  return n($1); " + _L_ //
               + "}";
         System.out.println(block1);
         m.setBody(block1);

         block2 = "{ " + _L_ //
               + "  return $1; " + _L_ //
               + "}";
         System.out.println(block2);
         n.setBody(block2);
         cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);

         cc.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}
