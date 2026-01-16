package com.ensa.agile.application.product.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.application.product.request.ProductBackLogUpdateRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateProductBackLogUseCaseTest {
    @Mock private ITransactionalWrapper transactionalWrapper;
    @Mock private ProductBackLogRepository productBackLogRepository;

    @InjectMocks
    private UpdateProductBackLogInfoUseCase updateProductBackLogInfoUseCase;

    @Test
    void execute_ShouldUpdateProductBackLog_WhenDataIsValid() {
        //
        ProductBackLog existingBackLog = ProductBackLog.builder()
                                             .id(UUID.randomUUID())
                                             .name("Old Name")
                                             .description("Old Description")
                                             .build();
        ProductBackLog updatedBackLog = ProductBackLog.builder()
                                            .id(UUID.randomUUID())
                                            .name("Updated Name")
                                            .description("Updated Description")
                                            .build();
        ProductBackLogUpdateRequest request = new ProductBackLogUpdateRequest(
            UUID.randomUUID(), "Updated Name", "Updated Description");

        when(productBackLogRepository.findById(request.getId()))
            .thenReturn(existingBackLog);

        when(productBackLogRepository.save(any(ProductBackLog.class)))
            .thenReturn(updatedBackLog);

        ProductBackLogResponse response =
            updateProductBackLogInfoUseCase.execute(request);

        assertNotNull(response);
        assertEquals("Updated Name", response.getName());
        assertEquals("Updated Description", response.getDescription());

        verify(productBackLogRepository, times(1)).findById(any(UUID.class));
        verify(productBackLogRepository, times(1))
            .save(any(ProductBackLog.class));
    }

    @Test
    void execute_ShouldThrowException_WhenProductBackLogNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        ProductBackLogUpdateRequest request = new ProductBackLogUpdateRequest(
            nonExistentId, "Updated Name", "Updated Description");

        when(productBackLogRepository.findById(nonExistentId))
            .thenThrow(ProductBackLogNotFoundException.class);

        assertThrows(ProductBackLogNotFoundException.class,
                     () -> updateProductBackLogInfoUseCase.execute(request));

        verify(productBackLogRepository, times(1)).findById(nonExistentId);
        verify(productBackLogRepository, times(0))
            .save(any(ProductBackLog.class));
    }
}
