/*
 * (C) 2017 NEXT COLORS Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-----------------------------------------------------------------------------
//    Import Classes
//-----------------------------------------------------------------------------
import io.franzbecker.gradle.lombok.task.DelombokTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.time.Year

//-----------------------------------------------------------------------------
//    Plugins
//-----------------------------------------------------------------------------
plugins {
    `java-library`
    jacoco
    `eclipse-wtp`
    alias(libs.plugins.gradle.dependency.graph.generator)
    alias(libs.plugins.gradle.dokka)
    alias(libs.plugins.gradle.lombok)
    alias(libs.plugins.gradle.versions)
}

//-----------------------------------------------------------------------------
//    Build Script Settings
//-----------------------------------------------------------------------------
buildscript {
    // 依存関係の設定
    dependencies {
        classpath(libs.dokka.base)
    }
}

//-----------------------------------------------------------------------------
//    Project Settings
//-----------------------------------------------------------------------------
group = "jp.co.next-colors"

//-----------------------------------------------------------------------------
//    Project Layout Settings
//-----------------------------------------------------------------------------
// ビルド出力ディレクトリ
layout.buildDirectory.set(layout.projectDirectory.dir(findProperty("build.dir.output") as String))

//-----------------------------------------------------------------------------
//    Dependency Management
//-----------------------------------------------------------------------------
// コンフィギュレーションの設定
configurations {
    testImplementation {
        // テスト時にのみ必要なライブラリに、コンパイル時にのみ必要なライブラリを追加
        extendsFrom(compileOnly.get())
    }
}

// 依存関係の設定
dependencies {
    // アノテーションプロセッサ
    annotationProcessor(libs.lombok)

    // パッケージに含めるライブラリ
    api(libs.classgraph)
    api(libs.commons.beanutils)
    api(libs.commons.codec)
    api(libs.commons.collections4)
    api(libs.commons.io)
    api(libs.commons.lang3)
    api(libs.generics.resolver)
    api(libs.jool)
    api(libs.jooq)
    api(libs.ognl)
    api(libs.slf4j.api)
    api(libs.toolfactory.jvm.driver)
    api(libs.uroborosql)

    // コンパイル時にのみ必要なライブラリ
    compileOnly(libs.jakartaee.api)
    compileOnly(libs.javax.faces)

    // テスト時にのみ必要なライブラリ
    testImplementation(libs.assertj.core)
    testImplementation(libs.assertj.db)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.ninja.squad.dbsetup)

    // テスト実行時にのみ必要なライブラリ
    testRuntimeOnly(libs.junit.jupiter.engine)

    // Dokka のタスク実行時に必要なライブラリ
    // ※ Dokka で Java の書式でドキュメントを生成するためのライブラリ
    dokkaPlugin(libs.dokka.kotlin.java.plugin)
}

//-----------------------------------------------------------------------------
//    Plugin Configurations
//-----------------------------------------------------------------------------
// Java Plugin の設定
java {
    toolchain {
        // ビルド時に使用する Java のバージョン
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.majorVersion))
    }
}

// Gradle Lombok Plugin の設定
lombok {
    version = findProperty("lombok.version") as String
    sha256 = findProperty("lombok.checksum.sha256") as String
}

// Eclipse Plugin の設定
eclipse {
    // .project の設定
    project {
        // Gradle ネーチャーを追加
        natures("org.eclipse.buildship.core.gradleprojectnature")
        buildCommand("org.eclipse.buildship.core.gradleprojectbuilder")
    }
}

//-----------------------------------------------------------------------------
//    Tasks
//-----------------------------------------------------------------------------
// Java コンパイルのタスク
tasks.withType<JavaCompile>().configureEach {
    options.encoding = Charsets.UTF_8.name()
}

// Delombok のタスク
tasks.withType<DelombokTask>().configureEach {
    mainClass.set("lombok.launch.Main")
}

// Dokka のタスク
tasks.withType<DokkaTask>().configureEach {
    dependsOn(delombok)

    dokkaSourceSets {
        register(sourceSets.main.name) {
            val outputDir: Directory by delombok.get().extra

            sourceRoots.from(outputDir)

            documentedVisibilities.set(
                setOf(
                    DokkaConfiguration.Visibility.PUBLIC,
                    DokkaConfiguration.Visibility.PROTECTED
                )
            )

            jdkVersion.set(java.targetCompatibility.majorVersion.toInt())
        }
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        separateInheritedMembers = true
        footerMessage = "&#169; 2017-${Year.now()} NEXT COLORS Co., Ltd."
    }
}

// JAR ファイルを構築するタスク
tasks.jar {
    // マニフェストファイル
    manifest {
        attributes["Created-By"] = GradleVersion.current()
        attributes["Build-Jdk"] = System.getProperty("java.vm.version")
    }
}

// JUnit でテストするタスク
tasks.test {
    useJUnitPlatform()

    ignoreFailures = true
    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2).coerceAtLeast(1)

    testLogging {
        events(TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}

// Jacoco でテストのレポートを生成するためのタスク
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

// Lombok による変換後のソースコードを生成するタスク
val delombok by tasks.registering(DelombokTask::class) {
    dependsOn(tasks.compileJava)

    description = "Generates delomboked sources."
    group = name

    val outputDir by extra(layout.buildDirectory.dir(name).get())

    outputs.dir(outputDir)

    sourceSets.main {
        java.srcDirs.filter(File::exists).forEach {
            inputs.dir(it)

            args(it, "-d", outputDir)
        }
    }

    doFirst {
        delete(outputDir)
    }
}

// Lombok による変換後のソースコードを生成する際のヘルプを表示するタスク
val delombokHelp by tasks.registering(DelombokTask::class) {
    description = "Displays the help about delomboking."
    group = delombok.get().group

    args("--help")
}

// Lombok による変換後のソースコードを生成する際のフォーマットに関するヘルプを表示するタスク
val delombokFormatHelp by tasks.registering(DelombokTask::class) {
    description = "Displays the help about the format of delomboking."
    group = delombok.get().group

    args("--format-help")
}

// Javadoc のタスク
tasks.javadoc {
    dependsOn(delombok)

    val outputDir: Directory by delombok.get().extra

    source = outputDir.asFileTree
    isFailOnError = false

    options {
        this as StandardJavadocDocletOptions

        encoding = Charsets.UTF_8.name()
        isAuthor = true
        isUse = true
        version = true
        bottom = "&#169; 2017-${Year.now()} NEXT COLORS Co., Ltd."
        links(
            "https://commons.apache.org/proper/commons-beanutils/apidocs/",
            "https://commons.apache.org/proper/commons-codec/apidocs/",
            "https://commons.apache.org/proper/commons-collections/apidocs/",
            "https://commons.apache.org/proper/commons-io/javadocs/api-release/",
            "https://commons.apache.org/proper/commons-lang/apidocs/",
            "https://docs.oracle.com/javase/jp/17/docs/api/",
            "https://jakarta.ee/specifications/platform/8/apidocs/",
            "https://javadoc.io/doc/io.github.classgraph/classgraph/",
            "https://javadoc.io/doc/ru.vyarus/generics-resolver/",
            "https://javadoc.io/doc/jp.co.future/uroborosql/",
            "https://projectlombok.org/api/",
            "https://www.jooq.org/javadoc/latest/",
            "https://www.jooq.org/products/jOO%CE%BB/javadoc/latest/",
            "https://www.slf4j.org/apidocs/"
        )
    }
}

// Eclipse プロジェクトのタスク
tasks.eclipse {
    doFirst {
        // Buildship の設定
        layout.projectDirectory.file(".settings/org.eclipse.buildship.core.prefs").asFile.printWriter().use {
            it.println(
                """
                eclipse.preferences.version=1
                connection.project.dir=${relativePath(rootDir)}
                """.trimIndent()
            )
        }

        // テキストファイルのエンコードの設定
        layout.projectDirectory.file(".settings/org.eclipse.core.resources.prefs").asFile.printWriter().use {
            it.println(
                """
                eclipse.preferences.version=1
                encoding/<project>=${Charsets.UTF_8}
                """.trimIndent()
            )
        }
    }
}

// Gradle ラッパーのタスク
tasks.wrapper {
    gradleVersion = findProperty("gradle.version") as String
    distributionType = Wrapper.DistributionType.ALL
}
