package ru.agpu.artikproject.background_work.main_show.fragments.zach_book;

public class GetMarkImageUrl {
    /**
     * Данный класс позволяет получить картинку на оценку (картинка с цифрой)
     * @param mark Оценка (тестовая) [отл\хор\зачет\...]
     * @return Ссылка на картинку с цифрой
     */
    public String get(String mark){
        switch (mark){
            case "зачет":
            case "Зачет":
                return "https://i.ibb.co/X5dthws/galochka.png";
            case "отл":
            case "Отл":
                return "https://i.ibb.co/HgfbMkV/5.png";
            case "хор":
            case "Хор":
                return "https://i.ibb.co/GT4X9Cd/4.png";
            case "удовл":
            case "Удовл":
                return "https://i.ibb.co/CK9KcNW/3.png";
            case "н/я":
            case "Н/я":
                return "https://i.ibb.co/NWhYkKF/krest.png";
            default:
                return "https://i.ibb.co/PZhZjkZ/agpu-ico.png";
        }
    }
}
