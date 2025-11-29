package com.dqhieuse.sportbookingbackend.modules.wallet.service;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.wallet.entity.Wallet;
import com.dqhieuse.sportbookingbackend.modules.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public void createWallet(User user) {
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        walletRepository.save(wallet);
    }
}
