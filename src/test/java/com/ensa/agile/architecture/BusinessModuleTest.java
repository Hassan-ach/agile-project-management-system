package com.ensa.agile.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.ensa.agile",
                importOptions = DoNotIncludeTests.class)
public class BusinessModuleTest {

    @ArchTest
    static final ArchRule sprint_related_classes_should_be_in_sprint_package =
        classes()
            .that()
            .haveNameMatching(".*Sprint(BackLog|History|Member).*")
            .and()
            .resideOutsideOfPackages("..global..", "..common..",
                                     "..controller..")
            .should()
            .resideInAPackage("..sprint..");

    @ArchTest
    static final ArchRule user_story_classes_should_be_in_story_package =
        classes()
            .that()
            .haveSimpleNameContaining("UserStory")
            .and()
            .resideOutsideOfPackages("..global..", "..common..",
                                     "..controller..")
            .should()
            .resideInAPackage("..story..");

    @ArchTest
    static final ArchRule task_classes_should_be_in_task_package =
        classes()
            .that()
            .haveSimpleNameContaining("Task")
            .and()
            .haveSimpleNameNotContaining("UserStory")
            .and()
            .resideOutsideOfPackages("..global..", "..common..",
                                     "..controller..")
            .should()
            .resideInAPackage("..task..");
}
