# IDEA 创建 SpringBoot 多级 MAVEN 父子项目

## 前言

官方网址：https://spring.io/projects/spring-boot#learn

```
Spring Boot的版本以数字表示。
例如：Spring Boot 2.5.2.RELEASE --> 主版本.次版本.增量版本（Bug修复）

版本号介绍：
Alpha：不建议使用，主要是以实现软件功能为主，通常只在软件开发者内部交流，Bug较多；
Beta：该版本相对于α版已有了很大的改进，消除了严重的错误，但还是存在着一些缺陷，需要经过多次测试来进一步消除；
GA：General Availability，正式版本，官方推荐使用此版本，在国外都是用GA来说明release版本；
M：又叫里程碑版本，表示该版本较之前版本有功能上的重大更新；
PRE(不建议使用)：预览版，内部测试版，主要是给开发人员和测试人员测试和找BUG用的；
Release：最终版本，Release不会以单词形式出现在软件封面上，取而代之的是符号®；
RC：该版本已经相当成熟了，基本上不存在导致错误的BUG，与即将发行的正式版相差无几；
SNAPSHOT：快照版，可以稳定使用，且仍在继续改进版本。
```



本次演示依赖 **Spring Boot 2.5.2**，创建三层 MAVEN 父子项目目录。

```txt
template-parent                     // 父项目
├── template-common                 // 通用模块
│       └── pom.xml                 // 通用模块依赖
├── template-modules                // 业务模块
│       └── template-job            // 任务模块
│       |       └── pom.xml         // 任务模块依赖
│       └── template-log            // 日志模块
│       |       └── pom.xml         // 日志模块依赖
│       └── pom.xml                 // 业务模块依赖
├── template-system                 // 系统模块
│       └── pom.xml                 // 系统模块依赖
├── template-test                   // 测试模块
│       └── pom.xml                 // 测试模块依赖
├── pom.xml                         // 父项目依赖
```



## 一、建立一级项目

### template-parent

`File -> new -> project`，选 Maven，创建 maven 项目

```
Groupld:                   com.template
Artifactld:                parent
Version:                   1.0-SNAPSHOT

Project name:              template-parent
Project location:          G:\JavaProjects\template-parent
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>parent</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--父模块打包类型必须为 pom-->
    <packaging>pom</packaging>
    <name>parent</name>
    <description>parent project for Maven</description>

    <!--在 parent 指明继承关系，给出被继承的父项目的具体信息-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <!--在 properties 中统一管理项目依赖包的版本，更清晰-->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <springboot.version>2.5.2</springboot.version>
    </properties>

    <!--
       父项目引用依赖
       注：父项目依赖引用有两种方式

       1、使用此方式进行依赖管理时，父项目相当于一个依赖发布工厂。
           子项目需要哪些依赖，需要手动指定引入，无法继承父项目依赖直接使用
           <dependencyManagement>
               <dependencies>
                   <dependency>
                       <groupId>org.springframework.boot</groupId>
                       <artifactId>spring-boot-starter-web</artifactId>
                       <version>${springboot.version}</version>
                   </dependency>
               </dependencies>
           </dependencyManagement>

        2、使用此方式经行依赖管理时，子项目无需手动指定依赖引入，会自动继承父依赖直接使用。
           <dependencies>
               <dependency>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-starter-web</artifactId>
                   <version>${springboot.version}</version>
               </dependency>
           </dependencies>
    -->

    <!--删除 spring-boot-starter 和 spring-boot-starter-test，
        因为 parent 中继承的祖先中已经有了，并且一般 dependencyManagement 管理的依赖都要写版本号
    -->
    <dependencyManagement>
        <dependencies>
            <!--SpringBoot的 web 依赖配置-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>${springboot.version}</version>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <version>${springboot.version}</version>
                <scope>test</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.junit.vintage</groupId>
                        <artifactId>junit-vintage-engine</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>

    </dependencies>

    <!--注册子项目 后面创建二级子项目后，需在父项目中注册-->
    <modules>
        <!--<module>template-system</module>-->
        <!--<module>template-common</module>-->
        <!--<module>template-test</module>-->
        <!--<module>template-modules</module>-->
    </modules>

    <!--该插件作用是打一个可运行的包，必须要写在需要打包的项目里。这里的父模块不需要打包运行，所以删掉该插件-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <!--配置 Maven 项目的依赖远程仓库-->
    <repositories>
        <repository>
            <!--远程仓库唯一标识符-->
            <id>public</id>
            <!--描述-->
            <name>aliyun nexus</name>
            <!--远程仓库 url-->
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <!--用于定位和排序构件的仓库布局类型-可以是 default（默认）或者 legacy（遗留）-->
            <layout>default</layout>
            <!--Maven 要不要从这个仓库下载 release 版本的构件-->
            <releases>
                <enabled>true</enabled>
            </releases>
            <!--Maven 要不要从这个仓库下载 snapshot 版本的构件
                禁止从公共仓库下载 snapshot 构件是推荐的做法，因为这些构件不稳定，且不受你控制，你应该避免使用。
                当然，如果你想使用局域网内组织内部的仓库，你可以激活 snapshot 的支持。
            -->
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

    <!--配置 Maven 项目的插件构件远程仓库，maven 命令需要的插件（比如clean、install 都是 maven 的插件）-->
    <pluginRepositories>
        <pluginRepository>
            <id>public</id>
            <name>aliyun nexus</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```



