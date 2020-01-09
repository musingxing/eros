#!/bin/bash

PATH=$PATH:$HOME/bin
export PATH
CLASS="com.eros.shell.main.ErosMain"

## ##################################################################### ##
## Name:    Check JDK ENV
## Author:  P.F.XING
## Function:
## ##################################################################### ##
check_jdk_env()
{
  if [ -z $JAVA_HOME ]; then
    echo "The $JAVA_HOME environment variable is not defined correctly"
    exit 1
  fi

  if [ ! -x "$JAVA_HOME"/bin/java ]; then
      echo "The JAVA_HOME environment variable is not defined correctly"
      echo "This environment variable is needed to run this program"
      echo "NB: JAVA_HOME should point to a JDK not a JRE"
      exit 1
  fi
}

## ##################################################################### ##
## Name:    Shell Program
## Author:  P.F.XING
## Function:
## ##################################################################### ##
shell()
{
  pid=$(ps -ef |grep $CLASS| grep -v grep | awk '{print $2}')
  if [ -n "$pid" ]; then
    echo "The eros system already startup"
    exit 1
  fi

  HOME=$(dirname $(dirname $(readlink -f "$0")))
  CLASSPATH="$CLASSPATH"
  for i in $HOME/lib/*.jar;
  do CLASSPATH=$i:"$CLASSPATH";
  done
  export CLASSPATH=.:./bin:$CLASSPATH
  $JAVA_HOME/bin/java -Xms2048m -Xmx2048m -XX:+UseParallelGC -Dcom.eros.home="$HOME" $CLASS

  pid=$(ps -ef |grep $CLASS| grep -v grep | awk '{print $2}')
  if [ -z "$pid" ]; then
    echo "The eros system startup failure"
    exit 1
  fi
}

## ##################################################################### ##
## Name:    Start Program
## Author:  P.F.XING
## Function:
## ##################################################################### ##
start()
{
  pid=$(ps -ef |grep $CLASS| grep -v grep | awk '{print $2}')
  if [ -n "$pid" ]; then
    echo "The eros system already startup"
    exit 1
  fi

  HOME=$(dirname $(dirname $(readlink -f "$0")))
  CLASSPATH="$CLASSPATH"
  for i in $HOME/lib/*.jar;
  do CLASSPATH=$i:"$CLASSPATH";
  done
  export CLASSPATH=.:./bin:$CLASSPATH
  LOG_FILE="$HOME/logs/startup.log"
  nohup $JAVA_HOME/bin/java -Xms2048m -Xmx2048m -XX:+UseParallelGC -Dcom.eros.home="$HOME" $CLASS >> $LOG_FILE 2>&1 &

  pid=$(ps -ef |grep $CLASS| grep -v grep | awk '{print $2}')
  if [ -z "$pid" ]; then
    echo "The eros system startup failure"
    exit 1
  fi
}

## ##################################################################### ##
## Name:    Stop Program
## Author:  P.F.XING
## Function:
## ##################################################################### ##
stop()
{
  pid=$(ps -ef |grep $CLASS| grep -v grep | awk '{print $2}')

  if [ -n "$pid" ]; then
    echo "Stoping eros system with pid: $pid"
    kill -9 $pid
    echo "Stop success "
  fi
}

## ##################################################################### ##
## Name:    Status Query
## Author:  P.F.XING
## Function:
## ##################################################################### ##
status()
{
  pid=$(ps -ef |grep $CLASS| grep -v grep | awk '{print $2}')

  if [ -n "$pid" ]; then
    echo "Eros system with pid: $pid Running Normal."
  else
    echo "Eros system stopped."
  fi
}

## ##################################################################### ##
## Name:    Restart Porgram
## Author:  P.F.XING
## Function:
## ##################################################################### ##
restart()
{
    stop
    start
}

check_jdk_env
case "$1" in
    shell)
        shell
        ;;
    start)
        start
        ;;
    stop)
        stop
        ;;
    status)
         status
        ;;
    restart)
        restart
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart}"
        RETVAL=1
esac
exit $RETVAL
