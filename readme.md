#### Load test comparison using `ab`

Testing was done against two instances of an identical Kotlin web server, one using coroutines for request handling, the other using synchronous request handling on the main thread.

Each load test was run using Apache Benchmark with a configuration of 1000 total requests per-server, and a concurrency level of 10 requests at a time.

##### Test 1
     Asynchronous                                                               Synchronous
                                                                                
     Requests per second:    2499.93 [#/sec] (mean)                             Requests per second:    2273.14 [#/sec] (mean)
     Time per request:       4.000 [ms] (mean)                                  Time per request:       4.399 [ms] (mean)
     Time per request:       0.400 [ms] (mean, across all concurrent requests)  Time per request:       0.440 [ms] (mean, across all concurrent requests)
     Transfer rate:          527.33 [Kbytes/sec] received                       Transfer rate:          479.49 [Kbytes/sec] received
                                                                                
     Connection Times (ms)                                                      Connection Times (ms)
                   min  mean[+/-sd] median   max                                                  min  mean[+/-sd] median   max
     Connect:        0    1   0.9      1       7                                Connect:        0    2   1.3      1      12
     Processing:     1    2   1.3      2       9                                Processing:     0    3   1.9      2      15
     Waiting:        0    2   1.3      2       9                                Waiting:        0    2   1.6      2      14
     Total:          1    4   1.7      3      13                                Total:          1    4   2.6      4      21
                                                                                
     Percentage of the requests served within a certain time (ms)               Percentage of the requests served within a certain time (ms)
       50%      3                                                                 50%      4
       66%      4                                                                 66%      4
       75%      5                                                                 75%      5
       80%      5                                                                 80%      5
       90%      6                                                                 90%      7
       95%      7                                                                 95%      9
       98%      9                                                                 98%     13
       99%      9                                                                 99%     15
      100%     13 (longest request)                                              100%     21 (longest request)
                                        
                                        
##### Test 2
     Asynchronous                                                               Synchronous
                                                                                  
     Requests per second:    2338.32 [#/sec] (mean)                             Requests per second:    2824.36 [#/sec] (mean)
     Time per request:       4.277 [ms] (mean)                                  Time per request:       3.541 [ms] (mean)
     Time per request:       0.428 [ms] (mean, across all concurrent requests)  Time per request:       0.354 [ms] (mean, across all concurrent requests)
     Transfer rate:          493.24 [Kbytes/sec] received                       Transfer rate:          595.76 [Kbytes/sec] received
                                                                                    
     Connection Times (ms)                                                      Connection Times (ms)
                   min  mean[+/-sd] median   max                                              min  mean[+/-sd] median   max
     Connect:        0    1   1.1      1       8                                Connect:        0    1   0.7      1       4 
     Processing:     0    3   1.3      3      10                                Processing:     1    2   1.6      2      16 
     Waiting:        0    2   1.0      2      10                                Waiting:        0    2   1.6      2      16 
     Total:          1    4   1.8      4      13                                Total:          1    3   1.6      3      16 
                                                                                    
     Percentage of the requests served within a certain time (ms)               Percentage of the requests served within a certain time (ms)
       50%      4                                                                 50%      3
       66%      5                                                                 66%      4
       75%      5                                                                 75%      4
       80%      6                                                                 80%      4
       90%      6                                                                 90%      4
       95%      7                                                                 95%      5
       98%      9                                                                 98%      6
       99%     11                                                                 99%     16
      100%     13 (longest request)                                              100%     16 (longest request)
                                    
##### Test 3                       
      Asynchronous                                                               Synchronous
                                                                                                    
      Requests per second:    2248.21 [#/sec] (mean)                            Requests per second:    2214.46 [#/sec] (mean)                           
      Time per request:       4.448 [ms] (mean)                                 Time per request:       4.516 [ms] (mean)
      Time per request:       0.445 [ms] (mean, across all concurrent requests) Time per request:       0.452 [ms] (mean, across all concurrent requests)
      Transfer rate:          474.23 [Kbytes/sec] received                      Transfer rate:          467.11 [Kbytes/sec] received
                                                                                
      Connection Times (ms)                                                     Connection Times (ms)
                    min  mean[+/-sd] median   max                                             min  mean[+/-sd] median   max
      Connect:        0    2   1.0      2       7                               Connect:        0    2   1.0      2       6
      Processing:     1    3   1.1      2       8                               Processing:     0    3   1.4      2      15
      Waiting:        0    2   1.0      2       7                               Waiting:        0    2   1.2      2      15
      Total:          1    4   1.7      4      12                               Total:          1    4   1.7      4      17
                                                                                
      Percentage of the requests served within a certain time (ms)              Percentage of the requests served within a certain time (ms)
        50%      4                                                                50%      4
        66%      5                                                                66%      5
        75%      5                                                                75%      5
        80%      5                                                                80%      5
        90%      6                                                                90%      6
        95%      7                                                                95%      7
        98%      9                                                                98%      8
        99%     10                                                                99%     10
       100%     12 (longest request)                                             100%     17 (longest request)
                                        
                                        
##### Average
    Async                                                                       Synchronous
    
    Requests per second: 2362.15                                                Requests per second: 2437.32
    Time per request: 4.24 [ms]                                                 Time per request: 4.516 [ms]
    
        
                                        
#### Unit testing results after 5 executions
                                        
Duration to handle 500 requests
    
    Asynchronous                Synchronous
    
    1156 ms                     1615 ms
    950 ms                      1220 ms
    950 ms                      1159 ms
    1187 ms                     1320 ms
    867 ms                      1266 ms
    
    Average                     Average
    1022 ms                     1316 ms
    
Memory usage after handling 500 requests
    
    Asynchronous                Synchronous
        
    8447 kb                     16960 kb
    8621 kb                     17525 kb
    8035 kb                     16945 kb
    8545 kb                     17365 kb
    8522 kb                     17496 kb
    
    Average                     Average
    8434 kb                     17258.2 kb