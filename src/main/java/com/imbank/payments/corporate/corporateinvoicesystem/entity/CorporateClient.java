package com.imbank.payments.corporate.corporateinvoicesystem.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

import java.math.BigDecimal;
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "corporate_client")
public class CorporateClient extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;
    @Column(name = "registration_number",unique = true,nullable = false,length = 100)
    private String registrationNumber;
    @Column(name = "email",nullable = false,length = 100)
    private String email;
    @Column(name = "phone",length = 20)
    private String phone;
    @Column(name = "credit_limit",nullable = false,precision = 15,scale = 2)
    private BigDecimal creditLimit;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status",nullable = false)
    private AccountStatus accountStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    private ClientType clientType;
    @OneToMany(mappedBy = "client")
    private List<Account> accounts;

}