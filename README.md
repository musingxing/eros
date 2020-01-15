# Welcome to Eros System
![alt text](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1579098824802&di=abf5698d303cf21770162ce6f76f5e7f&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20130113%2FImg363287295.jpg "Eros")

Eros is a system based on producer and consumer mode.<br>
It defines the frame and user only care about how to produce and consume.
---------------------------


#### Packaging/release artifacts

All the source files which can be built by running:
``` package 
  maven clean install -DskipTests 
```
And then you can find in tool-package/target directory after building the project with maven.
``` package 
  eros-[version].tar.gz
```
---------------------------

#### Quick Start Guide
This quick start guide goes over how to run Eros on a local machine. The guide will cover the following tasks:
- Set up eros environment
- Perform basic tasks via Eros Shell
- Stop Eros

##### 1. Set up
The only thing, you should do, unpack the compressed package via command 'tar'.
```  
  $ tar -zvxf eros-[version].tar.gz
  $ cd eros/bin
```
##### 2. Eros Shell
Running start command:
```  
  ./eros shell
```
Start your job:
```  
  eros> dmaker create -j firstJob -t local -c /data1/eros/conf/eros-dmaker-create-local.xml 
```
Help command can work if you are interest in command details:
``` shell 
  eros> help dmaker
        dmaker  [create]
		    -j	--job-name	job name
		    -c	--conf	configuration
		    -t	--job-type	type
		    -p	--print-detail	print job detail
```
This job status will be show while you execute command as follow: 

``` shell 
  eros> dmaker status -j firstJob -t local
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

##### 3. Stop
Ctrl+C or Ctrl+D can work, but we suggest you execute 'quit' command on eros-terminal.

---------------------------

#### Contributing
We always welcome new contributors to the project! We need friends!