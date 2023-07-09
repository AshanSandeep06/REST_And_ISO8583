package lk.epic.springBoot_middleware.util;

import lk.epic.springBoot_middleware.entity.ISO8583Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ResponseUtil {
    private String state;
    private String message;
    private ISO8583Fields iso8583Fields;
}
