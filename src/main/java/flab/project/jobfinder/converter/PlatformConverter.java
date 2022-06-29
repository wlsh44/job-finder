package flab.project.jobfinder.converter;

import flab.project.jobfinder.enums.Platform;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;

@Slf4j
public class PlatformConverter implements AttributeConverter<Platform, String> {
    @Override
    public String convertToDatabaseColumn(Platform attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Platform 값은 null일 수 없습니다.");
        }
        return attribute.koreaName();
    }

    @Override
    public Platform convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(Platform.class).stream()
                .filter(platform -> platform.koreaName().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("올바르지 않은 플랫폼 명(%s)이 저장되어있습니다.", dbData)));
    }
}
