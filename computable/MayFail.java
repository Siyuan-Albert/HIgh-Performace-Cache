package GoodCache.computable;

import java.io.IOException;

/**
 * @author: Albert
 * @date: 9/14/20 21:38
 */
public class MayFail implements Computable<String,Integer> {

    @Override
    public Integer compute(String arg) throws Exception {
        double random = Math.random();
        if(random>0.5){
            throw new IOException("read file exception");
        }
        Thread.sleep(5000);
        return Integer.valueOf(arg);
    }
}
