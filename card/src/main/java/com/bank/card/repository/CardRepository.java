package com.bank.card.repository;

import com.bank.card.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.smartcardio.Card;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Cards, Long> {
    Optional<Cards> findCardByMobileNumber(String mobileNumber);
    Optional<Cards> findCardByCardNumber(String cardNumber);
}