## 二、建立二级项目

### template-system

`File -> new -> Module`，选 String Initializr，创建 springboot 项目

```
Groupld:                   com.template
Artifactld:                system
Version:                   1.0-SNAPSHOT

Project name:              template-system
Project location:          G:\JavaProjects\template-parent\template-system
Module file location:      G:\JavaProjects\template-parent\template-system
```



> template-system -- pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>system</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--将当前项目定义为 jar 项目-->
    <packaging>jar</packaging>
    <name>system</name>
    <description>system project for Spring Boot</description>

    <!--子模块的 parent 要使用顶层的父模块-->
    <parent>
        <groupId>com.template</groupId>
        <artifactId>parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    </parent>

    <!--properties 可删掉，会继承父模块的-->
    <properties>
        <!--<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
        <!--<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>-->
        <!--<java.version>1.8</java.version>-->
    </properties>

    <!--父项目通过 dependencyManagement 管理依赖 – 无法继承父项目依赖直接使用，子项目需手动引用依赖-->
    <dependencies>
        <!--SpringBoot的 web 依赖配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!--让多模块化拆分之后还能打成完整的可执行 jar 包-->
    <build>
        <finalName>system-demo</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.template.system.SystemApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



> template-parent -- pom.xml 加入 template-system 模块

```xml
    <modules>
        <module>template-system</module>
        <!--<module>template-common</module>-->
        <!--<module>template-test</module>-->
        <!--<module>template-modules</module>-->
    </modules>
```



### template-common

`File -> new -> Module`，选 String Initializr，创建 springboot 项目

```
Groupld:                   com.template
Artifactld:                common
Version:                   1.0-SNAPSHOT

Project name:              template-common
Project location:          G:\JavaProjects\template-parent\template-common
Module file location:      G:\JavaProjects\template-parent\template-common
```



> template-common -- pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>common</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--将当前项目定义为 jar 项目-->
    <packaging>jar</packaging>
    <name>common</name>
    <description>common project for Spring Boot</description>

    <!--子模块的 parent 要使用顶层的父模块-->
    <parent>
        <groupId>com.template</groupId>
        <artifactId>parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    </parent>

    <!--properties 可删掉，会继承父模块的-->
    <properties>
        <!--<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
        <!--<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>-->
        <!--<java.version>1.8</java.version>-->
    </properties>

    <!--父项目通过 dependencyManagement 管理依赖 – 无法继承父项目依赖直接使用，子项目需手动引用依赖-->
    <dependencies>
        <!--SpringBoot的 web 依赖配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!--让多模块化拆分之后还能打成完整的可执行 jar 包-->
    <build>
        <finalName>common-demo</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.template.common.CommonApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



> template-parent -- pom.xml 加入 template-common 模块

```xml
    <modules>
        <module>template-system</module>
        <module>template-common</module>
        <!--<module>template-test</module>-->
        <!--<module>template-modules</module>-->
    </modules>
```



### template-test

