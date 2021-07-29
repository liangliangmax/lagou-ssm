# 自定义ssm框架总结

### 目录

1. 结构说明
2. 整合过程
3. 启动运行



### 正文

1. 结构说明

   本项目是在前两个项目（自定义mybatis和自定义spring容器）的基础上进行整合。

首先目录结构分为三大部分

- com.liang.mybatis.core		之前写的mybatis的核心功能，解决了持久层的初始化
- com.liang.spring.core           之前写的spring的核心容器，解决了bean依赖注入的功能
- com.liang.spring.webmvc    这次开发的mvc的核心组件
- com.liang.ssm_demo            用于测试的项目，模拟正式环境下用户的使用



2. 整合过程

   - 首先是spring和mybatis的整合

      		mybatis启动时候会自己解析xml生成MappedStatement，这个是最终运行的sql对象，这里的关键就是如何将这些代理对象添加为spring的bean对象。

     

     ​		由于这里是一个简版的spring容器，没有使用factoryBean来生成bean，（ 正常的mybatis和spring整合参考https://www.cnblogs.com/lanqingzhou/p/13592232.html这个文章）,  这个简单的容器缺失其他功能，所以需要另外想办法。

     

     ​		既然mybatis的功能是完整的，意味着SqlSessionFactoryBuilder是好用的，也能创建出sqlSessionFactory，里面也有statement对象，那么只需要把SqlSessionFactory当做一个bean托管到spring容器进行生成，这样在spring启动时候，就会触发mybatis核心功能的加载，进而触发解析xml等动作。于是在ssm_demo项目中创建了自定义的bean。

     ​	

     ```java
     @Configuration
     public class SqlSessionFactoryConfig {
         @Value("${mybatis.mapper.configPath}")
         private String mapperScanPackage;
     
         @Autowired
         private DataSource dataSource;
         
         @Bean
         public SqlSessionFactory sqlSessionFactory() throws Exception {
             if(mapperScanPackage.startsWith("classpath:")){
                 mapperScanPackage = mapperScanPackage.replace("classpath:","");
             }
             InputStream resourceAsStream = Resources.getResourceAsStream(mapperScanPackage);
             SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
             sqlSessionFactoryBuilder.setDataSource(dataSource);
             SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsStream);
             return sqlSessionFactory;
         }
     }
     ```

     ​		这样生成完成之后spring容器里面就会有sqlSessionFactory对象。

     

     ​		那怎样才能扫描到接口呢，这里只能是在spring容器初始化的时候最后生成指定路径下的mapper接口的代理类，因为如果一开始就生成代理类的话由于sqlSessionFactory还没创建出来，代理类中需要sqlSessionFactory，所以没办法一开始就生成接口代理类。

     

     ​		等到之前的bean都生成完毕，最后用cglib生成指定接口的代理类，填加到spring容器中。

     ```java
     //生成mapper代理对象
     Set<Class<?>> classes = getClasses();
     
     for (Class<?> aClass : classes) {
     
         //如果是mapper的类，是不能被实例化出来的，因为没有实现类，需要直接创建代理类
         if(aClass.isAnnotationPresent(Mapper.class)){
             doCreateMapperProxy(GenerateBeanNameUtil.generateBeanName(aClass),aClass);
         }
     }
     ```

     ​		代理对象生成的过程是

     ```java
     public class MapperProxyFactory {
     
         private SqlSessionFactory sqlSessionFactory;
     
         public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
             this.sqlSessionFactory = sqlSessionFactory;
         }
     
         /**
          * 使用cglib动态代理生成代理对象
          * @param obj 委托对象
          * @return
          */
         public Object getCglibProxy(Class obj) {
             return  Enhancer.create(obj, new MethodInterceptor() {
                 @Override
                 public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                     Object result = null;
                     Object mapper = sqlSessionFactory.openSession().getMapper(obj);
                     result = method.invoke(mapper,objects);
                     return result;
                 }
             });
         }
     }
     ```

     ​		当service调用mapper时候，会通过sqlSessionFactory.openSession().getMapper(obj) 来获取具体的代理对象，这里的obj就是传入的接口类型，mybatis会根据接口的class去搜索对应的mappedStatement对象，然后调用mybatis的方法来执行sql。

     

   - 然后是spring和springmvc的整合

        ​		整合springmvc最核心的就是dispatchServlet，需要在tomcat启动的时候就要加载这个类。整个spring容器的初始化也是有这个类调用的。

        

        ​		第一步就是配置web.xml,使这个servlet在tomcat启动时候就初始化。

   ```xml
   <!DOCTYPE web-app PUBLIC
           "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
           "http://java.sun.com/dtd/web-app_2_3.dtd" >
   
   <web-app>
       <display-name>Archetype Created Web Application</display-name>
   
   
       <servlet>
           <servlet-name>liangmvc</servlet-name>
           <servlet-class>com.liang.spring.webmvc.servlet.DispatcherServlet</servlet-class>
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:springmvc.properties</param-value>
           </init-param>
           <load-on-startup>1</load-on-startup>
       </servlet>
       <servlet-mapping>
           <servlet-name>liangmvc</servlet-name>
           <url-pattern>/*</url-pattern>
       </servlet-mapping>
   
   
   </web-app>
   ```

   






​	启动运行

