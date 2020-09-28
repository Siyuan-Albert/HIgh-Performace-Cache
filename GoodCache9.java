package GoodCache;

import GoodCache.computable.Computable;
import GoodCache.computable.MayFail;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author: Albert
 * @date: 9/5/20 00:05
 * @description: set expiration time for the result. the result will be deleted auto
 */
public class GoodCache9<A,V> implements Computable<A,V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A,V> c;

    public GoodCache9(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        System.out.println("进入缓存机制");
        while(true){
            Future<V> f = cache.get(arg);

            if(f==null){
                Callable callable = new Callable<V>(){
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(callable);
                f = cache.putIfAbsent(arg,ft);
                if(f==null){
                    f = ft;
                    System.out.println("从FutureTask调用了计算函数");
                    ft.run();
                }

            }
            // if the calculation throw exception, we need to remove the future from cache
            // or it will pollute the cache
            try {
                return f.get();
            }catch(CancellationException e){
                cache.remove(arg);
                throw e;
            }catch (InterruptedException e) {
                cache.remove(arg);
                throw e;
            }catch (ExecutionException e) {
                cache.remove(arg);
                System.out.println("try to compute one more time");
            }
        }

    }
    public final static ScheduledExecutorService excutor = Executors.newScheduledThreadPool(5);
    public V compute(A arg, long expire) throws InterruptedException {
        if(expire > 0){
            excutor.schedule(new Runnable() {
                @Override
                public void run() {
                    expire(arg);
                }
            },expire,TimeUnit.MILLISECONDS);
        }
        return compute(arg);
    }

    public V computeRandomExpire(A arg) throws InterruptedException {
        long randomExpire = (long)(Math.random()*10000+5000);
        return compute(arg,randomExpire);
    }
    public synchronized void expire(A key){
        Future<V> future = cache.get(key);
        if(future!=null){
            if(!future.isDone()){
                System.out.println("future 任务还没完成就过期，直接取消任务");
                future.cancel(true);
            }
            System.out.println("过期时间到");
            cache.remove(key);
        }
    }
    public static void main(String[] args) throws Exception {
        GoodCache9<String, Integer> cache3 = new GoodCache9<>(new MayFail());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Integer res = cache3.compute("666",5000L);
                    System.out.println("第一次结果是： "+ res);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Integer res = cache3.compute("666");
                    System.out.println("第er次结果是： "+ res);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(6000L);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Integer res = cache3.compute("666");
                    System.out.println("第san次结果是： "+ res);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
