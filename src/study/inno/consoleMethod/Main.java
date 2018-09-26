package study.inno.consoleMethod;

//import org.apache.commons.jci.compilers.JavaCompiler;
//import org.apache.commons.jci.compilers.JavaCompilerFactory;
//import org.apache.commons.jci.compilers.JavaCompilerSettings;

import com.sun.webkit.network.URLs;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private final static String beginOfClass = "public class AutoMakedClass {\r\n    public void doWork(){\r\n";
    private final static String endOfClass = "\r\n    }\r\n}\r\n";
    private final static String sourceFileName = "AutoMakedClass.java";
    private final static String classFileName = "AutoMakedClass.class";
    private static String sourceClassString;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String methodSrc = "", str;

        while (scanner.hasNextLine()) {
            str = scanner.nextLine();
            if (str.length() == 0) {
                System.out.println("compilation time");

                makeClassSourceString(methodSrc);
                saveClassSourceToFile();

                compileJavaClassUsingJavac();
                compileJavaClassUsingJavax();
//                compileJavaClassUsingJci();
                executeClassMethod();

                methodSrc = "";
                System.out.println("Once again?\r\n");
            } else methodSrc += " " + str;
        }
    }

    private static boolean compileJavaClassUsingJavax() {
        try {
            System.out.println("Compile using javax.tools");
            Files.deleteIfExists(Paths.get(classFileName));
            ToolProvider.getSystemJavaCompiler().run(null, System.out, System.err, sourceFileName);
            System.out.println("Ok\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean compileJavaClassUsingJavac() {
        try {
            System.out.println("Compile using javac");
            Files.deleteIfExists(Paths.get(classFileName));
            Runtime.getRuntime().exec(new String[]{"javac", sourceFileName}).waitFor();
            System.out.println("Ok\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

//    private static boolean compileJavaClassUsingJci(){
//
//    }

    private static boolean executeClassMethod() {
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{Paths.get("").toAbsolutePath().toUri().toURL()});
            Class clazz = Class.forName("AutoMakedClass", true, classLoader);
            clazz.getDeclaredMethod("doWork", new Class[]{}).invoke(clazz.newInstance(), null);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String makeClassSourceString(String commands) {
        return sourceClassString = beginOfClass + commands + endOfClass;
    }

    private static boolean saveClassSourceToFile() {
        try {
            Files.deleteIfExists(Paths.get(sourceFileName));
            Files.write(Paths.get(sourceFileName), sourceClassString.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