`File -> new -> Module`，选 String Initializr，创建 springboot 项目

```
Groupld:                   com.template
Artifactld:                test
Version:                   1.0-SNAPSHOT

Project name:              template-test
Project location:          G:\JavaProjects\template-parent\template-test
Module file location:      G:\JavaProjects\template-parent\template-test
```



> template-test -- pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>test</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--将当前项目定义为 jar 项目-->
    <packaging>jar</packaging>
    <name>test</name>
    <description>test project for Spring Boot</description>

    <!--子模块的 parent 要使用顶层的父模块-->
    <parent>
        <groupId>com.template</groupId>
        <artifactId>parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    </parent>

    <!--properties 可删掉，会继承父模块的-->
    <properties>
        <!--<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
        <!--<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>-->
        <!--<java.version>1.8</java.version>-->
    </properties>

    <!--父项目通过 dependencyManagement 管理依赖 – 无法继承父项目依赖直接使用，子项目需手动引用依赖-->
    <dependencies>
        <!--SpringBoot的 web 依赖配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!--让多模块化拆分之后还能打成完整的可执行 jar 包-->
    <build>
        <finalName>test-demo</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.template.test.TestApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



> template-parent -- pom.xml 加入 template-test 模块

```xml
    <modules>
        <module>template-system</module>
        <module>template-common</module>
        <module>template-test</module>
        <!--<module>template-modules</module>-->
    </modules>
```



### template-modules

`File -> new -> Module`，选 Maven，创建 maven 项目

```
Groupld:                   com.template
Artifactld:                modules
Version:                   1.0-SNAPSHOT

Project name:              template-modules
Project location:          G:\JavaProjects\template-parent\template-modules
Module file location:      G:\JavaProjects\template-parent\template-modules
```

  

> template-modules -- pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>modules</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--将当前项目定义为 pom 项目-->
    <packaging>pom</packaging>
    <name>modules</name>
    <description>modules project for Maven</description>

    <!--子模块的 parent 要使用顶层的父模块-->
    <parent>
        <artifactId>parent</artifactId>
        <groupId>com.template</groupId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    </parent>

    <!--properties 可删掉，会继承父模块的-->
    <properties>
        <!--<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
        <!--<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>-->
        <!--<java.version>1.8</java.version>-->
    </properties>

    <!--父项目通过 dependencyManagement 管理依赖 – 无法继承父项目依赖直接使用，子项目需手动引用依赖-->
    <dependencies>
        <!--SpringBoot的 web 依赖配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!--注册子项目 后面创建三级子项目后，需在二级子项目中注册-->
    <modules>
        <!--<module>template-job</module>-->
        <!--<module>template-log</module>-->
    </modules>

    <!--让多模块化拆分之后还能打成完整的可执行 jar 包-->
    <!--<build>-->
    <!--    <finalName>modules-demo</finalName>-->
    <!--    <plugins>-->
    <!--        <plugin>-->
    <!--            <groupId>org.springframework.boot</groupId>-->
    <!--            <artifactId>spring-boot-maven-plugin</artifactId>-->
    <!--            <executions>-->
    <!--                <execution>-->
    <!--                    &lt;!&ndash;可以把依赖的包都打包到生成的 jar 包中&ndash;&gt;-->
    <!--                    <goals>-->
    <!--                        <goal>repackage</goal>-->
    <!--                    </goals>-->
    <!--                    &lt;!&ndash;可以生成不含依赖包的不可执行 jar 包&ndash;&gt;-->
    <!--                    <configuration>-->
    <!--                        <classifier>exec</classifier>-->
    <!--                    </configuration>-->
    <!--                </execution>-->
    <!--            </executions>-->
    <!--        </plugin>-->
    <!--    </plugins>-->
    <!--</build>-->

</project>
```



> template-parent -- pom.xml 加入 template-modules 模块

```xml
    <modules>
        <module>template-system</module>
        <module>template-common</module>
        <module>template-test</module>
        <module>template-modules</module>
    </modules>
```



## 三、建立三级项目

### template-job

`File -> new -> Module`，选 String Initializr，创建 springboot 项目

```
Groupld:                   com.template
Artifactld:                job
Version:                   1.0-SNAPSHOT

