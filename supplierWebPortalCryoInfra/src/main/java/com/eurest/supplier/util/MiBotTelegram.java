package com.eurest.supplier.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class MiBotTelegram extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        // Devuelve el nombre de usuario de tu bot
        return "AlertSmartCryoBoot";
    }

    @Override
    public String getBotToken() {
        // Devuelve el token de acceso de tu bot
        return "7007783226:AAE13Ro4gBwF9RuxIEBKSfuQrABCZZRF49g";
    }

    // Método para enviar un mensaje al grupo
    public void enviarMensaje(String mensaje) {
        SendMessage message = new SendMessage();
        
        message.setChatId(getChatIdActual());
        message.setText(mensaje);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private  String getChatIdActual() {
		return "-4161710675";
	}

	public static void main(String[] args) {
        MiBotTelegram miBot = new MiBotTelegram();
        miBot.enviarMensaje("Hola, este es un mensaje enviado por mi bot.");
    }

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
	}
}