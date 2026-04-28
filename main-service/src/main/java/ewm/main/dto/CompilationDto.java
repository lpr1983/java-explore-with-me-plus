package ewm.main.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    @Valid
    private Set<EventShortDto> events;

    @NotNull
    private Long id;

    @NotNull
    private Boolean pinned;

    @NotBlank
    private String title;
}
