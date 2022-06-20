package ru.agpu.artikproject.background_work.rasp_show.recycler_view;

public class RecyclerViewItems {

    public RecyclerViewItems(String card_para_number, String card_para_time, String card_para_name, String card_para_aud, String card_para_prepod, int para_num_and_time_layout_color, int para_description_layout_color) {
        this.card_para_number = card_para_number;
        this.card_para_time = card_para_time;
        this.card_para_name = card_para_name;
        this.card_para_aud = card_para_aud;
        this.card_para_prepod = card_para_prepod;
        this.para_num_and_time_layout_color = para_num_and_time_layout_color;
        this.para_description_layout_color = para_description_layout_color;
    }

    private final String card_para_number;
    private final String card_para_time;
    private final String card_para_name;
    private final String card_para_aud;
    private final String card_para_prepod;
    private final int para_num_and_time_layout_color;
    private final int para_description_layout_color;



    public String getCard_para_number() { return card_para_number; }
    public String getCard_para_time() { return card_para_time; }
    public String getCard_para_name() { return card_para_name; }
    public String getCard_para_aud() { return card_para_aud; }
    public String getCard_para_prepod() { return card_para_prepod; }
    public int getPara_num_and_time_layout_color() { return para_num_and_time_layout_color; }
    public int getPara_description_layout_color() { return para_description_layout_color; }

}
