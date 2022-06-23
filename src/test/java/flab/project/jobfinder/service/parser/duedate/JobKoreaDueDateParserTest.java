package flab.project.jobfinder.service.parser.duedate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JobKoreaDueDateParserTest {

    DueDateParser jobKoreaDueDateParser = new JobKoreaDueDateParser();

    @Test
    @DisplayName("상시 채용 테스트")
    void alwaysRecruitTest() {
        //given
        String dueDateStr = "상시채용";

        //when
        boolean alwaysRecruiting = jobKoreaDueDateParser.isAlwaysRecruiting(dueDateStr);

        //then
        assertThat(alwaysRecruiting).isTrue();
    }

    @Test
    @DisplayName("상시 채용 아닌 경우")
    void notAlwaysRecruitTest() {
        //given
        String dueDateStr = "상시채용x";

        //when
        boolean alwaysRecruiting = jobKoreaDueDateParser.isAlwaysRecruiting(dueDateStr);

        //then
        assertThat(alwaysRecruiting).isFalse();
    }

    @Test
    @DisplayName("모집 기간 파싱")
    void parsingDueDateTest() {
        //given
        LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        String dueDateStr = String.format("~%02d/%02d(일)", nextSunday.getMonthValue(), nextSunday.getDayOfMonth());

        //when
        LocalDate dueDate = jobKoreaDueDateParser.parseDueDate(dueDateStr);

        //then
        assertThat(dueDate).isEqualTo(nextSunday);
    }

    @Test
    @DisplayName("모집 기간 내년인 경우")
    void parsingNextYearDueDateTest() {
        //given
        LocalDate lastSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        String dueDateStr = String.format("~%02d/%02d(일)", lastSunday.getMonthValue(), lastSunday.getDayOfMonth());

        //when
        LocalDate dueDate = jobKoreaDueDateParser.parseDueDate(dueDateStr);

        //then
        assertThat(dueDate).isEqualTo(lastSunday.plusYears(1));
    }
}