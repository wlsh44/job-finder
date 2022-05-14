package flab.project.jobfinder.exception;

public class CrawlFailedException extends RuntimeException {

    public CrawlFailedException(String errorMsg) {
        super(errorMsg);
    }
}
