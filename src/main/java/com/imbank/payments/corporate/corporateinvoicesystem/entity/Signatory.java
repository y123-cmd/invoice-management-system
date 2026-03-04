package com.imbank.payments.corporate.corporateinvoicesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="signatory")
public class Signatory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long signatoryId;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false,length = 100)
    private String position;//CEO,CFO,CHIEF FINANCIAL OFFICER
    @Column(nullable = false, length = 100)
    private String email;
    @Column(length = 100)
    private String phone;
    @Column(length = 50)
    private String idNumber;
    @ManyToMany(mappedBy = "signatories")
    @JsonIgnore
    private List<Account> accounts;
}
