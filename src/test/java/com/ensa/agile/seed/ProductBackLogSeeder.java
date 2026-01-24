package com.ensa.agile.seed;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.member.JpaProjectMemberRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestProjectMemberFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductBackLogSeeder implements Seeder {

    private final JpaProductBackLogRepository productRepository;
    private final JpaProjectMemberRepository projectMemberRepository;
    private final JpaUserRepository userRepository;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void seed() {
        if (productRepository.count() > 0) return;

        UserJpaEntity creator = userRepository.findAll().stream().findFirst()
            .orElseThrow(() -> new RuntimeException("No users found for product creation"));

        List<ProductBackLogJpaEntity> products = TestProductBackLogFactory.multipleJpaProducts(creator, 3);

        for(ProductBackLogJpaEntity product : products) {
        ProductBackLogJpaEntity pb = productRepository.save(product);
        projectMemberRepository.save(TestProjectMemberFactory.validJpaProjectMember(
            RoleType.PRODUCT_OWNER,
            MemberStatus.ACTIVE,
            pb,
            creator,
            creator
        ));
        }
    }
}

