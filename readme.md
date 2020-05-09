用法示例:
    对于Filter的划分，根据其面向的对象不同，可以分为service端和consumer端；根据其所作用的范围的不同，则可以分为单个服务过滤器（单个service或reference）和全局过滤器（整个provider或consumer）。
Filter的指定方式主要有三种：
    1.在<dubbo:service filter=""/>或<dubbo:reference filter=""/>标签中使用filter属性来指定具体的filter名称，这种使用方式的作用级别只针对于所指定的某个provider或consumer；
    2.在<dubbo:provider filter=""/>或<dubbo:consumer filter=""/>标签中使用filter属性来指定具体的filter名称，这种使用方式的作用级别针对于所有的provider或consumer；
    3.在所声明的实现了Filter接口的类上使用@Activate注解来标注，并且注解中的group属性指定为provider或consumer。
第一步:创建一个实现了Filter接口的异常捕获器：ServerTraceFilter
第二步:需要在META-INF/dubbo目录下新建一个名称为org.apache.dubbo.rpc.Filter的文件，然后在该文件中以键值对的形式将上面的异常处理器添加进去，
    如：erverTraceFilter=org.apache.dubbo.demo.example.ServerTraceFilter

这么做的原因在于，Dubbo在加载过滤器的时候会在META-INF/dubbo目录下查找所有名称为org.apache.dubbo.rpc.Filter的文件，并且读取文件内容，将文件内容以键值对的形式保存下来。
通过这种方式，Dubbo就实现了将数据的加载过程与用户使用的过程进行解耦。用户只需要按照上述方式声明一个过滤器，然后在指定文件(一般是自己创建)中添加该过滤器即可，
Dubbo会加载所有的这些指定名称的文件，这里的文件名其实就是所加载的类所实现的接口全限定名。上面的步骤只是声明了需要加载这些过滤器，但是如果针对不同的服务提供者或消费者进行差异化的过滤器指定则是需要在配置文件中进行的。如下分别是针对单个服务提供者和针对所有的服务提供者指定该过滤器的三种方式：

针对单个服务服务过滤:
    xml:
    <dubbo:service interface="org.apache.dubbo.demo.example.eg4.DemoService" ref="demoService" filter="exceptionResolver"/>
    注解:
    @Service(timeout = 3000, filter = "serverTraceFilter")
针对所有的provider服务提供者的过滤器:
    xml:
    <dubbo:provider filter="exceptionResolver"/>
在Filter实现类上，group属性表示当前类会针对所有的provider所使用
    @Activate(group = Constants.PROVIDER)
        需要注意的是，上面的第一种和第二种方式中filter属性的值都是在前面配置文件中所使用的键名，
        第三种方式则不需要在配置文件中进行指定，而只需要在实现Filter接口的实现类上进行指定该注解即可，
        group字段表示该实现类所属的一个分组，这里是provider端。

