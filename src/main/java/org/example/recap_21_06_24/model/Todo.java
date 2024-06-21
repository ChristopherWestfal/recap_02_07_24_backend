package org.example.recap_21_06_24.model;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@With
public record Todo(
        @Id
        String id,
        String description,
        Status status) {
}
