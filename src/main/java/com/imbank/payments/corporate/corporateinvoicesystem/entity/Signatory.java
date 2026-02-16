package com.imbank.payments.corporate.corporateinvoicesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="signatory")
public class Signatory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long signatoryId;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false,length = 100)
    private String position;//CEO,CFO,FINANCIAL MANAGER
    @Column(nullable = false, length = 100)
    private String email;
    @Column(length = 100)
    private String phone;
    @Column(length = 50)
    private String idNumber;
    @ManyToMany(mappedBy = "signatories")
    @JsonIgnore
    private List<Account> accounts;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
