package flab.project.jobfinder.dto;

import flab.project.jobfinder.enums.CareerType;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.PayType;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public abstract class DetailedSearchDto {

    protected final String searchText;
    protected final List<Location> location;
    protected final List<JobType> jobType;
    protected final Pay pay;
    protected final Career career;

    @Getter
    public static class Pay {
        private final PayType payType;
        private final String payMin;
        private final String payMax;

        public Pay(PayType payType, String payMin, String payMax) {
            this.payType = payType;
            this.payMin = payMin;
            this.payMax = payMax;
        }
    }

    @Getter
    public static class Career {
        private final CareerType careerType;
        private final String careerMin;
        private final String careerMax;

        public Career(CareerType careerType, String careerMin, String careerMax) {
            this.careerType = careerType;
            this.careerMin = careerMin;
            this.careerMax = careerMax;
        }
    }

    public abstract String toQueryParams();
}
