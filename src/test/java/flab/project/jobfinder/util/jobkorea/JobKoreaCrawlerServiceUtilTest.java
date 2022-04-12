//package flab.project.jobfinder.util.jobkorea;
//
//import flab.project.jobfinder.dto.DetailedSearchDto;
//import flab.project.jobfinder.enums.CareerType;
//import flab.project.jobfinder.enums.JobType;
//import flab.project.jobfinder.enums.Location;
//import flab.project.jobfinder.enums.PayType;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//import static flab.project.jobfinder.enums.CareerType.*;
//import static flab.project.jobfinder.enums.CareerType.ANY;
//import static flab.project.jobfinder.enums.JobType.*;
//import static flab.project.jobfinder.enums.JobType.MILITARY;
//import static flab.project.jobfinder.enums.Location.*;
//import static flab.project.jobfinder.enums.Location.JONGNO;
//import static flab.project.jobfinder.enums.PayType.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//class JobKoreaCrawlerServiceUtilTest {
//
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @Nested
//    @DisplayName("getSearchText 메서드 테스트")
//    class getSearchTextTest {
//
//        private Stream<Arguments> provideSearchText() {
//            return Stream.of(
//                    Arguments.of("spring", "stext=spring"),
//                    Arguments.of("spring boot", "stext=spring+boot"),
//                    Arguments.of("docker", "stext=docker")
//            );
//        }
//
//        @ParameterizedTest(name = "{index} => {0} = {1}")
//        @MethodSource("provideSearchText")
//        @DisplayName("정상 url 리턴")
//        void getSearchText테스트_정상출력해야됨(String searchText, String expected) {
//            DetailedSearchDto dto = DetailedSearchDto.builder().searchText(searchText).build();
//
//            String url = JobKoreaCrawlerServiceUtil.toSearchTextParam(dto);
//
//            assertThat(url).isEqualTo(expected);
//        }
//    }
//
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @Nested
//    @DisplayName("getLocation 메서드 테스트")
//    class getLocationTest {
//
//        private Stream<Arguments> provideLocation() {
//            return Stream.of(
//                    Arguments.of(List.of(GANGNAM, GANGDONG), "&local=I010%2CI020"),
//                    Arguments.of(List.of(SEOCHO), "&local=I150"),
//                    Arguments.of(List.of(BUNDANG, YONGSAN, JONGNO), "&local=B150%2CI210%2CI230")
//            );
//        }
//
//        @ParameterizedTest(name = "{index} => {0} = {1}")
//        @MethodSource("provideLocation")
//        @DisplayName("정상 url 리턴")
//        void getLocation테스트_정상출력해야됨(List<Location> locations, String expected) {
//            DetailedSearchDto dto = DetailedSearchDto.builder().location(locations).build();
//
//            String url = JobKoreaCrawlerServiceUtil.toLocationParam(dto);
//
//            assertThat(url).isEqualTo(expected);
//        }
//    }
//
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @Nested
//    @DisplayName("getJobType 메서드 테스트")
//    class getJobTypeTest {
//
//        private Stream<Arguments> provideJob() {
//            return Stream.of(
//                    Arguments.of(List.of(FULL_TIME, TEMPORARY), "&jobtype=1%2C2"),
//                    Arguments.of(List.of(FULL_TIME, FREELANCER), "&jobtype=1%2C6"),
//                    Arguments.of(List.of(FULL_TIME, INTERN, MILITARY), "&jobtype=1%2C3%2C9")
//            );
//        }
//
//        @ParameterizedTest(name = "{index} => {0} = {1}")
//        @MethodSource("provideJob")
//        @DisplayName("정상 url 리턴")
//        void getLocation테스트_정상출력해야됨(List<JobType> jobType, String expected) {
//            DetailedSearchDto dto = DetailedSearchDto.builder().jobType(jobType).build();
//
//            String url = JobKoreaCrawlerServiceUtil.toJobTypeParam(dto);
//
//            assertThat(url).isEqualTo(expected);
//        }
//    }
//
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @Nested
//    @DisplayName("getCareer 메서드 테스트")
//    class getCareerTest {
//
//        private Stream<Arguments> provideCareer() {
//            return Stream.of(
//                    Arguments.of(JUNIOR, "1", "2", "&careerType=1&careerMin=1&careerMax=2"),
//                    Arguments.of(SENIOR, "5", "6", "&careerType=2&careerMin=5&careerMax=6"),
//                    Arguments.of(SENIOR, null, "10", "&careerType=2&careerMax=10"),
//                    Arguments.of(ANY, "3", null, "&careerType=4&careerMin=3"),
//                    Arguments.of(ANY, null, null, "&careerType=4")
//            );
//        }
//
//        @ParameterizedTest(name = "{index} => {0}, {1}, {2} = {3}")
//        @MethodSource("provideCareer")
//        @DisplayName("정상 url 리턴")
//        void getCareer테스트_정상출력해야됨(CareerType careerType, String careerMin, String careerMax, String expected) {
//            DetailedSearchDto dto = DetailedSearchDto.builder().career(new DetailedSearchDto.Career(careerType, careerMin, careerMax)).build();
//
//            String url = JobKoreaCrawlerServiceUtil.toCareerParam(dto);
//
//            assertThat(url).isEqualTo(expected);
//        }
//    }
//
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @Nested
//    @DisplayName("getPay 메서드 테스트")
//    class getPayTest {
//
//        private Stream<Arguments> providePay() {
//            return Stream.of(
//                    Arguments.of(ANNUAL, "1000", "2000", "&payType=1&payMin=1000&payMax=2000"),
//                    Arguments.of(ANNUAL, "5000", "6000", "&payType=1&payMin=5000&payMax=6000"),
//                    Arguments.of(MONTH, null, "3000", "&payType=2&payMax=3000"),
//                    Arguments.of(WEEK, "30", null, "&payType=3&payMin=30"),
//                    Arguments.of(WEEK, null, "50", "&payType=3&payMax=50"),
//                    Arguments.of(MONTH, null, null, "&payType=2")
//            );
//        }
//
//        @ParameterizedTest(name = "{index} => {0}, {1}, {2} = {3}")
//        @MethodSource("providePay")
//        @DisplayName("정상 url 리턴")
//        void getPay테스트_정상출력해야됨(PayType payType, String payMin, String payMax, String expected) {
//            DetailedSearchDto dto = DetailedSearchDto.builder().pay(new DetailedSearchDto.Pay(payType, payMin, payMax)).build();
//
//            String url = JobKoreaCrawlerServiceUtil.toPayParam(dto);
//
//            assertThat(url).isEqualTo(expected);
//        }
//    }
//
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @Nested
//    @DisplayName("빈 문자열 리턴")
//    class blankReturnTest {
//
//        private Stream<Arguments> provideMethod() {
//            return Stream.of(
//                    Arguments.of("getSearchText"),
//                    Arguments.of("getLocation"),
//                    Arguments.of("getPay"),
//                    Arguments.of("getCareer"),
//                    Arguments.of("getJobType")
//            );
//        }
//
//        @ParameterizedTest(name = "{index} => {0}")
//        @MethodSource("provideMethod")
//        void 빈_문자열_리턴해야됨(String methodName) {
//            JobKoreaCrawlerServiceUtil jobKoreaCrawlerServiceUtil = new JobKoreaCrawlerServiceUtil();
//            DetailedSearchDto dto = DetailedSearchDto.builder().build();
//
//            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerServiceUtil, methodName, dto);
//
//            assertThat(url).isEqualTo("");
//        }
//    }
//
//}