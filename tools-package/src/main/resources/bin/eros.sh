#!/bin/bash

PATH=$PATH:$HOME/bin
export PATH
SHELL_CLASS="com.eros.shell.main.ErosMain"

## ##################################################################### ##
## Name:        Check JDK ENV
## Function:    eros shell
## Author:      P.F.XING
## Date:        2020-01-15
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
## Name:        Shell Program
## Function:    eros shell
## Author:      P.F.XING
## Date:        2020-01-15
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
  $JAVA_HOME/bin/java -Xms2048m -Xmx2048m -XX:+UseParallelGC -Dcom.eros.home="$HOME" $SHELL_CLASS
#  nohup $JAVA_HOME/bin/java -Xms2048m -Xmx2048m -XX:+UseParallelGC -Dcom.eros.home="$HOME" $CLASS >> $LOG_FILE 2>&1 &
}

## ##################################################################### ##
## Name:        help
## Function:    eros shell
## Author:      P.F.XING
## Date:        2020-01-15
## ##################################################################### ##
help()
{
  echo -e "./eros [shell|help]"
}

check_jdk_env
case "$1" in
    shell)
        shell
        ;;
    help)
        help
        ;;
    *)
        echo $"Usage: $0 {shell|help}"
        RETVAL=1
        exit $RETVAL
esac
