
class A {
    public void show() {
        System.out.println("show");
    }
}
class B extends A {
    public void display() {
        System.out.println("display");
    }
    public void show() {
        System.out.println("better show");
    }
}
public class test {
    public static void main(String[] args) {
        A a = new B(); // possible because B extends A
        a.show(); // this will now call to show() method of class B
    }
}
