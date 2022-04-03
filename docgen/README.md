bitrxc-doclet
===========================

Introduction
------------
This java doclet allows for auto generation of  API documentation that can be read by front-end workers from a Spring project.

It is modified from [calrissian/rest-doclet: Automatic generation of REST documentation from Spring and JAX-RS Controllers. (github.com)](https://github.com/calrissian/rest-doclet).

How to generate API documentation from a Spring project
----------------------------
If you want to use it in your Spring project, follow these steps:

1.  Install
Enter following command under the root path of this project("bitrxc-doclet") 
  ```
  mvn clean install
  ```
Then the doclet is generated in your Maven local repository. The groupId is "cn.edu.bit.ruixin.rest-doclet"

2.  Maven
Configure the javadoc plugin to use this custom doclet. To be specific, add the following xml to your Maven configuration file 
  ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.1.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                    <useStandardDocletOptions>false</useStandardDocletOptions>
                    <show>private</show>
                    <doclet>cn.edu.bit.ruixin.doclet.restful.RestDoclet</doclet>
                    <docletArtifact>
                        <groupId>cn.edu.bit.ruixin</groupId>
                        <artifactId>rest-doclet</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </docletArtifact>
                </configuration>
            </plugin>
        </plugins>
    </build>
  
  ```
3.  Command
Enter the following command under your project path
  ```
  mvn javadoc:javadoc
  ```

4.  Result
    Then the API document index.html and stylesheet.css is generated at target/site/apidocs

How to build doclet from source code
----------------------------

If you want to modify the doclet source code and generate doclet from source code, just
