package com.ensa.agile.seed;

import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.member.JpaProjectMemberRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.member.ProjectMemberJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestProjectMemberFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMemberSeeder implements Seeder {

    private final JpaProjectMemberRepository memberRepository;
    private final JpaProductBackLogRepository productRepository;
    private final JpaUserRepository userRepository;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public void seed() {
        List<ProductBackLogJpaEntity> products = productRepository.findAll();
        List<UserJpaEntity> users = userRepository.findAll();
        
        if (products.isEmpty() || users.isEmpty()) return;

        UserJpaEntity admin = users.get(0);

        products.forEach(product -> {
            // Add all users to all products for testing
            users.forEach(user -> {
                ProjectMemberJpaEntity member = TestProjectMemberFactory.validJpaProjectMember(
                    RoleType.DEVELOPER, 
                    MemberStatus.ACTIVE, 
                    product, 
                    user, 
                    admin
                );
                memberRepository.save(member);
            });
        });
    }
}
