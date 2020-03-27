package pt.tech4covid;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pt.tech4covid");

        noClasses()
            .that()
                .resideInAnyPackage("pt.tech4covid.service..")
            .or()
                .resideInAnyPackage("pt.tech4covid.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..pt.tech4covid.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
