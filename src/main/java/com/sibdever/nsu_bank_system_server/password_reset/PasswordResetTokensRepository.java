package com.sibdever.nsu_bank_system_server.password_reset;

import com.sibdever.nsu_bank_system_server.operator.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordResetTokensRepository extends JpaRepository<PasswordResetTokens, Operator> {

//    @Query(value = "select tokens.operator from PasswordResetTokens tokens where tokens.resetToken = ?1")
//    List<Operator> findByValue(String token);
    PasswordResetTokens findFirstByResetToken(String resetToken);
}
