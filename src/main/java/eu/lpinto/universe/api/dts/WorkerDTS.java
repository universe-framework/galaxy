package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.api.dto.WorkerDTO;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.Worker;

/**
 *
 * @author Rita
 */
public class WorkerDTS extends AbstractDTS<Worker, WorkerDTO> {

    public static final WorkerDTS T = new WorkerDTS();

    @Override
    protected WorkerDTO buildDTO(Worker entity) {
        if (entity == null) {
            return null;
        } else if (entity.isFull()) {
            return new WorkerDTO(
                    entity.getProfessionalLetterNumber(),
                    entity.getBirthdate(),
                    AbstractDTS.toApiID(entity.getOrganization()),
                    AbstractDTS.toApiID(entity.getEmployee()),
                    entity.getEnable(),
                    entity.getExternalID(),
                    entity.getAddress(),
                    entity.getPhone(),
                    entity.getMobilePhone(),
                    entity.getEmail(),
                    entity.getRole() == null ? null : entity.getRole().ordinal()
            );

        } else {
            return new WorkerDTO(
                    entity.getProfessionalLetterNumber(),
                    entity.getBirthdate(),
                    AbstractDTS.toApiID(entity.getOrganization()),
                    AbstractDTS.toApiID(entity.getEmployee()),
                    entity.getEnable(),
                    entity.getExternalID(),
                    entity.getAddress(),
                    entity.getPhone(),
                    entity.getMobilePhone(),
                    entity.getEmail(),
                    entity.getRole() == null ? null : entity.getRole().ordinal()
            );
        }
    }

    @Override
    public Worker toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Worker(id);
    }

    @Override
    protected Worker buildEntity(WorkerDTO dto) {
        return new Worker(
                dto.getProfessionalLetterNumber(),
                dto.getBirthdate(),
                dto.getOrganization() == null ? null : new Organization(dto.getOrganization()),
                dto.getEmployee() == null ? null : new Employee(dto.getEmployee()),
                dto.getEnable(),
                dto.getExternalID(),
                dto.getAddress(),
                dto.getPhone(),
                dto.getMobilePhone(),
                dto.getEmail(),
                dto.getRole() == null ? null : eu.lpinto.universe.persistence.entities.WorkerProfile.values()[dto.getRole()],
                dto.getName(),
                UserDTS.T.toDomain(dto.getCreator()),
                dto.getCreated(),
                UserDTS.T.toDomain(dto.getUpdater()),
                dto.getUpdated(),
                dto.getId());
    }

}
