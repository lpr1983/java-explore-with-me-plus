package ewm.main.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResultDto {
    @Valid
    private List<ParticipationRequestDto> confirmedRequests;

    @Valid
    private List<ParticipationRequestDto> rejectedRequests;
}
