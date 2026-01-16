package com.ensa.agile.application.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.application.product.request.ProductBackLogUpdateRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.product.usecase.UpdateProductBackLogInfoUseCase;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
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
                                             .id("Backlog123")
                                             .name("Old Name")
                                             .description("Old Description")
                                             .build();
        ProductBackLog updatedBackLog = ProductBackLog.builder()
                                            .id("Backlog123")
                                            .name("Updated Name")
                                            .description("Updated Description")
                                            .build();
        ProductBackLogUpdateRequest request = new ProductBackLogUpdateRequest(
            "Backlog123", "Updated Name", "Updated Description");

        when(productBackLogRepository.findById(request.getId()))
            .thenReturn(existingBackLog);

        when(productBackLogRepository.save(any(ProductBackLog.class)))
            .thenReturn(updatedBackLog);

        ProductBackLogResponse response =
            updateProductBackLogInfoUseCase.execute(request);

        assertNotNull(response);
        assertEquals("Updated Name", response.getName());
        assertEquals("Updated Description", response.getDescription());

        verify(productBackLogRepository, times(1)).findById(anyString());
        verify(productBackLogRepository, times(1))
            .save(any(ProductBackLog.class));
    }

    @Test
    void execute_ShouldThrowException_WhenProductBackLogNotFound() {
        ProductBackLogUpdateRequest request = new ProductBackLogUpdateRequest(
            "NonExistentID", "Updated Name", "Updated Description");

        when(productBackLogRepository.findById("NonExistentID"))
            .thenThrow(ProductBackLogNotFoundException.class);

        assertThrows(ProductBackLogNotFoundException.class,
                     () -> updateProductBackLogInfoUseCase.execute(request));

        verify(productBackLogRepository, times(1)).findById(anyString());
        verify(productBackLogRepository, times(0))
            .save(any(ProductBackLog.class));
    }
}
