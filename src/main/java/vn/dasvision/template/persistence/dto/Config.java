package vn.dasvision.template.persistence.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Config {
    private int maxNumbersLoginAttempts;
    private int maxExpiredPassword;
}
