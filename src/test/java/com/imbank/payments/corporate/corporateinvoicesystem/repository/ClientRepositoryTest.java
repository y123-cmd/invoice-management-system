package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")

public class ClientRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testFindAll_ShouldReturnAllClients_WhenClientsExists() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setEmail("corporate@safaricom.co.ke");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("5000000.00"));
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);

        CorporateClient corporateClient1 = new CorporateClient();
        corporateClient1.setCompanyName("national bank");
        corporateClient1.setRegistrationNumber("REG105");
        corporateClient1.setEmail("info@nationalbank.com");
        corporateClient1.setPhone("+254700000005");
        corporateClient1.setCreditLimit(new BigDecimal("5000000.00"));
        corporateClient1.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient1.setClientType(ClientType.RETAIL);
        corporateClient1.setDeleted(false);
        testEntityManager.persist(corporateClient1);
        testEntityManager.flush();

        List<CorporateClient> corporateClients = clientRepository.findAll();

        assertNotNull(corporateClients);
        assertEquals(2, corporateClients.size());

    }

    @Test
    void findAll_ShouldReturnEmpty_WhenNoClientsExists() {
        List<CorporateClient> corporateClients = clientRepository.findAll();
        assertNotNull(corporateClients);
        assertEquals(0, corporateClients.size());
    }

    @Test
    void findAll_ShouldNotReturnDeletedClients() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setEmail("corporate@safaricom.co.ke");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("5000000.00"));
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setDeleted(true);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();
        List<CorporateClient> corporateClients = clientRepository.findAll();
        assertNotNull(corporateClients);
        assertEquals(0, corporateClients.size());

    }

    @Test
    void findById_ShouldReturnClient_WhenClientExists() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setCompanyName("I&M");
        corporateClient.setRegistrationNumber("I&M-2026-005");
        corporateClient.setEmail("i&m@gmail.com");
        corporateClient.setPhone("+254722000078");
        corporateClient.setCreditLimit(new BigDecimal("9000000.00"));
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();

        Optional<CorporateClient> found = clientRepository.findById(corporateClient.getClientId());
        assertTrue(found.isPresent());
        assertEquals("I&M", found.get().getCompanyName());
        assertEquals("I&M-2026-005", found.get().getRegistrationNumber());

    }

    @Test
    void findById_ShouldReturnEmpty_WhenClientDoesNotExist() {
        Optional<CorporateClient> found = clientRepository.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findByAccountStatus_ShouldReturnActiveClients_WhenStatusisActive() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("9000000.00"));
        corporateClient.setEmail("saf@gmail.com");
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();

        CorporateClient corporateClient1 = new CorporateClient();
        corporateClient1.setAccountStatus(AccountStatus.SUSPENDED);
        corporateClient1.setClientType(ClientType.CORPORATE);
        corporateClient1.setCompanyName("Oreo");
        corporateClient1.setRegistrationNumber("Oreo-2026-001");
        corporateClient1.setPhone("+254722000890");
        corporateClient1.setCreditLimit(new BigDecimal("6000000.00"));
        corporateClient1.setEmail("oreo@gmail.com");
        corporateClient1.setDeleted(false);
        testEntityManager.persist(corporateClient1);
        testEntityManager.flush();

        List<CorporateClient> clients = clientRepository.findByAccountStatus(corporateClient.getAccountStatus());
        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals(AccountStatus.ACTIVE, clients.get(0).getAccountStatus());
    }

    @Test
    void findByAccountStatus_ShouldReturnEmpty_WhenNoClientsHaveStatus() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("9000000.00"));
        corporateClient.setEmail("saf@gmail.com");
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();

        List<CorporateClient> clients = clientRepository.findByAccountStatus(AccountStatus.CLOSED);

        assertNotNull(clients);
        assertEquals(0, clients.size());
    }

    @Test
    void findByRegistrationNumber_ShouldReturnClients_WhenRegistrationNumberExists() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("9000000.00"));
        corporateClient.setEmail("saf@gmail.com");
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();

        Optional<CorporateClient> found = clientRepository.findByRegistrationNumber(corporateClient.getRegistrationNumber());
        assertTrue(found.isPresent());
        assertEquals("SAF-2026-001", found.get().getRegistrationNumber());
    }

    @Test
    void findByRegistrationNumber_ShouldReturnEmpty_WhenRegistrationNumberDoesNotExist() {
        Optional<CorporateClient> found = clientRepository.findByRegistrationNumber("UNKNOWN-999");
        assertTrue(found.isEmpty());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("9000000.00"));
        corporateClient.setEmail("saf@gmail.com");
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();

        boolean exists = clientRepository.existsByEmail(corporateClient.getEmail());
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        boolean exists = clientRepository.existsByEmail("unknown@gmail.com");
        assertFalse(exists);
    }

    @Test
    void existsByRegistrationNumber_ShouldReturnTrue_WhenRegistrationNumberExists() {
        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setAccountStatus(AccountStatus.ACTIVE);
        corporateClient.setClientType(ClientType.CORPORATE);
        corporateClient.setCompanyName("Safaricom");
        corporateClient.setRegistrationNumber("SAF-2026-001");
        corporateClient.setPhone("+254722000000");
        corporateClient.setCreditLimit(new BigDecimal("9000000.00"));
        corporateClient.setEmail("saf@gmail.com");
        corporateClient.setDeleted(false);
        testEntityManager.persist(corporateClient);
        testEntityManager.flush();
        boolean exists = clientRepository.existsByRegistrationNumber("SAF-2026-001");
        assertTrue(exists);
    }

    @Test
    void existsByRegistrationNumber_ShouldReturnFalse_WhenRegistrationDoesNotExist() {
        boolean exists = clientRepository.existsByRegistrationNumber("SAF-2026-001");
        assertFalse(exists);
    }
}
