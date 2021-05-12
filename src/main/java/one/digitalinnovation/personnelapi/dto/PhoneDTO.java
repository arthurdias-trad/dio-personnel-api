package one.digitalinnovation.personnelapi.dto;

import lombok.*;
import one.digitalinnovation.personnelapi.enums.PhoneType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PhoneDTO {

    private Long id;

    @Enumerated(EnumType.STRING)
    private PhoneType type;

    @NotEmpty
    @Size(min=7, max=14)
    private String number;
}