Project name:              template-job
Project location:          G:\JavaProjects\template-parent\template-modules\template-job
Module file location:      G:\JavaProjects\template-parent\template-modules\template-job
```



> template-job -- pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>job</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--将当前项目定义为 jar 项目-->
    <packaging>jar</packaging>
    <name>job</name>
    <description>Demo project for Spring Boot</description>

    <!--子模块的 parent 要使用顶层的父模块-->
    <parent>
        <groupId>com.template</groupId>
        <artifactId>modules</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    </parent>

    <!--properties 可删掉，会继承父模块的-->
    <properties>
        <!--<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
        <!--<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>-->
        <!--<java.version>1.8</java.version>-->
    </properties>

    <!--父项目通过 dependencies 管理依赖 – 继承父项目依赖直接使用，子项目无需手动引用依赖-->
    <dependencies>
        <!--SpringBoot的 web 依赖配置-->
        <!--<dependency>-->
        <!--    <groupId>org.springframework.boot</groupId>-->
        <!--    <artifactId>spring-boot-starter-web</artifactId>-->
        <!--</dependency>-->

        <!--<dependency>-->
        <!--    <groupId>org.springframework.boot</groupId>-->
        <!--    <artifactId>spring-boot-starter-test</artifactId>-->
        <!--    <scope>test</scope>-->
        <!--</dependency>-->
    </dependencies>

    <!--让多模块化拆分之后还能打成完整的可执行 jar 包-->
    <build>
        <finalName>job-demo</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.template.job.JobApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



> template-modules -- pom.xml 加入 template-job 模块

```xml
    <modules>
        <module>template-job</module>
        <!--<module>template-log</module>-->
    </modules>
```



### template-log

`File -> new -> Module`，选 String Initializr，创建 springboot 项目

```
Groupld:                   com.template
Artifactld:                log
Version:                   1.0-SNAPSHOT

Project name:              template-log
Project location:          G:\JavaProjects\template-parent\template-modules\template-log
Module file location:      G:\JavaProjects\template-parent\template-modules\template-log
```



> template-log -- pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.template</groupId>
    <artifactId>log</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--将当前项目定义为 jar 项目-->
    <packaging>jar</packaging>
    <name>log</name>
    <description>Demo project for Spring Boot</description>

    <!--子模块的 parent 要使用顶层的父模块-->
    <parent>
        <groupId>com.template</groupId>
        <artifactId>modules</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    </parent>

    <!--properties 可删掉，会继承父模块的-->
    <properties>
        <!--<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
        <!--<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>-->
        <!--<java.version>1.8</java.version>-->
    </properties>

    <!--父项目通过 dependencies 管理依赖 – 继承父项目依赖直接使用，子项目无需手动引用依赖-->
    <dependencies>
        <!--SpringBoot的 web 依赖配置-->
        <!--<dependency>-->
        <!--    <groupId>org.springframework.boot</groupId>-->
        <!--    <artifactId>spring-boot-starter-web</artifactId>-->
        <!--</dependency>-->

        <!--<dependency>-->
        <!--    <groupId>org.springframework.boot</groupId>-->
        <!--    <artifactId>spring-boot-starter-test</artifactId>-->
        <!--    <scope>test</scope>-->
        <!--</dependency>-->
    </dependencies>

    <!--让多模块化拆分之后还能打成完整的可执行 jar 包-->
    <build>
        <finalName>log-demo</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.template.log.LogApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



> template-modules -- pom.xml 加入 template-log 模块

```xml
    <modules>
        <module>template-job</module>
        <module>template-log</module>
    </modules>
