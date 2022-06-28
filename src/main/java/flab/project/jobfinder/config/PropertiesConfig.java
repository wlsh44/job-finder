package flab.project.jobfinder.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PropertiesConfig {
    protected String url;
    protected String searchUrl;
    protected String delimiter;
    protected String selector;
    protected String pageSelector;
    protected String dueDateFormat;
    protected String alwaysRecruitingFormat;
}
