package newmethod;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class NewMethodExample3 {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(INPUT_DIR);
         CtClass cc = pool.get("target.Point");

         CtMethod newMethod = new CtMethod(CtClass.intType, "shift", new CtClass[] { CtClass.intType }, cc);
         cc.addMethod(newMethod);
         String block1 = "{ " + _L_ //
               + "  x += $1; " + _L_ //
               + "  return x;" + _L_ //
               + "}";
         System.out.println(block1);
         newMethod.setBody(block1);
         // Since Javassist makes a class abstract if an abstract method is added to the class,
         // you have to explicitly change the class back to a non-abstract one after calling setBody().
         cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);
         cc.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}
