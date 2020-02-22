package newfield;

import java.io.File;
import newfield.UtilMenu;
import java.lang.reflect.Proxy;
import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;

public class AnnotatedFieldExample3 {
	static String workDir = System.getProperty("user.dir");
	static String inputDir = workDir + File.separator + "classfiles";
	static String outputDir = workDir + File.separator + "output";
	static String target_app = "";
	static String first_anno = "target.Column";
	static String second_anno = "target.Author";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out
						.println("Enter the four inputs: (e.g, ComponentApp,Column,Author, or ServiceApp,Row,Author)");
				String[] input = UtilMenu.getArguments();
				try {
					if (input.length == 3) {
						first_anno = "target." + input[1];
						second_anno = "target." + input[2];
						ClassPool pool = ClassPool.getDefault();
						pool.insertClassPath(inputDir);

						CtClass ct = pool.get("target." + input[0]);
						CtField[] all_fields = ct.getFields();
						for (CtField cf : all_fields) {
							process(cf.getAnnotations());

						}
					}
					else {
						System.out.println("[WRN] Invalid input size!");
					}

					// process(ct.getAnnotations()); System.out.println();

				} catch (NotFoundException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}

	static void process(Object[] annoList)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Annotation annos = null;
		for (int i = 0; i < annoList.length - 1; i++) {
			//System.out.println(annoList[i].getClass().getName());
			Annotation annotation = getAnnotation(annoList[i]);
	         if (annotation.getTypeName().equals(first_anno)) {
	        	 for (int j = 0; j < annoList.length; j++) {
	        		 Annotation temp_anno = getAnnotation(annoList[j]);
	                 if (temp_anno.getTypeName().equals(second_anno)) {
	                	 annos = temp_anno;
	                 }
	        		 
	        	 }
	             
	          }
		}
	      if (!(annos == null)) {
	      	 showAnnotation(annos);
	       }
	}

	static Annotation getAnnotation(Object obj) {
		// Get the underlying type of a proxy object in java
		AnnotationImpl annotationImpl = //
				(AnnotationImpl) Proxy.getInvocationHandler(obj);
		return annotationImpl.getAnnotation();
	}

	static void showAnnotation(Annotation annotation) {
		Iterator<?> iterator = annotation.getMemberNames().iterator();
		String blk = "";
		while (iterator.hasNext()) {
			Object keyObj = (Object) iterator.next();
			MemberValue value = annotation.getMemberValue(keyObj.toString());
			if (iterator.hasNext()) {
				blk += keyObj + ": " + value.toString().replaceAll("^\"|\"$", "") + ", ";
			} else {
				blk += keyObj + ": " + value;
			}
		}
		System.out.println(blk);

	}
}