```



## 四、建立模块互相调用关系

### 4.1、template-parent 管理 template-common

> template-parent -- pom.xml 新增部分如下：

```xml
    <!--在 properties 中统一管理项目依赖包的版本，更清晰-->
    <properties>
        <template.version>1.0-SNAPSHOT</template.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!--自定义的通用模块-->
            <dependency>
                <groupId>com.template</groupId>
                <artifactId>common</artifactId>
                <version>${template.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
```



### 4.2、template-common 管理除 template-system 以外的其他模块

> template-common -- pom.xml 新增部分如下：

```xml
    <!--父项目通过 dependencyManagement 管理依赖 – 无法继承父项目依赖直接使用，子项目需手动引用依赖-->
    <dependencies>
        <!--自定义的测试模块-->
        <dependency>
            <groupId>com.template</groupId>
            <artifactId>test</artifactId>
            <version>${template.version}</version>
        </dependency>

        <!--自定义的业务模块-->
        <dependency>
            <groupId>com.template</groupId>
            <artifactId>modules</artifactId>
            <version>${template.version}</version>
        </dependency>

        <!--自定义的任务模块-->
        <dependency>
            <groupId>com.template</groupId>
            <artifactId>job</artifactId>
            <version>${template.version}</version>
        </dependency>

        <!--自定义的日志模块-->
        <dependency>
            <groupId>com.template</groupId>
            <artifactId>log</artifactId>
            <version>${template.version}</version>
        </dependency>
    </dependencies>
```



### 4.3、template-system 通过引入 template-common 调用其他模块

> template-system -- pom.xml 新增部分如下：

```xml
    <!--父项目通过 dependencyManagement 管理依赖 – 无法继承父项目依赖直接使用，子项目需手动引用依赖-->
    <dependencies>
        <!--自定义的通用模块-->
        <dependency>
            <groupId>com.template</groupId>
            <artifactId>common</artifactId>
        </dependency>
    </dependencies>
```



## 五、测试模块互相调用关系

> template-test 模块新增测试类 Test.java

```java
package com.template.test.service;

/**
 * @ClassName: Test
 * @Description: TODO
 * @Author: LiuJiaTao
 * @CreateDate: 2021-08-21
 * @Version : V1.0.0
 */
public class Test {
    public static void sayHello() {
        System.out.println("Hello,I am Test!");
    }
}
```



> template-common 模块新增测试类 Common.java

```java
package com.template.common.service;

/**
 * @ClassName: Common
 * @Description: TODO
 * @Author: LiuJiaTao
 * @CreateDate: 2021-08-21
 * @Version : V1.0.0
 */
public class Common {
    public static void sayHello() {
        System.out.println("Hello,I am Common!");
    }
}
```



> template-job 模块新增测试类 Job.java

```java
package com.template.job.service;

/**
 * @ClassName: Job
 * @Description: TODO
 * @Author: LiuJiaTao
 * @CreateDate: 2021-08-21
 * @Version : V1.0.0
 */
public class Job {
    public static void sayHello() {
        System.out.println("Hello,I am Job!");
    }
}
```



> template-log 模块新增测试类 Log.java

```java
package com.template.log.service;

/**
 * @ClassName: Log
 * @Description: TODO
 * @Author: LiuJiaTao
 * @CreateDate: 2021-08-21
 * @Version : V1.0.0
 */
public class Log {
    public static void sayHello() {
        System.out.println("Hello,I am Log!");
    }
}
```



> template-system 模块新增测试类 SystemController.java

```java
package com.template.system.controller;

import com.template.common.service.Common;
import com.template.job.service.Job;
import com.template.log.service.Log;
import com.template.test.service.Test;

/**
 * @ClassName: SystemController
 * @Description: TODO
 * @Author: LiuJiaTao
 * @CreateDate: 2021-08-21
 * @Version : V1.0.0
 */
public class SystemController {

    public static void main(String[] args) {
        Common.sayHello();
        Test.sayHello();
        Job.sayHello();
        Log.sayHello();
    }
}
```



> 打印结果：

```txt
Hello,I am Common!
Hello,I am Test!
Hello,I am Job!
Hello,I am Log!

进程已结束，退出代码 0
```



## 六、可能报错

### 报错信息：

> Could not find artifact com.template:parent:pom:1.0-SNAPSHOT

无法解析父级 Maven 的 pom



### 原因：

子级 Maven 的 pom 文件中，relativePath 标签使用的默认值：“<relativePath/>”，文档中介绍默认值表示<relativePath>../pom.xml</relativePath>，但编译环境（ IDEA 和命令行）不认这种写法。



### 解决：

只有显示写为“<relativePath>../pom.xml</relativePath>”，再次执行 mvn clean package 名称，编译通过，没有报错。

或者直接去掉“<relativePath/>”。
