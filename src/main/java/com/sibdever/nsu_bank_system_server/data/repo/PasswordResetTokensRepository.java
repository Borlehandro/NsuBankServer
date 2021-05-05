package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.Operator;
import com.sibdever.nsu_bank_system_server.data.model.PasswordResetTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokensRepository extends JpaRepository<PasswordResetTokens, Operator> {

//    @Query(value = "select tokens.operator from PasswordResetTokens tokens where tokens.resetToken = ?1")
//    List<Operator> findByValue(String token);
    PasswordResetTokens findFirstByResetToken(String resetToken);
}
