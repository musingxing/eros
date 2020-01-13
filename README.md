# Eros System function

Eros is a system based on producer and consumer mode.<br>
It defines the frame and user only care about how to produce and consume.

# Usage
## command line
### shell
``` shell 
  sh bin/eros shell 

  eros(2020-01-13 20:56:04):0> 
```

### start a job 
``` shell 
  sh bin/eros shell 
  eros(2020-01-13 20:56:04):0> help dmaker
        dmaker  [create]
		    -j	--job-name	job name
		    -c	--conf	configuration
		    -t	--job-type	type
		    -p	--print-detail	print job detail

    eros(2020-01-13 20:56:04):0> dmaker create -j firstJob -t local -c /data1/eros/conf/eros-dmaker-create-local.xml
```

### job status 
``` shell 
  eros(2020-01-13 21:01:53):2> dmaker status -j firstJob -t local
  Job<type=local, name=firstJob>
  
  --------------------------------------------------------------------------------------------------------------------------------
                                                             ITEMS	|	VALUES                                                          
  --------------------------------------------------------------------------------------------------------------------------------
                                                 Running producers	|	2                                                               
                                                 Running consumers	|	2                                                               
                     Escaped Time(01-13 21:03:55 ~ 01-13 21:04:06)	|	11022ms                                                         
                                                   Produced number	|	39573665                                                        
                                                   Consumed number	|	39573678                                                        
                                                   Used file count	|	40                                                              
  --------------------------------------------------------------------------------------------------------------------------------
```

### job stop 
``` shell 
 eros(2020-01-13 21:04:06):4> dmaker stop -j firstJob -t local
 Stopped job<type=local, name=firstJob>
```