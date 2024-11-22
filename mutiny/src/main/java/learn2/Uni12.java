package learn2;

import io.smallrye.mutiny.Uni;

import java.util.List;

public class Uni12 {

    public static void main(String[] args) {
        test04();
    }

    //so using disjoint we can flatten a container of uni to multi
    static void test01(){
        Uni.createFrom().item(List.of(1, 2, 3, 4, 5))
                .onItem().disjoint()
                .subscribe().with(System.out::println);
    }

    // this gets the whole object
    static void test02(){
        Uni.createFrom().item(List.of(1,2,3,4,5))
                .subscribe().with(System.out::println, Throwable::printStackTrace);
    }

    //not a container will fail
    static void test03(){
        Uni.createFrom().item(1)
                .onItem()
                .disjoint()
                .subscribe()
                .with(System.out::println, Throwable::printStackTrace);
    }

    static void test04(){
        Uni.createFrom().item(List.of(1, 2, 3, 4, 5)).onItem()
                .disjoint().onItem().transform(item -> "Number: " + item)
                .subscribe().with(
                        System.out::println,
                        error -> System.err.println("Error: " + error),
                        () -> System.out.println("Done!")
                );
    }

    static void test05(){

    }

    static void test06(){

    }

    static void test07(){

    }

}
