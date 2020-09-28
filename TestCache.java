package GoodCache;

import GoodCache.computable.ExpensiveFunction;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Albert
 * @date: 9/28/20 17:08
 * @description:
 */
public class TestCache {
    static GoodCache9<String,Integer> expensiveComputer = new GoodCache9<>(new ExpensiveFunction());
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(100);
        long start = System.currentTimeMillis();
        for(int i = 0; i < 100; i++){
            service.submit(()->{
                Integer result = null;
                try{
                    result = expensiveComputer.compute("666");
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(result);
            });
        }
        service.shutdown();
        while(!service.isTerminated()){

        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

}
