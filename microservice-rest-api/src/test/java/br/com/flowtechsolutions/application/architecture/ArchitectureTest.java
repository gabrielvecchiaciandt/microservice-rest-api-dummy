package br.com.flowtechsolutions.application.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

class ArchitectureTest {
    private final JavaClasses importedClasses =
        new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("br.com.flowtechsolutions");

    private LayeredArchitecture layeredArchitectureTemplate;

    @BeforeEach
    void init() {
        layeredArchitectureTemplate = layeredArchitecture()
            .consideringAllDependencies()
            .withOptionalLayers(true)
            .layer("Configuration").definedBy("..configuration..")
            .layer("CoreDataprovider").definedBy("..core.dataprovider..")
            .layer("CoreEntity").definedBy("..core.entity..")
            .layer("CoreUsecase").definedBy("..core.usecase..")
            .layer("Dataproviders").definedBy("..dataproviders..")
            .layer("Entrypoints").definedBy("..entrypoints..")
            .layer("OpenapiGeneratedApiSources").definedBy("..api..")
            .layer("OpenapiGeneratedDtoSources").definedBy("..dto..");
    }

    @Test
    void coreShouldNotAccessAnyOtherLayer() {
        final ArchRule rule = noClasses()
            .that()
            .resideInAPackage("..core..")
            .should()
            .accessClassesThat()
            .resideInAnyPackage("..configuration..", "..dataproviders..", "..entrypoints..",
                "..api..", "..dto..");

        rule.check(importedClasses);
    }

    @Test
    void dataprovidersShouldOnlyBeAccessedByConfiguration() {
        final ArchRule rule = layeredArchitectureTemplate
            .whereLayer("Dataproviders")
            .mayOnlyBeAccessedByLayers("Configuration");

        rule.check(importedClasses);
    }

    @Test
    void entrypointsShouldOnlyBeAccessedByConfiguration() {
        final ArchRule rule = layeredArchitectureTemplate
            .whereLayer("Entrypoints")
            .mayOnlyBeAccessedByLayers("Configuration");

        rule.check(importedClasses);
    }

    @Test
    void configurationShouldNotBeAccessedByAnyLayer() {
        final ArchRule rule = layeredArchitectureTemplate
            .whereLayer("Configuration")
            .mayNotBeAccessedByAnyLayer();

        rule.check(importedClasses);
    }

    @ParameterizedTest
    @ValueSource(classes = {Component.class, Service.class, Controller.class, Repository.class})
    void coreClassCannotUseComponentBasedAnnotation(final Class<? extends Annotation> clazz) {
        final ArchRule rule = noClasses()
            .that()
            .resideInAPackage("..core..")
            .should()
            .beAnnotatedWith(clazz);

        rule.check(importedClasses);
    }
}
