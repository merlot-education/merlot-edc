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
    implementation(libs.edc.dsp)
	implementation(libs.edc.http)
    implementation(libs.edc.iam.mock)
    implementation(libs.edc.management.api)
	implementation(libs.edc.auth.tokenbased)	
    implementation(libs.edc.vault.filesystem)
    implementation(libs.edc.control.plane.core)
    implementation(libs.edc.transfer.data.plane)
    implementation(libs.edc.configuration.filesystem)
    implementation(libs.edc.api.observability)

    implementation(libs.edc.data.plane.selector.api)
    implementation(libs.edc.data.plane.selector.core)
    implementation(libs.edc.data.plane.selector.client)

    implementation(libs.edc.data.plane.api)
    implementation(libs.edc.data.plane.core)
    implementation(libs.edc.data.plane.http)
	implementation(libs.edc.data.plane.client)

	implementation("${group}:contract-spi:${edcVersion}")	
	implementation("${group}:policy-model:${edcVersion}")		
	implementation("${group}:policy-spi:${edcVersion}")	
	implementation("${group}:core-spi:${edcVersion}")	

	implementation(project(":edc-ionos-extension:provision-ionos-s3"))
	implementation(project(":edc-ionos-extension:data-plane-ionos-s3"))
}

application {
    mainClass.set("$group.boot.system.runtime.BaseRuntime")
}

var distTar = tasks.getByName("distTar")
var distZip = tasks.getByName("distZip")

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles()
    archiveFileName.set("connector.jar")
    dependsOn(distTar, distZip)
}
