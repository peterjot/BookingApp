package pl.deadwood.bookingapp;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = {
        "pl.deadwood.bookingapp.screening",
        "pl.deadwood.bookingapp.reservation",
        "pl.deadwood.bookingapp.confirmation"})
public class ModuleArchitectureTest {

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_infrastructure =
            noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..infrastructure..");

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_application =
            noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..application..");
}