package ewm.main.request.repository;

import ewm.main.request.model.ParticipationRequest;
import ewm.main.request.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    // все заявки конкретного пользователя
    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    // проверка дубля: один юзер — одна заявка на событие
    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    // количество подтверждённых заявок на событие (для проверки лимита)
    long countByEventIdAndStatus(Long eventId, RequestStatus status);
}