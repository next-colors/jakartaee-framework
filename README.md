# Jakarta EE Framework

[![Java CI with Gradle](https://github.com/next-colors/jakartaee-framework/actions/workflows/gradle.yml/badge.svg)](https://github.com/next-colors/jakartaee-framework/actions/workflows/gradle.yml)
[![CodeQL](https://github.com/next-colors/jakartaee-framework/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/next-colors/jakartaee-framework/actions/workflows/codeql-analysis.yml)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

A collection of utilities that support Jakarta EE application development.

This library does not replace any part of the Jakarta EE specification. Instead, it packages the
boilerplate that tends to be written over and over when building applications on top of Jakarta EE,
[jOOQ](https://www.jooq.org/), and [uroboroSQL](https://future-architect.github.io/uroborosql-doc/) —
converting code-backed enums, paginated queries, running SQL files, base classes for interceptors,
and so on.

## Requirements

| Item       | Version |
| :--------- | :------ |
| Java       | 25      |
| Jakarta EE | 11      |
| Build tool | Gradle  |

The Jakarta EE API (`jakarta.platform:jakarta.jakartaee-api`) and the Faces implementation
(`org.glassfish:jakarta.faces`) are `compileOnly` dependencies, so they must be provided at runtime
by the application server or by the application itself.

## Features

### Enums with a code property (`enumeration`)

[`ICodeEnum`](src/main/java/jp/co/nextcolors/framework/enumeration/type/ICodeEnum.java) models enums
that carry a code value, typically the value stored in a database column.

```java
@Getter
@RequiredArgsConstructor
public enum Gender implements ICodeEnum<Gender, Integer> {
    MALE(1),
    FEMALE(2);

    private final Integer code;
}

Gender gender = ICodeEnum.codeOf(Gender.class, 1);      // MALE
boolean valid = ICodeEnum.isValidCode(Gender.class, 3); // false
Set<Integer> codes = ICodeEnum.codes(Gender.class);     // [1, 2]
```

Such enums can be converted in each layer by extending the corresponding base class below.

| Layer                       | Base class                                                                                                                                 |
| :-------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------- |
| JavaBeans (BeanUtils)       | [`bean.converter.CodeEnumConverter`](src/main/java/jp/co/nextcolors/framework/bean/converter/CodeEnumConverter.java)                       |
| Database (jOOQ `Converter`) | [`jdbc.converter.CodeEnumConverter`](src/main/java/jp/co/nextcolors/framework/jdbc/converter/CodeEnumConverter.java)                       |
| Faces (`Converter`)         | [`faces.converter.CodeEnumConverter`](src/main/java/jp/co/nextcolors/framework/faces/converter/CodeEnumConverter.java)                     |
| JSON-B (serialization)      | [`json.bind.serializer.CodeEnumSerializer`](src/main/java/jp/co/nextcolors/framework/json/bind/serializer/CodeEnumSerializer.java)         |
| JSON-B (deserialization)    | [`json.bind.deserializer.CodeEnumDeserializer`](src/main/java/jp/co/nextcolors/framework/json/bind/deserializer/CodeEnumDeserializer.java) |

Each base class resolves the target types from its type arguments, so extending it is all that is
required.

```java
public class GenderConverter extends CodeEnumConverter<Gender, Integer, Integer> {
}
```

### JavaBeans converters (`bean`)

Commons BeanUtils `Converter` implementations annotated with
[`@BeanConverter`](src/main/java/jp/co/nextcolors/framework/bean/annotation/BeanConverter.java) are
discovered on the classpath and registered in bulk.

```java
@BeanConverter(forClass = Gender.class)
public class GenderConverter extends CodeEnumConverter<Gender, Integer> {
}

// Call once during application startup.
BeanConverterUtil.registerConverters();
```

Converters for `java.util.Date` and for the jOOQ unsigned number types (`UByte`, `UShort`,
`UInteger`, `ULong`) are provided out of the box.

### Pagination (`data.pagination`, `jdbc.pagination`)

Pagination is modelled by
[`IPageRequest`](src/main/java/jp/co/nextcolors/framework/data/pagination/IPageRequest.java) (the
requested page) and [`IPage`](src/main/java/jp/co/nextcolors/framework/data/pagination/IPage.java)
(the resulting page), with
[`IPager`](src/main/java/jp/co/nextcolors/framework/jdbc/pagination/IPager.java) running the
paginated query through jOOQ.

```java
IPager<User> pager = new Pager<>(dslContext, User.class);

IPageRequest pageRequest = new PageRequest(1, 20); // page numbers are 1-based

// Query a table
IPage<User> page = pager.fetchPage(pageRequest, USER, USER.AGE.ge(20), USER.ID.asc());

// Query using a SQL file
IPage<User> page = pager.fetchPageBySqlFile(pageRequest, Path.of("sql", "user", "select.sql"),
                                            Map.of("age", 20));
```

`fetchPageBySqlFile` automatically binds the pagination values to the `offset` and `limit`
parameters of the SQL file.

### Queries backed by SQL files (`jdbc.query`)

Two-way SQL files are parsed with uroboroSQL and executed through the jOOQ `DSLContext`.

```java
ISqlFileSelect select = new SqlFileSelect(dslContext, Path.of("sql", "user", "select.sql"))
        .addParameter("age", 20)
        .addParameter("ids", List.of(1, 2, 3));

Result<Record> result = select.fetch();

ISqlFileWrite write = new SqlFileWrite(dslContext, Path.of("sql", "user", "update.sql"))
        .addParameters(params);

int count = write.execute();
```

Parameters passed as a `Collection` or an array are expanded into an IN list.
[`Sort`](src/main/java/jp/co/nextcolors/framework/jdbc/query/Sort.java) is provided for building
sort orders.

### Database converters (`jdbc.converter`)

The following jOOQ `Converter` implementations are provided.

- [`Base64Converter`](src/main/java/jp/co/nextcolors/framework/jdbc/converter/Base64Converter.java) — Base64 encoding of strings
- [`Sha256Converter`](src/main/java/jp/co/nextcolors/framework/jdbc/converter/Sha256Converter.java) / [`Sha512Converter`](src/main/java/jp/co/nextcolors/framework/jdbc/converter/Sha512Converter.java) — hashing of strings
- [`UrlConverter`](src/main/java/jp/co/nextcolors/framework/jdbc/converter/UrlConverter.java) — conversion between `java.net.URL` and `String`

[`BeanRecordMapper`](src/main/java/jp/co/nextcolors/framework/jdbc/record/mapper/BeanRecordMapper.java)
maps a record to a JavaBean.

### Interceptors (`interceptor`)

Base classes for implementing Jakarta Interceptors. Implementing `invokeInternal` is enough — the
interception annotations (`@AroundInvoke` and friends) are already declared on the base class.

| Class                                                                                                            | Target                           |
| :--------------------------------------------------------------------------------------------------------------- | :------------------------------- |
| [`MethodInterceptor`](src/main/java/jp/co/nextcolors/framework/interceptor/MethodInterceptor.java)               | Method invocations               |
| [`ConstructorInterceptor`](src/main/java/jp/co/nextcolors/framework/interceptor/ConstructorInterceptor.java)     | Constructors                     |
| [`PostConstructInterceptor`](src/main/java/jp/co/nextcolors/framework/interceptor/PostConstructInterceptor.java) | `@PostConstruct` lifecycle event |
| [`PreDestroyInterceptor`](src/main/java/jp/co/nextcolors/framework/interceptor/PreDestroyInterceptor.java)       | `@PreDestroy` lifecycle event    |

### Request dumping (`filter`)

Registering
[`RequestDumpFilter`](src/main/java/jp/co/nextcolors/framework/filter/RequestDumpFilter.java) logs
the request properties, session properties, headers, parameters, and cookies at DEBUG level. Nothing
is done when DEBUG logging is disabled.

```xml
<filter>
    <filter-name>requestDumpFilter</filter-name>
    <filter-class>jp.co.nextcolors.framework.filter.RequestDumpFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>requestDumpFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

### Resource bundles (`resource`)

[`ResourceBundleControl`](src/main/java/jp/co/nextcolors/framework/resource/ResourceBundleControl.java)
lets you configure the cache expiration and the resource formats, and
[`PropertyResourceBundleControl`](src/main/java/jp/co/nextcolors/framework/resource/PropertyResourceBundleControl.java)
restricts the formats to `*.properties` files. The cache expiration can be given with the `TTL_1MIN`,
`TTL_5MIN`, `TTL_10MIN`, `TTL_30MIN`, and `TTL_1H` constants.

```java
ResourceBundle bundle = ResourceBundle.getBundle("messages", locale,
                                                 new PropertyResourceBundleControl(ResourceBundleControl.TTL_5MIN));
```

## Building

If you use [mise](https://mise.jdx.dev/), the configuration in
[mise.toml](mise.toml) installs Temurin.

```console
$ ./gradlew build
```

The main tasks are:

| Task                          | Description                                         |
| :---------------------------- | :-------------------------------------------------- |
| `./gradlew build`             | Compile, run the tests, and build the JAR           |
| `./gradlew test`              | Run the tests                                       |
| `./gradlew jacocoTestReport`  | Generate the coverage report                        |
| `./gradlew javadoc`           | Generate the Javadoc (from the delombok-ed sources) |
| `./gradlew dokkaGenerate`     | Generate the documentation with Dokka               |
| `./gradlew eclipse`           | Generate the Eclipse configuration files            |
| `./gradlew dependencyUpdates` | Check for dependency updates                        |

The resulting JAR is written to `build/libs/`.

## Dependencies

Dependencies are managed by the version catalog in
[gradle/libs.versions.toml](gradle/libs.versions.toml), where versions are declared as
`latest.release`.<br>
The main ones are:

- [Apache Commons](https://commons.apache.org/) (BeanUtils 2, Codec, Collections 4, IO, Lang 3)
- [ClassGraph](https://github.com/classgraph/classgraph)
- [generics-resolver](https://github.com/xvik/generics-resolver)
- [jOOQ](https://www.jooq.org/) / [jOOλ](https://github.com/jOOQ/jOOL)
- [Lombok](https://projectlombok.org/) (compile time only)
- [OGNL](https://commons.apache.org/proper/commons-ognl/)
- [SLF4J](https://www.slf4j.org/)
- [uroboroSQL](https://future-architect.github.io/uroborosql-doc/)

## License

[Apache License, Version 2.0](LICENSE)

&copy; 2017 NEXT COLORS Co., Ltd.
