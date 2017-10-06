package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class EmployeeDTS extends AbstractDTS<Employee, eu.lpinto.universe.api.dto.Employee> {

    public static final EmployeeDTS T = new EmployeeDTS();

    @Override
    public eu.lpinto.universe.api.dto.Employee toAPI(Employee entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Employee(
                    entity.getExternalID(),
                    OrganizationDTS.id(entity.getOrganization()),
                    entity.getProfile() == null ? null : entity.getProfile().ordinal(),
                    UserDTS.id(entity.getUser()),
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());

        } else {
            return new eu.lpinto.universe.api.dto.Employee(
                    entity.getExternalID(),
                    OrganizationDTS.id(entity.getOrganization()),
                    entity.getProfile() == null ? null : entity.getProfile().ordinal(),
                    UserDTS.id(entity.getUser()),
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());
        }
    }

    @Override
    public Employee toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Employee(id);
    }

    @Override
    public Employee toDomain(eu.lpinto.universe.api.dto.Employee entity) {
        return new Employee(
                entity.getExternalID(),
                OrganizationDTS.T.toDomain(entity.getOrganization()),
                entity.getProfile() == null ? null : EmployeeProfile.values()[entity.getProfile()],
                UserDTS.T.toDomain(entity.getUser()),
                entity.getName(),
                UserDTS.T.toDomain(entity.getCreator()),
                entity.getCreated(),
                UserDTS.T.toDomain(entity.getUpdater()),
                entity.getUpdated(),
                entity.getId());
    }
}
