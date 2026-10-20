package medconnect.service;

import java.util.List;
import medconnect.exception.NaoEncontradoException;
import medconnect.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/** CRUD comum; cada serviço implementa apenas as regras de negócio em validar(). */
public abstract class CrudService<T extends Entidade> {

    protected final JpaRepository<T, Long> repo;

    protected CrudService(JpaRepository<T, Long> repo) { this.repo = repo; }

    /** Valida regras de negócio; o id do objeto é nulo em novos registros. */
    protected abstract void validar(T obj);

    public List<T> listar() { return repo.findAll(); }

    public T buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontradoException("Registro não encontrado"));
    }

    @Transactional
    public T salvar(T obj) {
        obj.setId(null);
        validar(obj);
        return repo.save(obj);
    }

    @Transactional
    public T atualizar(Long id, T obj) {
        buscar(id);
        obj.setId(id);
        validar(obj);
        return repo.save(obj);
    }

    @Transactional
    public void excluir(Long id) {
        buscar(id);
        repo.deleteById(id);
        repo.flush();
    }
}
