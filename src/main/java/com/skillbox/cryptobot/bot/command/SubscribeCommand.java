package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.db.entity.Subscribers;
import com.skillbox.cryptobot.db.repository.SubscribersRepo;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@AllArgsConstructor
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private final SubscribersRepo subscribersRepo;
    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        if (arguments.length == 0 || !arguments[0].matches("\\d+")){
            answer.setText("Введена не правильная команда. Удостовертесь в правильности числа");
        } else {
            Long userId = message.getFrom().getId();
            Subscribers subscriber = subscribersRepo.findByTgID(userId);

            subscriber.setPrice(Integer.valueOf(arguments[0]));
            subscribersRepo.save(subscriber);

            GetPriceCommand getPriceCommand = new GetPriceCommand(service);
            getPriceCommand.processMessage(absSender, message, arguments);

            answer.setText("Новая подписка создана на стоимость " + arguments[0] + " USD");
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}