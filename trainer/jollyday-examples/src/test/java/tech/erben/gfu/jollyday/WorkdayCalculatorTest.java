package tech.erben.gfu.jollyday;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class WorkdayCalculatorTest {

    private final WorkdayCalculator calculator = new WorkdayCalculator();

    @Test
    void countsWorkingDaysAcrossBavarianHoliday() {
        LocalDate start = LocalDate.of(2024, 4, 30);
        LocalDate end = LocalDate.of(2024, 5, 3);

        int days = calculator.workingDaysBetween(start, end);

        assertThat(days).isEqualTo(3);
    }

    @Test
    void countsWorkingDaysAcrossYearBoundary() {
        LocalDate start = LocalDate.of(2023, 12, 22);
        LocalDate end = LocalDate.of(2024, 1, 5);

        int days = calculator.workingDaysBetween(start, end);

        assertThat(days).isEqualTo(8);
    }

    @Test
    void returnsZeroWhenStartAfterEnd() {
        assertThat(calculator.workingDaysBetween(
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2024, 5, 31))).isZero();
    }
}
