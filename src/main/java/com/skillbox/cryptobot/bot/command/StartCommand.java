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

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {

    private final SubscribersRepo subscribersRepo;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        Long userId = message.getFrom().getId();
        UUID uuid = UUID.randomUUID();

        if (subscribersRepo.findByTgID(userId) == null){
            Subscribers subscriber = new Subscribers();
            subscriber.setUUID(String.valueOf(uuid));
            subscriber.setTgID(userId);
            subscriber.setPrice(null);
            subscriber.setLastTimeSendMessage(LocalDateTime.now());
            subscribersRepo.save(subscriber);
        }

        answer.setText("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина
                 /subscribe число - подписка на нужный курс биткоина
                 /unsubscribe - удаление подписки
                 /get_subscription - получить информацию о вашей подписке
                """);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}