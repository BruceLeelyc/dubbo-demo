#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

SERVER_NAME=``
SERVER_PROTOCOL=``
SERVER_PORT=``
LOGS_FILE=``
SKYWALKING_AGENT=``

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`ps -f | grep java | grep "$CONF_DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
    if [ $SERVER_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"
        exit 1
    fi
fi


LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS=" -Dfile.encoding=UTF8 -Duser.timezone=GMT+08 -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Des.set.netty.runtime.available.processors=false"
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=2099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi
JAVA_EVN_OPTS=""
if [ "$1" = "dev" ]; then
    JAVA_EVN_OPTS=" -Dspring.profiles.active=dev "
    SKYWALKING_AGENT=" -javaagent:/opt/clouder/server/agent/skywalking-agent.jar -Dskywalking.agent.service_name=dev_mall "
fi
if [ "$1" = "test" ]; then
    JAVA_EVN_OPTS=" -Dspring.profiles.active=test -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=2101"
    SKYWALKING_AGENT=" -javaagent:/opt/clouder/server/agent/skywalking-agent.jar -Dskywalking.agent.service_name=test_mall "
fi
if [ "$1" = "online" ]; then
    JAVA_EVN_OPTS=" -Dspring.profiles.active=online "
fi
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xms2g -Xmx2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true"
else
    JAVA_MEM_OPTS="  -server -Xms1g -Xmx1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true "
fi

echo -e "Starting the $SERVER_NAME ...\c"
nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS $JAVA_EVN_OPTS $SKYWALKING_AGENT -classpath $COMMON_CONF_DIR:$CONF_DIR:$LIB_JARS com.bitfty.mall.Application > $STDOUT_FILE 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1
    COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"