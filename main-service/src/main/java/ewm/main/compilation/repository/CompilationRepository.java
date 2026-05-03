package ewm.main.compilation.repository;

import ewm.main.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository  extends JpaRepository<Compilation, Long> {
}
