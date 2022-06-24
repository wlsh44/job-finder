package flab.project.jobfinder.service.parser.duedate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DueDateParserTest {

    DueDateParser dueDateParser = new DueDateParser();

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("provideFormat")
    @DisplayName("모집 기간 올해인 경우")
    void parsingDueDateTest(String platform, String parseFormat, String dueDateFormat) {
        //given
        LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        String dueDateStr = String.format(dueDateFormat, nextSunday.getMonthValue(), nextSunday.getDayOfMonth());

        //when
        LocalDate dueDate = dueDateParser.parseDueDate(dueDateStr, parseFormat);

        //then
        assertThat(dueDate).isEqualTo(nextSunday);
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("provideFormat")
    @DisplayName("모집 기간 내년인 경우")
    void parsingNextYearDueDateTest(String platform, String parseFormat, String dueDateFormat) {
        //given
        LocalDate lastSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        String dueDateStr = String.format(dueDateFormat, lastSunday.getMonthValue(), lastSunday.getDayOfMonth());

        //when
        LocalDate dueDate = dueDateParser.parseDueDate(dueDateStr, parseFormat);

        //then
        assertThat(dueDate).isEqualTo(lastSunday.plusYears(1));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("provideAlwaysRecruit")
    @DisplayName("상시 채용 테스트")
    void alwaysRecruitTest(String platform, String dueDateStr, String alwaysRecruitFormat, boolean res) {
        //given

        //when
        boolean alwaysRecruiting = dueDateParser.isAlwaysRecruiting(dueDateStr, alwaysRecruitFormat);

        //then
        assertThat(alwaysRecruiting).isEqualTo(res);
    }

    private Stream<Arguments> provideAlwaysRecruit() {
        return Stream.of(
                Arguments.of("잡코리아 성공", "상시채용", "상시채용", true),
                Arguments.of("잡코리아 실패", "상시채용x", "상시채용", false),
                Arguments.of("로켓펀치 성공", "수시채용", "수시채용", true),
                Arguments.of("로켓펀치 실패", "수시채용x", "수시채용", false)
        );
    }

    private Stream<Arguments> provideFormat() {
        return Stream.of(
                Arguments.of("잡코리아 파싱", "~MM/dd(E)", "~%02d/%02d(일)"),
                Arguments.of("로켓펀치 파싱", "MM/dd 마감", "%02d/%02d 마감")
        );
    }
//
//    @Nested
//    @DisplayName("잡코리아 모집 기간 테스트")
//    class JobKoreaTest {
//
//        String alwaysRecruit = "상시채용";
//        String parseFormat = "~MM/dd(E)";
//
//        @Test
//        @DisplayName("상시 채용 테스트")
//        void alwaysRecruitTest() {
//            //given
//            String dueDateStr = "상시채용";
//
//            //when
//            boolean alwaysRecruiting = dueDateParser.isAlwaysRecruiting(dueDateStr, alwaysRecruit);
//
//            //then
//            assertThat(alwaysRecruiting).isTrue();
//        }
//
//        @Test
//        @DisplayName("상시 채용 아닌 경우")
//        void notAlwaysRecruitTest() {
//            //given
//            String dueDateStr = "상시채용x";
//
//            //when
//            boolean alwaysRecruiting = dueDateParser.isAlwaysRecruiting(dueDateStr, alwaysRecruit);
//
//            //then
//            assertThat(alwaysRecruiting).isFalse();
//        }
//
//        @Test
//        @DisplayName("모집 기간 파싱")
//        void parsingDueDateTest() {
//            //given
//            LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
//            String dueDateStr = String.format("~%02d/%02d(일)", nextSunday.getMonthValue(), nextSunday.getDayOfMonth());
//
//            //when
//            LocalDate dueDate = dueDateParser.parseDueDate(dueDateStr, parseFormat);
//
//            //then
//            assertThat(dueDate).isEqualTo(nextSunday);
//        }
//
//        @Test
//        @DisplayName("모집 기간 내년인 경우")
//        void parsingNextYearDueDateTest() {
//            //given
//            LocalDate lastSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
//            String dueDateStr = String.format("~%02d/%02d(일)", lastSunday.getMonthValue(), lastSunday.getDayOfMonth());
//
//            //when
//            LocalDate dueDate = dueDateParser.parseDueDate(dueDateStr, parseFormat);
//
//            //then
//            assertThat(dueDate).isEqualTo(lastSunday.plusYears(1));
//        }
//    }
//
//    @Nested
//    @DisplayName("로켓펀치 모집 기간 테스트")
//    class RocketPunchTest {
//
//        String alwaysRecruit = "수시채용";
//        String parseFormat = "MM/dd 마감";
//
//        @Test
//        @DisplayName("상시 채용 테스트")
//        void alwaysRecruitTest() {
//            //given
//            String dueDateStr = "수시채용";
//
//            //when
//            boolean alwaysRecruiting = dueDateParser.isAlwaysRecruiting(dueDateStr, alwaysRecruit);
//
//            //then
//            assertThat(alwaysRecruiting).isTrue();
//        }
//
//        @Test
//        @DisplayName("상시 채용 아닌 경우")
//        void notAlwaysRecruitTest() {
//            //given
//            String dueDateStr = "상시채용x";
//
//            //when
//            boolean alwaysRecruiting = dueDateParser.isAlwaysRecruiting(dueDateStr, alwaysRecruit);
//
//            //then
//            assertThat(alwaysRecruiting).isFalse();
//        }
//
//        @Test
//        @DisplayName("모집 기간 파싱")
//        void parsingDueDateTest() {
//            //given
//            LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
//            String dueDateStr = String.format("%02d/%02d 마감", nextSunday.getMonthValue(), nextSunday.getDayOfMonth());
//
//            //when
//            LocalDate dueDate = dueDateParser.parseDueDate(dueDateStr, parseFormat);
//
//            //then
//            assertThat(dueDate).isEqualTo(nextSunday);
//        }
//
//        @Test
//        @DisplayName("모집 기간 내년인 경우")
//        void parsingNextYearDueDateTest() {
//            //given
//            LocalDate lastSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
//            String dueDateStr = String.format("%02d/%02d 마감", lastSunday.getMonthValue(), lastSunday.getDayOfMonth());
//
//            //when
//            LocalDate dueDate = dueDateParser.parseDueDate(dueDateStr, parseFormat);
//
//            //then
//            assertThat(dueDate).isEqualTo(lastSunday.plusYears(1));
//        }
//    }

}