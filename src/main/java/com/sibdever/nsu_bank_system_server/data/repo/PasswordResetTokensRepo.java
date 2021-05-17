package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.Operator;
import com.sibdever.nsu_bank_system_server.data.model.PasswordResetTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokensRepo extends JpaRepository<PasswordResetTokens, Operator> {
    PasswordResetTokens findFirstByResetToken(String resetToken);
}
