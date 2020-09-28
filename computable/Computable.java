package GoodCache.computable;

public interface Computable<A, V> {
    V compute(A arg) throws Exception;
}
