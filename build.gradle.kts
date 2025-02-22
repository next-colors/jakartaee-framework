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
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask
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
//    Project Settings
//-----------------------------------------------------------------------------
group = "jp.co.next-colors"

//-----------------------------------------------------------------------------
//    Dependency Management
//-----------------------------------------------------------------------------
// コンフィギュレーションの設定
configurations {
    // テスト時にのみ必要なライブラリ
    testImplementation {
        // コンパイル時にのみ必要なライブラリを追加
        extendsFrom(compileOnly.get())
    }
}

// 依存関係の設定
dependencies {
    // アノテーションプロセッサ
    annotationProcessor(libs.lombok)

    // パッケージに含めるライブラリ
    api(libs.classgraph)
    api(libs.commons.beanutils2)
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
    compileOnly(libs.jakarta.faces)
    compileOnly(libs.jakartaee.api)

    // テスト時にのみ必要なライブラリ
    testImplementation(libs.assertj.core)
    testImplementation(libs.assertj.db)
    testImplementation(libs.commons.text)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.mockneat)
    testImplementation(libs.ninja.squad.dbsetup)

    // テスト実行時にのみ必要なライブラリ
    testRuntimeOnly(libs.jakarta.servlet.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Dokka の HTML 生成タスク実行時に必要なライブラリ
    // ※ Dokka で Java の書式でドキュメントを生成するためのライブラリ
    dokkaHtmlPlugin(libs.dokka.kotlin.java.plugin)
}

//-----------------------------------------------------------------------------
//    Plugin Configurations
//-----------------------------------------------------------------------------
// Java Plugin の設定
java {
    toolchain {
        // ビルド時に使用する Java のバージョン
        languageVersion = JavaLanguageVersion.of(JavaVersion.VERSION_21.majorVersion)
    }
}

// Lombok Plugin の設定
lombok {
    version = providers.gradleProperty("lombok.version")
}

// Dokka Plugin の設定
dokka {
    dokkaSourceSets.javaMain {
        sourceRoots.setFrom(tasks.delombok.get().target)

        documentedVisibilities(
            VisibilityModifier.Public,
            VisibilityModifier.Protected
        )

        jdkVersion = java.targetCompatibility.majorVersion.toInt()
    }

    pluginsConfiguration.html {
        separateInheritedMembers = true
        footerMessage = "&#169; 2017-${Year.now()} NEXT COLORS Co., Ltd."
    }
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

// Dokka のタスク
tasks.withType<DokkaGenerateTask>().configureEach {
    dependsOn(tasks.delombok)
}

// JAR ファイルを構築するタスク
tasks.jar {
    // マニフェストファイル
    manifest {
        attributes(
            "Created-By" to GradleVersion.current(),
            "Build-Jdk" to System.getProperty("java.vm.version")
        )
    }
}

// テストするタスク
tasks.test {
    useJUnitPlatform()

    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2).coerceAtLeast(1)

    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
}

// Jacoco でテストのレポートを生成するためのタスク
tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required = true
    }
}

// Javadoc のタスク
tasks.javadoc {
    dependsOn(tasks.delombok)

    source = tasks.delombok.get().target.asFileTree
    isFailOnError = false

    options {
        this as StandardJavadocDocletOptions

        encoding = tasks.compileJava.get().options.encoding
        isAuthor = true
        isUse = true
        version = true
        bottom = "&#169; 2017-${Year.now()} NEXT COLORS Co., Ltd."
        links(
            "https://commons.apache.org/proper/commons-beanutils/apidocs/",
            "https://commons.apache.org/proper/commons-codec/apidocs/",
            "https://commons.apache.org/proper/commons-collections/apidocs/",
            "https://commons.apache.org/proper/commons-io/apidocs/",
            "https://commons.apache.org/proper/commons-lang/apidocs/",
            "https://docs.oracle.com/en/java/javase/21/docs/api/",
            "https://jakarta.ee/specifications/platform/11/apidocs/",
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

// Eclipse の設定ファイルを生成するタスク
tasks.eclipse {
    dependsOn(tasks.cleanEclipse)

    doFirst {
        // Buildship の設定
        layout.projectDirectory.file(".settings/org.eclipse.buildship.core.prefs").asFile.writer().use {
            it.appendLine(
                """
                eclipse.preferences.version=1
                connection.project.dir=${relativePath(rootDir)}
                """.trimIndent()
            )
        }

        // テキストファイルのエンコードの設定
        layout.projectDirectory.file(".settings/org.eclipse.core.resources.prefs").asFile.writer().use {
            it.appendLine(
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
    gradleVersion = providers.gradleProperty("gradle.version").get()
    distributionType = Wrapper.DistributionType.ALL
}
