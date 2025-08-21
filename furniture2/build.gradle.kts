import org.jooq.meta.jaxb.ForcedType

plugins {
    java
    war
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "9.0"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val jooqVersion = "3.19.22"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:3.1.0")




    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    implementation("org.jooq:jooq:${jooqVersion}")
    jooqGenerator("org.jooq:jooq-codegen:$jooqVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    jooqGenerator("org.mariadb.jdbc:mariadb-java-client:3.3.1")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


jooq {
    version.set(jooqVersion)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.mariadb.jdbc.Driver"
                    url = "jdbc:mariadb://localhost:3306/test"
                    user = "root"
                    password = "3302"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    database.apply {
                        name = "org.jooq.meta.mariadb.MariaDBDatabase"
                        inputSchema = "test" // 일반적으로 DB명과 동일함
                        forcedTypes = listOf(
                            ForcedType().apply {
                                name = "INSTANT" // <-- java.time.Instant
                                types = "timestamp|datetime" // MariaDB, MySQL 기준
                                expression = ".*" // 모든 컬럼에 적용하거나 원하는 컬럼명 정규식
                            }
                        )
                    }
                    target.apply {
                        packageName = "com.example.jooq.generated"
                        directory = "build/generated/jooq/main"
                    }
                }


            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
