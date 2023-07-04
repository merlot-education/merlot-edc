/*
 *  Copyright (c) 2022 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

plugins {
    `java-library`
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
	mavenCentral()
    maven {// while runtime-metamodel dependency is still a snapshot
		url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

val group: String by project
val edcVersion: String by project

dependencies {
    implementation("${group}:dsp:${edcVersion}")
    implementation("${group}:http:${edcVersion}")
    implementation("${group}:iam-mock:${edcVersion}")
    implementation("${group}:management-api:${edcVersion}")
    implementation("${group}:auth-tokenbased:${edcVersion}")
    implementation("${group}:vault-filesystem:${edcVersion}")
    implementation("${group}:api-observability:${edcVersion}")
    implementation("${group}:control-plane-core:${edcVersion}")
    implementation("${group}:configuration-filesystem:${edcVersion}")

    implementation("${group}:data-plane-selector-core:${edcVersion}")
    implementation("${group}:data-plane-selector-client:${edcVersion}")

	implementation("${group}:core-spi:${edcVersion}")	
	implementation("${group}:policy-spi:${edcVersion}")	
	implementation("${group}:policy-model:${edcVersion}")		
	implementation("${group}:contract-spi:${edcVersion}")	

	implementation(project(":edc-ionos-extension:provision-ionos-s3"))

    // provider
    implementation("${group}:transfer-data-plane:${edcVersion}")
    implementation("${group}:data-plane-core:${edcVersion}")
    implementation("${group}:data-plane-client:${edcVersion}")
    implementation(project(":edc-ionos-extension:data-plane-ionos-s3"))
}

application {
    mainClass.set("$group.boot.system.runtime.BaseRuntime")
}

tasks.shadowJar {
   isZip64 = true  
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("merlot-connector.jar")
}
