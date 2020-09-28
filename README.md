# HIgh-Performace-Cache
A cache without duplicate computation



1. Data structure

   HashMap (unsafe) -> add synchronous to **compute**(not efficient, the computation is in series) -> concurrentHashMap(final)

   

2. How to avoid duplicate compuation? 
   1. The value in **concurrentHashMap**(cache) will be wrapped by **Future** interface. A **Future** represents the result of an asynchronous computation. 
   2. Put the key and value to the cache, and then do computing. 
      1. If the second task with same calculation, it will block. 



​			

