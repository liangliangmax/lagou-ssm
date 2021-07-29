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

   

    		既然mybatis的功能是完整的，意味着SqlSessionFactoryBuilder是好用的，也能创建出sqlSessionFactory，里面也有statement对象，那么只需要把SqlSessionFactory当做一个bean托管到spring容器进行生成，这样在spring启动时候，就会触发mybatis核心功能的加载，进而触发解析xml等动作。于是在ssm_demo项目中创建了自定义的bean。

   

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

   这样生成完成之后spring容器里面就会有sqlSessionFactory对象。

   

3. 启动运行

