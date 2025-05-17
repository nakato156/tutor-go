package tutorgo.com.tutorgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.dto.response.TutorListResponse;
import tutorgo.com.tutorgo.dto.response.TutorResponse;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.repository.TutorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public TutorListResponse obtenerTutores(int pagina, int tamanio) {
        Pageable paginacion = PageRequest.of(pagina, tamanio);
        Page<Tutor> tutoresPage = tutorRepository.findAll(paginacion);

        if (tutoresPage.isEmpty()) {
            return TutorListResponse.emptyList();
        }

        List<TutorResponse> tutores = tutoresPage.getContent().stream()
                .map(tutor -> new TutorResponse(
                        tutor.getId(),
                        tutor.getUsuario().getNombre(),
                        tutor.getRubro(),
                        tutor.getEstrellasPromedio(),
                        tutor.getUsuario().getFotoUrl(),
                        tutor.getTarifaHora() != null ? tutor.getTarifaHora().doubleValue() : null
                ))
                .collect(Collectors.toList());

        return new TutorListResponse(
                tutores,
                tutoresPage.getNumber(),
                tutoresPage.getTotalPages(),
                tutoresPage.getTotalElements(),
                false
        );
    }
}
