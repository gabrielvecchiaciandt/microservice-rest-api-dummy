package br.com.flowtechsolutions.dataproviders.database;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.dataproviders.database.entity.EmpresaEntity;
import br.com.flowtechsolutions.dataproviders.database.repository.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do EmpresaDataProvider usando Spring Data JPA.
 * O CNPJ é persistido como string de 14 dígitos numéricos (formato legado).
 */
public class EmpresaDataProviderImpl implements EmpresaDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaDataProviderImpl.class);

    private final EmpresaRepository empresaRepository;

    public EmpresaDataProviderImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa salvar(Empresa empresa) {
        logger.debug("Salvando empresa com CNPJ: {}", empresa.cnpj().soDigitos());
        EmpresaEntity entity = toEntity(empresa);
        EmpresaEntity salva = empresaRepository.save(entity);
        return toDomain(salva);
    }

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        logger.debug("Buscando empresa por CNPJ: {}", cnpj);
        return empresaRepository.findByCnpj(cnpj).map(this::toDomain);
    }

    @Override
    public Optional<Empresa> buscarPorId(Long id) {
        logger.debug("Buscando empresa por ID: {}", id);
        return empresaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Empresa> listarTodas() {
        logger.debug("Listando todas as empresas");
        return empresaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Empresa> listarAtivas() {
        logger.debug("Listando empresas ativas");
        return empresaRepository.findByAtivaTrue().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existePorCnpj(String cnpj) {
        return empresaRepository.existsByCnpj(cnpj);
    }

    private EmpresaEntity toEntity(Empresa empresa) {
        return new EmpresaEntity(
            empresa.id(),
            empresa.cnpj().soDigitos(),
            empresa.razaoSocial(),
            empresa.nomeFantasia(),
            empresa.email(),
            empresa.telefone(),
            empresa.ativa(),
            empresa.dataCadastro()
        );
    }

    private Empresa toDomain(EmpresaEntity entity) {
        return new Empresa(
            entity.getId(),
            new Cnpj(entity.getCnpj()),
            entity.getRazaoSocial(),
            entity.getNomeFantasia(),
            entity.getEmail(),
            entity.getTelefone(),
            entity.isAtiva(),
            entity.getDataCadastro()
        );
    }
}
