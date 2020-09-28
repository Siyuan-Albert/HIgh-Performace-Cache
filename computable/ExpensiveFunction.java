package GoodCache.computable;

/**
 * @author: Albert
 * @date: 9/4/20 23:53
 */
public class ExpensiveFunction implements Computable<String, Integer>{
    @Override
    public Integer compute(String arg) throws Exception {
        Thread.sleep(5000);
        return Integer.valueOf(arg);
    }
}
