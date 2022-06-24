package flab.project.jobfinder.service.parser.duedate;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

@Component
public class DueDateParser {

    public LocalDate parseDueDate(String dueDateStr, String parseFormat) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(parseFormat)
                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                .toFormatter(Locale.KOREA);
        LocalDate dueDate = LocalDate.parse(dueDateStr, formatter);
        if (dueDate.isBefore(LocalDate.now())) {
            dueDate = dueDate.plusYears(1);
        }
        return dueDate;
    }

    public boolean isAlwaysRecruiting(String dueDate, String alwaysRecruitFormat) {
        return alwaysRecruitFormat.equals(dueDate);
    }
}
