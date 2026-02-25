package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


    @DataJpaTest
    @ActiveProfiles("tests")
    public class SignatoryRepositoryTest {

        @Autowired
        private TestEntityManager testEntityManager;

        @Autowired
        private SignatoryRepository signatoryRepository;

        private Signatory createSignatory(String name, String email, String idNumber, boolean deleted) {
            Signatory signatory = new Signatory();
            signatory.setName(name);
            signatory.setPosition("CEO");
            signatory.setEmail(email);
            signatory.setIdNumber(idNumber);
            signatory.setDeleted(deleted);
            return testEntityManager.persist(signatory);
        }

        @Test
        void findAll_ShouldReturnAllSignatories_WhenSignatoriesExist() {
            createSignatory("John Doe", "john@gmail.com", "ID001", false);
            createSignatory("Jane Doe", "jane@gmail.com", "ID002", false);
            testEntityManager.flush();

            List<Signatory> signatories = signatoryRepository.findAll();

            assertNotNull(signatories);
            assertEquals(2, signatories.size());
        }

        @Test
        void findAll_ShouldReturnEmpty_WhenNoSignatoriesExist() {
            List<Signatory> signatories = signatoryRepository.findAll();

            assertNotNull(signatories);
            assertEquals(0, signatories.size());
        }

        @Test
        void findAll_ShouldNotReturnDeletedSignatories() {
            createSignatory("John Doe", "john@gmail.com", "ID001", true);
            testEntityManager.flush();

            List<Signatory> signatories = signatoryRepository.findAll();

            assertNotNull(signatories);
            assertEquals(0, signatories.size());
        }

        @Test
        void findById_ShouldReturnSignatory_WhenSignatoryExists() {
            Signatory signatory = createSignatory("John Doe", "john@gmail.com", "ID001", false);
            testEntityManager.flush();

            Optional<Signatory> found = signatoryRepository.findById(signatory.getSignatoryId());

            assertTrue(found.isPresent());
            assertEquals("John Doe", found.get().getName());
        }

        @Test
        void findById_ShouldReturnEmpty_WhenSignatoryDoesNotExist() {
            Optional<Signatory> found = signatoryRepository.findById(999L);

            assertTrue(found.isEmpty());
        }

        @Test
        void findByEmail_ShouldReturnSignatory_WhenEmailExists() {
            createSignatory("John Doe", "john@gmail.com", "ID001", false);
            testEntityManager.flush();

            Optional<Signatory> found = signatoryRepository.findByEmail("john@gmail.com");

            assertTrue(found.isPresent());
            assertEquals("john@gmail.com", found.get().getEmail());
        }

        @Test
        void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
            Optional<Signatory> found = signatoryRepository.findByEmail("unknown@gmail.com");

            assertTrue(found.isEmpty());
        }

        @Test
        void findByIdNumber_ShouldReturnSignatory_WhenIdNumberExists() {
            createSignatory("John Doe", "john@gmail.com", "ID001", false);
            testEntityManager.flush();

            Optional<Signatory> found = signatoryRepository.findByIdNumber("ID001");

            assertTrue(found.isPresent());
            assertEquals("ID001", found.get().getIdNumber());
        }

        @Test
        void findByIdNumber_ShouldReturnEmpty_WhenIdNumberDoesNotExist() {
            Optional<Signatory> found = signatoryRepository.findByIdNumber("UNKNOWN");

            assertTrue(found.isEmpty());
        }

        @Test
        void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
            createSignatory("John Doe", "john@gmail.com", "ID001", false);
            testEntityManager.flush();

            boolean exists = signatoryRepository.existsByEmail("john@gmail.com");

            assertTrue(exists);
        }

        @Test
        void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
            boolean exists = signatoryRepository.existsByEmail("unknown@gmail.com");

            assertFalse(exists);
        }

        @Test
        void existsByIdNumber_ShouldReturnTrue_WhenIdNumberExists() {
            createSignatory("John Doe", "john@gmail.com", "ID001", false);
            testEntityManager.flush();

            boolean exists = signatoryRepository.existsByIdNumber("ID001");

            assertTrue(exists);
        }

        @Test
        void existsByIdNumber_ShouldReturnFalse_WhenIdNumberDoesNotExist() {
            boolean exists = signatoryRepository.existsByIdNumber("UNKNOWN");

            assertFalse(exists);
        }
    }