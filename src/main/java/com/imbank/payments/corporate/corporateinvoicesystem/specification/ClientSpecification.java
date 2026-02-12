package com.imbank.payments.corporate.corporateinvoicesystem.specification;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {

    public static Specification<CorporateClient> hasAccountStatus(AccountStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("accountStatus"), status);
        };
    }

    public static Specification<CorporateClient> hasClientType(ClientType clientType) {
        return (root, query, criteriaBuilder) -> {
            if (clientType == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("clientType"), clientType);
        };
    }

    public static Specification<CorporateClient> hasCompanyNameContaining(String companyName) {
        return (root, query, criteriaBuilder) -> {
            if (companyName == null || companyName.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("companyName")),
                    "%" + companyName.toLowerCase() + "%"
            );
        };
    }
}