package kjstyle.jwtloginsample.sample;

import jakarta.validation.constraints.NotNull;

public class SampleDto {

    @NotNull
    private String subject;
    private String code;

    public SampleDto() {
    }

    public SampleDto(String subject, String code) {
        this.subject = subject;
        this.code = code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}