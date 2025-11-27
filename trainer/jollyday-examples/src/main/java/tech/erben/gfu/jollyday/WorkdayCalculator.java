package tech.erben.gfu.jollyday;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Zählt Werktage unter Berücksichtigung der bayerischen Feiertage.
 */
public class WorkdayCalculator {

    private static final String BAVARIA_CODE = "by";

    private final HolidayManager holidayManager;
    private final Map<Integer, Set<LocalDate>> holidaysByYear = new HashMap<>();

    public WorkdayCalculator() {
        this(HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.GERMANY)));
    }

    WorkdayCalculator(HolidayManager holidayManager) {
        this.holidayManager = holidayManager;
    }

    /**
     * Zählt alle Werktage ab dem Tag nach {@code referenceDate} bis heute (einschließlich).
     */
    public int workingDaysSince(LocalDate referenceDate) {
        return workingDaysBetween(referenceDate.plusDays(1), LocalDate.now());
    }

    /**
     * Zählt die Werktage zwischen Start- und Enddatum (beide inklusive).
     * Gibt 0 zurück, wenn das Startdatum nach dem Enddatum liegt.
     */
    public int workingDaysBetween(LocalDate startDateInclusive, LocalDate endDateInclusive) {
        if (startDateInclusive.isAfter(endDateInclusive)) {
            return 0;
        }

        int workingDays = 0;
        LocalDate current = startDateInclusive;
        while (!current.isAfter(endDateInclusive)) {
            if (isWorkingDay(current)) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }

    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return false;
        }

        return !holidaysForYear(date.getYear()).contains(date);
    }

    private Set<LocalDate> holidaysForYear(int year) {
        return holidaysByYear.computeIfAbsent(year, y -> holidayManager.getHolidays(y, BAVARIA_CODE).stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet()));
    }
}
