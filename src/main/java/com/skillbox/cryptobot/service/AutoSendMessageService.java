package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.db.entity.Subscribers;
import com.skillbox.cryptobot.db.repository.SubscribersRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class AutoSendMessageService {

    @Value("${telegram.bot.notify.delay.value}")
    private final Integer frequency;
    private final DefaultAbsSender sender;
    private final SubscribersRepo subscribersRepo;
    private final CryptoCurrencyService service;

    @Scheduled(fixedRateString = "${telegram.bot.notify.delay.valueScheduled}")
    public void autoSendMessage() throws IOException {

        Integer price = (int) service.getBitcoinPrice();
        List<Subscribers> subscribers = subscribersRepo.findWherePriceLower(price,
                LocalDateTime.now().minusMinutes(frequency));

        for (Subscribers subscriber : subscribers){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(subscriber.getTgID());
            sendMessage.setText("Пора покупать, стоимость биткоина " + price);

            subscriber.setLastTimeSendMessage(LocalDateTime.now());
            subscribersRepo.save(subscriber);

            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
