package flab.project.jobfinder.service.parser.duedate;

import java.time.LocalDate;

public interface DueDateParser {
    LocalDate parseDueDate(String dueDate);
    boolean isAlwaysRecruiting(String dueDate);
}
