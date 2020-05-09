package com.demo.dubbo;

import com.bitfty.eventBus.EventBusDispatcher;
import com.bitfty.eventBus.listener.EventBusListener;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.container.Container;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.dubbo.common.constants.CommonConstants.COMMA_SPLIT_PATTERN;

/**
 * Hello world!
 * @author wuhui
 */
@EnableDubbo
@MapperScan("com.bitfty.mall.dao")
@ComponentScan("com.bitfty")
@Import(SpringBootConfiguration.class)
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class Application {

    public static void main( String[] args ) {
        try {
            ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
            DubboHook.dubboHook();
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(Thread.currentThread(), run));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Bean
    public EventBusDispatcher eventBusDispatcher(){
        return new EventBusDispatcher();
    }

    @Bean
    public EventBusListener eventBusListener(){
        return new EventBusListener();
    }
}

/**
 * springboot 优雅停机
 */
class ShutdownHook extends Thread {
    private Thread mainThread;
    private boolean shutDownSignalReceived;
    private static ConfigurableApplicationContext configurableApplicationContext;

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    @Override
    public void run() {
        configurableApplicationContext.stop();
    }

    public ShutdownHook(Thread mainThread,ConfigurableApplicationContext configurableApplicationContext) {
        super();
        this.mainThread = mainThread;
        this.shutDownSignalReceived = false;
        ShutdownHook.configurableApplicationContext = configurableApplicationContext;
    }

}

/**
 * dubbo优雅停机
 */
class DubboHook {

    public static final String CONTAINER_KEY = "dubbo.container";

    private static final Logger logger = LoggerFactory.getLogger(DubboHook.class);

    private static final ExtensionLoader<Container> loader = ExtensionLoader.getExtensionLoader(Container.class);

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final Condition STOP = LOCK.newCondition();

    public static void dubboHook() {
        try {
            String config = ConfigUtils.getProperty(CONTAINER_KEY, loader.getDefaultExtensionName());
            String[] args = COMMA_SPLIT_PATTERN.split(config);

            final List<Container> containers = new ArrayList<Container>();
            for (int i = 0; i < args.length; i++) {
                containers.add(loader.getExtension(args[i]));
            }
            logger.info(" Use container type(" + Arrays.toString(args) + ") to run dubbo serivce.");

            Runtime.getRuntime().addShutdownHook(new Thread("dubbo-container-shutdown-hook") {
                @Override
                public void run() {
                    for (Container container : containers) {
                        try {
                            container.stop();
                            logger.info(" Dubbo " + container.getClass().getSimpleName() + " stopped!");
                        } catch (Throwable t) {
                            logger.error(t.getMessage(), t);
                        }

                        LOCK.lock();
                        try {
                            STOP.signal();
                        } finally {
                            LOCK.unlock();
                        }
                    }
                }
            });
            logger.info(" dubbo hook run finish");
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }

        LOCK.lock();
        try {
            STOP.await();
        } catch (InterruptedException e) {
            logger.warn(" Dubbo service server stopped, interrupted by other thread!", e);
        } finally {
            LOCK.unlock();
        }
    }

}
