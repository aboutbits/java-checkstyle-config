# Java Checkstyle Config

Checkstyle config for all JAVA projects.

## Usage

Add this repository as a submodule of your main project by creating a `.gitmodules` file:
```
[submodule "checkstyle-config"]
	path = checkstyle-config
	url = https://github.com/aboutbits/java-checkstyle-config
	branch = main
```

Add the `maven-checkstyle-plugin` to your `pom.xml`:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.5.0</version>
    <configuration>
        <configLocation>checkstyle-config/checkstyle.xml</configLocation>
        <suppressionsLocation>checkstyle-config/checkstyle-suppressions.xml</suppressionsLocation>
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
    </dependencies>
</plugin>
```

For use with github-actions, you will also have to tell `actions/checkout` to also recurse submodules:
```yaml
- uses: actions/checkout@v4
  with:
    submodules: 'true'
```

**Configure your IDE:**  
Go to `Settings` then search for "checkstyle".  
Add a new checkstyle configuration and select the `checkstyle-config/checkstyle.xml` file.  
Set the property `org.checkstyle.sun.suppressionfilter.config` to `checkstyle-config/checkstyle-suppressions.xml`

## Information

About Bits is a company based in South Tyrol, Italy. You can find more information about us on [our website](https://aboutbits.it).

### Support

For support, please contact [info@aboutbits.it](mailto:info@aboutbits.it).

### Credits

- [All Contributors](../../contributors)

### License

The MIT License (MIT). Please see the [license file](license.md) for more information.
