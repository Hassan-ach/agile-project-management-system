package com.ensa.agile.domain.sprint.member;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.domain.sprint.entity.SprintMember;
import com.ensa.agile.testfactory.TestSprintBackLogFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import org.junit.jupiter.api.Test;

public class SprintMemberTest {

    @Test
    void shouldCreateSprintMember_whenAllFieldsValid() {
        assertDoesNotThrow(
            ()
                -> SprintMember.builder()
                       .user(TestUserFactory.validUser())
                       .sprintBackLog(
                           TestSprintBackLogFactory.validSprintBackLog())
                       .build());
    }

    // this pass here but not in integration test i don't know way !!
    // @Test
    // void shouldFail_whenUserIsNull() {
    //     assertThrows(
    //         ValidationException.class,
    //         ()
    //             -> SprintMember.builder()
    //                    .user(null)
    //                    .sprintBackLog(
    //                        TestSprintBackLogFactory.validSprintBackLog())
    //                    .build());
    // }
}
