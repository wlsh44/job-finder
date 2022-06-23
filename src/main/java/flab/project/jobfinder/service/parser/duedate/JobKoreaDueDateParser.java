package flab.project.jobfinder.service.parser.duedate;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

@Component
public class JobKoreaDueDateParser implements DueDateParser {

    private final String ALWAYS_RECRUITING = "상시채용";
    private final String PARSE_FORMAT = "~MM/dd(E)";

    @Override
    public LocalDate parseDueDate(String dueDateStr) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(PARSE_FORMAT)
                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                .toFormatter(Locale.KOREA);
        LocalDate dueDate = LocalDate.parse(dueDateStr, formatter);
        if (dueDate.isBefore(LocalDate.now())) {
            dueDate = dueDate.plusYears(1);
        }
        return dueDate;
    }

    @Override
    public boolean isAlwaysRecruiting(String dueDate) {
        return ALWAYS_RECRUITING.equals(dueDate);
    }
}
