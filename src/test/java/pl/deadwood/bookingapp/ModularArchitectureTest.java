package pl.deadwood.bookingapp;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "pl.deadwood.bookingapp")
public class ModularArchitectureTest {

    @ArchTest
    public static final ArchRule reservation_should_not_depend_on_confirmation =
            noClasses()
                    .that()
                    .resideInAPackage("..reservation..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..confirmation..");

    @ArchTest
    public static final ArchRule screening_should_not_depend_on_reservation =
            noClasses()
                    .that()
                    .resideInAPackage("..screening..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..reservation..");

    @ArchTest
    public static final ArchRule screening_should_not_depend_on_confirmation =
            noClasses()
                    .that()
                    .resideInAPackage("..screening..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..confirmation..");
}
