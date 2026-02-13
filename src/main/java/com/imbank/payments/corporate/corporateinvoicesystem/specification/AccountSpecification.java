package com.imbank.payments.corporate.corporateinvoicesystem.specification;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {

    public static Specification<Account> hasAccountType(AccountType accountType) {
        return (root, query, criteriaBuilder) -> {
            if (accountType == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("accountType"), accountType);
        };
    }

    public static Specification<Account> hasStatus(AccountStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}