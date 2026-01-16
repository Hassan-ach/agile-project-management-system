package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.common.request.InviteRequest;
import com.ensa.agile.application.common.request.RemoveRequest;
import com.ensa.agile.application.common.response.InviteResponse;
import com.ensa.agile.application.common.response.RemoveResponse;
import com.ensa.agile.application.product.request.ProductBackLogCreateRequest;
import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import com.ensa.agile.application.product.request.ProductBackLogUpdateRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.product.usecase.CreateProductBackLogUseCase;
import com.ensa.agile.application.product.usecase.GetProductBackLogUseCase;
import com.ensa.agile.application.product.usecase.InviteScrumMasterUseCase;
import com.ensa.agile.application.product.usecase.RemoveScrumMasterUseCase;
import com.ensa.agile.application.product.usecase.UpdateProductBackLogInfoUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product-backlog")
public class ProductBackLogController {

    private final CreateProductBackLogUseCase createProductBacklogUseCase;
    private final UpdateProductBackLogInfoUseCase updateProductBackLogUseCase;
    private final InviteScrumMasterUseCase inviteScrumMasterUseCase;
    private final RemoveScrumMasterUseCase removeScrumMasterUseCase;
    private final GetProductBackLogUseCase getProductBackLogUseCase;

    @PostMapping
    public ResponseEntity<ProductBackLogResponse>
    createProductBacklog(@RequestBody ProductBackLogCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createProductBacklogUseCase.executeTransactionally(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBackLogResponse> getProductBacklogById(
        @PathVariable UUID id,
        @RequestParam(name = "with", required = false) String with) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(getProductBackLogUseCase.execute(
                new ProductBackLogGetRequest(id, with)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@abacService.canAccessProject(#id,  'UPDATE')")
    public ResponseEntity<ProductBackLogResponse>
    updateProductBacklog(@PathVariable UUID id,
                         @RequestBody ProductBackLogUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(updateProductBackLogUseCase.executeTransactionally(
                new ProductBackLogUpdateRequest(id, request)));
    }

    @PostMapping("/{id}/invite/scrum-master")
    public ResponseEntity<InviteResponse>
    inviteScrumMaster(@RequestBody InviteRequest request,
                      @PathVariable UUID id) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(inviteScrumMasterUseCase.executeTransactionally(
                new InviteRequest(id, request)));
    }

    @PostMapping("/{id}/remove/scrum-master")
    public ResponseEntity<RemoveResponse>
    removeScrumeMaster(@RequestBody RemoveRequest request,
                       @PathVariable UUID id) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(removeScrumMasterUseCase.executeTransactionally(
                new RemoveRequest(id, request)));
    }
}
