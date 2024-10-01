# Java Checkstyle Config

Checkstyle config for all JAVA projects.

## Usage

Add the `maven-checkstyle-plugin` to your `pom.xml`:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.5.0</version>
    <configuration>
        <configLocation>checkstyle.xml</configLocation>
        <suppressionsLocation>checkstyle-suppressions.xml</suppressionsLocation>
        <includeTestSourceDirectory>true</includeTestSourceDirectory>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <linkXRef>false</linkXRef>
    </configuration>
    <executions>
        <execution>
            <id>validate</id>
            <phase>validate</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>10.18.1</version>
        </dependency>
        <dependency>
            <groupId>it.aboutbits</groupId>
            <artifactId>java-checkstyle-config</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</plugin>
```

**Configure your IDE:**  
First you need to run `mvn verify` once.
In your IDE go to `Settings` then search for "checkstyle".  
Add a new checkstyle configuration and select the `target/checkstyle-checker.xml` file.  
Set the property `org.checkstyle.sun.suppressionfilter.config` to `checkstyle-suppressions.xml`

## Information

About Bits is a company based in South Tyrol, Italy. You can find more information about us on [our website](https://aboutbits.it).

### Support

For support, please contact [info@aboutbits.it](mailto:info@aboutbits.it).

### Credits

- [All Contributors](../../contributors)

### License

The MIT License (MIT). Please see the [license file](license.md) for more information.
