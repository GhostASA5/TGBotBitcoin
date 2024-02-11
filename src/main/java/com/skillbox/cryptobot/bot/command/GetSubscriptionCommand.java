package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.db.entity.Subscribers;
import com.skillbox.cryptobot.db.repository.SubscribersRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscribersRepo subscribersRepo;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        Long userId = message.getFrom().getId();
        Subscribers subscriber = subscribersRepo.findByTgID(userId);

        if (subscriber.getPrice() != null){
            answer.setText("Вы подписаны на стоимость биткоина " + subscriber.getPrice() + " USD");
        } else {
            answer.setText("Активные подписки отсутствуют");
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /get_subscription command", e);
        }
    }
}