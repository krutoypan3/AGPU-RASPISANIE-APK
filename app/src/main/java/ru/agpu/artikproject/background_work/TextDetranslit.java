package ru.agpu.artikproject.background_work;

public class TextDetranslit {
    /**
     * Если вы читаете это то значит и на вашем берегу попался *плохой* человек который вбивает
     * транслитом текст, который не стоило бы. Дай боже вам здоровья. Аминь!
     * @param text Текст, который вам нужно *исправить*
     * @return Исправленный текст
     */
    public String detranslit(String text){
        text = text.toUpperCase();
        text = text.replace("A", "А");
        text = text.replace("B", "В");
        text = text.replace("C", "С");
        text = text.replace("E", "Е");
        text = text.replace("H", "Н");
        text = text.replace("K", "К");
        text = text.replace("M", "М");
        text = text.replace("O", "О");
        text = text.replace("P", "Р");
        text = text.replace("T", "Т");
        text = text.replace("X", "Х");
        text = text.toLowerCase();
        return text;
    }
}
