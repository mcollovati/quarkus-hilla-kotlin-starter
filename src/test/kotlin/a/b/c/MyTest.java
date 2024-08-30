package a.b.c;

public class MyTest {

    public static void main(String[] args) throws ClassNotFoundException {
        //System.out.println("Hello World " + A.B.C.class.getName());
        System.out.println("Hello World " +
                Thread.currentThread().getContextClassLoader().loadClass("a.b.c.MyTest$A$B$C")
                        .getName());
    }

    static class A {

        static class B {

            static class C {}
        }
    }
}
